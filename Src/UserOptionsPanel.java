package ObjectTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 *  This class implements a panel that contains a
 *  menubar. This menubar offers the user a range
 *  of options. This class also handles some of
 *  the functionality of these options.
 *
 *  @author  Eoin O'Connor
 *  @see ObjectTracker
 *  @see Browser
 *  @see FilterPanel
 *  @see BreakpointsPanel
 */
public class UserOptionsPanel extends JPanel
{
   /**
    *  The program name entered in the Set Program
    *  Options dialog box.
    */
   private String programName;
   /**
    *  The command-line arguments entered in the Set Program
    *  Options dialog box.
    */
   private String cmdLineArgs;
   /**
    *  Sets whether or not to display JDK classes
    *  during the execution of the target program.
    */
   private boolean fullExec;
   /**
    *  Used to listen for menu options to be selected.
    */
   private UserOptionsPanelListener listener;
   /**
    *  Used to set the class filter list.
    */
   private FilterPanel filterPanel;
   /**
    *  The list of classes to be displayed during
    *  the execution of the target program.
    */
   private ArrayList classFilterList;
   /**
    *  Used to set the breakpoints in the target program.
    */
   private BreakpointsPanel breakpointsPanel;
   /**
    *  The list of breakpoint methods.
    */
   private ArrayList methodList;
   /**
    *  Displays the menu options.
    */
   private JMenuBar menuBar;
   /**
    *  The "Options" menu.
    */
   private JMenu optionsMenu;
   /**
    *  The "Controls" menu.
    */
   private JMenu controlsMenu;
   /**
    *  The "Search" menu.
    */
   private JMenu findMenu;
   /**
    *  The "Help" menu.
    */
   private JMenu helpMenu;
   /**
    *  Displays the Set Class Filter dialog box.
    */
   private JMenuItem filterMenuItem;
   /**
    *  Displays the Set Breakpoints dialog box.
    */
   private JMenuItem breakpointsMenuItem;
   /**
    *  Closes the application.
    */
   private JMenuItem exitMenuItem;
   /**
    *  Starts the execution of the target program.
    */
   private JMenuItem runMenuItem;
   /**
    *  Pauses the execution of the target program.
    */
   private JMenuItem pauseMenuItem;
   /**
    *  Displays the search window for the Execution
    *  Tracker.
    */
   private JMenuItem execMenuItem;
   /**
    *  Displays the search window for the Pattern
    *  Tracker.
    */
   private JMenuItem patternMenuItem;
   /**
    *  Displays or hides the connection lines in
    *  Instance Tracker.
    */
   private JMenuItem showLinesMenuItem;
   /**
    *  Displays the instances in the Instance Tracker
    *  in a linear form or a graph form.
    */
   private JMenuItem showLinearMenuItem;
   /**
    *  Increases the distance between the instances in the
    *  Instance Tracker.
    */
   private JMenuItem increaseMenuItem;
   /**
    *  Reduces the distance between the instances in the
    *  Instance Tracker.
    */
   private JMenuItem reduceMenuItem;
   /**
    *  Displays a browser containing the User Manual
    *  web pages.
    */
   private JMenuItem helpMenuItem;

   /**
    *  Constructor: creates a new UserOptionsPanel.
    *  Initializes the menubar and sets the listener
    *  for the menu options.
    */
   public UserOptionsPanel()
   {
      programName = null;
      cmdLineArgs = null;
      fullExec = false;

      classFilterList = new ArrayList();
      breakpointsPanel = new BreakpointsPanel();
      methodList = new ArrayList();
      listener = new UserOptionsPanelListener();

      menuBar = new JMenuBar();

      controlsMenu = new JMenu("Controls");
      optionsMenu = new JMenu("Options");
      findMenu = new JMenu("Search");
      helpMenu = new JMenu("Help");

      exitMenuItem = new JMenuItem("Exit");
      runMenuItem = new JMenuItem("Run Program");
      pauseMenuItem = new JMenuItem("Pause Execution");
      filterMenuItem = new JMenuItem("Filter Classes");
      breakpointsMenuItem = new JMenuItem("Set Breakpoints");
      showLinesMenuItem = new JMenuItem();
      showLinearMenuItem = new JMenuItem();
      reduceMenuItem = new JMenuItem("Reduce Instance Tracker Size");
      increaseMenuItem = new JMenuItem("Increase Instance Tracker Size");
      execMenuItem = new JMenuItem("Execution Tracker");
      patternMenuItem = new JMenuItem("Pattern Tracker");
      helpMenuItem = new JMenuItem("User Manual");

      menuBar.add(controlsMenu);
      menuBar.add(optionsMenu);
      menuBar.add(findMenu);
      menuBar.add(helpMenu);

      controlsMenu.add(runMenuItem);
      controlsMenu.add(pauseMenuItem);
      controlsMenu.add(exitMenuItem);
      optionsMenu.add(filterMenuItem);
      optionsMenu.add(breakpointsMenuItem);
      optionsMenu.add(showLinesMenuItem);
      optionsMenu.add(showLinearMenuItem);
      optionsMenu.add(reduceMenuItem);
      optionsMenu.add(increaseMenuItem);
      findMenu.add(execMenuItem);
      findMenu.add(patternMenuItem);
      helpMenu.add(helpMenuItem);

      exitMenuItem.addActionListener(listener);
      helpMenuItem.addActionListener(listener);
   }

   /**
    *  Returns the menubar.
    *
    *  @return The menubar.
    */
   public JMenuBar getMenuBar()
   {
      return menuBar;
   }

   /**
    *  Displays the Set Program Options dialog box.
    *
    *  @param  frame The frame in which to display
    *                the dialog box.
    */
   public void setOptions(JFrame frame)
   {
      JTextField programNameField = new JTextField();
      JTextField cmdLineArgsField = new JTextField();
      JCheckBox fullExecBox = new JCheckBox("Include JDK Classes");
      Object [] message = {"Program name:",programNameField,"Command line arguments:",cmdLineArgsField,fullExecBox};
      Object [] buttons = {"Set"};
      int s = JOptionPane.showOptionDialog(
                    frame,
                    message,
                    "Set Program Options",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    buttons,
                    buttons[0]
                    );

      programName = programNameField.getText();
      cmdLineArgs = cmdLineArgsField.getText();
      fullExec = fullExecBox.isSelected();
   }

   /**
    *  Displays the Set Class Filter dialog box.
    *
    *  @param  frame The frame in which to display
    *                the dialog box.
    */
   public boolean setClassFilter(JFrame frame)
   {
      filterPanel = new FilterPanel();
      Object [] message = {filterPanel};
      Object [] buttons = {"Set","Cancel"};
      int s = JOptionPane.showOptionDialog(
                    frame,
                    message,
                    "Set Class Filter",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    buttons,
                    buttons[0]
                    );

      if(s==0) // set
      {
         classFilterList = filterPanel.getClassList();
         if(!classFilterList.isEmpty())
            return true;
      }
      return false; // cancel
   }

   /**
    *  Displays the Set Breakpoints dialog box.
    *
    *  @param  frame The frame in which to display
    *                the dialog box.
    */
   public boolean setBreakpoints(JFrame frame)
   {
      Object [] message = {breakpointsPanel};
      Object [] buttons = {"Set","Cancel"};
      int s = JOptionPane.showOptionDialog(
                    frame,
                    message,
                    "Set Breakpoints",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    buttons,
                    buttons[0]
                    );

      if(s==0) // set
      {
         methodList = new ArrayList(breakpointsPanel.getMethodList());
         return true;
      }
      else // cancel
      {
         breakpointsPanel.setMethodList(methodList); // reset method list
         return false;
      }
   }

   /**
    *  Returns the class filter list.
    *
    *  @return The class filter list.
    */
   public ArrayList getClassFilterList()
   {
      return classFilterList;
   }

   /**
    *  Returns the list of breakpoints methods.
    *
    *  @return The list of breakpoints methods.
    */
   public ArrayList getMethodList()
   {
      return methodList;
   }

   /**
    *  Returns the program name to be run.
    *
    *  @return The program name to be run.
    */
   public String getProgramName()
   {
      return programName;
   }

   /**
    *  Returns the command-line arguments.
    *
    *  @return The command-line arguments.
    */
   public String getCmdLineArgs()
   {
      return cmdLineArgs;
   }

   /**
    *  Returns whether or not to display JDK
    *  classes during the execution of the program.
    *
    *  @return Whether or not to display JDK classes
    *          during the execution of the program.
    */
   public boolean getFullExec()
   {
     return fullExec;
   }

   /**
    *  Returns the Set Class Filter menu option.
    *
    *  @return The Set Class Filter menu option.
    */
   public JMenuItem getFilterMenuItem()
   {
      return filterMenuItem;
   }

   /**
    *  Returns the Set Breakpoints menu option.
    *
    *  @return The Set Breakpoints menu option.
    */
   public JMenuItem getBreakpointsMenuItem()
   {
      return breakpointsMenuItem;
   }

   /**
    *  Returns the search Execution Tracker menu option.
    *
    *  @return The search Execution Tracker menu option.
    */
   public JMenuItem getExecMenuItem()
   {
      return execMenuItem;
   }

   /**
    *  Returns the search Pattern Tracker menu option.
    *
    *  @return The search Pattern Tracker menu option.
    */
   public JMenuItem getPatternMenuItem()
   {
      return patternMenuItem;
   }

   /**
    *  Returns the Run Program menu option.
    *
    *  @return The Run Program menu option.
    */
   public JMenuItem getRunMenuItem()
   {
      return runMenuItem;
   }

   /**
    *  Returns the Pause Program menu option.
    *
    *  @return The Pause Program menu option.
    */
   public JMenuItem getPauseMenuItem()
   {
      return pauseMenuItem;
   }

   /**
    *  Returns the Show Connection Lines menu option.
    *
    *  @return The Show Connection Lines menu option.
    */
   public JMenuItem getShowLinesMenuItem()
   {
      return showLinesMenuItem;
   }

   /**
    *  Returns the Show Instances in Linear/Graph Form menu option.
    *
    *  @return The Show Instances in Linear/Graph Form menu option.
    */
   public JMenuItem getShowLinearMenuItem()
   {
      return showLinearMenuItem;
   }

   /**
    *  Returns the Reduce Instance Tracker Size menu option.
    *
    *  @return The Reduce Instance Tracker Size menu option.
    */
   public JMenuItem getReduceMenuItem()
   {
      return reduceMenuItem;
   }

   /**
    *  Returns the Increase Instance Tracker Size menu option.
    *
    *  @return The Increase Instance Tracker Size menu option.
    */
   public JMenuItem getIncreaseMenuItem()
   {
      return increaseMenuItem;
   }

   /**
    *  Displays a browser containing the User Manual web pages.
    */
   public void launchHelp()
   {
      Browser browser = new Browser("http://www.redbrick.dcu.ie/~eoin55/year4/proj/docs/user_manual");
      browser.show();
   }

  /**
    *  This class listens for a menu option to be selected.
    */
   private class UserOptionsPanelListener implements ActionListener
   {
      /**
       *  If Exit menu option is selected, the application
       *  is closed down. If the User Manual menu option
       *  is selected, a browser containing the User Manual
       *  web pages is displayed.
       *
       *  @param  event The ActionEvent that has occurred.
       */
      public void actionPerformed(ActionEvent event)
      {
         Object source = event.getSource();

         if(source==exitMenuItem)
            System.exit(0);
         else if(source==helpMenuItem)
            launchHelp();
      }
   }
}