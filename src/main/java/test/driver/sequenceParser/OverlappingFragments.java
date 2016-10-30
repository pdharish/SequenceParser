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

  // Do not provide set() functions here as it could lead to wrongly setting the start and end fragments.
  // Use constructor to create the correct objects instead.

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
