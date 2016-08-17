package ObjectTracker;

import java.awt.*;
import java.util.*;

/**
 *  This class represents a class that has been used
 *  by the target program.
 *
 *  @author  Eoin O'Connor
 *  @see SequencePanel
 *  @see ClassPanel
 *  @see RunTimePanel
 *  @see MethodClass
 *  @see Tree
 */
public class ObjectClass implements Comparable
{
   /**
    *  The name of the class.
    */
   private String name;
   /**
    *  The start time of the class.
    */
   private long startTime;
   /**
    *  The list of classes called by the class.
    */
   private ArrayList calledObjects;
   /**
    *  The number of classes called by the class.
    */
   private int numCalledObjects;
   /**
    *  A list of the number of times the class
    *  calls each class.
    */
   private ArrayList calledObjectCount;
   /**
    *  The colour associated with the class.
    */
   private Color colour;
   /**
    *  The list of methods called by the class.
    */
   private ArrayList calledMethods;
   /**
    *  The number of methods called by the class.
    */
   private int numCalledMethods;
   /**
    *  A list of the number of times the class
    *  calls each method.
    */
   private ArrayList methodCounts;
   /**
    *  True if the methods called by the class should
    *  be displayed.
    */
   private boolean showMethods;

   /**
    *  Constructor: creates a new ObjectClass.
    *
    *  @param  n  The name of the class.
    *  @param  c The colour of the class.
    */
   public ObjectClass(String n,Color c)
   {
      name = n;
      colour = c;
      startTime = System.currentTimeMillis();
      calledObjects = new ArrayList();
      calledObjectCount = new ArrayList();
      numCalledObjects = 0;
      calledMethods = new ArrayList();
      numCalledMethods = 0;
      methodCounts = new ArrayList();
      showMethods = false;
   }

   /**
    *  Returns the name of the class.
    *
    *  @return The name of the class.
    */
   public String getName()
   {
      return name;
   }

   /**
    *  Sets the colour of the class.
    *
    *  @param  c  The new colour of the class.
    */
   public void setColour(Color c)
   {
      colour = c;
   }

   /**
    *  Returns the colour of the class.
    *
    *  @return The colour of the class.
    */
   public Color getColour()
   {
      return colour;
   }

   /**
    *  Returns the start time of the class.
    *
    *  @return The start time of the class.
    */
   public long getStartTime()
   {
      return startTime;
   }

   /**
    *  Returns true if the class is equal to objectClass.
    *
    *  @param  objectClass  The class to be compared to.
    *  @return True if the class is equal to objectClass.
    */
   public boolean equals(ObjectClass objectClass)
   {
      return name.equals(objectClass.getName());
   }

   /**
    *  Compares the class to obj.
    *
    *  @param  obj   The object to compare to.
    *  @return 1 if the class's name before obj's.
    *  @return 1 if the class's name after obj's.
    *  @return 1 if the class's name is the same as obj's.
    */
   public int compareTo(Object obj)
   {
      if(name.compareTo(((ObjectClass)obj).getName())>0)
         return 1;
      if(name.compareTo(((ObjectClass)obj).getName())<0)
         return -1;
      return 0;
   }

   /**
    *  Returns a string representation of the class.
    *
    *  @return The string representation of the class.
    */
   public String toString()
   {
      return name;
   }

   /**
    *  Adds a class to the list of called classes.
    *  If the class is alread in the list, then the
    *  number of times that it is called is incremented.
    *
    *  @param  obj   The called class.
    */
   public void callsObject(ObjectClass obj)
   {
      // check if the object has already been called
      int count = 0;
      ListIterator it = calledObjects.listIterator();
      while(it.hasNext())
      {
         if(((ObjectClass)it.next()).equals(obj))
            break;
         count++;
      }

      if(count==numCalledObjects) // the object is being called for the first time
      {
         calledObjects.add(obj);
         calledObjectCount.add(new Integer(1));
         numCalledObjects++;
      }
      else // the object has been called before - increment the number of times it has been called
      {
         int num = Integer.parseInt(calledObjectCount.get(count).toString()) + 1;
         calledObjectCount.set(count,new Integer(num));
      }
   }

   /**
    *  Returns the list of classes called by this class.
    *
    *  @return The list of classes called by this class.
    */
   public ArrayList getCalledObjects()
   {
      return calledObjects;
   }

   /**
    *  Returns the number of times, that a class in the
    *  list of called classes, is called.
    *
    *  @param  n  The index of the called class in the list.
    *  @return The number of times, that a class in the
    *          list of called classes, is called.
    */
   public int getCalledObjectCount(int n)
   {
      return Integer.parseInt(calledObjectCount.get(n).toString());
   }

   /**
    *  Returns the number of classes called by this class.
    *
    *  @return The number of classes called by this class.
    */
   public int getNumCalledObjects()
   {
      return numCalledObjects;
   }

   /**
    *  Adds a method to the list of called methods.
    *  If the method is alread in the list, then the
    *  number of times that it is called is incremented.
    *
    *  @param  method   The method called.
    */
   public void callsMethod(MethodClass method)
   {
      // check if the method already exists
      ListIterator it = calledMethods.listIterator();
      MethodClass temp;
      boolean foundMethod = false;
      int count = 0;
      while(it.hasNext())
      {
         temp = (MethodClass)it.next();
         // if the method is present - increment the count for this method
         if(method.getName().equals(temp.getName()))
         {
            int num = Integer.parseInt(methodCounts.get(count).toString()) + 1;
            methodCounts.set(count,new Integer(num));
            foundMethod = true;
            break;
         }
         count++;
      }
      if(!foundMethod) // it is a new method
      {
         calledMethods.add(method);
         methodCounts.add(new Integer(1));
         numCalledMethods++;
      }
   }

   /**
    *  Toggles between showing and hiding the methods
    *  called by the class.
    */
   public void setShowMethods()
   {
      if(showMethods)
         showMethods = false;
      else
         showMethods = true;
   }

   /**
    *  Returns whether or not to show the methods
    *  called by the class.
    *
    *  @return Whether or not to show the methods
    *          called by the class.
    */
   public boolean getShowMethods()
   {
       return showMethods;
   }

   /**
    *  Returns the number of methods called by this class.
    *
    *  @return The number of methods called by this class.
    */
   public int getNumCalledMethods()
   {
      return numCalledMethods;
   }

   /**
    *  Returns the list of methods called by this class.
    *
    *  @return The list of methods called by this class.
    */
   public ArrayList getCalledMethods()
   {
      return calledMethods;
   }

   /**
    *  Returns the number of times, that a method in the
    *  list of called methods, is called.
    *
    *  @param  n  The index of the called method in the list.
    *  @return The number of times, that a method in the
    *          list of called methods, is called.
    */
   public int getMethodCount(int n)
   {
      return Integer.parseInt(methodCounts.get(n).toString());
   }
}