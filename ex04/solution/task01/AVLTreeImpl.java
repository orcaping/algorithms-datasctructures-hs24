/*
 * OST - Uebungen 'Algorithmen & Datenstrukturen (AlgDat)'
 * Version: Sun Oct  6 13:58:27 CEST 2024
 */

package ex04.solution.task01;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import ex02.solution.task01.BinarySearchTree;


class AVLTreeImpl<K extends Comparable<? super K>, V> extends
    BinarySearchTree<K, V> {

  /**
   * After a BST-operation, actionNode shall point to where the balance has to
   * be checked. -> rebalance() will then be called with actionNode.
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
    
    @SuppressWarnings("unchecked")
    @Override
    public void setLeftChild(BinarySearchTree<K, V>.Node leftChild) {
      super.setLeftChild(leftChild);
      if (leftChild != null) {
        ((AVLNode)leftChild).setParent(this);
      }
    }

    @Override
    public AVLNode getLeftChild() {
      return avlNode(super.getLeftChild());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void setRightChild(BinarySearchTree<K, V>.Node rightChild) {
      super.setRightChild(rightChild);
      if (rightChild != null) {
        ((AVLNode)rightChild).setParent(this);
      }
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
    rebalance(actionNode);
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
   * Restructures the tree with rotations.
   * 
   * @param xPos
   *          The X-node.
   * @return The new root-node of this subtree.
   */
  protected AVLNode restructure(AVLNode xPos) {
    AVLNode yPos = xPos.getParent(); 
    AVLNode zPos = yPos.getParent();
    AVLNode newSubTreeRoot = null;
    if (yPos == zPos.getLeftChild()) {
      if (xPos == yPos.getLeftChild()) {
        newSubTreeRoot = rotateWithLeftChild(zPos);
      } else {
        newSubTreeRoot = doubleRotateWithLeftChild(zPos);
      }
    } else {
      if (xPos == yPos.getRightChild()) {
        newSubTreeRoot = rotateWithRightChild(zPos);
      } else {
        newSubTreeRoot = doubleRotateWithRightChild(zPos);
      }
    }
    return newSubTreeRoot;
  }

  protected AVLNode tallerChild(AVLNode node) {
    AVLNode result;
    if (height(node.getLeftChild()) >= height(node.getRightChild())) {
      result = node.getLeftChild();
    } else {
      result = node.getRightChild();
    }
    return result;
  }

  protected AVLNode rotateWithLeftChild(AVLNode k2) {
    AVLNode parentSubtree = k2.getParent();
    AVLNode k1 = k2.getLeftChild();
    k2.setLeftChild(k1.getRightChild());
    k1.setRightChild(k2);
    adjustSubtreeParent(k2,  k1, parentSubtree);
    return k1;
  }

  protected AVLNode doubleRotateWithLeftChild(AVLNode k3) {
    //k3.setLeftChild(rotateWithRightChild(k3.getLeftChild()));
    // -> k3.setLeftChild() is done/ensured in adjustSubtreeParent():
    rotateWithRightChild(k3.getLeftChild());
    return rotateWithLeftChild(k3);
  }

  protected AVLNode rotateWithRightChild(AVLNode k1) {
    AVLNode parentSubtree = k1.getParent();
    AVLNode k2 = k1.getRightChild();
    k1.setRightChild(k2.getLeftChild());
    k2.setLeftChild(k1);
    adjustSubtreeParent(k1, k2, parentSubtree);
    return k2;
  }

  protected AVLNode doubleRotateWithRightChild(AVLNode k3) {
    //k3.setRightChild(rotateWithLeftChild(k3.getRightChild()));
    // -> k3.setRightChild() is done/ensured in adjustSubtreeParent():
    rotateWithLeftChild(k3.getRightChild());
    return rotateWithRightChild(k3);
  }
  
  /**
   * Assures the connection between a restructured subtree and the parent of
   * this subtree. 
   * Used after rotations.
   * 
   * @param oldSubtreeRoot
   *          The old root-node of this subtree.
   * @param newSubtreeRoot
   *          The new root-node of this subtree.
   * @param parentSubtree
   *          The parent-node of this subtree.
   */
  protected void adjustSubtreeParent(AVLNode oldSubtreeRoot,
      AVLNode newSubtreeRoot, AVLNode parentSubtree) {
    if (parentSubtree != null) {
      if (parentSubtree.getLeftChild() == oldSubtreeRoot) {
        parentSubtree.setLeftChild(newSubtreeRoot);
      } else {
        parentSubtree.setRightChild(newSubtreeRoot);
      }
    } else { // newSubtreeRoot is now also the root of the whole tree 
      root = newSubtreeRoot;
      newSubtreeRoot.setParent(null);
    }
  }

  protected boolean isBalanced(AVLNode node) {
    int bf = height(node.getLeftChild()) - height(node.getRightChild());
    return (-1 <= bf) && (bf <= 1);
  }
  
  /**
   * Assures the balance of the tree from 'node' up to the root.
   * 
   * @param node
   *          The node from where to start.
   */
  protected void rebalance(AVLNode node) {
    while (node != null) {
      setHeight(node);
      if (!isBalanced(node)) {
        AVLNode xPos = tallerChild(tallerChild(node));
        node = restructure(xPos);
        setHeight(node.getLeftChild());
        setHeight(node.getRightChild());
        setHeight(node);
      }
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
  
  public V remove(K key) {
    Entry<K, V> entry = find(key);
    if (entry == null) {
      return null;
    }
    // calling now the BST-remove(Entry) which will do the work:
    super.remove(entry);
    if (actionNode != null) { 
      assureParentForChilds(actionNode);
      rebalance(actionNode);
      actionNode = null;
    }
    return entry.getValue();
  }
 
  @Override
  protected RemoveResult remove(Node node, Entry<K, V> entry) {
    if (node.getEntry() == entry) {
      actionNode = avlNode(node).getParent();
    }
    // calling now the BST-remove(Node, Entry) which will do the work:
    return super.remove(node, entry);
  }

  @Override
  protected Node getParentNext(Node p) {
    actionNode = avlNode(super.getParentNext(p));
    return actionNode;
  }
  
  @SuppressWarnings("static-method")
  protected void assureParentForChilds(AVLNode parent) {
    @SuppressWarnings("unchecked")
    AVLNode[] childs = (AVLNode[])Array.newInstance(parent.getClass(), 2);
    childs[0] = parent.getLeftChild();
    childs[1] = parent.getRightChild();
    for (AVLNode child : childs) {
      if (child != null) {
        child.setParent(parent);
      }
    }
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

 
