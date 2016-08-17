package ObjectTracker;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/**
 *  This class implements a panel that displays the
 *  methods executed by a thread in the running
 *  target program.
 *
 *  @author  Eoin O'Connor
 *  @see ThreadManager
 *  @see ThreadFrame
 *  @see MethodClass
 *  @see ObjectClass
 */
public class SequencePanel extends JPanel
{
   /**
    *  Used to draw the 2D shapes.
    */
   private Graphics2D g2;
   /**
    *  Contains the list of methods to be drawn.
    */
   private ThreadManager manager;
   /**
    *  The width of the panel.
    */
   private int width;
   /**
    *  The height of the panel.
    */
   private int height;
   /**
    *  The name of the thead being executed.
    */
   private String threadName;
   /**
    *  A text field used to display information
    *  about the classes.
    */
   private JTextField info;
   /**
    *  Used to render the graphics.
    */
   private RenderingHints renderHints;

   /**
    *  Constructor: initializes the panel and sets
    *  listeners for mouse clicks. Sets the rendering
    *  for the graphics.
    *
    *  @param  threadName  The name of the thread.
    *  @param  info  A JTextField used to display
    *                information about the methods.
    */
   public SequencePanel(String threadName,JTextField info)
   {
      manager = new ThreadManager();
      width = 0;
      height = 0;
      threadName = null;
      setPreferredSize(new Dimension(width,height));
      MouseClickListener listener = new MouseClickListener();
      addMouseListener(listener);
      this.threadName = threadName;
      this.info = info;
      setBackground(Color.white);

      renderHints =  new RenderingHints(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
      renderHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
      renderHints.put(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
      renderHints.put(RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_DISABLE);
      renderHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
      renderHints.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
      renderHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
   }

   /**
    *  Paints the panel. Draws the methods executed
    *  by the thread.
    *
    *  @param  g  Used to draw the graphics.
    */
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      g2 = (Graphics2D)g;
      g2.setRenderingHints(renderHints);
      g2.setFont(new Font("Courier new",0,12)); // the Courier New chars are about 7 pixels wide

      // displays a method's name and its sequence bar
      Rectangle rect = getVisibleRect(); // visible part of the panel
      ListIterator iterator = manager.getMethodList().listIterator();
      MethodClass meth;
      while(iterator.hasNext())
      {
         try
         {
            meth = (MethodClass)iterator.next();
            // only show the methods that are visible
            if(meth.getHeight() < rect.getY() + rect.getHeight() + 10)
            {
               if(rect.getY() < meth.getHeight() + meth.getBarLength())
               {
                  g2.drawString(meth.getName(),meth.getWidth(),meth.getHeight());
                  g2.draw(new Rectangle(meth.getWidth()-7,meth.getHeight()-8,5,meth.getBarLength()));
                  g2.setPaint(meth.getObjectClass().getColour());
                  g2.fill(new Rectangle(meth.getWidth()-6,meth.getHeight()-7,4,meth.getBarLength()-1));
                  g2.setPaint(Color.black);
                  if(!meth.getIsFirstMethod())
                  {
                     g2.drawLine(meth.getWidth()-8,meth.getHeight()-3,meth.getWidth()-31,meth.getHeight()-3);
                     g2.drawLine(meth.getWidth()-8,meth.getHeight()-3,meth.getWidth()-13,meth.getHeight()-6);
                     g2.drawLine(meth.getWidth()-8,meth.getHeight()-3,meth.getWidth()-13,meth.getHeight());
                  }
               }
            }
         }
         catch(ConcurrentModificationException e)
         {
            e.printStackTrace();
            return;
         }
      }
   }

   /**
    *  A new method has been executed. Update the
    *  the panel to take account of this.
    *
    *  @param  manager  An updated version of the
    *                   ThreadManager.
    */
   public void writeMethod(ThreadManager manager)
   {
      this.manager = manager;

      if((manager.getStackCount()*30)+(manager.getMethod(manager.getNumMethods()-1).getName().length()*7) > width)
      {
         width = ((manager.getStackCount()*30)+(manager.getMethod(manager.getNumMethods()-1).getName().length()*7))+10;
         setPreferredSize(new Dimension(width,height));
         revalidate();
      }
   }

   /**
    *  A method has been exited. Update the
    *  the panel to take account of this.
    *
    *  @param  manager  An updated version of the
    *                   ThreadManager.
    */
   public void endMethod(ThreadManager manager)
   {
      this.manager = manager;

      if(manager.getBiggestBarLength()+20 > height)
      {
         height = manager.getBiggestBarLength() + 40;
         setPreferredSize(new Dimension(width,height));
         revalidate();
      }
   }

   /**
    *  This class listens for the mouse to click on
    *  a method in the panel.
    */
   private class MouseClickListener extends MouseAdapter
   {
      /**
       *  If the mouse is clicked on the panel this
       *  method checks if a method name or a method bar
       *  has been clicked. If so, the method's name,
       *  start time and runtime are displayed in the
       *  information field.
       *
       *  @param  event The MouseEvent that has occurred.
       */
      public void mouseClicked(MouseEvent event)
      {
         Point point = new Point(event.getX(),event.getY());

         // searches for the method that has been clicked on
         ListIterator iterator = manager.getMethodList().listIterator();
         MethodClass meth;
         Rectangle rect1,rect2;
         while(iterator.hasNext())
         {
            try
            {
               meth = (MethodClass)iterator.next();
               rect1 = new Rectangle(meth.getWidth()-7,meth.getHeight()-8,5,meth.getBarLength());
               rect2 = new Rectangle(meth.getWidth(),meth.getHeight()-10,(meth.getName().length()*7),12);
               if(rect1.contains(point) || rect2.contains(point))
               {
                  if(meth.getExecutionTime()>=0)
                     info.setText(meth.getName() + " | Start time: " + meth.getStartTime() + " | Execution time: " + meth.getExecutionTime());
                  else
                     info.setText(meth.getName() + " | Start time: " + meth.getStartTime());
                  break;
               }
            }
            catch(ConcurrentModificationException e)
            {
               e.printStackTrace();
               return;
            }
         }
      }
   }
}