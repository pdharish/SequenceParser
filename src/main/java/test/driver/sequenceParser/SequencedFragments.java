package test.driver.sequenceParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
Stores the output in a map.
 */
public class SequencedFragments {
  private Map<String, OverlappingFragments> output;

  private int totalStringLength;

  public SequencedFragments() {
    this.output = new ConcurrentHashMap<>();
  }

  public void add(Fragment firstFragment, Fragment secondFragment, int overlapLength) {
    output.put(firstFragment.getName(), new OverlappingFragments(firstFragment, secondFragment, overlapLength));
  }

  public OverlappingFragments get(String name) {
    return output.get(name);
  }

  // use this function to confirm length of final string, if required
  // I read that practically, this might not be useful so I didn't write a test for it.
  public int getTotalStringLength() {
    output.forEach((k, v) -> {
      totalStringLength += v.getEndFragment().getSequence().length() - v.getOverlapLength();
    });

    return totalStringLength;
  }
}
