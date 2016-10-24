package test.driver.sequenceParser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SequencedFragments {
  private Map<String, OverlappingFragments> output;

  int totalStringLength;

  public SequencedFragments() {
    this.output = new ConcurrentHashMap<>();
  }

  public void add(Fragment firstFragment, Fragment secondFragment, int overlapLength) {
    output.put(firstFragment.getName(), new OverlappingFragments(firstFragment, secondFragment, overlapLength));
  }

  public OverlappingFragments get(String name) {
    return output.get(name);
  }

  public int getTotalStringLength() {
    output.forEach((k, v) -> {
      totalStringLength += v.getSecondFragment().getSequence().length() - v.getOverlapLength();
    });

    return totalStringLength;
  }
}
