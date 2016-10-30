package test.driver.sequenceParser.unitTests;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import test.driver.exceptions.IllegalInputFormatException;
import test.driver.sequenceParser.FastaReader;
import test.driver.sequenceParser.Fragment;
import test.driver.sequenceParser.SequenceParser;

public class FastReaderTest {

  SequenceParser sequenceParser;

  @Mock
  FastaReader mockReader;

  String fileName;

  @Before
  public void setUp() {
    sequenceParser = new SequenceParser();
    Properties prop = new Properties();
    InputStream input;
    try {
      input = getClass().getClassLoader().getResourceAsStream("project.properties");
      prop.load(input);
      fileName = prop.getProperty("inputFilePath");
    } catch (IOException ex) {
      assert false;
    }
  }

  @Test
  public void testFastaReader_SuccessfulParse() {
    FastaReader reader = new FastaReader();
    List<Fragment> fragments = null;
    try {
      fragments = reader.readFile(fileName);
    } catch (FileNotFoundException ex) {
      assert false;
    } catch (IllegalInputFormatException ex) {
      assert false;
    }

    if (fragments != null) {
      assert true;
    }
  }
}
