package test.driver.sequenceParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;
import com.google.inject.Inject;

import org.apache.log4j.Logger;

import test.driver.exceptions.IllegalInputFormatException;

public class FastaReader implements Reader {
  static final Logger LOGGER = Logger.getLogger(FastaReader.class.getName());

  private File fastaFile;

  private List<Fragment> fragments;

  @Inject
  public FastaReader(File fastaFile, List<Fragment> fragments) {
    this.fastaFile = fastaFile;
    this.fragments = fragments;
  }

  public FastaReader() {

  }

  public List<Fragment> readFile(String filePath) throws FileNotFoundException, IllegalFormatException {
    fastaFile = new File(filePath);

    FileReader fastaReader = new FileReader(fastaFile);
    BufferedReader reader = new BufferedReader(fastaReader);


    String line;
    fragments = new ArrayList<Fragment>();
    try {
      while((line = reader.readLine()) != null) {
        if (line.startsWith(">") || line.startsWith("<") || line.startsWith(";")) {
          String name = line.trim();
          StringBuilder sequenceBuilder = new StringBuilder();
          String sequence;
          while((sequence = reader.readLine()) != null
              && (!sequence.contains("<") && !sequence.contains(">") && !sequence.contains(";"))
              )  {
            sequenceBuilder.append(sequence);
            reader.mark(0);
          }
          fragments.add(new Fragment(name, sequenceBuilder.toString()));
          reader.reset();

        }
      }

    } catch (IOException ex) {
      // suppress invalid mark error here.
    }

    return fragments;
  }

}
