package test.driver.sequenceParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.log4j.Level;

public class SequenceParser {

  static Logger logger = Logger.getLogger(SequenceParser.class.getName());

  static SequencedFragments processedFragments = new SequencedFragments();

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

      fragments.forEach(fragment -> {
        //System.out.println(fragment.getName());
        Thread processor = new Thread(new Sequencer(fragment, Collections.unmodifiableList(fragments)));
        processor.start();

      });

    } catch (IOException ex) {
      logger.log(java.util.logging.Level.SEVERE, "Error getting project properties");
      ex.printStackTrace();
    }
  }

}
