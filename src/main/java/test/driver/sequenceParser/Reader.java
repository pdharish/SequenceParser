package test.driver.sequenceParser;

import java.io.IOException;
import java.util.List;

public interface Reader {
  List<Fragment> readFile(String filePath) throws IOException;
  //void parseContent();
}
