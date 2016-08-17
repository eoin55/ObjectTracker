package ObjectTracker;

import java.util.*;

/**
 *  This class represents the node of a tree.
 *
 *  @author  Eoin O'Connor
 *  @see Tree
 */
public class Node
{
   /**
    *  The object contained in the node.
    */
   public Comparable data;
   /**
    *  The node to the left.
    */
   public Node left;
   /**
    *  The node to the right.
    */
   public Node right;

   /**
    *  Inserts a node to the left or right of this node.
    *
    *  @param  newNode  The node to insert.
    */
   public void insertNode(Node newNode)
   {
      if(newNode.data.compareTo(data) > 0)
      {
         if(left==null)
            left = newNode;
         else
            left.insertNode(newNode);
      }
      else
      {
         if(right==null)
            right = newNode;
         else
            right.insertNode(newNode);
      }
   }

   /**
    *  Returns the list of nodes.
    *
    *  @param  list  The list to append to.
    *  @return The list of nodes.
    */
   public ArrayList get(ArrayList list)
   {
      if(left!=null)
         list = left.get(list);
      list.add(data);
      if(right!=null)
         list = right.get(list);
      return list;
   }
}