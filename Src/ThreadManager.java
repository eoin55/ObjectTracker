// this class manages the class and method information from the running program.

package ObjectTracker;

import java.util.*;
import java.awt.*;

/**
 *  This class manages the class and method information
 *  for a thread from the running target program and displays it in
 *  a ThreadFrame.
 *
 *  @author  Eoin O'Connor
 *  @see ObjectClass
 *  @see MethodClass
 *  @see ThreadFrame
 */
public class ThreadManager
{
   /**
    *  The list of methods encountered so far.
    */
   private ArrayList methodList;
   /**
    *  The number of methods encountered so far.
    */
   private int numMethods;
   /**
    *  A stack of currently executing method.
    */
   private LinkedList stackList;
   /**
    *  The graphical height of the last method encountered.
    */
   private int height;
   /**
    *  The number of currently executing method.
    */
   private int stackCount;
   /**
    *  The biggest bar length so far.
    */
   private int biggestBarLength;
   /**
    *  The list of classes encountered so far.
    */
   private ArrayList classes;
   /**
    *  The number of classes encountered so far.
    */
   private int numClasses;
   /**
    *  The list of colours to use.
    */
   private Color [] colours = {Color.black,Color.blue,Color.cyan,Color.gray,
      Color.green,Color.lightGray,Color.magenta,Color.orange,Color.pink,Color.red,Color.yellow};
   /**
    *  The current colour to use.
    */
   private int currentColour;
   /**
    *  The name of the thread.
    */
   private String threadName;
   /**
    *  The unique ID number of the thread.
    */
   private long threadID;
   /**
    *  Used to display the method and class data.
    */
   private ThreadFrame threadFrame;

   /**
    *  Constructor: creates a default ThreadManager.
    */
   public ThreadManager()
   {
      methodList = new ArrayList();
      numMethods = 0;
      stackList = new LinkedList();
      stackCount = 0;
      classes = new ArrayList();
      numClasses = 0;
      height = 20;
      threadName = null;
      threadID = 0;
      biggestBarLength = 0;
      currentColour = 0;
   }

   /**
    *  Constructor: creates a new ThreadManager for.
    *  a thread.
    *
    *  @param  t  The name of the thread.
    *  @param  id The unique ID number of the thread.
    */
   public ThreadManager(String t,long id)
   {
      methodList = new ArrayList();
      numMethods = 0;
      stackList = new LinkedList();
      stackCount = 0;
      classes = new ArrayList();
      numClasses = 0;
      height = 20;
      threadName = t;
      threadID = id;
      currentColour = 0;
      biggestBarLength = 0;
      threadFrame = new ThreadFrame(threadName);
      threadFrame.show();
   }

   /**
    *  Returns the ThreadFrame displaying the thread's
    *  execution.
    *
    *  @return The ThreadFrame displaying the thread.
    */
   public ThreadFrame getThreadFrame()
   {
      return threadFrame;
   }

   /**
    *  A new method has been started. Add it to the list of
    *  methods and to the method stack. Update the ThreadFrame
    *  to take account of this.
    *
    *  @param  methodName  The name of the new method.
    *  @param  className   The class that the method belongs to.
    */
   public void startMethod(String methodName,String className)
   {
      Color classColour = null;
      ObjectClass tempClass;
      boolean foundClass = false;

      // search for the class in the class list
      ListIterator classIterator = classes.listIterator();
      while(classIterator.hasNext())
      {
         tempClass = (ObjectClass)classIterator.next();
         if(tempClass.getName().equals(className))
         {
            foundClass = true;
            classColour = tempClass.getColour();
            break;
         }
      }

      // if the class is new, add it to the class list and assign it a colour
      if(!foundClass)
      {
         classes.add(new ObjectClass(className,colours[currentColour]));
         classColour = colours[currentColour];
         numClasses++;
         currentColour++;
         if(currentColour == colours.length)
            currentColour = 0;
      }

      // add the method to the method stack and the method list:
      height += 10;
      stackList.addLast(new Integer(numMethods));
      stackCount++;
      numMethods++;
      methodList.add(numMethods-1,new MethodClass(methodName,new ObjectClass(className,classColour),stackCount,height));

      if(stackCount==1)
         getMethod(numMethods-1).setIsFirstMethod();
      else
      {
         getMethod(Integer.parseInt(stackList.get(stackCount-2).toString())).getObjectClass().getName();
         getMethod(Integer.parseInt(stackList.get(stackCount-1).toString())).getObjectClass().getName();
      }

      // increment the bar lengths for each method that is alive
      ListIterator stackIterator = stackList.listIterator();
      while(stackIterator.hasNext())
         getMethod(Integer.parseInt(stackIterator.next().toString())).incNumMethodsCalled();

      threadFrame.writeMethod(this);
   }

   /**
    *  A method has been exited. Remove it from the method
    *  stack. Update the ThreadFrame to take account of this.
    */
   public MethodClass endMethod()
   {
      height += 20;
      stackCount--;

      if(stackCount > 0)
         getMethod(getStackNumber(stackCount-1)).setBarLength(getMethod(getStackNumber(stackCount)).getBarLength()+10);

      if(stackCount==0)
         getBiggestBarLength();

      MethodClass lastMethod = getMethod(getStackNumber(stackCount)); // get the last method on the stack
      lastMethod.setEndTime(); // set the time that the method exitted at
      stackList.removeLast();
      threadFrame.endMethod(this);

      return lastMethod;
   }

   /**
    *  Returns the number of methods in the method list.
    *
    *  @return The number of methods in the method list.
    */
   public int getNumMethods()
   {
      return numMethods;
   }

   /**
    *  Returns a method from the method list.
    *
    *  @param  n  The index of the method.
    *  @return The method at index n in the list.
    */
   public MethodClass getMethod(int n)
   {
        return (MethodClass)methodList.get(n);
   }

   /**
    *  Returns the index of a method in the list from the stack.
    *
    *  @param  n  The index of the method on the stack.
    *  @return The index of the method in the list.
    */
   public int getStackNumber(int n)
   {
      return Integer.parseInt(stackList.get(n).toString());
   }

   /**
    *  Returns the last method on the stack.
    *
    *  @return The last method on the stack.
    */
   public MethodClass getLastMethodOnStack()
   {
      if(stackCount>0)
         return getMethod(getStackNumber(stackCount-1));
      else
         return null;
   }

   /**
    *  Returns the second last method on the stack.
    *
    *  @return The second last method on the stack.
    */
   public MethodClass getSecondLastMethodOnStack()
   {
      return getMethod(getStackNumber(stackCount-2));
   }

   /**
    *  Returns the biggest bar length belonging to
    *  the methods on the stack.
    *
    *  @return The biggest bar length.
    */
   public int getBiggestBarLength()
   {
      int temp;
      MethodClass current;

      ListIterator iterator = stackList.listIterator();
      while(iterator.hasNext())
      {
         current = getMethod(Integer.parseInt(iterator.next().toString()));
         temp = current.getBarLength() + current.getHeight();
         if(temp > biggestBarLength)
            biggestBarLength = temp;
      }

      return biggestBarLength;
   }

   /**
    *  Returns the number of methods on the stack.
    *
    *  @return The number of methods on the stack.
    */
   public int getStackCount()
   {
      return stackCount;
   }

   /**
    *  Returns the number of classes in the class list.
    *
    *  @return The number of classes in the class list.
    */
   public int getNumClasses()
   {
      return numClasses;
   }

   /**
    *  Returns the class from the class list at index n.
    *
    *  @param  n  The index of the class.
    *  @return The class at index n.
    */
   public ObjectClass getClass(int n)
   {
      return (ObjectClass)classes.get(n);
   }

   /**
    *  Returns the method list.
    *
    *  @return The method list.
    */
   public ArrayList getMethodList()
   {
      return methodList;
   }

   /**
    *  Returns the class list.
    *
    *  @return The class list.
    */
   public ArrayList getClassList()
   {
      return classes;
   }

   /**
    *  Returns the thread name.
    *
    *  @return The thread name.
    */
   public String getThreadName()
   {
      return threadName;
   }

   /**
    *  Returns the unique thread ID number.
    *
    *  @return The thread ID number.
    */
   public long getThreadID()
   {
      return threadID;
   }

   /**
    *  Searches the method names for a string.
    *  It returns the method found.
    *
    *  @param  findMethodName The string to search for.
    *  @param  currentHeight  The height at which to start searching.
    *  @return The method match found.
    */
   public MethodClass findMethod(String findMethodName,int currentHeight)
   {
      findMethodName = findMethodName.toLowerCase();
      MethodClass tempMethod;
      String methName;
      ListIterator iterator = methodList.listIterator();
      while(iterator.hasNext())
      {
         tempMethod = (MethodClass)iterator.next();
         methName = tempMethod.getName().toLowerCase();
         if(methName.indexOf(findMethodName)>-1 && tempMethod.getHeight()-10 > currentHeight)
            return tempMethod;
      }
      return null;
   }

   /**
    *  Pauses the execution time for each method in the
    *  list.
    *
    *  @param  stopTime The time at which the program was paused.
    */
   public void pauseExecutionTime(long stopTime)
   {
      // for each method pause the execution time
      ListIterator it = methodList.listIterator();
      while(it.hasNext())
      {
         ((MethodClass)(it.next())).pauseExecutionTime(stopTime);
      }
   }

   /**
    *  Restarts the execution time for each method in the
    *  list.
    */
   public void restartExecutionTime()
   {
      // for each method restart the execution time
      ListIterator it = methodList.listIterator();
      while(it.hasNext())
      {
         ((MethodClass)(it.next())).restartExecutionTime();
      }
   }
}