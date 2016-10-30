package test.driver.sequenceParser;

import java.io.IOException;
import java.util.List;

/*
  Interface so that we can implement multiple format readers if necessary.
*/

public interface Reader {
  List<Fragment> readFile(String filePath) throws IOException;

}
