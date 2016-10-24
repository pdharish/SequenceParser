package test.driver.sequenceParser;

public class OverlappingFragments {
  Fragment firstFragment;
  Fragment secondFragment;
  Integer overlapLength;

  public OverlappingFragments(Fragment firstFragment, Fragment secondFragment, Integer overlapLength) {
    this.firstFragment = firstFragment;
    this.secondFragment = secondFragment;
    this.overlapLength = overlapLength;
  }

  public Fragment getFirstFragment() {
    return firstFragment;
  }

  public void setFirstFragment(Fragment firstFragment) {
    this.firstFragment = firstFragment;
  }

  public Fragment getSecondFragment() {
    return secondFragment;
  }

  public void setSecondFragment(Fragment secondFragment) {
    this.secondFragment = secondFragment;
  }

  public Integer getOverlapLength() {
    return overlapLength;
  }

  public void setOverlapLength(Integer overlapLength) {
    this.overlapLength = overlapLength;
  }
}
