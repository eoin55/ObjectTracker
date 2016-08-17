package ObjectTracker;

import java.awt.*;
import java.util.*;

/**
 *  This class represents a method that has been executed
 *  by the target program.
 *
 *  @author  Eoin O'Connor
 *  @see SequencePanel
 *  @see ObjectClass
 */
public class MethodClass implements Comparable
{
   /**
    *  The name of the method.
    */
   private String name;
   /**
    *  The x-coordinate of the method.
    */
   private int width;
   /**
    *  The y-coordinate of the method.
    */
   private int height;
   /**
    *  The number of methods called by the method.
    */
   private int numMethodsCalled;
   /**
    *  The length of the bar of the method.
    */
   private int barLength;
   /**
    *  The class that the method belongs to.
    */
   private ObjectClass objectClass;
   /**
    *  True if the method is the first to be called.
    */
   private boolean isFirstMethod;
   /**
    *  The start time of the method.
    */
   private long startTime;
   /**
    *  The end time of the method.
    */
   private long endTime;
   /**
    *  The total runtime of the method.
    */
   private long runTime;
   /**
    *  The list of methods called by the method.
    */
   private ArrayList calledMethods;
   /**
    *  The number of methods called by the method.
    */
   private int numCalledMethods;
   /**
    *  A list of the number of times the method
    *  calls each method.
    */
   private ArrayList calledMethodCount;
   /**
    *  The colour associated with the method.
    */
   private Color colour;

   /**
    *  Constructor: creates a new method.
    *
    *  @param  n  The name of the method.
    *  @param  oc The class of the method.
    *  @param  w  The x-coordinate of the method.
    *  @param  h  The y-coordinate of the method.
    */
   public MethodClass(String n,ObjectClass oc,int w,int h)
   {
      name = n;
      objectClass = oc;
      width = w*30;
      height = h;
      numMethodsCalled = 0;
      barLength = 20;
      isFirstMethod = false;
      startTime = System.currentTimeMillis();
      endTime = 0;
      runTime = 0;
      calledMethods = new ArrayList();
      calledMethodCount = new ArrayList();
      numCalledMethods = 0;
   }

   /**
    *  Returns the name of the method.
    *
    *  @return The name of the method.
    */
   public String getName()
   {
      return name;
   }

   /**
    *  Returns the x-coordinate of the method.
    *
    *  @return The x-coordinate of the method.
    */
   public int getWidth()
   {
      return width;
   }

   /**
    *  Returns the y-coordinate of the method.
    *
    *  @return The y-coordinate of the method.
    */
   public int getHeight()
   {
      return height;
   }

   /**
    *  Increments the number of methods called by
    *  the method.
    */
   public void incNumMethodsCalled()
   {
      numMethodsCalled++;
   }

   /**
    *  Returns the number of methods called by
    *  the method.
    *
    *  @return The number of methods called by
    *          the method.
    */
   public int getNumMethodsCalled()
   {
      return numMethodsCalled;
   }

   /**
    *  Increments the bar length by n.
    *
    *  @param  n  The amount to increment the bar by.
    */
   public void setBarLength(int n)
   {
      barLength += n;
   }

   /**
    *  Returns the bar length of the method.
    *
    *  @return The bar length of the method.
    */
   public int getBarLength()
   {
      return barLength;
   }

   /**
    *  Returns the class of the method.
    *
    *  @return The class of the method.
    */
   public ObjectClass getObjectClass()
   {
      return objectClass;
   }

   /**
    *  Sets this method as being the first to be called.
    */
   public void setIsFirstMethod()
   {
      isFirstMethod = true;
   }

   /**
    *  Returns whether this method is the first to be called.
    *
    *  @return Whether this method is the first to be called.
    */
   public boolean getIsFirstMethod()
   {
      return isFirstMethod;
   }

   /**
    *  Returns the start time of the method.
    *
    *  @return The start time of the method.
    */
   public long getStartTime()
   {
      return startTime;
   }

   /**
    *  Sets the end time of the method.
    */
   public void setEndTime()
   {
      endTime = System.currentTimeMillis();
   }

   /**
    *  Returns the run time of the method.
    *
    *  @return The run time of the method.
    */
   public long getExecutionTime()
   {
      return (endTime - startTime) + runTime;
   }

   /**
    *  Pauses the execution time of the method.
    *
    *  @param  stopTime The time that the execution is paused.
    */
   public void pauseExecutionTime(long stopTime)
   {
      if(endTime==0)
         runTime = runTime + (stopTime - startTime);
   }

   /**
    *  Restarts the execution time of the method.
    */
   public void restartExecutionTime()
   {
      if(endTime==0)
         startTime = System.currentTimeMillis();
   }

   /**
    *  Returns true if the method is equal to meth.
    *
    *  @param  meth  The method to be compared to.
    *  @return True if the method is equal to meth.
    */
   public boolean equals(MethodClass meth)
   {
      return name.equals(meth.getName());
   }

   /**
    *  Adds a method to the list of called methods.
    *  If the method is alread in the list, then the
    *  number of times that it is called is incremented.
    *
    *  @param  meth  The called method.
    */
   public void callsMethod(MethodClass meth)
   {
      // check if the method has already been called
      int count = 0;
      ListIterator it = calledMethods.listIterator();
      while(it.hasNext())
      {
         if(((MethodClass)it.next()).equals(meth))
            break;
         count++;
      }

      if(count==numCalledMethods) // the method is being called for the first time
      {
         calledMethods.add(meth);
         calledMethodCount.add(new Integer(1));
         numCalledMethods++;
      }
      else // the method has been called before - increment the number of times it has been called
      {
         int num = Integer.parseInt(calledMethodCount.get(count).toString()) + 1;
         calledMethodCount.set(count,new Integer(num));
      }
   }

   /**
    *  Returns the number of methods called by this method.
    *
    *  @return The number of methods called by this method.
    */
   public int getNumCalledMethods()
   {
      return numCalledMethods;
   }

   /**
    *  Returns the list of methods called by this method.
    *
    *  @return The list of methods called by this method.
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
    *          list of called method, is called.
    */
   public int getCalledMethodCount(int n)
   {
      return Integer.parseInt(calledMethodCount.get(n).toString());
   }

   /**
    *  Sets the colour of the method.
    *
    *  @param  c  The new colour of the method.
    */
   public void setColour(Color c)
   {
      colour = c;
   }

   /**
    *  Returns the colour of the method.
    *
    *  @return The colour of the method.
    */
   public Color getColour()
   {
      return colour;
   }

   /**
    *  Compares the method to obj.
    *
    *  @param  obj   The object to compare to.
    *  @return 1 if the method's runtime is longer than obj.
    *  @return -1 if the method's runtime is shorter that obj.
    *  @return 0 if the method has the same runtime as obj.
    */
   public int compareTo(Object obj)
   {
      if(getExecutionTime() > ((MethodClass)obj).getExecutionTime())
         return 1;
      else if(getExecutionTime() > ((MethodClass)obj).getExecutionTime())
         return -1;
      else
         return 0;
   }

   /**
    *  Returns a string representation of the method.
    *
    *  @return The string representation of the method.
    */
   public String toString()
   {
      return name + " " + getExecutionTime();
   }
}