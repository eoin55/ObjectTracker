package ObjectTracker;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/**
 *  This class represents a panel that displays the
 *  objects used during the execution of the
 *  target program.
 *
 *  @author  Eoin O'Connor
 *  @see ObjectClass
 *  @see PatternDisplayer
 *  @see ObjectDisplayer
 */
public class ObjectInteractionPanel extends JPanel
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
    *  The list of known classes.
    */
   private ArrayList knownObjects;
   /**
    *  The number of known classes.
    */
   private int numKnownObjects;
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
    *  Used to line up the called classes.
    */
   private int rightWidth;
   /**
    *  Used to line up the called methods.
    */
   private int methodWidth;

   /**
    *  Constructor: initializes the panel and sets
    *  the rendering for the graphics.
    *
    *  @param  field  A JTextField used to display
    *                the number of classes.
    */
   public ObjectInteractionPanel(JTextField field)
   {
      panelWidth = 0;
      panelHeight = 0;
      currColour = 0;
      rightWidth = 0;
      methodWidth = 0;
      setPreferredSize(new Dimension(panelWidth,panelHeight));
      setBackground(Color.white);
      infoField = field;
      infoField.setText("Number of classes: " + numKnownObjects);
      MouseClickListener listener = new MouseClickListener();
      addMouseListener(listener);
      knownObjects = new ArrayList();
      numKnownObjects = 0;

      renderHints =  new RenderingHints(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
      renderHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
      renderHints.put(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
      renderHints.put(RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_DISABLE);
      renderHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
      renderHints.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
      renderHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
   }

   /**
    *  Paints the panel. Draws the list of classes.
    *  Draws the classes called by each classes. If
    *  the user has clicked on a class, then the
    *  methods called by that class are displayed.
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
      int callerHeight,calledHeight;

      ObjectClass caller,called;
      MethodClass method;
      String countString,methodCountString;
      ArrayList calledList,methodList;
      ListIterator callerIt,calledIt,methodIt;
      Rectangle rect; // visible part of the panel

      try
      {
         // for each object, draw the object and the objects it calls
         callerIt = knownObjects.listIterator();
         while(callerIt.hasNext())
         {
            rect = getVisibleRect(); // visible part of the panel
            boolean displayedCaller = false; // indicates if the caller object was displayed

            // draw the caller object
            caller = (ObjectClass)callerIt.next();

            // only display the object if it is visible
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

            // @rightWidth aligns the called objects
            if((caller.getName().length()*7)+60 > rightWidth)
               rightWidth = (caller.getName().length()*7)+60;

            callerHeight = height; // keeps track of the height

            int count = 0;
            calledList = caller.getCalledObjects();
            calledIt = calledList.listIterator();

            // draw the objects called by this object
            while(calledIt.hasNext())
            {
               called = (ObjectClass)calledIt.next();
               countString = (new Integer(caller.getCalledObjectCount(count))).toString();

               // only display the object if it is visible or its caller object is visible
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

               calledHeight = height;

               // methods called by the caller:
               if(methodWidth < rightWidth+(called.getName().length()*7)+15+(countString.length()*7)+30)
                  methodWidth = rightWidth+(called.getName().length()*7)+15+(countString.length()*7)+30;
               // if the user clicks on a class - display the methods called by the caller
               if(caller.getShowMethods())
               {
                  methodList = caller.getCalledMethods();
                  methodIt = methodList.listIterator();
                  int methodCount = 0;
                  while(methodIt.hasNext())
                  {
                     // if the method belongs to the called class - display it and the number of times it is called
                     method = (MethodClass)methodIt.next();
                     if(method.getObjectClass().getName().equals(called.getName()))
                     {
                        methodCountString = (new Integer(caller.getMethodCount(methodCount))).toString();
                        // only display the method if it is visible or its caller object is visible
                        if(displayedCaller || (height>rect.getY()-50 && height<rect.getHeight()+rect.getY()+50))
                        {
                           g2.draw(new Rectangle(methodWidth,height,(method.getName().length()*7)+10,15));
                           g2.setPaint(called.getColour());
                           g2.fill(new Rectangle(methodWidth+1,height+1,(method.getName().length()*7)+9,14));
                           if(isDark(called.getColour()))
                              g2.setPaint(Color.white);
                           else
                              g2.setPaint(Color.black);
                           g2.drawString(method.getName(),methodWidth+5,height+12);
                           g2.setPaint(Color.black);
                           g2.drawLine(rightWidth+(called.getName().length()*7)+15+(countString.length()*7),calledHeight+7,methodWidth,height+7);
                           g2.drawString(methodCountString,methodWidth+(method.getName().length()*7)+15,height+12);
                           displayedCaller = true; // at least one of the called methods was displayed
                        }
                        height += 30;

                        //  adjust the width of the panel if out of bounds
                        if(panelWidth < methodWidth+(method.getName().length()*7)+15+(methodCountString.length()*7))
                        {
                           panelWidth = methodWidth+(method.getName().length()*7)+15+(methodCountString.length()*7)+10;
                           setPreferredSize(new Dimension(panelWidth,panelHeight));
                           revalidate();
                        }
                     }
                     methodCount++;
                  }
               }
               else
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
            if(caller.getNumCalledObjects()==0)
               height += 30;

            //  adjust the width of the panel if out of bounds
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
    *  @param  caller  The caller class.
    *  @param  called  The called class.
    *  @param  method  The called method.
    */
   public void methodStart(ObjectClass caller,ObjectClass called,MethodClass method)
   {
      ObjectClass obj;
      ListIterator it;
      boolean foundObject;

      try
      {
         // check if the caller object has been encountered before
         foundObject = false;
         it = knownObjects.listIterator();
         while(it.hasNext())
         {
            if(caller.equals((ObjectClass)it.next()))
            {
               foundObject = true;
               break;
            }
         }
         if(!foundObject) // if it is a new object add it to knownObjects
         {
            caller.setColour(colours[currColour++]);
            if(currColour==colours.length) 
               currColour = 0;
            knownObjects.add(caller);
            numKnownObjects++;
         }

         // check if the called object has been encountered before
         foundObject = false;
         it = knownObjects.listIterator();
         while(it.hasNext())
         {
            obj = (ObjectClass)it.next();
            if(called.equals(obj))
            {
               foundObject = true;
               called.setColour(obj.getColour());
               break;
            }
         }
         if(!foundObject) // if it is a new object add it to knownObjects
         { 
            called.setColour(colours[currColour++]);
            if(currColour==colours.length) 
               currColour = 0;
            knownObjects.add(called);
            numKnownObjects++;
         }

         // add the called object to the caller object's list of called objects
         it = knownObjects.listIterator();
         while(it.hasNext())
         {
            obj = (ObjectClass)it.next();
            if(caller.equals(obj))
            {
               obj.callsObject(called);
               obj.callsMethod(method); // indicate that the caller calls the method
               break;
            }
         }

         infoField.setText("Number of classes: " + numKnownObjects);

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
    *  Searches the object names and the method names for a string.
    *  If it finds one, it moves the scrollbars to focus on the
    *  found string and returns true.
    *
    *  @return True if a match is found.
    *  @param  searchString   The string to search for.
    */
   public boolean findObject(String searchString)
   {
      searchString = searchString.toLowerCase();
      String objectName;

      int width = 10;
      int height = 10;
      int callerHeight;

      ObjectClass caller,called;
      MethodClass method;
      ArrayList calledList,methodList;
      ListIterator callerIt,calledIt,methodIt;
      String countString,methodName;

      try
      {
         // check each caller object
         callerIt = knownObjects.listIterator();
         while(callerIt.hasNext())
         {
            // check the caller object
            caller = (ObjectClass)callerIt.next();
            objectName = caller.getName().toLowerCase();
            if(scroller.getVerticalScrollBar().getValue()<height && objectName.indexOf(searchString)>-1)
            {
               scroller.getVerticalScrollBar().setValue(height);
               scroller.getHorizontalScrollBar().setValue(width);
               return true;
            }

            // @rightWidth aligns the called objects
            if((caller.getName().length()*7)+60 > rightWidth)
               rightWidth = (caller.getName().length()*7)+60;

            callerHeight = height; // keeps track of the height

            // check the objects called by this object
            calledList = caller.getCalledObjects();
            calledIt = calledList.listIterator();
            int count = 0;
            while(calledIt.hasNext())
            {
               called = (ObjectClass)calledIt.next();
               objectName = called.getName().toLowerCase();
               countString = (new Integer(caller.getCalledObjectCount(count))).toString();
               if(scroller.getVerticalScrollBar().getValue()<height && objectName.indexOf(searchString)>-1)
               {
                  scroller.getVerticalScrollBar().setValue(height);
                  scroller.getHorizontalScrollBar().setValue(rightWidth);
                  return true;
               }

               // check the methods called by the caller
               if(methodWidth < rightWidth+(called.getName().length()*7)+15+(countString.length()*7)+30)
                  methodWidth = rightWidth+(called.getName().length()*7)+15+(countString.length()*7)+30;
               if(caller.getShowMethods())
               {
                  methodList = caller.getCalledMethods();
                  methodIt = methodList.listIterator();
                  while(methodIt.hasNext())
                  {
                     method = (MethodClass)methodIt.next();
                     if(method.getObjectClass().getName().equals(called.getName()))
                     {
                        methodName = method.getName();
                        if(scroller.getVerticalScrollBar().getValue()<height && methodName.indexOf(searchString)>-1)
                        {
                           scroller.getVerticalScrollBar().setValue(height);
                           scroller.getHorizontalScrollBar().setValue(methodWidth);
                           return true;
                        }
                        height += 30;
                     }
                  }
               }
               else
                  height += 30;
            }
            if(caller.getNumCalledObjects()==0)
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

   /**
    *  Searches the object names for a point.
    *  If it finds one, it sets whether or not to display
    *  the object's called methods
    *
    *  @param  point The point to search for.
    */
   public void findObject(Point point)
   {
      ObjectClass caller,called;
      MethodClass method;
      ArrayList calledList,methodList;
      ListIterator callerIt,calledIt,methodIt;
      Rectangle rect;

      int width = 10;
      int height = 10;
      int callerHeight;

      try
      {
         // check each caller object
         callerIt = knownObjects.listIterator();
         while(callerIt.hasNext())
         {
            // check the caller object
            caller = (ObjectClass)callerIt.next();
            rect = new Rectangle(width,height,(caller.getName().length()*7)+10,15);
            if(rect.contains(point))
            {
               caller.setShowMethods();
               panelWidth = 0;
               panelHeight = 0;
               setPreferredSize(new Dimension(panelWidth,panelHeight));
               repaint();
               return;
            }

            // @rightWidth aligns the called objects
            if((caller.getName().length()*7)+60 > rightWidth)
               rightWidth = (caller.getName().length()*7)+60;

            callerHeight = height; // keeps track of the height

            calledList = caller.getCalledObjects();
            calledIt = calledList.listIterator();

            // check the objects called by this object
            while(calledIt.hasNext())
            {
               called = (ObjectClass)calledIt.next();
               rect = new Rectangle(rightWidth,height,(called.getName().length()*7)+10,15);
               if(rect.contains(point))
               {
                  caller.setShowMethods();
                  panelWidth = 0;
                  panelHeight = 0;
                  setPreferredSize(new Dimension(panelWidth,panelHeight));
                  repaint();
                  return;
               }

               // methods called by the caller:
               if(caller.getShowMethods())
               {
                  methodList = caller.getCalledMethods();
                  methodIt = methodList.listIterator();
                  while(methodIt.hasNext())
                  {
                     method = (MethodClass)methodIt.next();
                     if(method.getObjectClass().getName().equals(called.getName()))
                        height += 30;
                  }
               }
               else
                  height += 30;
            }
            if(caller.getNumCalledObjects()==0)
               height += 30;
         }
      }
      catch(ConcurrentModificationException e)
      {
         e.printStackTrace();
         return;
      }
   }

   /**
    *  This class listens for the mouse to click on
    *  a class in the ObjectInteractionPanel.
    */
   private class MouseClickListener extends MouseAdapter
   {
      /**
       *  If the mouse is clicked on the panel this
       *  method checks if a class name has been clicked
       *  on. If a class has been clicked on then its
       *  called methods will be either hidden or displayed.
       *
       *  @param  event The MouseEvent that has occurred.
       */
      public void mouseClicked(MouseEvent event)
      {
         Point point = new Point(event.getX(),event.getY());
         findObject(point);
      }
   }
}