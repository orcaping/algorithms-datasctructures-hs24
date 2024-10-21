/*
 * OST - Uebungen 'Algorithmen & Datenstrukturen (AlgDat)'
 * Version: Mon Sep 30 16:38:18 CEST 2024
 */

package ex03.solution.task03;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import ex02.solution.task01.BinarySearchTree;

class AVLTreeImpl<K extends Comparable<? super K>, V> extends
    BinarySearchTree<K, V> {
  
  /**
   * After the BST-operation 'insert()': 
   * actionNode shall point to the parent of the new inserted node.
   */
  protected AVLNode actionNode;


  protected class AVLNode extends BinarySearchTree<K, V>.Node {

    private int height;
    private Node parent;

    AVLNode(Entry<K, V> entry) {
      super(entry);
    }

    protected AVLNode setParent(AVLNode parent) {
      AVLNode old = avlNode(this.parent);
      this.parent = parent;
      return old;
    }

    protected AVLNode getParent() {
      return avlNode(parent);
    }

    protected int setHeight(int height) {
      int old = this.height;
      this.height = height;
      return old;
    }

    protected int getHeight() {
      return height;
    }

    @Override
    public AVLNode getLeftChild() {
      return avlNode(super.getLeftChild());
    }

    @Override
    public AVLNode getRightChild() {
      return avlNode(super.getRightChild());
    }

    @Override
    public String toString() {
      String result = String.format("%2d - %-6s : h=%d", 
                             getEntry().getKey(), getEntry().getValue(), height);
      if (parent == null) {
        result += " ROOT";
      } else {  
        boolean left = (parent.getLeftChild() == this) ? true : false;
        result += (left ? " / " : " \\ ") + "parent(key)="
            + parent.getEntry().getKey();
      }
      return result;
    }

  } // End of class AVLNode

  
  protected AVLNode getRoot() {
    return avlNode(root);
  }
  
  public V put(K key, V value) {
    Entry<K, V> entry = find(key);
    if (entry != null) { 
      // key already exists in the Tree
      return entry.setValue(value);
    } 
    // key does not exist in the Tree yet
    super.insert(key, value);
    assureHeights(actionNode);
    actionNode = null;
    return null;
  }
  
  public V get(K key) {
    Entry<K, V> entry = super.find(key);
    if (entry == null) {
      return null;
    } 
    return entry.getValue();
  }
  
  @Override
  protected Node insert(Node node, Entry<K, V> entry) {
    if (node != null) { 
      actionNode = avlNode(node);
    } 
    // calling now the BST-insert() which will do the work:
    AVLNode result = avlNode(super.insert(node, entry));
    if (node == null) { 
      // In this case: result of super.insert() is the new node!
      result.setParent(actionNode);
    }
    return result;
  }

  /**
   * The height of the tree.
   * 
   * @return The current height. -1 for an empty tree.
   */
  @Override
  public int getHeight() {
    return height(avlNode(root));
  }
  
  /**
   * Returns the height of this node.
   * 
   * @param node
   * @return The height or -1 if null.
   */
  @SuppressWarnings("static-method")
  protected int height(AVLNode node) {
    return (node != null) ? node.getHeight() : -1;
  }
  
  /**
   * Assures the heights of the tree from 'node' up to the root.
   * 
   * @param node
   *          The node from where to start.
   */
  protected void assureHeights(AVLNode node) {
    while (node != null) {
      setHeight(node);
      node = node.getParent();
    }
  }

  /**
   * Assures the correct height for node.
   * 
   * @param node
   *          The node to assure its height.
   */
  protected void setHeight(AVLNode node) {
    if (node == null) {
      return;
    }
    int heightLeftChild = height(node.getLeftChild());
    int heightRightChild = height(node.getRightChild());
    node.setHeight(1 + Math.max(heightLeftChild, heightRightChild));
  }
  
  /**
   * Factory-Method. Creates a new node.
   * 
   * @param entry
   *          The entry to be inserted in the new node.
   * @return The new created node.
   */
  @Override
  protected Node newNode(Entry<K, V> entry) {
    return new AVLNode(entry);
  }
  
  @Override
  protected void inorder(Node node, Collection<Node> inorderList) {
    super.inorder(node, inorderList);
  }
  
  // Type-Casting: Node -> AVLNode (Cast-Encapsulation)
  @SuppressWarnings({ "unchecked", "static-method" })
  protected AVLNode avlNode(Node node) {
    return (AVLNode)node;
  }
  
  public void print() {
    List<Node> nodeList = new LinkedList<>();
    inorder(root, nodeList);
    for (Node node: nodeList) {
      System.out.println(node + "  ");
    }
  }
  
}

 