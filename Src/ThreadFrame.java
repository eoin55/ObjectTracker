package ObjectTracker;

import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 *  This class implements an internal frame that
 *  displays the methods executed by a thread in
 *  the running target program.
 *
 *  @author  Eoin O'Connor
 *  @see SequencePanel
 *  @see ThreadManager
 *  @see ClassPanel
 */
public class ThreadFrame extends JInternalFrame
{
   /**
    *  Used to display content in the frame.
    */
   private Container jifContainer;
   /**
    *  Displays the execution of methods by the thread.
    */
   private SequencePanel sequencePanel;
   /**
    *  Displays the classes used by the thread.
    */
   private ClassPanel classPanel;
   /**
    *  Used to scroll the SequencePanel.
    */
   private JScrollPane sequenceScroller;
   /**
    *  Used to scroll the ClassPanel.
    */
   private JScrollPane classScroller;
   /**
    *  Used to split the SequencePanel and the ClassPanel.
    */
   private JSplitPane splitPane;
   /**
    *  The initial width of the frame.
    */
   private int width;
   /**
    *  The initial height of the frame.
    */
   private int height;
   /**
    *  A text field used to display information
    *  about the classes and methods.
    */
   private JTextField info;

   /**
    *  Constructor: creates a new ThreadFrame. Initializes
    *  its SequencePanel and ClassPanel.
    *
    *  @param  threadName  The name of the thread being displayed.
    */
   public ThreadFrame(String threadName)
   {
      // set properties
      width = 250;
      height = 400;
      setClosable(true);
      setMaximizable(true);
      setIconifiable(true);
      setResizable(true);
      setSize(width,height/2);
      setTitle("Thread - " + threadName);

      info = new JTextField();
      info.setEditable(false);

      sequencePanel = new SequencePanel(threadName,info);
      sequenceScroller = new JScrollPane(sequencePanel); // enables scoll bars
      sequenceScroller.getVerticalScrollBar().setUnitIncrement(20);
      sequenceScroller.getHorizontalScrollBar().setUnitIncrement(20);
      sequenceScroller.isWheelScrollingEnabled();

      classPanel = new ClassPanel(info);
      classScroller = new JScrollPane(classPanel); // enables scoll bars
      classScroller.getVerticalScrollBar().setUnitIncrement(20);
      classScroller.getHorizontalScrollBar().setUnitIncrement(20);
      classScroller.isWheelScrollingEnabled();

      splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,sequenceScroller,classScroller);
      splitPane.setContinuousLayout(true);
      splitPane.setOneTouchExpandable(true);
      splitPane.setDividerLocation(width/2);

      jifContainer = getContentPane();
      jifContainer.add(splitPane,"Center");
      jifContainer.add(info,"North");
   }

   /**
    *  A new method has been executed. Update the
    *  the SequencePanel and ClassPanel to take
    *  account of this.
    *
    *  @param  manager  An updated version of the
    *                   ThreadManager.
    */
   public void writeMethod(ThreadManager manager)
   {
      sequencePanel.writeMethod(manager);
      classPanel.writeMethod(manager);
   }

   /**
    *  A method has been exited. Update the
    *  the SequencePanel to take account of this.
    *
    *  @param  manager  An updated version of the
    *                   ThreadManager.
    */
   public void endMethod(ThreadManager manager)
   {
      sequencePanel.endMethod(manager);
   }

   /**
    *  Sets the height of the SequencePanel's scrollbar.
    *
    *  @param  scrollBarHeight   The new height of the
    *                            SequencePanel's scrollbar.
    */
   public void setScrollbarHeight(int scrollBarHeight)
   {
      sequenceScroller.getVerticalScrollBar().setValue(scrollBarHeight);
   }

   /**
    *  Sets the width of the SequencePanel's scrollbar.
    *
    *  @param  scrollBarWidth The new width of the
    *                         SequencePanel's scrollbar.
    */
   public void setScrollbarWidth(int scrollBarWidth)
   {
      sequenceScroller.getHorizontalScrollBar().setValue(scrollBarWidth);
   }

   /**
    *  Returns the current position of the SequencePanel's scrollbar.
    *
    *  @return The current position of the SequencePanel's scrollbar.
    */
   public int getScrollbarPosition()
   {
      return sequenceScroller.getVerticalScrollBar().getValue();
   }
}