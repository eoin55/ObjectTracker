package ObjectTracker;

import com.sun.jdi.*;
import com.sun.jdi.connect.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.Container;
import java.awt.event.*;

/**
 *  This class connects to the Java Virtual Machine and runs
 *  a Java program. It requests notification of certain events
 *  from the JVM and when these events occur, the information
 *  supplied by the event is used to send to the ObjectDisplayer
 *  so that it can be displayed graphically.
 *
 *  @author  Eoin O'Connor
 *  @see ObjectClass
 *  @see MethodClass
 *  @see InstanceClass
 *  @see FieldClass
 *  @see ObjectDisplayer
 *  @see IOPanel
 *  @see UserOptionsPanel
 */
public class ObjectTracker extends Thread
{
   /**
    *  The command-line arguments for the target program.
    */
   private String cmdLineArgs;
   /**
    *  The name of the target program.
    */
   private String programName;
   /**
    *  The arguments to be passed to the JVM.
    */
   private Map arguments;
   /**
    *  Used to connect to the JVM.
    */
   private VirtualMachineManager manager;
   /**
    *  Used to connect to the JVM.
    */
   private LaunchingConnector connector;
   /**
    *  A reference to the JVM.
    */
   private VirtualMachine vm;
   /**
    *  True if the JDK classes should be displayed during execution.
    */
   private boolean includeJDKClasses;
   /**
    *  Manages and displays the information received from the JVM
    *  during the running of the target program.
    */
   private ObjectDisplayer displayer;
   /**
    *  Handles the input, error and output streams of the target program.
    */
   private IOPanel ioPanel;
   /**
    *  The main GUI frame.
    */
   private JFrame frame;
   /**
    *  Used to add content to the main GUI frame.
    */
   private Container contentPane;
   /**
    *  Splits the IOPanel and the ObjectDisplayer.
    */
   private JSplitPane viewerSplitPane;
   /**
    *  Splits the Execution Tracker and the Pattern Tracker.
    */
   private JSplitPane displayerSplitPane;
   /**
    *  The Set Class Filter menu item.
    */
   private JMenuItem filterMenuItem;
   /**
    *  The Set Breakpoints menu item.
    */
   private JMenuItem breakpointsMenuItem;
   /**
    *  The Run Program menu item.
    */
   private JMenuItem runMenuItem;
   /**
    *  The Pause Program menu item.
    */
   private JMenuItem pauseMenuItem;
   /**
    *  Contains options for the user in a menubar.
    */
   private UserOptionsPanel userOptionsPanel;
   /**
    *  The initial width of the frame.
    */
   private int width;
   /**
    *  The initial height of the frame.
    */
   private int height;
   /**
    *  Displays information about instances, objects and methods.
    */
   private PatternDisplayer patternDisplayer;
   /**
    *  The set of events that have occurred in the target program.
    */
   private EventSet es;
   /**
    *  Used to iterate through the EventSet.
    */
   private EventIterator it;
   /**
    *  An event from the JVM.
    */
   private Event event;
   /**
    *  The name of a method.
    */
   private String methodName;
   /**
    *  The name of a class.
    */
   private String className;
   /**
    *  The name of a thread.
    */
   private String threadName;
   /**
    *  The unique ID number of a thread.
    */
   private long threadID;
   /**
    *  The number of threads currently executing in the program.
    */
   private int numThreadsAlive;
   /**
    *  True if the program execution is paused.
    */
   private boolean isPause;
   /**
    *  True if the program has been started.
    */
   private boolean isStarted;
   /**
    *  The start time of the target program.
    */
   private long startTime;
   /**
    *  The end time of the target program.
    */
   private long endTime;
   /**
    *  A string of class names used to filter out all other classes from
    *  the execution of the program.
    */
   private String classFilterString;
   /**
    *  True if the class filter list has been set.
    */
   private boolean isFiltered;
   /**
    *  A list of class names used to filter out all other classes
    *  from the execution of the program.
    */
   private ArrayList filterList;
   /**
    *  A list of methods at which breakpoints are set.
    */
   private ArrayList breakpointMethodList;

   /**
    *  Constructor: Creates a new ObjectTracker. Sets up the GUI.
    *  Gets the program details and connects to the JVM.
    */
   public ObjectTracker()
   {
      width = 500;
      height = 700;
      numThreadsAlive = 0;
      isPause = true;
      isStarted = false;
      startTime = 0;
      endTime = 0;
      isFiltered = false;
      vm = null;

      setUpGUI();
      setUpProgram();
   }

   /**
    *  Sets up GUI frame. Initializes the IOPanel, the
    *  menubar, the ObjectDisplayer, the UserOptionsPanel
    *  and the PatternDisplayer. Adds them to its content
    *  pane.
    */
   private void setUpGUI()
   {
      // set up main frame:
      frame = new JFrame();
      frame.setSize(width,height);
      frame.setTitle("Graphical Java Displayer");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      contentPane = frame.getContentPane();

      // set up the user menu bar:
      userOptionsPanel = new UserOptionsPanel();
      UserControlsListener listener = new UserControlsListener();
      filterMenuItem = userOptionsPanel.getFilterMenuItem();
      breakpointsMenuItem = userOptionsPanel.getBreakpointsMenuItem();
      runMenuItem = userOptionsPanel.getRunMenuItem();
      pauseMenuItem = userOptionsPanel.getPauseMenuItem();
      runMenuItem.setEnabled(true);
      pauseMenuItem.setEnabled(false);
      filterMenuItem.addActionListener(listener);
      breakpointsMenuItem.addActionListener(listener);
      runMenuItem.addActionListener(listener);
      pauseMenuItem.addActionListener(listener);

      displayer = new ObjectDisplayer(userOptionsPanel.getExecMenuItem()); // displays objects, methods and threads
      ioPanel = new IOPanel(); // handles the I/O

      patternDisplayer = new PatternDisplayer();  // displays connections between classes
      patternDisplayer.setPatternMenuItem(userOptionsPanel.getPatternMenuItem());
      patternDisplayer.setShowLinesMenuItem(userOptionsPanel.getShowLinesMenuItem());
      patternDisplayer.setShowLinearMenuItem(userOptionsPanel.getShowLinearMenuItem());
      patternDisplayer.setReduceMenuItem(userOptionsPanel.getReduceMenuItem());
      patternDisplayer.setIncreaseMenuItem(userOptionsPanel.getIncreaseMenuItem());

      displayer.setObjectInteractionPanel(patternDisplayer.getObjectInteractionPanel());
      displayer.setMethodInteractionPanel(patternDisplayer.getMethodInteractionPanel());
      displayer.setInstanceTrackerPanel(patternDisplayer.getInstanceTrackerPanel());

      JPanel displayerPanel = new JPanel();
      JPanel titlePanel = new JPanel();
      displayerPanel.setLayout(new BorderLayout());
      titlePanel.add(new JLabel("Execution Tracker"));
      displayerPanel.add(titlePanel,"North");
      displayerPanel.add(displayer,"Center");

      JPanel patternDisplayerPanel = new JPanel();
      JPanel patternTitlePanel = new JPanel();
      patternDisplayerPanel.setLayout(new BorderLayout());
      patternTitlePanel.add(new JLabel("Pattern Tracker"));
      patternDisplayerPanel.add(patternTitlePanel,"North");
      patternDisplayerPanel.add(patternDisplayer,"Center");

      displayerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,displayerPanel,patternDisplayerPanel);
      displayerSplitPane.setContinuousLayout(true);
      displayerSplitPane.setOneTouchExpandable(true);
      displayerSplitPane.setDividerLocation(width/2);

      viewerSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,displayerSplitPane,ioPanel);
      viewerSplitPane.setContinuousLayout(true);
      viewerSplitPane.setOneTouchExpandable(true);
      viewerSplitPane.setDividerLocation(height/2);

      contentPane.add(viewerSplitPane,"Center");
      frame.setJMenuBar(userOptionsPanel.getMenuBar());
      frame.show();
   }

   /**
    *  Gets the programs details and tries to connect to
    *  the JVM. Sets the IOPanel to handle the target program's
    *  input, error and output streams. Pauses the execution of
    *  the program.
    */
   private void setUpProgram()
   {
      // get the program name and arguments
      userOptionsPanel.setOptions(frame);
      programName = userOptionsPanel.getProgramName();
      frame.setTitle("Graphical Java Displayer - " + programName);
      cmdLineArgs = userOptionsPanel.getCmdLineArgs();
      cmdLineArgs = programName + " " + cmdLineArgs;
      includeJDKClasses = userOptionsPanel.getFullExec();

      // prepare the program for running on the JVM
      manager = Bootstrap.virtualMachineManager();
      connector = manager.defaultConnector();
      arguments = connector.defaultArguments();
      setMain();

      try
      {
         vm = connector.launch(arguments);
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }

      requestClassPrepare(vm.eventRequestManager());
      requestThreadStart(vm.eventRequestManager());
      requestThreadDeath(vm.eventRequestManager());

      ioPanel.set(vm.process()); // handles the I/O streams

      breakpointMethodList = new ArrayList(); // contains the names of methods at which the program should halt
   }

   /**
    *  Begins the execution of the target program.
    */
   public void run()
   {
      vm.resume();
      eventLoop();
      endTime = System.currentTimeMillis();
      System.out.println("Run time: " + (endTime - startTime));
   }

   /**
    *  Sets the arguments to send to the JVM. Sets the
    *  class containing the 'main' method to be run.
    */
   private void setMain()
   {
      Connector.Argument arg;
      arg = (Connector.Argument)arguments.get("main");
      arg.setValue(cmdLineArgs);
   }

   /**
    *  Sets the class filter list.
    *
    *  @param  list  The new class filter list.
    */
   private void setClassFilter(ArrayList list)
   {
      isFiltered = true;
      filterList = list;
   }

   /**
    *  Requests notification of class preparation events.
    *
    *  @param  manager  The request manager.
    */
   private void requestClassPrepare(EventRequestManager manager)
   {
      ClassPrepareRequest request;
      request = manager.createClassPrepareRequest();
      request.setSuspendPolicy(ClassPrepareRequest.SUSPEND_EVENT_THREAD);
      request.enable();
   }

   /**
    *  Requests notification of thread creation events.
    *
    *  @param  manager  The request manager.
    */
   private void requestThreadStart(EventRequestManager manager)
   {
      ThreadStartRequest request;
      request = manager.createThreadStartRequest();
      request.setSuspendPolicy(ThreadStartRequest.SUSPEND_EVENT_THREAD);
      request.enable();
   }

   /**
    *  Requests notification of thread death events.
    *
    *  @param  manager  The request manager.
    */
   private void requestThreadDeath(EventRequestManager manager)
   {
      ThreadDeathRequest request;
      request = manager.createThreadDeathRequest();
      request.setSuspendPolicy(ThreadDeathRequest.SUSPEND_EVENT_THREAD);
      request.enable();
   }

   /**
    *  Requests notification of method entry events for a given class.
    *
    *  @param  manager  The request manager.
    *  @param  className   The class to get notification for.
    */
   private void requestMethodEntry(EventRequestManager manager,String className)
   {
      MethodEntryRequest request;
      request = manager.createMethodEntryRequest();
      request.addClassFilter(className);
      request.setSuspendPolicy(MethodEntryRequest.SUSPEND_EVENT_THREAD);
      request.enable();
   }

   /**
    *  Requests notification of method exit events for a given class.
    *
    *  @param  manager  The request manager.
    *  @param  className   The class to get notification for.
    */
   private void requestMethodExit(EventRequestManager manager,String className)
   {
      MethodExitRequest request;
      request = manager.createMethodExitRequest();
      request.addClassFilter(className);
      request.setSuspendPolicy(MethodExitRequest.SUSPEND_EVENT_THREAD);
      request.enable();
   }

   /**
    *  Requests notification of watchpoint modification events for a given field.
    *
    *  @param  manager  The request manager.
    *  @param  className   The field to get notification for.
    */
   private void requestModificationWatchpoint(EventRequestManager manager,Field field)
   {
      ModificationWatchpointRequest request;
      request = manager.createModificationWatchpointRequest(field);
      request.setSuspendPolicy(ModificationWatchpointRequest.SUSPEND_EVENT_THREAD);
      request.enable();
   }

   /**
    *  Loops through the event stack as the target program executes.
    *  Depending on the event, it sends it to the appropriate hander.
    */
   private void eventLoop()
   {
      try
      {
         while(true)
         {
            es = vm.eventQueue().remove();
            it = es.eventIterator(); 

            while(it.hasNext())
            {
               event = it.nextEvent();

               // check if the pause button has been pressed
               while(isPause)
               {
                  sleep(1000);
               }

               // the program has finished
               if(event instanceof VMDeathEvent)
               {
                  pauseMenuItem.setEnabled(false);
                  breakpointsMenuItem.setEnabled(false);

                  System.out.println("The program has ended.");
                  System.out.println();
                  return;
               }

               // a class has begun
               else if(event instanceof ClassPrepareEvent)
               {
                  classPrepareHandler();
               }

               // a method has begun
               else if(event instanceof MethodEntryEvent)
               {
                  methodEntryHandler();
               }

               // a method has ended
               else if(event instanceof MethodExitEvent)
               {
                  methodExitHandler();
               }

               // a thread has started
               else if(event instanceof ThreadStartEvent)
               {
                  threadStartHandler();
               }

               // a thread has ended
               else if(event instanceof ThreadDeathEvent)
               {
                  numThreadsAlive--;
               }

               // a modification watch point event has occurred
               else if(event instanceof ModificationWatchpointEvent)
               {
                  modificationWatchpointHandler();
               }

               // check if there is a pause
               while(isPause)
               {
                  sleep(1000);
               }

               resumeProgram();
            }
         }
      }
      catch(InterruptedException e)
      {
         e.printStackTrace();
         return;
      }
   }

   /**
    *  A class has been prepared. Requests notification of method
    *  and watchpoint events for that class, unless the user has
    *  chosen to exclude this class from being displayed.
    */
   private void classPrepareHandler()
   {
      try
      {
         className = ((ClassPrepareEvent)event).referenceType().name();

         if(includeJDKClasses) // include JDK classes
         {
            if(isFiltered) // only display filtered classes
            {
               if(filterList.contains(className))
               {
                  requestMethodEntry(vm.eventRequestManager(),className);
                  requestMethodExit(vm.eventRequestManager(),className);

                  // request Modification Watchpoints for the class' fields
                  List fieldList = ((ClassPrepareEvent)event).referenceType().fields();
                  Iterator it = fieldList.iterator();
                  while(it.hasNext())
                  {
                     Field field = (Field)it.next();
                     requestModificationWatchpoint(vm.eventRequestManager(),field);
                  }
               }
            }
            else // there is no filter
            {
               requestMethodEntry(vm.eventRequestManager(),className);
               requestMethodExit(vm.eventRequestManager(),className);

               // request Modification Watchpoints for the class' fields
               List fieldList = ((ClassPrepareEvent)event).referenceType().fields();
               Iterator it = fieldList.iterator();
               while(it.hasNext())
               {
                  Field field = (Field)it.next();
                  requestModificationWatchpoint(vm.eventRequestManager(),field);
               }
            }
         }
         else // only display non-JDK classes
         {
            if(className.indexOf("java.")!=0 && className.indexOf("javax.")!=0 && className.indexOf("org.")!=0 && className.indexOf("sun.")!=0 && className.indexOf("com.")!=0)
            {
               if(isFiltered) // only display filtered classes
               {
                  if(filterList.contains(className))
                  {
                     requestMethodEntry(vm.eventRequestManager(),className);
                     requestMethodExit(vm.eventRequestManager(),className);

                     // request Modification Watchpoints for the class' fields
                     List fieldList = ((ClassPrepareEvent)event).referenceType().fields();
                     Iterator it = fieldList.iterator();
                     while(it.hasNext())
                     {
                        Field field = (Field)it.next();
                        requestModificationWatchpoint(vm.eventRequestManager(),field);
                     }
                  }
               }
               else // there is no filter
               {
                  requestMethodEntry(vm.eventRequestManager(),className);
                  requestMethodExit(vm.eventRequestManager(),className);

                  // request Modification Watchpoints for the class' fields
                  List fieldList = ((ClassPrepareEvent)event).referenceType().fields();
                  Iterator it = fieldList.iterator();
                  while(it.hasNext())
                  {
                     Field field = (Field)it.next();
                     requestModificationWatchpoint(vm.eventRequestManager(),field);
                  }
               }
            }
         }
      }
      catch(VMDisconnectedException e)
      {
         e.printStackTrace();
         return;
      }
   }

   /**
    *  A method entry event has occurred. Updates the ObjectDisplayer to
    *  to take account of this. If the method is in the breakpoints list, the
    *  breakpoint handler is called.
    */
   private void methodEntryHandler()
   {
      try
      {
         if(numThreadsAlive>0)
         {
            methodName = ((MethodEntryEvent)event).method().toString();
            className = ((MethodEntryEvent)event).method().declaringType().name().toString();
            threadName = ((MethodEntryEvent)event).thread().name();
            threadID = ((MethodEntryEvent)event).thread().uniqueID();
            displayer.startMethod(methodName,className,threadName,threadID);

            if(breakpointMethodList.contains(methodName)) // breakpoint has been reached
               breakpointHandler();
         }
      }
      catch(VMDisconnectedException e)
      {
         e.printStackTrace();
         return;
      }
   }

   /**
    *  A method exit event has occurred. Updates the ObjectDisplayer
    *  to take account of this.
    */
   private void methodExitHandler()
   {
      try
      {
         if(numThreadsAlive>0)
         {
            methodName = ((MethodExitEvent)event).method().toString();
            threadName = ((MethodExitEvent)event).thread().name();
            threadID = ((MethodExitEvent)event).thread().uniqueID();
            displayer.endMethod(methodName,threadName,threadID);
         }
      }
      catch(VMDisconnectedException e)
      {
         e.printStackTrace();
         return;
      }
   }

   /**
    *  A thread creation event has occurred. Updates the ObjectDisplayr
    *  to take account of this.
    */
   private void threadStartHandler()
   {
      try
      {
         threadName = ((ThreadStartEvent)event).thread().name();
         threadID = ((ThreadStartEvent)event).thread().uniqueID();
         displayer.startThread(threadName,threadID);
         numThreadsAlive++;
      }
      catch(VMDisconnectedException e)
      {
         e.printStackTrace();
         return;
      }
   }

   /**
    *  A breakpoint method has been reached by the target program.
    *  Displays an appropriate message to the user and pauses
    *  the execution of the targer program. Pauses the execution
    *  times of methods in the ObjectDisplayer.
    */
   private void breakpointHandler()
   {
      try
      {
         MethodEntryEvent bpEvent = (MethodEntryEvent)event;
         String bpMethod = bpEvent.location().method().toString();
         String bpThread = bpEvent.thread().name();
         int bpLineNum = bpEvent.location().lineNumber();
         isPause = true; // pause the execution
         displayer.pauseExecutionTime(System.currentTimeMillis());
         runMenuItem.setEnabled(true);
         pauseMenuItem.setEnabled(false);
         JOptionPane.showMessageDialog(frame,
            "Breakpoint reached at:\nMethod: " + bpMethod + "\nLine number: " + bpLineNum + "\nThread: " + bpThread,
            "Breakpoint Reached",
            JOptionPane.PLAIN_MESSAGE);
      }
      catch(VMDisconnectedException e)
      {
         e.printStackTrace();
         return;
      }
   }

   /**
    *  A watchpoint event has occurred. Gets the information
    *  about the field and it instance. Sends the information
    *  to the ObjectDisplayer so that it can be displayed by
    *  the InstanceTrackerPanel.
    */
   private void modificationWatchpointHandler()
   {
      try
      {
         ModificationWatchpointEvent wpEvent = (ModificationWatchpointEvent)event;

         // get the instance informaion
         String objectName = null;
         long id = 0;
         if(wpEvent.object()!=null)
         {
            objectName = wpEvent.object().referenceType().name();
            id = wpEvent.object().uniqueID();
         }
         else
            objectName = "null";

         // get the field information
         String fieldName = wpEvent.field().name();
         String fieldType = wpEvent.field().typeName();
         String fieldValue = null;
         Value value = wpEvent.valueToBe();

         try
         {
            if(value instanceof ObjectReference)
            {
               if(value instanceof StringReference)
                  fieldValue = value.toString();
               else if(value instanceof ArrayReference)
                  fieldValue = value.type().name() + " (Length:" + ((ArrayReference)value).length() + ")";
               else
                  fieldValue = value.type().name() + " " + ((ObjectReference)value).uniqueID();
            }
            else
               fieldValue = value.toString();
         }
         catch(NullPointerException npe)
         {
            npe.printStackTrace();
            fieldValue = "null";
         }

         // add the instance and field information to the instance tracker
         InstanceClass instance = new InstanceClass(objectName,id);
         FieldClass field = new FieldClass(fieldName,fieldType,fieldValue);
         displayer.addInstanceClass(instance,field);

         // if the field value is an instance add it to the instance tracker
         if(value instanceof ObjectReference && !(value instanceof StringReference) && !(value instanceof ArrayReference))
         {
            objectName = value.type().name();
            id = ((ObjectReference)value).uniqueID();
            instance = new InstanceClass(objectName,id);
            displayer.addInstanceClass(instance);
         }
      }
      catch(VMDisconnectedException e)
      {
         e.printStackTrace();
         return;
      }
   }

   /**
    *  Restarts the execution of the target program.
    */
   private void resumeProgram()
   {
      try
      {
         vm.resume(); // restart the program
      }
      catch(VMDisconnectedException e)
      {
         e.printStackTrace();
         return;
      }
   }

   /**
    *  This class listens for a menu option in its menubar to
    *  be selected.
    */
   private class UserControlsListener implements ActionListener
   {
      /**
       *  If the Run Program menu item is selected, the program execution
       *  is either started or restarted and the method runtimes.
       *  are restarted. If the Pause Program menu item is selected,
       *  the program execution is paused and the method runtimes are
       *  paused. If the Set Class Filter is selected, the Set Class
       *  Filter dialog box is displayed and the class filter list
       *  can be set. If the Set Breakpoints menu item is selected,
       *  the Set Breakpoints dialog box is displayed and the list
       *  of method breakpoints can be set or reset.
       *
       *  @param  event The ActionEvent that has occurred.
       */
      public void actionPerformed(ActionEvent event)
      {
         Object source = event.getSource();

         if(source==runMenuItem)
         {
            if(!isStarted)
            {
               startTime = System.currentTimeMillis();
               isStarted = true;
               filterMenuItem.setEnabled(false);
            }
            if(isPause)
               displayer.restartExecutionTime();
            isPause = false;
            runMenuItem.setEnabled(false);
            pauseMenuItem.setEnabled(true);
         }
         else if(source==pauseMenuItem)
         {
            displayer.pauseExecutionTime(System.currentTimeMillis());
            isPause = true;
            runMenuItem.setEnabled(true);
            pauseMenuItem.setEnabled(false);
         }
         else if(source==filterMenuItem)
         {
            boolean setFilter = userOptionsPanel.setClassFilter(frame);
            if(setFilter)
            {
               setClassFilter(userOptionsPanel.getClassFilterList());
               filterMenuItem.setEnabled(false);
            }
         }
         else if(source==breakpointsMenuItem)
         {
            boolean setFilter = userOptionsPanel.setBreakpoints(frame);
            if(setFilter)
               breakpointMethodList = userOptionsPanel.getMethodList(); // new breakpoints
         }
      }
   }
}