package test.driver.sequenceParser;

import java.util.List;

public class Sequencer implements Runnable {
  Fragment current;
  List<Fragment> fragments;

  public Sequencer(Fragment current, List<Fragment> fragments) {
    this.current = current;
    this.fragments = fragments;
  }

  /**
   * When an object implementing interface <code>Runnable</code> is used
   * to create a thread, starting the thread causes the object's
   * <code>run</code> method to be called in that separately executing
   * thread.
   * <p>
   * The general contract of the method <code>run</code> is that it may
   * take any action whatsoever.
   *
   * @see Thread#run()
   */
  // Variation of Knuth Morris Pratt algorithm for calculating string overlap in linear time
  private static int overlappedStringLength(String s1, String s2) {
    if (s1.length() > s2.length()) {
      s1 = s1.substring(s1.length() - s2.length());
    }
    int[] T = computeBackTrackTable(s2); //O(n)
    int m = 0;
    int i = 0;
    while (m + i < s1.length()) {
      if (s2.charAt(i) == s1.charAt(m + i)) {
        i += 1;
      } else {
        m += i - T[i];
        if (i > 0) {
          i = T[i];
        }
      }
    }
    return i;
  }

  // Match table for KMP algo
  private static int[] computeBackTrackTable(String s) {
    int[] T = new int[s.length()];
    int cnd = 0;
    T[0] = -1;
    T[1] = 0;
    int pos = 2;
    while (pos < s.length()) {
      if (s.charAt(pos - 1) == s.charAt(cnd)) {
        T[pos] = cnd + 1;
        pos += 1;
        cnd += 1;
      } else if (cnd > 0) {
        cnd = T[cnd];
      } else {
        T[pos] = 0;
        pos += 1;
      }
    }
    return T;
  }


  @Override
  public void run() {
    //System.out.println(current.getName());
    for(Fragment fragment: fragments) {
      if (!current.getName().equals(fragment.getName())) {
        int overlapLength = overlappedStringLength(current.getSequence(), fragment.getSequence());
        // If the overlap is more than half of the larger sequence, we assume assume it's a match
        // See README for special cases
        double overlapThreshold = Math.max(Math.ceil(current.getSequence().length() / 2), Math.ceil(fragment.getSequence().length() / 2));
        if (overlapLength > overlapThreshold) {
          //System.out.println(current.getName() + "->" + fragment.getName());
          SequenceParser.processedFragments.add(current, fragment, overlapLength);
          SequenceParser.fragmentNames.remove(fragment.getName());
          break;
        }
      }
    }


  }
}
