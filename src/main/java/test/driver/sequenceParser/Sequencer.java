package test.driver.sequenceParser;

import java.util.List;

public class Sequencer implements Runnable {
  Fragment current;
  List<Fragment> fragments;

  public Sequencer(Fragment current, List<Fragment> fragments) {
    this.current = current;
    this.fragments = fragments;
  }

  // https://en.wikipedia.org/wiki/Knuth%E2%80%93Morris%E2%80%93Pratt_algorithm
  private int[] getBackTrackTable(String str2) {
    int[] backTrackTable = new int[str2.length()];
    int nextCharIndex = 0;
    backTrackTable[0] = -1;
    backTrackTable[1] = 0;
    int currentPosition = 2;
    while (currentPosition < str2.length()) {
      if (str2.charAt(currentPosition - 1) == str2.charAt(nextCharIndex)) {
        backTrackTable[currentPosition] = nextCharIndex + 1;
        currentPosition += 1;
        nextCharIndex += 1;
      } else if (nextCharIndex > 0) {
        nextCharIndex = backTrackTable[nextCharIndex];
      } else {
        backTrackTable[currentPosition] = 0;
        currentPosition += 1;
      }
    }
    return backTrackTable;
  }

  private int overlappedLength(String str1, String str2) {
    if (str1.length() > str2.length()) {
      str1 = str1.substring(str1.length() - str2.length());
    }
    int[] backTrackStr2 = getBackTrackTable(str2);
    int m = 0;
    int i = 0;
    while (m + i < str1.length()) {
      if (str2.charAt(i) == str1.charAt(m + i)) {
        i++;
      } else {
        m = m + i - backTrackStr2[i];
        if (i > 0) {
          i = backTrackStr2[i];
        }
      }
    }
    return i;
  }

  @Override
  public void run() {
    for (Fragment fragment : fragments) {
      if (!current.getName().equals(fragment.getName())) {
        int overlapLength = overlappedLength(current.getSequence(), fragment.getSequence());
        double minOverlapLength =
            Math.min(Math.ceil(current.getSequence().length() / 2), Math.ceil(fragment.getSequence().length() / 2));
        if (overlapLength > minOverlapLength) {
          SequenceParser.processedFragments.add(current, fragment, overlapLength);
          SequenceParser.fragmentNames.remove(fragment.getName());
          break;
        }
      }
    }
  }
}
