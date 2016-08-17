package ObjectTracker;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/**
 *  This class implements a panel that displays a
 *  list of classes that have been used by the
 *  current thread in the target program. Each
 *  class is given a colour to help to distinguish
 *  it in the SequencePanel.
 *
 *  @author  Eoin O'Connor
 *  @see SequencePanel
 *  @see ThreadManager
 *  @see ThreadFrame
 *  @see ObjectClass
 */
public class ClassPanel extends JPanel
{
   /**
    *  Used to draw the 2D shapes.
    */
   private Graphics2D g2;
   /**
    *  Contains the list of classes to be drawn.
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
    *  A text field used to display information
    *  about the classes.
    */
   private JTextField info;

   /**
    *  Constructor: initializes the panel and sets
    *  listeners for mouse clicks.
    *
    *  @param  info  A JTextField used to display
    *                information about the classes.
    */
   public ClassPanel(JTextField info)
   {
      setPreferredSize(new Dimension(width,height));
      MouseClickListener listener = new MouseClickListener();
      addMouseListener(listener);
      this.info = info;
      manager = new ThreadManager();
      width = 0;
      height = 0;
   }

   /**
    *  Paints the panel. Draws the list of classes.
    *  Each class name is draw in the colour
    *  associated with it.
    *
    *  @param  g  Used to draw the graphics.
    */
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      g2 = (Graphics2D)g;
      setBackground(Color.white);

      int ypos = 20;
      int xpos = 0;
      g2.setPaint(Color.black);
      g2.setFont(new Font("Arial",Font.BOLD,12));

      String key = "Key: Class Colours";
      g2.drawString(key,5,ypos);
      if((key.length()*6)+5 > width)
      {
         width = (key.length()*6)+15;
         setPreferredSize(new Dimension(width,height));
         revalidate();
      }

      ypos += 40;
      g2.setFont(new Font("courier new",Font.BOLD,12));

      // displays a method's name and its sequence bar
      ListIterator iterator = manager.getClassList().listIterator();
      ObjectClass obj;
      while(iterator.hasNext())
      {
         try
         {
            obj = (ObjectClass)iterator.next();
            g2.setPaint(obj.getColour());
            g2.drawString(obj.getName(),5,ypos);
            ypos += 20;

            if(ypos > height)
            {
               height += 40;
               setPreferredSize(new Dimension(width,height));
               revalidate();
            }

            if((obj.getName().length()*7)+5 > width)
            {
               width = (obj.getName().length()*7)+15;
               setPreferredSize(new Dimension(width,height));
               revalidate();
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
      repaint();
   }

   /**
    *  This class listens for the mouse to click on
    *  a class in the ClassPanel.
    */
   private class MouseClickListener extends MouseAdapter
   {
      /**
       *  If the mouse is clicked on the panel this
       *  method checks if a class name has been clicked
       *  on. If a class has been clicked on then its
       *  name and start time is displayed in the
       *  information field.
       *
       *  @param  event The MouseEvent that has occurred.
       */
      public void mouseClicked(MouseEvent event)
      {
         Point point = new Point(event.getX(),event.getY());

         ListIterator iterator = manager.getClassList().listIterator();
         ObjectClass obj;
         Rectangle rect;
         int ypos = 60;
         while(iterator.hasNext())
         {
            try
            {
               obj = (ObjectClass)iterator.next();
               rect = new Rectangle(5,ypos-10,(obj.getName().length()*7)+5,10);
               if(rect.contains(point))
                  info.setText(obj.getName() + " | " + "Start time: " + obj.getStartTime());
               ypos += 20;
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