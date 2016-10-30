package test.driver.sequenceParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.inject.Inject;

import org.apache.log4j.Logger;

public class SequenceParser {

  static Logger logger = Logger.getLogger(SequenceParser.class.getName());

  static SequencedFragments processedFragments = new SequencedFragments();

  static Set<String> fragmentNames = new HashSet<>();

  private FastaReader reader;

  private String filePath;

  private String finalSequence;

  @Inject
  public SequenceParser(FastaReader reader, String filePath) {
    this.reader = reader;
    this.filePath = filePath;
  }

  public SequenceParser() {
  }

  // Initialization block, in case reader is not injected
  {
    Properties prop = new Properties();
    InputStream input;
    try {
      input = getClass().getClassLoader().getResourceAsStream("project.properties");
      prop.load(input);
      reader = new FastaReader();
      filePath = prop.getProperty("inputFilePath");
    } catch (IOException ex) {
      logger.error("Error processing file");
      ex.printStackTrace();
    }
  }

  public static void main(String[] args) {
    String output = new SequenceParser().getMergedDnaSequence();
    System.out.println("\nOutput sequence returned is:");
    System.out.println("" + output);
    System.out.println("\nLength of the output is: " + output.length());
  }

  public String getMergedDnaSequence() {
    List<Fragment> fragments = null;
    try {
      fragments = reader.readFile(filePath);
      logger.info("Data in input file processed");
       /*
       Fork threads for each of the input segment in the list.
       I did not use this as join() was taking more time than with ExecutorService.

       fragments.forEach(fragment -> {
          //System.out.println(fragment.getName());
          Thread processor = new Thread(new Sequencer(fragment, Collections.unmodifiableList(fragments)));
          processor.start();

        });
      */
      fragments.forEach(f -> fragmentNames.add(f.getName()));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    // Fork threads to process in parallel.
    logger.info("Forking " + fragments.size() + " threads.");
    ExecutorService executorServ = Executors.newCachedThreadPool();
    for (int i = 0; i < fragments.size(); i++) {
      executorServ.execute(new Sequencer(fragments.get(i), fragments));
    }
    executorServ.shutdown();
    try {
      boolean done = executorServ.awaitTermination(1, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    finalSequence = printMergedOutput();

    return finalSequence;
  }

  private String printMergedOutput() {
    StringBuilder finalSequence = new StringBuilder();
    // Check if output is correctly formed.
    if (fragmentNames.size() > 1 || fragmentNames.size() == 0) {
      logger.fatal("The output doesn't seem to be formed correctly as there are multiple starting points for the DNA sequence.",
          new InputMismatchException("Please ensure input is from a single DNA sequence"));
    }
    String head = null;
    // get the starting point of the final output
    for (Iterator<String> it = fragmentNames.iterator(); it.hasNext(); ) {
      head = it.next();
    }
    logger.info("Starting fragment of output sequence is: " + head);
    // Add start of sequence to StringBuilder
    OverlappingFragments olFragment = processedFragments.get(head);
    finalSequence.append(olFragment.getStartFragment().getSequence());

    // Iterate through the processed output and append to output.
    while (true) {
      int overlapLength = olFragment.getOverlapLength();
      finalSequence.append(olFragment.getEndFragment().getSequence().substring(overlapLength));
      head = olFragment.getEndFragment().getName();
      olFragment = processedFragments.get(head);
      if (olFragment == null) {
        break;
      }
    }
    return finalSequence.toString();
  }
}
