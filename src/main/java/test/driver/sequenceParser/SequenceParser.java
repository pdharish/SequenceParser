package test.driver.sequenceParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.log4j.Level;

public class SequenceParser {

  static Logger logger = Logger.getLogger(SequenceParser.class.getName());

  static SequencedFragments processedFragments = new SequencedFragments();

  static Set<String> fragmentNames = new HashSet<>();

  public static void main(String[] args) {

    new SequenceParser().getMergedDnaSequence();

  }

  public void getMergedDnaSequence() {
    Properties prop = new Properties();
    InputStream input = null;

    try {

      input = getClass().getClassLoader().getResourceAsStream("project.properties");
      prop.load(input);
      FastaReader reader = new FastaReader();
      List<Fragment> fragments = reader.readFile(prop.getProperty("inputFilePath"));
      //fragmentNames.addAll(fragments.forEach(fragment -> fragment.getName()));
      for(Fragment f: fragments) {
        fragmentNames.add(f.getName());
      }
      fragments.forEach(fragment -> {
        //System.out.println(fragment.getName());
        Thread processor = new Thread(new Sequencer(fragment, Collections.unmodifiableList(fragments)));
        processor.start();

      });
      try {
        Thread.sleep(100);
      } catch (InterruptedException ex) {

      }
      printMergedOutput();
    } catch (IOException ex) {
      logger.log(java.util.logging.Level.SEVERE, "Error getting project properties");
      ex.printStackTrace();
    }
  }

  private void printMergedOutput() {
    StringBuilder outputSequence = new StringBuilder();
    if (fragmentNames.size() > 1 || fragmentNames.size() == 0 ) {
      logger.log(java.util.logging.Level.SEVERE, "Fked up yo");
    }
    String head = null;
    for (Iterator<String> it = fragmentNames.iterator(); it.hasNext(); ) {
      head = it.next();
      System.out.println("head is " + head);
    }
    OverlappingFragments olFragment = processedFragments.get(head);
    outputSequence.append(olFragment.getFirstFragment().getSequence());
    int answer = olFragment.getFirstFragment().getSequence().length();

    while (true) {
      int overlapLength = olFragment.getOverlapLength();
      outputSequence.append(olFragment.getSecondFragment().getSequence().substring(overlapLength));
      head = olFragment.getSecondFragment().getName();
      olFragment = processedFragments.get(head);
      if (olFragment == null) {
        break;
      }
      //answer += olFragment.getFirstFragment().getSequence().substring(olFragment.getOverlapLength()).length();

    }

    System.out.println(outputSequence.toString());

    System.out.println(outputSequence.toString().length());

    answer += processedFragments.getTotalStringLength();

    System.out.println(answer);

  }

}
