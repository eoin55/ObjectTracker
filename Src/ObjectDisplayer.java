package ObjectTracker;

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *  This class receives information about the running target
 *  program and sends it to the appropriate panel to display it.
 *  It displays these panels in internal frames, which are kept
 *  on the ObjectDisplayer desktop.
 *
 *  @author  Eoin O'Connor
 *  @see ObjectClass
 *  @see MethodClass
 *  @see InstanceClass
 *  @see FieldClass
 *  @see ThreadManager
 *  @see ObjectInteractionPanel
 *  @see MethodInteractionPanel
 *  @see InstanceTrackerPanel
 *  @see RunTimeFrame
 */
public class ObjectDisplayer extends JDesktopPane
{
   /**
    *  Manages and displays the execution of threads.
    */
   private ThreadManager manager;
   /**
    *  The list of threads in the program.
    */
   private ArrayList threads;
   /**
    *  The number of threads in the program.
    */
   private int numThreads;
   /**
    *  Displays object interaction.
    */
   private ObjectInteractionPanel objectInteractionPanel;
   /**
    *  Displays method interaction.
    */
   private MethodInteractionPanel methodInteractionPanel;
   /**
    *  Displays the instances modified in the program.
    */
   private InstanceTrackerPanel instanceTrackerPanel;
   /**
    *  Displays methods and their runtimes.
    */
   private RunTimeFrame runTimeFrame;
   /**
    *  The find button in the Search Execution Tracker
    *  frame.
    */
   private JButton findNextButton;
   /**
    *  The close button in the Search Execution Tracker
    *  frame.
    */
   private JButton closeButton;
   /**
    *  The string entered into the Search Execution Tracker
    *  frame.
    */
   private String findMethodName;
   /**
    *  The thread chosen in the Search Execution Tracker
    *  frame.
    */
   private String findThreadName;
   /**
    *  The Search Execution Tracker frame.
    */
   private JInternalFrame findFrame;
   /**
    *  Used to enter a search string in the Search Execution Tracker
    *  frame.
    */
   private JTextField findField;
   /**
    *  Used to choose a thread in the Search Execution Tracker
    *  frame.
    */
   private JComboBox threadCombo;
   /**
    *  The search Execution Tracker menu option.
    */
   private JMenuItem findMenuItem;
   /**
    *  Used to add internal frames to the desktop.
    */
   private Container jifContainer;
   /**
    *  The main panel in the Search Execution Tracker
    *  frame.
    */
   private JPanel findPanel;
   /**
    *  A panel in the Search Execution Tracker
    *  frame. Used to display the thread combo box.
    */
   private JPanel threadPanel;
   /**
    *  A panel in the Search Execution Tracker
    *  frame. Used to display the thread combo box.
    */
   private JPanel threadLayerPanel;
   /**
    *  A panel in the Search Execution Tracker
    *  frame. Used to display the text field.
    */
   private JPanel inputPanel;
   /**
    *  A panel in the Search Execution Tracker
    *  frame. Used to display the text field.
    */
   private JPanel inputLayerPanel;
   /**
    *  A panel in the Search Execution Tracker
    *  frame. Used to display the buttons.
    */
   private JPanel buttonPanel;
   /**
    *  A panel in the Search Execution Tracker
    *  frame. Used to display the buttons.
    */
   private JPanel buttonLayerPanel;

   /**
    *  Constructor: creates a new ObjectDisplayer. Sets
    *  listeners for the search menu option.
    *
    *  @param  findMenuItem   The search Execution Tracker
    *                         menu option.
    */
   public ObjectDisplayer(JMenuItem findMenuItem)
   {
      findMethodName = null;
      findThreadName = null;
      threads = new ArrayList();
      numThreads = 0;
      this.findMenuItem = findMenuItem;
      findMenuItem.addActionListener(new FindButtonListener());

      runTimeFrame = new RunTimeFrame();
      add(runTimeFrame,new Integer(1));
      runTimeFrame.show();
      try
      {
         runTimeFrame.setIcon(true);
      }
      catch(java.beans.PropertyVetoException e)
      {
         e.printStackTrace();
         return;
      }
   }

   /**
    *  A new method has been executed. Update the Execution Tracker
    *  and the Pattern Tracker to take account of this.
    *
    *  @param  methodName  The name of the method.
    *  @param  className   The name of the class of the method.
    *  @param  threadName  The thread name that executed the method.
    *  @param  threadID    The ID number of the thread.
    */
   public void startMethod(String methodName,String className,String threadName,long threadID)
   {
      ListIterator iterator = threads.listIterator();
      while(iterator.hasNext())
      {
         manager = (ThreadManager)iterator.next();
         if(threadName.equals(manager.getThreadName()) && threadID==manager.getThreadID())
         {
            manager.startMethod(methodName,className);
            break;
         }
      }

      // Update the pattern displayer
      MethodClass called = manager.getLastMethodOnStack();
      if(called!=null && !called.getIsFirstMethod())
      {
         MethodClass caller = manager.getSecondLastMethodOnStack();
         ObjectClass callerClass = new ObjectClass(caller.getObjectClass().getName(),null);
         ObjectClass calledClass = new ObjectClass(called.getObjectClass().getName(),null);
         objectInteractionPanel.methodStart(callerClass,calledClass,called);
         methodInteractionPanel.methodStart(caller,called);
      }
   }

   /**
    *  A method has been exited. Update the Execution Tracker
    *  to take account of this.
    *
    *  @param  methodName  The name of the method.
    *  @param  threadName  The thread name that executed the method.
    *  @param  threadID    The ID number of the thread.
    */
   public void endMethod(String methodName,String threadName,long threadID)
   {
      ListIterator iterator = threads.listIterator();
      while(iterator.hasNext())
      {
         manager = (ThreadManager)iterator.next();
         if(threadName.equals(manager.getThreadName()) && threadID==manager.getThreadID())
         {
            // check that the ending method is the same as the one at the top of the stack
            if(manager.getStackCount()>0)
            {
               if(methodName.equals(manager.getLastMethodOnStack().getName()))
               {
                  MethodClass method = manager.endMethod(); // indicate that the method has ended
                  runTimeFrame.addMethod(method);
               }
            }
            break;
         }
      }
   }

   /**
    *  A new thread has been created. Start a new ThreadManager
    *  to manage and display its execution.
    *
    *  @param  threadName  The thread name.
    *  @param  threadID    The ID number of the thread.
    */
   public void startThread(String threadName,long threadID)
   {
      threads.add(new ThreadManager(threadName,threadID));
      ThreadFrame threadFrame = ((ThreadManager)threads.get(numThreads)).getThreadFrame();
      add(threadFrame,new Integer(1));
      try
      {
         threadFrame.setIcon(true);
      }
      catch(java.beans.PropertyVetoException e)
      {
         e.printStackTrace();
         return;
      }
      numThreads++;
   }

   /**
    *  Sets the ObjectInteractionPanel.
    *
    *  @param  objectInteractionPanel  The new ObjectInteractionPanel.
    */
   public void setObjectInteractionPanel(ObjectInteractionPanel objectInteractionPanel)
   {
      this.objectInteractionPanel = objectInteractionPanel;
   }

   /**
    *  Sets the MethodInteractionPanel.
    *
    *  @param  methodInteractionPanel  The new MethodInteractionPanel.
    */
   public void setMethodInteractionPanel(MethodInteractionPanel methodInteractionPanel)
   {
      this.methodInteractionPanel = methodInteractionPanel;
   }

   /**
    *  Sets the InstanceTrackerPanel.
    *
    *  @param  instanceTrackerPanel  The new InstanceTrackerPanel.
    */
   public void setInstanceTrackerPanel(InstanceTrackerPanel instanceTrackerPanel)
   {
      this.instanceTrackerPanel = instanceTrackerPanel;
   }

   /**
    *  Adds an instance and a field to the Instance Tracker
    *  to be displayed.
    *
    *  @param  instanceClass  The new instance.
    *  @param  field          The new field.
    */
   public void addInstanceClass(InstanceClass instanceClass,FieldClass field)
   {
      instanceTrackerPanel.addInstanceClass(instanceClass,field);
   }

   /**
    *  Adds an instance to the Instance Tracker to be displayed.
    *
    *  @param  instanceClass  The new instance.
    */
   public void addInstanceClass(InstanceClass instanceClass)
   {
      instanceTrackerPanel.addInstanceClass(instanceClass);
   }

   /**
    *  Pauses the execution time of each method in each thread.
    *
    *  @param  stopTime The time that the execution was paused.
    */
   public void pauseExecutionTime(long stopTime)
   {
      // for each thread pause the execution time
      ListIterator it = threads.listIterator();
      while(it.hasNext())
      {
         ((ThreadManager)(it.next())).pauseExecutionTime(stopTime);
      }
   }

   /**
    *  Restarts the execution time of each method in each thread.
    */
   public void restartExecutionTime()
   {
      // for each thread restart the execution time
      ListIterator it = threads.listIterator();
      while(it.hasNext())
      {
         ((ThreadManager)(it.next())).restartExecutionTime();
      }
   }

   /**
    *  Searches the method names of a chosen thread for a search string.
    *  If a match is found, then the thread window is selected
    *  and the scrollbars are scrolled down to focus on the match.
    *  If no (more) matches are found, an appropriate message
    *  is displayed.
    */
   private void findMethod()
   {
      findThreadName = (String)threadCombo.getSelectedItem();
      findMethodName = findField.getText();

      if(findThreadName.equals("Method Execution Times")) // search the method execution times frame
      {
         // search for the method
         if(findMethodName.length()>0)
         {
            boolean foundMethod = runTimeFrame.findMethod(findMethodName);

            // show the method in the thread frame
            if(foundMethod)
            {
               try
               {
                  // show the thread frame:
                  if(runTimeFrame.isIcon())
                     runTimeFrame.setIcon(false);
                  if(!runTimeFrame.isSelected())
                     runTimeFrame.setSelected(true);
               }
               catch(java.beans.PropertyVetoException e)
               {
                  e.printStackTrace();
               }
            }
            else // method not found
            {
               JOptionPane.showMessageDialog(this,
                  "There were no matches found for \"" + findMethodName + "\" in the Method Execution Times frame",
                  "No matches found",
                  JOptionPane.PLAIN_MESSAGE);
            }
         }
      }
      else // search a thread frame
      {
         // search for the chosen thread
         ListIterator iterator = threads.listIterator();
         while(iterator.hasNext())
         {
            manager = (ThreadManager)iterator.next();
            if(findThreadName.equals(manager.getThreadName()))
               break;
         }

         // search for the method
         if(findMethodName.length()>0)
         {
            MethodClass foundMethod = manager.findMethod(findMethodName,manager.getThreadFrame().getScrollbarPosition());

            // show the method in the thread frame
            if(foundMethod!=null)
            {
               try
               {
                  // show the thread frame:
                  if(manager.getThreadFrame().isIcon())
                     manager.getThreadFrame().setIcon(false);
                  if(!manager.getThreadFrame().isSelected())
                     manager.getThreadFrame().setSelected(true);
                  manager.getThreadFrame().setScrollbarHeight(foundMethod.getHeight()-10);
                  manager.getThreadFrame().setScrollbarWidth(foundMethod.getWidth()-10);
               }
               catch(java.beans.PropertyVetoException e)
               {
                  e.printStackTrace();
               }
            }
            else // method not found
            {
               JOptionPane.showMessageDialog(this,
                  "There were no matches found for \"" + findMethodName + "\" in thread \"" + findThreadName + "\"",
                  "No matches found",
                  JOptionPane.PLAIN_MESSAGE);
            }
         }
      }
   }

   /**
    *  Displays the Search Execution Tracker frame.
    */
   private void startFindFrame()
   {
      findFrame = new JInternalFrame();
      findFrame.setSize(250,135);
      findFrame.setTitle("Search Execution Tracker");
      findFrame.setClosable(true);
      jifContainer = findFrame.getContentPane();

         findPanel = new JPanel();
         findPanel.setLayout(new GridLayout(3,1));

            threadPanel = new JPanel();
            threadPanel.setLayout(new BorderLayout());
               threadLayerPanel = new JPanel();
               threadLayerPanel.setLayout(new FlowLayout());
            threadPanel.add(threadLayerPanel,"West");
               threadLayerPanel.add(new Label("Frame:"));
                  threadCombo = new JComboBox();
                  threadCombo.addItem(new String("Method Execution Times"));
                  ListIterator iterator = threads.listIterator();
                  while(iterator.hasNext())
                  {
                     manager = (ThreadManager)iterator.next();
                     threadCombo.addItem(manager.getThreadName());
                  }
               threadLayerPanel.add(threadCombo);

            inputPanel = new JPanel();
            inputPanel.setLayout(new BorderLayout());
               inputLayerPanel = new JPanel();
               inputLayerPanel.setLayout(new FlowLayout());
            inputPanel.add(inputLayerPanel,"West");
               inputLayerPanel.add(new Label("Find:"));
                  findField = new JTextField(10);
               inputLayerPanel.add(findField);

            buttonPanel = new JPanel();
            buttonPanel.setLayout(new BorderLayout());
               buttonLayerPanel = new JPanel();
               buttonLayerPanel.setLayout(new FlowLayout());
            buttonPanel.add(buttonLayerPanel,"West");
                  findNextButton = new JButton("Find");
                  findNextButton.addActionListener(new FindButtonListener());
               buttonLayerPanel.add(findNextButton);
                  closeButton = new JButton("Close");
                  closeButton.addActionListener(new FindButtonListener());
               buttonLayerPanel.add(closeButton);

         findPanel.add(threadPanel);
         findPanel.add(inputPanel);
         findPanel.add(buttonPanel);

      jifContainer.add(findPanel,"Center");
      findFrame.show();
      add(findFrame,new Integer(2));
      try
      {
         findFrame.setSelected(true);
      }
      catch(java.beans.PropertyVetoException e)
      {
         e.printStackTrace();
      }
   }

   /**
    *  This class listens for a button to be pressed.
    */
   private class FindButtonListener implements ActionListener
   {
      /**
       *  If the search Execution Tracker menu option is
       *  selected, the Search Execution Tracker frame is
       *  displayed. If the Find button is pressed, the
       *  chosen thread in the combo box is searched for
       *  a string matching the one in the text field.
       *  If the Close button is pressed, the Search
       *  Execution Tracker frame is closed.
       */
      public void actionPerformed(ActionEvent event)
      {
         Object source = event.getSource();
         if(source==findMenuItem)
         {
            startFindFrame();
         }
         else if(source==findNextButton)
         {
            findMethod();
         }
         else if(source==closeButton)
         {
            findFrame.dispose();
         }
      }
   }
}