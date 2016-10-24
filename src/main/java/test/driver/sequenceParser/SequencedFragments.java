package test.driver.sequenceParser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SequencedFragments {
  private Map<String, OverlappingFragments> output;

  public SequencedFragments() {
    this.output = new ConcurrentHashMap<>();
  }

  public void add(Fragment firstFragment, Fragment secondFragment, int overlapLength) {
    output.put(firstFragment.getName(), new OverlappingFragments(firstFragment, secondFragment, overlapLength));
  }
}
