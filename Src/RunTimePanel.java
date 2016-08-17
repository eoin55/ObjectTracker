package ObjectTracker;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 *  This class implements a panel that displays the
 *  methods executed by the target program and their
 *  runtimes.
 *
 *  @author  Eoin O'Connor
 *  @see ObjectDisplayer
 *  @see RunTimeFrame
 *  @see MethodClass
 */
public class RunTimePanel extends JPanel
{
   /**
    *  Used to draw the 2D shapes.
    */
   private Graphics2D g2;
   /**
    *  Used to render the graphics.
    */
   private RenderingHints renderHints;
   /**
    *  The number of methods executed by the program.
    */
   private int numMethods;
   /**
    *  Used to line up the runtime bars.
    */
   private int rightWidth;
   /**
    *  Used to scroll the panel.
    */
   private JScrollPane scroller;
   /**
    *  The width of the panel.
    */
   private int panelWidth;
   /**
    *  The height of the panel.
    */
   private int panelHeight;
   /**
    *  The list of methods executed by the program.
    */
   private Tree methodTree;
   /**
    *  A sorted list of methods executed by the program.
    */
   private ArrayList methodList;
   /**
    *  The scale that the runtime bars are measured by.
    */
   private int scale;
   /**
    *  A text field used to display the number of methods.
    */
   private JTextField scaleField;

   /**
    *  Constructor: initializes the panel and sets the rendering
    *  for the graphics.
    */
   public RunTimePanel()
   {
      setBackground(Color.white);
      panelWidth = 0;
      panelHeight = 0;
      rightWidth = 0;
      methodTree = new Tree();
      methodList = new ArrayList();
      numMethods = 0;
      scale = 1;

      renderHints =  new RenderingHints(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
      renderHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
      renderHints.put(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
      renderHints.put(RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_DISABLE);
      renderHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
      renderHints.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
      renderHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
   }

   /**
    *  A new method has been executed. Update the
    *  the panel to take account of this.
    *
    *  @param  method   The new method.
    */
   public void addMethod(MethodClass method)
   {
      methodTree.insert(method);
      methodList = methodTree.get();
      numMethods++;
      scaleField.setText("Number of methods: " + numMethods + " | Scale: 1 / " + (scale*500));
      repaint();
   }

   /**
    *  Paints the panel. Draws the list of methods executed
    *  by the program and and their runtimes. The list is
    *  sorted in order of longest runtime.
    *
    *  @param  g  Used to draw the graphics.
    */
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      g2 = (Graphics2D)g;
      g2.setRenderingHints(renderHints);
      g2.setFont(new Font("Courier new",0,12)); // the Courier New chars are about 7 pixels wide

      MethodClass method;
      String countString;
      int width = 10;
      int height = 20;
      Rectangle rect; // visible part of the panel
      int barLength;

      try
      {
         Iterator it = methodList.listIterator();
         while(it.hasNext())
         {
            method = (MethodClass)it.next();
            countString = "" + method.getExecutionTime();
            rect = getVisibleRect(); // visible part of the panel

            if(rightWidth < method.getName().length()*7)
               rightWidth = (method.getName().length()*7)+10;

            if((int)method.getExecutionTime()/scale > 500)
            {
               scale = scale + ((int)method.getExecutionTime()/500);
               scaleField.setText("Number of methods: " + numMethods + " | Scale: 1 / " + (scale*500));
            }
            barLength = (int)method.getExecutionTime()/scale;

            // only diplay methods that are visible
            if(height>rect.getY()-50 && height<rect.getHeight()+rect.getY()+50)
            {
               g2.draw(new Rectangle(rightWidth,height-10,barLength,10));
               g2.drawString(method.getName(),width,height);
               g2.setPaint(method.getColour());
               g2.fill(new Rectangle(rightWidth+1,height-9,barLength-1,9));
               g2.drawLine(width+(method.getName().length()*7),height-5,rightWidth-1,height-5);
               g2.setPaint(Color.black);
               g2.drawString(countString,rightWidth+barLength+5,height);
            }

            height += 20;

            //  adjust the width of the panel if out of bounds
            if(panelWidth < rightWidth + barLength + (countString.length()*7) + 5)
            {
               panelWidth = rightWidth + barLength + (countString.length()*7) + 15;
               setPreferredSize(new Dimension(panelWidth,panelHeight));
               revalidate();
            }

            //  adjust the height of the panel if out of bounds
            if(panelHeight < height)
            {
               panelHeight = height+20;
               setPreferredSize(new Dimension(panelWidth,panelHeight));
               revalidate();
            }
         }
      }
      catch(ConcurrentModificationException e)
      {
         e.printStackTrace();
         return;
      }
   }

   /**
    *  Sets the scrollpane.
    *
    *  @param  scroll  The new scrollpane.
    */
   public void setScrollPane(JScrollPane scroll)
   {
      scroller = scroll;
      scroller.getVerticalScrollBar().setUnitIncrement(20);
      scroller.getHorizontalScrollBar().setUnitIncrement(20);
      scroller.isWheelScrollingEnabled();
   }

   /**
    *  Sets the information field.
    *
    *  @param  field The new text field.
    */
   public void setScaleField(JTextField field)
   {
      scaleField = field;
      scaleField.setEditable(false);
      scaleField.setText("Number of methods: " + numMethods + " | Scale: 1 / " + (scale*500));
   }

   /**
    *  Returns true if colour is dark.
    *
    *  @param  colour   The colour to check.
    *  @return True if colour is dark.
    */
   private boolean isDark(Color colour)
   {
      if(colour.equals(Color.black) || colour.equals(Color.blue) || colour.equals(Color.gray))
         return true;
      return false;
   }

   /**
    *  Searches the method names for a string.
    *  If it finds one, it moves the scrollbars to focus on the
    *  found string and returns true.
    *
    *  @return True if a match is found.
    *  @param  searchString   The string to search for.
    */
   public boolean findMethod(String searchString)
   {
      searchString = searchString.toLowerCase();
      MethodClass method;
      String name;
      int width = 10;
      int height = 20;

      try
      {
         Iterator it = methodList.listIterator();
         while(it.hasNext())
         {
            method = (MethodClass)it.next();
            name = method.toString().toLowerCase();
            if(name.indexOf(searchString)>-1)
            {
               // check if the scrollbar is above the method found
               if(scroller.getVerticalScrollBar().getValue() < height-15)
               {
                  scroller.getVerticalScrollBar().setValue(height-15);
                  scroller.getHorizontalScrollBar().setValue(width);
                  return true;
               }
            }
            height += 20;
         }
      }
      catch(ConcurrentModificationException e)
      {
         e.printStackTrace();
         return false;
      }
      return false;
   }
}