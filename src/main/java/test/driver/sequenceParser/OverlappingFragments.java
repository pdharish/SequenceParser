package test.driver.sequenceParser;

public class OverlappingFragments {
  private Fragment startFragment;
  private Fragment endFragment;
  private Integer overlapLength;

  public OverlappingFragments(Fragment firstFragment, Fragment secondFragment, Integer overlapLength) {
    this.startFragment = firstFragment;
    this.endFragment = secondFragment;
    this.overlapLength = overlapLength;
  }

  public Fragment getStartFragment() {
    return startFragment;
  }

  public Fragment getEndFragment() {
    return endFragment;
  }

  public Integer getOverlapLength() {
    return overlapLength;
  }

}
