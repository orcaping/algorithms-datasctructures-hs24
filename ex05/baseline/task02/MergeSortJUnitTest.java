/*
 * OST - Uebungen 'Algorithmen & Datenstrukturen (AlgDat)'
 * Version: Tue Oct 15 12:37:47 CEST 2024
 */

package ex05.baseline.task02;
import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.Random;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MergeSortJUnitTest {

  @Test
  public void test01() {
    Integer[] arr = {4, 1, 2, 3};
    sort(arr);
  }
  
  @Test
  public void test02() {
    Integer[] arr = {2, 4, 3, 1};
    sort(arr);
  }
  
  @Test
  public void test03() {
    Integer[] arr = {2, 1};
    sort(arr);
  }
  
  @Test
  public void test04() {
    Integer[] arr = {1, 2};
    sort(arr);
  }
  
  @Test
  public void test05() {
    Integer[] arr = {1};
    sort(arr);
  }
  
  @Test
  public void test06() {
    Integer[] arr = {};
    sort(arr);
  }

  @Test
  public void test07StressTest() {
    final int NUMBER_OF_TESTS = 50000;
    final int LENGTH = 128;
    for (int n = 0; n < NUMBER_OF_TESTS; n++) {
      Integer[] arr = 
          new Random().ints(LENGTH, 0, 10).boxed().toArray(Integer[]::new);
      sort(arr);
    }
  }
  
  private void sort(Integer[] arr) {
    Integer[] clonedArr = arr.clone();
    Integer[] sortedArr = MergeSort.mergeSort(arr);
    verify(clonedArr, sortedArr);
  }
  
  @SuppressWarnings("static-method")
  private void verify(Integer[] orgArr, Integer[] sortedArr) {
    Integer[] sortedOrgArr = Arrays.copyOf(orgArr, orgArr.length);
    Arrays.sort(sortedOrgArr);
    assertArrayEquals(sortedOrgArr, sortedArr);
  }

}
 
