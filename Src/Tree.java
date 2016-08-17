package ObjectTracker;

import java.util.*;

/**
 *  This class represents a binary tree.
 *
 *  @author  Eoin O'Connor
 *  @see Node
 *  @see RunTimePanel
 */
public class Tree
{
   /**
    *  The root node of the tree.
    */
   private Node root;

   /**
    *  Constructor: creates an empty tree.
    */
   public Tree()
   {
      root = null;
   }

   /**
    *  Inserts a new node into the tree.
    *
    *  @param  obj   The object to insert.
    */
   public void insert(Comparable obj)
   {
      Node newNode = new Node();
      newNode.data = obj;
      newNode.left = null;
      newNode.right = null;
      if(root==null)
         root = newNode;
      else
         root.insertNode(newNode);
   }

   /**
    *  Returns the list of nodes.
    *
    *  @return The list of nodes.
    */
   public ArrayList get()
   {
      ArrayList list = new ArrayList();
      if(root!=null)
         list = root.get(list);
      return list;
   }
}