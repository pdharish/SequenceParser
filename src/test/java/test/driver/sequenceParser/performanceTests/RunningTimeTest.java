package test.driver.sequenceParser.performanceTests;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.Test;

import test.driver.sequenceParser.SequenceParser;

public class RunningTimeTest {

  @Test
  public void getAverageRunningTime_Multithreaded() {
    LogManager.getRootLogger().setLevel(Level.ALL);
    long totalTime = 0;

      SequenceParser sequenceParser = new SequenceParser();
      long startTime = System.currentTimeMillis();
      String output = sequenceParser.getMergedDnaSequence();
      long endTime = System.currentTimeMillis();
      totalTime = endTime - startTime;
      try {
        sequenceParser = null;
        System.gc();
        Thread.sleep(250);

      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    System.out.print("\nRunning time is : ");
    System.out.println(totalTime);
  }
}
