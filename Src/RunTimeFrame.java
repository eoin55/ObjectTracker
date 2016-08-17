package ObjectTracker;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 *  This class implements an internal frame that
 *  displays the methods executed by a thread in
 *  the running target program and their runtimes.
 *
 *  @author  Eoin O'Connor
 *  @see RunTimePanel
 *  @see ObjectDisplayer
 */
public class RunTimeFrame extends JInternalFrame
{
   /**
    *  Used to display the methods and their runtimes.
    */
   private RunTimePanel runTimePanel;
   /**
    *  The initial width of the frame.
    */
   private int width;
   /**
    *  The initial height of the frame.
    */
   private int height;
   /**
    *  Used to display content in the frame.
    */
   private Container contentPane;
   /**
    *  Used to scroll the RunTimePanel.
    */
   private JScrollPane scroller;
   /**
    *  A text field used to display the number
    *  of methods and their scale.
    */
   private JTextField scaleField;

   /**
    *  Constructor: creates a new RunTimeFrame.
    *  Initializes its RunTimePanel.
    */
   public RunTimeFrame()
   {
      // set properties
      width = 250;
      height = 400;
      setClosable(true);
      setMaximizable(true);
      setIconifiable(true);
      setResizable(true);
      setSize(width,height/2);
      setTitle("Method Execution Times");

      contentPane = getContentPane();
      runTimePanel = new RunTimePanel();
      scroller = new JScrollPane(runTimePanel);
      runTimePanel.setScrollPane(scroller);
      scaleField = new JTextField();
      runTimePanel.setScaleField(scaleField);
      contentPane.add(scaleField,"North");
      contentPane.add(scroller,"Center");
   }

   /**
    *  A new method has been executed. Update the
    *  the RunTimePanel to take account of this.
    *
    *  @param  method   The new method.
    */
   public void addMethod(MethodClass method)
   {
      runTimePanel.addMethod(method);
   }

   /**
    *  Searches the RunTimePanel for a string.
    *  Returns true if it finds one.
    *
    *  @return True if a match is found.
    *  @param  searchString   The string to search for.
    */
   public boolean findMethod(String searchString)
   {
      return runTimePanel.findMethod(searchString);
   }
}