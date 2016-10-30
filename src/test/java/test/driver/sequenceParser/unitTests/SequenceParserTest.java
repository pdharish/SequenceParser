package test.driver.sequenceParser.unitTests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import test.driver.sequenceParser.FastaReader;
import test.driver.sequenceParser.Fragment;
import test.driver.sequenceParser.SequenceParser;

public class SequenceParserTest {

  SequenceParser sequenceParser;

  @Mock
  FastaReader mockReader;

  String fileName;

  @Before
  public void setUp() {
    try {
      Properties prop = new Properties();
      InputStream input = getClass().getClassLoader().getResourceAsStream("test.properties");
      prop.load(input);
      String basePath = new File("").getAbsolutePath();
      basePath = basePath.concat(prop.getProperty("testFilePath"));
      FastaReader reader = new FastaReader();
      sequenceParser = new SequenceParser(reader, basePath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testOutput_Success() {
    mockReader = Mockito.mock(FastaReader.class);
    List<Fragment> mockFragments = new ArrayList<>(Arrays.asList(
        new Fragment(">Frag_56", "ATTAGACCTG"),
        new Fragment(">Frag_57", "CCTGCCGGAA"),
        new Fragment(">Frag_58", "AGACCTGCCG"),
        new Fragment(">Frag_59", "GCCGGAATAC")
    ));

    try {
      Mockito.when(mockReader.readFile(fileName)).thenReturn(
          mockFragments
      );
    } catch (FileNotFoundException e) {
      assert false : "Invalid file";
    }
    String output = sequenceParser.getMergedDnaSequence();
    assert output != null;
    assert output.length() == 19;
  }
}
