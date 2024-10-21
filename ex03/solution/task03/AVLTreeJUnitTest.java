/*
 * OST - Uebungen 'Algorithmen & Datenstrukturen (AlgDat)'
 * Version: Mon Sep 30 16:38:18 CEST 2024
 */

package ex03.solution.task03;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ex02.solution.task01.BinarySearchTree;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AVLTreeJUnitTest {
  
  AVLTreeImpl<Integer, String> avlTree;

  @Before
  public void setUp() {
    avlTree = new AVLTreeImpl<>();
  }
  
  @Test
  public void test01Put() {
    int[] keys = { 2, 1, 3 };
    String[] expected = { 
        " 1 - Str1   : h=0 / parent(key)=2",
        " 2 - Str2   : h=1 ROOT", 
        " 3 - Str3   : h=0 \\ parent(key)=2", 
    };
    runTest(keys, expected);
    assertEquals(1, avlTree.getHeight());
  }
  
  @Test
  public void test02Get() {
    int[] keys = { 2, 1, 4, 5, 3 };
    String[] expected = { 
        " 1 - Str1   : h=0 / parent(key)=2",
        " 2 - Str2   : h=2 ROOT", 
        " 3 - Str3   : h=0 / parent(key)=4", 
        " 4 - Str4   : h=1 \\ parent(key)=2",
        " 5 - Str5   : h=0 \\ parent(key)=4",
    };
    runTest(keys, expected);
    assertEquals(2, avlTree.getHeight());
    assertEquals("Str2", avlTree.get(2));
    assertEquals("Str5", avlTree.get(5));
    assertNull(avlTree.get(0));
    assertNull(avlTree.get(6));
  }

  @Test
  public void test03() {
    int[] keys = { 2, 3, 1 };
    String[] expected = { 
        " 1 - Str1   : h=0 / parent(key)=2",
        " 2 - Str2   : h=1 ROOT", 
        " 3 - Str3   : h=0 \\ parent(key)=2", 
    };
    runTest(keys, expected);
    assertEquals(1, avlTree.getHeight());
    avlTree.put(2, "Str2:2");
    avlTree.put(2, "Str2:3");
    assertEquals(1, avlTree.getHeight());
    expected = new String[] { 
        " 1 - Str1   : h=0 / parent(key)=2",
        " 2 - Str2:3 : h=1 ROOT", 
        " 3 - Str3   : h=0 \\ parent(key)=2", 
    };
    Collection<BinarySearchTree<Integer, String>.Node> nodes = new LinkedList<>();
    avlTree.inorder(avlTree.getRoot(), nodes);
    verify(nodes, expected);
  }
  
  
  private void runTest(int[] keys, String[] expected) {
    for (int key : keys) {
      avlTree.put(key, "Str" + key);
    }
    Collection<BinarySearchTree<Integer, String>.Node> nodes = new LinkedList<>();
    avlTree.inorder(avlTree.getRoot(), nodes);
    assertEquals(expected.length, nodes.size());
    verify(nodes, expected);
  }

  private static void verify(Collection<BinarySearchTree<Integer, String>.Node> nodes, String[] expected) {
    int i = 0;
    for (BinarySearchTree<Integer, String>.Node node: nodes) {
      String nodeStr = node.toString();
      String expectedStr = expected[i];
      assertEquals(expectedStr, nodeStr);
      i++;
    }
  }
  
}
 
