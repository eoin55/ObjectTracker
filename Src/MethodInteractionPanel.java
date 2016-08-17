package ObjectTracker;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 *  This class represents a panel that displays the
 *  methods executed during the execution of the
 *  target program.
 *
 *  @author  Eoin O'Connor
 *  @see MethodClass
 *  @see PatternDisplayer
 *  @see ObjectDisplayer
 */
public class MethodInteractionPanel extends JPanel
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
    *  A text field used to display the number of methods.
    */
   private JTextField infoField;
   /**
    *  The width of the panel.
    */
   private int panelWidth;
   /**
    *  The height of the panel.
    */
   private int panelHeight;
   /**
    *  Used to scroll the panel.
    */
   private JScrollPane scroller;
   /**
    *  The list of known methods.
    */
   private ArrayList knownMethods;
   /**
    *  The number of known methods.
    */
   private int numKnownMethods;
   /**
    *  The list of colours to use.
    */
   private Color [] colours = {Color.black,Color.blue,Color.cyan,Color.gray,
      Color.green,Color.lightGray,Color.magenta,Color.orange,Color.pink,Color.red,Color.yellow};
   /**
    *  The current colour to use.
    */
   private int currColour;
   /**
    *  Used to line up the called methods.
    */
   private int rightWidth;

   /**
    *  Constructor: initializes the panel and sets
    *  the rendering for the graphics.
    *
    *  @param  field  A JTextField used to display
    *                the number of methods.
    */
   public MethodInteractionPanel(JTextField field)
   {
      knownMethods = new ArrayList();
      numKnownMethods = 0;
      currColour = 0;
      rightWidth = 0;
      panelWidth = 0;
      panelHeight = 0;
      setPreferredSize(new Dimension(panelWidth,panelHeight));
      setBackground(Color.white);
      infoField = field;
      infoField.setText("Number of methods: " + numKnownMethods);

      renderHints =  new RenderingHints(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
      renderHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
      renderHints.put(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
      renderHints.put(RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_DISABLE);
      renderHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
      renderHints.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
      renderHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
   }

   /**
    *  Paints the panel. Draws the list of methods.
    *  Draws the methods called by each method.
    *
    *  @param  g  Used to draw the graphics.
    */
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      g2 = (Graphics2D)g;
      g2.setRenderingHints(renderHints);
      g2.setFont(new Font("Courier new",0,12)); // the Courier New chars are about 7 pixels wide

      int width = 10;
      int height = 10;
      int callerHeight;

      MethodClass caller,called;
      String countString;
      ArrayList calledList;
      ListIterator callerIt,calledIt;
      Rectangle rect; // visible part of the panel

      try
      {
         // for each method, draw the method and the methods it calls
         callerIt = knownMethods.listIterator();
         while(callerIt.hasNext())
         {
            rect = getVisibleRect(); // visible part of the panel
            boolean displayedCaller = false; // indicates if the caller method was displayed

            // draw the caller method
            caller = (MethodClass)callerIt.next();

            // only display the method if it is visible
            if(height>rect.getY()-50 && height<rect.getHeight()+rect.getY()+50)
            {
               g2.draw(new Rectangle(width,height,(caller.getName().length()*7)+10,15));
               g2.setPaint(caller.getColour());
               g2.fill(new Rectangle(width+1,height+1,(caller.getName().length()*7)+9,14));
               if(isDark(caller.getColour()))
                  g2.setPaint(Color.white);
               else
                  g2.setPaint(Color.black);
               g2.drawString(caller.getName(),width+5,height+12);
               g2.setPaint(Color.black);
               displayedCaller = true;
            }

            // @rightWidth aligns the called methods
            if((caller.getName().length()*7)+60 > rightWidth)
               rightWidth = (caller.getName().length()*7)+60;

            callerHeight = height; // keeps track of the height

            int count = 0;
            calledList = caller.getCalledMethods();
            calledIt = calledList.listIterator();

            // draw the methods called by this method
            while(calledIt.hasNext())
            {
               called = (MethodClass)calledIt.next();
               countString = (new Integer(caller.getCalledMethodCount(count))).toString();

               // only display the method if it is visible or its caller method is visible
               if(displayedCaller || (height>rect.getY()-50 && height<rect.getHeight()+rect.getY()+50))
               {
                  g2.draw(new Rectangle(rightWidth,height,(called.getName().length()*7)+10,15));
                  g2.setPaint(called.getColour());
                  g2.fill(new Rectangle(rightWidth+1,height+1,(called.getName().length()*7)+9,14));
                  if(isDark(called.getColour()))
                     g2.setPaint(Color.white);
                  else
                     g2.setPaint(Color.black);
                  g2.drawString(called.getName(),rightWidth+5,height+12);
                  g2.setPaint(Color.black);
                  g2.drawString(countString,rightWidth+(called.getName().length()*7)+15,height+12);
                  g2.drawLine((caller.getName().length()*7)+20,callerHeight+7,rightWidth,height+7);
                  displayedCaller = true; // at least one of the called methods was displayed
               }
               height += 30;

               //  adjust the width of the panel if out of bounds
               if(panelWidth < rightWidth+(called.getName().length()*7)+15+(countString.length()*7))
               {
                  panelWidth = rightWidth+(called.getName().length()*7)+15+(countString.length()*7)+10;
                  setPreferredSize(new Dimension(panelWidth,panelHeight));
                  revalidate();
               }
               count++;
            }
            if(caller.getNumCalledMethods()==0)
               height += 30;

            //  adjust the height of the panel if out of bounds
            if(panelHeight < height)
            {
               panelHeight = height+10;
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
    *  @param  scrollPane  The new scrollpane.
    */
   public void setScrollPane(JScrollPane scrollPane)
   {
      scroller = scrollPane;
      scroller.getVerticalScrollBar().setUnitIncrement(20);
      scroller.getHorizontalScrollBar().setUnitIncrement(20);
      scroller.isWheelScrollingEnabled();
   }

   /**
    *  Indicates that a method calls a method. Adjusts the lists
    *  accordingly and updates the panel.
    *
    *  @param  caller  The caller method.
    *  @param  called  The called method.
    */
   public void methodStart(MethodClass caller,MethodClass called)
   {
      MethodClass meth;
      ListIterator it;
      boolean foundMethod;

      try
      {
         // check if the caller method has been encountered before
         foundMethod = false;
         it = knownMethods.listIterator();
         while(it.hasNext())
         {
            if(caller.equals((MethodClass)it.next()))
            {
               foundMethod = true;
               break;
            }
         }
         if(!foundMethod) // it is a new method, so add it to knownMethods
         {
            caller.setColour(colours[currColour++]);
            if(currColour==colours.length) 
               currColour = 0;
            knownMethods.add(caller);
            numKnownMethods++;
         }

         // check if the called method has been encountered before
         foundMethod = false;
         it = knownMethods.listIterator();
         while(it.hasNext())
         {
            meth = (MethodClass)it.next();
            if(called.equals(meth))
            {
               foundMethod = true;
               called.setColour(meth.getColour());
               break;
            }
         }
         if(!foundMethod) // it is a new method, so add it to knownMethods
         { 
            called.setColour(colours[currColour++]);
            if(currColour==colours.length) 
               currColour = 0;
            knownMethods.add(called);
            numKnownMethods++;
         }

         // add the called method to the caller method's list of called methods
         it = knownMethods.listIterator();
         while(it.hasNext())
         {
            meth = (MethodClass)it.next();
            if(caller.equals(meth))
            {
               meth.callsMethod(called);
               break;
            }
         }

         infoField.setText("Number of methods: " + numKnownMethods);

         repaint();
      }
      catch(ConcurrentModificationException e)
      {
         e.printStackTrace();
         return;
      }
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
      String methodName;

      int width = 10;
      int height = 10;
      int callerHeight;

      MethodClass caller,called;
      ArrayList calledList;
      ListIterator callerIt,calledIt;

      try
      {
         // check each caller method
         callerIt = knownMethods.listIterator();
         while(callerIt.hasNext())
         {
            // check the caller method
            caller = (MethodClass)callerIt.next();
            methodName = caller.getName().toLowerCase();
            if(scroller.getVerticalScrollBar().getValue()<height && methodName.indexOf(searchString)>-1)
            {
               scroller.getVerticalScrollBar().setValue(height);
               scroller.getHorizontalScrollBar().setValue(width);
               return true;
            }

            // @rightWidth aligns the called methods
            if((caller.getName().length()*7)+60 > rightWidth)
               rightWidth = (caller.getName().length()*7)+60;

            callerHeight = height; // keeps track of the height

            calledList = caller.getCalledMethods();
            calledIt = calledList.listIterator();

            // check the methods called by this object
            while(calledIt.hasNext())
            {
               called = (MethodClass)calledIt.next();
               methodName = called.getName().toLowerCase();
               if(scroller.getVerticalScrollBar().getValue()<height && methodName.indexOf(searchString)>-1)
               {
                  scroller.getVerticalScrollBar().setValue(height);
                  scroller.getHorizontalScrollBar().setValue(rightWidth);
                  return true;
               }

               height += 30;
            }
            if(caller.getNumCalledMethods()==0)
               height += 30;
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