package ObjectTracker;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/**
 *  This class represents a panel that displays the
 *  instances modified during the execution of the
 *  target program.
 *
 *  @author  Eoin O'Connor
 *  @see InstanceClass
 *  @see FieldClass
 *  @see PatternDisplayer
 */
public class InstanceTrackerPanel extends JPanel
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
    *  True if the connection lines should be displayed.
    */
   private boolean showLines;
   /**
    *  Used to set whether or not to show connection lines.
    */
   private JMenuItem showLinesMenuItem;
   /**
    *  Used to set which view to use.
    */
   private JMenuItem showLinearMenuItem;
   /**
    *  A text field used to display the number of instances.
    */
   private JTextField infoField;
   /**
    *  The list of instances.
    */
   private ArrayList instances;
   /**
    *  The number of instances.
    */
   private int numInstances;
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
    *  True if the linear view should be used.
    */
   private boolean showLinear;
   /**
    *  The width between instances.
    */
   private int spaceWidth;
   /**
    *  The height between instances.
    */
   private int spaceHeight;
   /**
    *  Used to increase the distance between instances.
    */
   private JMenuItem increaseMenuItem;
   /**
    *  Used to reduce the distance between instances.
    */
   private JMenuItem reduceMenuItem;

   /**
    *  Constructor: initializes the panel and sets
    *  listeners for mouse clicks. Sets the rendering
    *  for the graphics.
    *
    *  @param  info  A JTextField used to display
    *                the number of instances.
    */
   public InstanceTrackerPanel(JTextField info)
   {
      panelWidth = 0;
      panelHeight = 0;
      currColour = 0;
      setPreferredSize(new Dimension(panelWidth,panelHeight));
      setBackground(Color.white);
      infoField = info;
      infoField.setText("Number of instances: " + numInstances);
      instances = new ArrayList();
      numInstances = 0;
      showLines = true;
      MouseClickListener listener = new MouseClickListener();
      addMouseListener(listener);
      showLinear = true;
      spaceWidth = 30;
      spaceHeight = 60;

      renderHints =  new RenderingHints(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_SPEED);
      renderHints.put(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
      renderHints.put(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
      renderHints.put(RenderingHints.KEY_DITHERING,RenderingHints.VALUE_DITHER_DISABLE);
      renderHints.put(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);
      renderHints.put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
      renderHints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
   }

   /**
    *  Paints the panel. Draws the list of instances.
    *  Draws the fields belonging to each instance.
    *  Draws connection lines between references and
    *  instances. Uses the linear view if showLinear
    *  is true. Otherwise it uses the graph view.
    *
    *  @param  g  Used to draw the graphics.
    */
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      g2 = (Graphics2D)g;
      g2.setRenderingHints(renderHints);
      g2.setFont(new Font("Courier new",0,12)); // the Courier New chars are about 7 pixels wide

      if(showLinear) // display the instance information linearly
      {
         try
         {
            int width = 10;
            InstanceClass instance;
            ListIterator fieldIt;
            FieldClass field;
            Rectangle rect; // visible part of the panel
            InstanceClass fieldInstance;
            int instanceWidth,instanceHeight;

            // for each instance
            ListIterator instanceIt = instances.listIterator();
            while(instanceIt.hasNext())
            {
               rect = getVisibleRect(); // visible part of the panel

               // draw the instance
               int height = 10;
               instance = (InstanceClass)instanceIt.next();
               String instanceName = instance.getName() + " " + instance.getIDNum();
               int biggestWidth = width + (instanceName.length()*7)+10;
               instance.setWidth(width);
               instance.setHeight(height);

               // only display the instance if it is visible
               if(width>rect.getX()-(instanceName.length()*7)-50 && width<rect.getWidth()+rect.getX()+50)
               {
                  g2.draw(new Rectangle(width,height,(instanceName.length()*7)+10,15));
                  g2.setPaint(instance.getColour());
                  g2.fill(new Rectangle(width+1,height+1,(instanceName.length()*7)+9,14));
                  if(isDark(instance.getColour()))
                     g2.setPaint(Color.white);
                  else
                     g2.setPaint(Color.black);
                  g2.drawString(instanceName,width+5,height+12);
                  g2.setPaint(Color.black);
               }

               height = 20 + spaceHeight;

               // for each field in the instance
               ArrayList fieldList = instance.getFields();
               fieldIt = fieldList.listIterator();
               while(fieldIt.hasNext())
               {
                  // draw the field type and name
                  field = (FieldClass)fieldIt.next();
                  height += spaceHeight;
                  String fieldString = field.getType() + " " + field.getName();
                  if(biggestWidth < width + (fieldString.length()*7)+10)
                     biggestWidth = width + (fieldString.length()*7)+10;
                  field.setWidth(width);
                  field.setHeight(height);

                  // only display the field if it is visible
                  if(width>rect.getX()-(fieldString.length()*7)-50 && width<rect.getWidth()+rect.getX()+50)
                  {
                     g2.draw(new Rectangle(width,height,(fieldString.length()*7)+10,15));
                     g2.setPaint(field.getColour());
                     g2.fill(new Rectangle(width+1,height+1,(fieldString.length()*7)+9,14));
                     if(isDark(field.getColour()))
                        g2.setPaint(Color.white);
                     else
                        g2.setPaint(Color.black);
                     g2.drawString(fieldString,width+5,height+12);
                     g2.setPaint(Color.black);
                  }

                  // draw the field value
                  height += 15;
                  String fieldValue = field.getValue().toString();
                  if(biggestWidth < width + (fieldValue.length()*7)+10)
                     biggestWidth = width + (fieldValue.length()*7)+10;

                  if(field.getIsInstance() && !fieldValue.equals("null")) // if the value is an instance
                  {
                     Color instanceColour = ((InstanceClass)(field.getValue())).getColour();

                     // only display the field value if it is visible
                     if(width>rect.getX()-(fieldValue.length()*7)-50 && width<rect.getWidth()+rect.getX()+50)
                     {
                        g2.draw(new Rectangle(width,height,(fieldValue.length()*7)+10,15));
                        g2.setPaint(instanceColour);
                        g2.fill(new Rectangle(width+1,height+1,(fieldValue.length()*7)+9,14));
                        if(isDark(instanceColour))
                           g2.setPaint(Color.white);
                        else
                           g2.setPaint(Color.black);
                        g2.drawString(fieldValue,width+5,height+12);
                        g2.setPaint(Color.black);
                     }

                     // draw a line from this field value to its instance
                     fieldInstance = (InstanceClass)(field.getValue());
                     if(fieldInstance.getShowLines())
                     {
                        instanceWidth = fieldInstance.getWidth()+(int)(fieldInstance.toString().length()*3.5)+5;
                        instanceHeight = fieldInstance.getHeight()+15;
                        if(instanceWidth!=0 && instanceHeight!=0)
                           g2.drawLine(width+(int)(fieldValue.length()*3.5)+5,height,instanceWidth,instanceHeight);
                     }

                  }
                  else // if the value is a primitive type
                  {
                     // only display the field value if it is visible
                     if(width>rect.getX()-(fieldValue.length()*7)-50 && width<rect.getWidth()+rect.getX()+50)
                     {
                        g2.draw(new Rectangle(width,height,(fieldValue.length()*7)+10,15));
                        g2.drawString(fieldValue,width+5,height+12);
                     }
                  }

                  // adjust the height of the panel if out of bounds
                  if(panelHeight < height)
                  {
                     panelHeight = height+30;
                     setPreferredSize(new Dimension(panelWidth,panelHeight));
                     revalidate();
                  }
               }

               width = biggestWidth + spaceWidth;

               // adjust the width of the panel if out of bounds
               if(panelWidth < width)
               {
                  panelWidth = width+10;
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
      else // display the instance information as a graph
      {
         try
         {
            int width = 10;
            int height = 10;
            int numRows = (int)Math.sqrt((double)numInstances);
            int rowCount = 0;
            int biggestWidth = 0;
            int colCount = 0;
            InstanceClass instance,fieldInstance;
            ListIterator fieldIt;
            FieldClass field;
            String fieldValue;
            int instanceWidth,instanceHeight;
            Rectangle rect; // visible part of the panel

            // for each instance
            ListIterator instanceIt = instances.listIterator();
            while(instanceIt.hasNext())
            {
               rect = getVisibleRect(); // visible part of the panel
               // store original width and height
               int tempWidth = width;
               int tempHeight = height;

               // draw the instance
               instance = (InstanceClass)instanceIt.next();

               // if the instance moved use its new width and height
               if(instance.getWasMoved())
               {
                  width = instance.getWidth();
                  height = instance.getHeight();
               }

               String instanceName = instance.getName() + " " + instance.getIDNum();
               instance.setWidth(width);
               instance.setHeight(height);

               // only display the instance if it is visible
               if(width>rect.getX()-(instanceName.length()*7)-50 && width<rect.getWidth()+rect.getX()+50)
               {
                  if(height>rect.getY()-50 && height<rect.getHeight()+rect.getY()+50)
                  {
                     g2.draw(new Rectangle(width,height,(instanceName.length()*7)+10,15));
                     g2.setPaint(instance.getColour());
                     g2.fill(new Rectangle(width+1,height+1,(instanceName.length()*7)+9,14));
                     if(isDark(instance.getColour()))
                        g2.setPaint(Color.white);
                     else
                        g2.setPaint(Color.black);
                     g2.drawString(instanceName,width+5,height+12);
                     g2.setPaint(Color.black);
                  }
               }

               if(biggestWidth < (instanceName.length()*7)+10)
               {
                  biggestWidth = (instanceName.length()*7)+10;

                  // adjust the width of the panel if out of bounds
                  if(panelWidth < tempWidth+biggestWidth)
                  {
                     panelWidth = tempWidth+biggestWidth+10;
                     setPreferredSize(new Dimension(panelWidth,panelHeight));
                     revalidate();
                  }
               }

               // for each field of the instance
               fieldIt = instance.getFields().listIterator();
               while(fieldIt.hasNext())
               {
                  field = (FieldClass)fieldIt.next();
                  if(field.getIsInstance()) // if the field is an instance
                  {
                     fieldValue = field.getValue().toString();
                     if(!fieldValue.equals("null"))
                     {
                        // draw a line from this instance to the field's instance
                        if(instance.getShowLines())
                        {
                           fieldInstance = (InstanceClass)field.getValue();
                           instanceWidth = fieldInstance.getWidth()+(fieldInstance.toString().length()*7)+10;
                           instanceHeight = fieldInstance.getHeight()+7;
                           if(instanceWidth!=0 && instanceHeight!=0)
                              g2.drawLine(width,height+7,instanceWidth,instanceHeight);
                        }
                     }
                  }
               }

               // there may be a need restore the width and height (if the instance was moved)
               width = tempWidth;
               height = tempHeight;

               height += spaceHeight;
               rowCount++;

               // adjust the height of the panel if out of bounds
               if(panelHeight < height)
               {
                  panelHeight = height;
                  setPreferredSize(new Dimension(panelWidth,panelHeight));
                  revalidate();
               }

               //check if the end of the column has been reached
               if(rowCount==numRows)
               {
                  colCount++;
                  width += biggestWidth+spaceWidth;
                  biggestWidth = 0;
                  rowCount = 0;
                  height = 10+(30*(colCount%2));
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
    *  Adds an instance to the list. Adds a field to the
    *  instance's list of fields. Updates the panel.
    *
    *  @param  instanceClass  The new instance.
    *  @param  field The new field.
    */
   public void addInstanceClass(InstanceClass instanceClass,FieldClass field)
   {
      ListIterator it;
      InstanceClass temp;
      // check if the field is an instance
      it = instances.listIterator();
      while(it.hasNext())
      {
         temp = (InstanceClass)it.next();
         if(temp.toString().equals(field.getValue().toString()))
         {
            field.setValue(temp);
            field.setIsInstance(true);
            break;
         }
      }

      // check if the instance already exists
      boolean foundInstance = false;
      it = instances.listIterator();
      while(it.hasNext())
      {
         temp = (InstanceClass)it.next();
         if(instanceClass.equals(temp))
         {
            foundInstance = true;
            field.setColour(temp.getColour()); // set colour of field
            temp.addField(field);
            break;
         }
      }
      if(!foundInstance) // new instance
      {
         // set colour of instance and field
         instanceClass.setColour(colours[currColour]);
         field.setColour(colours[currColour]);
         currColour++;
         if(currColour==colours.length) 
            currColour = 0;
         instanceClass.setShowLines(showLines); // set whether or not to show the instance's connection lines
         instanceClass.addField(field);

         instances.add(instanceClass);
         numInstances++;
         infoField.setText("Number of instances: " + numInstances);
      }
      repaint();
   }

   /**
    *  Adds an instance to the list. Updates the panel.
    *
    *  @param  instanceClass  The new instance.
    */
   public void addInstanceClass(InstanceClass instanceClass)
   {
      ListIterator it;
      InstanceClass temp;
      // check if the instance already exists
      boolean foundInstance = false;
      it = instances.listIterator();
      while(it.hasNext())
      {
         temp = (InstanceClass)it.next();
         if(instanceClass.equals(temp))
         {
            foundInstance = true;
            break;
         }
      }
      if(!foundInstance) // new instance
      {
         // set colour of instance
         instanceClass.setColour(colours[currColour++]);
         if(currColour==colours.length) 
            currColour = 0;
         instanceClass.setShowLines(showLines); // set whether or not to show the instance's connection lines

         instances.add(instanceClass);
         numInstances++;
         infoField.setText("Number of instances: " + numInstances);

         // check if this instance is being referenced by another instance
         it = instances.listIterator();
         while(it.hasNext())
         {
            temp = (InstanceClass)it.next();
            for(int i=0;i<temp.getNumFields();i++)
            {
               if(temp.getField(i).getValue().toString().equals(instanceClass.toString()))
               {
                  // make the value of temp point to instanceClass
                  temp.setField(instanceClass,i);
                  break;
               }
            }
         }
      }
      repaint();
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
    *  Sets showLinesMenuItem and adds a listener to it.
    *
    *  @param  item  The new menu item
    */
   public void setShowLinesMenuItem(JMenuItem item)
   {
      showLinesMenuItem = item;
      showLinesMenuItem.addActionListener(new InstanceTrackerListener());
      if(showLines)
         showLinesMenuItem.setText("Hide Connection Lines");
      else
         showLinesMenuItem.setText("Show Connection Lines");
   }

   /**
    *  Sets showLinearMenuItem and adds a listener to it.
    *
    *  @param  item  The new menu item
    */
   public void setShowLinearMenuItem(JMenuItem item)
   {
      showLinearMenuItem = item;
      showLinearMenuItem.addActionListener(new InstanceTrackerListener());
      if(showLinear)
         showLinearMenuItem.setText("Show Instances in Graph Form");
      else
         showLinearMenuItem.setText("Show Instances in Linear Form");
   }

   /**
    *  Sets reduceMenuItem and adds a listener to it.
    *
    *  @param  item  The new menu item
    */
   public void setReduceMenuItem(JMenuItem item)
   {
      reduceMenuItem = item;
      reduceMenuItem.addActionListener(new InstanceTrackerListener());
   }

   /**
    *  Sets increaseMenuItem and adds a listener to it.
    *
    *  @param  item  The new menu item
    */
   public void setIncreaseMenuItem(JMenuItem item)
   {
      increaseMenuItem = item;
      increaseMenuItem.addActionListener(new InstanceTrackerListener());
   }

   /**
    *  Searches the instances' names and fields' names for a string.
    *  If it finds one, it moves the scrollbars to focus on the
    *  found string and returns true.
    *
    *  @return True if a match is found.
    *  @param  searchString   The string to search for.
    */
   public boolean findInstance(String searchString)
   {
      try
      {
         ListIterator instanceIt = instances.listIterator();
         InstanceClass instance;
         ListIterator fieldIt;
         FieldClass field;
         searchString = searchString.toLowerCase();
         String name;

         // search each instance for a match
         while(instanceIt.hasNext())
         {
            // check the instance name
            instance = (InstanceClass)instanceIt.next();
            name = instance.toString().toLowerCase();
            if(name.indexOf(searchString)>-1)
            {
               // check if the scrollbar is to the right of the instance's width
               if(scroller.getHorizontalScrollBar().getValue()<instance.getWidth())
               {
                  scroller.getVerticalScrollBar().setValue(instance.getHeight());
                  scroller.getHorizontalScrollBar().setValue(instance.getWidth());
                  return true;
               }
            }

            if(showLinear) // if using the linear view - search each field of the instance
            {
               // for each field belonging to the instance
               ArrayList fieldList = instance.getFields();
               fieldIt = fieldList.listIterator();
               while(fieldIt.hasNext())
               {
                  // check the names of the instance's fields
                  field = (FieldClass)fieldIt.next();
                  name = field.toString().toLowerCase();
                  if(name.indexOf(searchString)>-1)
                  {
                     // check if the scrollbar is to the right of the instance's width
                     if(scroller.getHorizontalScrollBar().getValue()<field.getWidth())
                     {
                        scroller.getVerticalScrollBar().setValue(field.getHeight());
                        scroller.getHorizontalScrollBar().setValue(field.getWidth());
                        return true;
                     }
                  }
               }
            }
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
    *  Searches the instances' names and fields' names for a point.
    *  If it finds one, it shows or hides the lines going and coming
    *  from that instance.
    *
    *  @param  point The point to search for.
    */
   public void setShowLines(Point point)
   {
      try
      {
         ListIterator instanceIt = instances.listIterator();
         InstanceClass instance,fieldInstance;
         ListIterator fieldIt;
         FieldClass field;
         Rectangle rect;

         // search each instance for a match
         while(instanceIt.hasNext())
         {
            // check the instance
            instance = (InstanceClass)instanceIt.next();
            rect = new Rectangle(instance.getWidth(),instance.getHeight(),(instance.toString().length()*7)+10,15);
            if(rect.contains(point)) // if the instance name was click on
            {
               // show or hide all lines from this instance
               if(instance.getShowLines())
                  instance.setShowLines(false);
               else
                  instance.setShowLines(true);
               repaint();
               return;
            }

            if(showLinear) // if linear view - check fields
            {
               // for each field belonging to the instance
               ArrayList fieldList = instance.getFields();
               fieldIt = fieldList.listIterator();
               while(fieldIt.hasNext())
               {
                  // check the instance's fields
                  field = (FieldClass)fieldIt.next();
                  if(field.getIsInstance())
                  {
                     rect = new Rectangle(field.getWidth(),field.getHeight(),(field.toString().length()*7)+10,15);
                     fieldInstance = (InstanceClass)field.getValue();
                     if(rect.contains(point)) // if the field name was clicked on
                     {
                        if(fieldInstance.getShowLines())
                           fieldInstance.setShowLines(false);
                        else
                           fieldInstance.setShowLines(true);
                        repaint();
                        return;
                     }

                     rect = new Rectangle(field.getWidth(),field.getHeight()+15,(fieldInstance.toString().length()*7)+10,15);
                     if(rect.contains(point)) // if the field value was click on
                     {
                        if(fieldInstance.getShowLines())
                           fieldInstance.setShowLines(false);
                        else
                           fieldInstance.setShowLines(true);
                        repaint();
                        return;
                     }
                  }
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

   /**
    *  Sets each instance in the list to show its connection
    *  lines or not.
    *
    *  @param  show  The new value of setShowLines().
    */
   public void setAllShowLines(boolean show)
   {
      try
      {
         ListIterator instanceIt = instances.listIterator();
         InstanceClass instance;

         // for each instance
         while(instanceIt.hasNext())
         {
            // set the instance's showLines
            instance = (InstanceClass)instanceIt.next();
            instance.setShowLines(show);
         }
      }
      catch(ConcurrentModificationException e)
      {
         e.printStackTrace();
         return;
      }
   }

   /**
    *  Sets each instance in the list to indicate that
    *  that the instance was not moved.
    */
   public void resetAllMoves()
   {
      try
      {
         ListIterator instanceIt = instances.listIterator();
         InstanceClass instance;

         // for each instance
         while(instanceIt.hasNext())
         {
            // set the instance's wasMoved
            instance = (InstanceClass)instanceIt.next();
            instance.setWasMoved(false);
         }
      }
      catch(ConcurrentModificationException e)
      {
         e.printStackTrace();
         return;
      }
   }

   /**
    *  Increases the width and height of each instance by 10.
    */
   public void increaseAllInstanceSizes()
   {
      try
      {
         ListIterator instanceIt = instances.listIterator();
         InstanceClass instance;

         // for each instance
         while(instanceIt.hasNext())
         {
            // if the instance was moved, increase its width and height by 10
            instance = (InstanceClass)instanceIt.next();
            if(instance.getWasMoved())
            {
               instance.setWidth(instance.getWidth()+10);
               instance.setHeight(instance.getHeight()+10);
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
    *  Reduces the width and height of each instance by 10.
    */
   public void reduceAllInstanceSizes()
   {
      try
      {
         ListIterator instanceIt = instances.listIterator();
         InstanceClass instance;

         // for each instance
         while(instanceIt.hasNext())
         {
            // if the instance was moved, reduce its width and height by 10
            instance = (InstanceClass)instanceIt.next();
            if(instance.getWasMoved())
            {
               instance.setWidth(instance.getWidth()-10);
               instance.setHeight(instance.getHeight()-10);
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
    *  Searches the instances' names for a point. If it finds
    *  one, it moves the instance to the new point.
    *
    *  @param  oldPoint The point to search for.
    *  @param  newPoint The point to move to.
    */
   public void moveInstance(Point oldPoint,Point newPoint)
   {
      try
      {
         ListIterator instanceIt = instances.listIterator();
         InstanceClass instance;
         Rectangle rect;

         // search each instance for a match
         while(instanceIt.hasNext())
         {
            // check the instance
            instance = (InstanceClass)instanceIt.next();
            rect = new Rectangle(instance.getWidth(),instance.getHeight(),(instance.toString().length()*7)+10,15);
            if(rect.contains(oldPoint)) // if the instance name was click on
            {
               // move this instance to a new point
               instance.setWidth((int)newPoint.getX());
               instance.setHeight((int)newPoint.getY());
               instance.setWasMoved(true);
               repaint();
               return;
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
    *  This class listens for a menu item to be selected.
    */
   private class InstanceTrackerListener implements ActionListener
   {
      /**
       *  If the showLinesMenuItem is selected, then the
       *  connection lines are either shown or hidden.
       *  If the showLinearMenuItem is selected, then the
       *  display is changed to the linear view or to the
       *  graph view. If the reduceMenuItem is selected,
       *  then the distance between instances is reduced.
       *  If the increaseMenuItem is selected, then the
       *  distance between instances is increased.
       *
       *  @param  event The MouseEvent that occurred.
       */
      public void actionPerformed(ActionEvent event)
      {
         Object source = event.getSource();

         if(source==showLinesMenuItem)
         {
            if(showLines)
            {
               showLinesMenuItem.setText("Show Connection Lines");
               showLines = false;
               setAllShowLines(false);
            }
            else
            {
               showLinesMenuItem.setText("Hide Connection Lines");
               showLines = true;
               setAllShowLines(true);
            }
            repaint();
         }
         else if(source==showLinearMenuItem)
         {
            if(showLinear) // linear view
            {
               showLinear = false;
               showLinearMenuItem.setText("Show Instances in Linear Form");
            }
            else // graph view
            {
               showLinear = true;
               showLinearMenuItem.setText("Show Instances in Graph Form");
               resetAllMoves();
            }
            if(showLines)
               setAllShowLines(true);
            else
               setAllShowLines(false);

            panelWidth = 0;
            panelHeight = 0;
            setPreferredSize(new Dimension(panelWidth,panelHeight));
            revalidate();
            update(g2);
         }
         else if(source==reduceMenuItem)
         {
            if(spaceWidth>30 && spaceHeight>60)
            {
               spaceWidth -= 10;
               spaceHeight -= 10;
               panelWidth = 0;
               panelHeight = 0;
               setPreferredSize(new Dimension(panelWidth,panelHeight));
               if(!showLinear)
                  reduceAllInstanceSizes();
               update(g2);
               repaint();
               revalidate();
            }
         }
         else if(source==increaseMenuItem)
         {
            spaceWidth += 10;
            spaceHeight += 10;
            if(!showLinear)
               increaseAllInstanceSizes();
            repaint();
            revalidate();
         }
      }
   }

   /**
    *  This class listens for the mouse to click on
    *  an instance in the panel.
    */
   private class MouseClickListener extends MouseAdapter
   {
      /**
       *  The point that was clicked.
       */
      private Point pressedPoint = new Point(0,0);
      /**
       *  The point where the mouse button was released.
       */
      private Point releasedPoint = new Point(0,0);

      /**
       *  Gets the point that was click on.
       *
       *  @param  event The MouseEvent that occurred.
       */
      public void mousePressed(MouseEvent event)
      {
         pressedPoint = new Point(event.getX(),event.getY());
      }

      /**
       *  Gets the point where the mouse button was released.
       *  If the mouse did not move, then the setShowLines(Point)
       *  method is called. If the mouse was dragged, then the
       *  moveInstance(Point,Point) is called.
       *
       *  @param  event The MouseEvent that occurred.
       */
      public void mouseReleased(MouseEvent event)
      {
         releasedPoint = new Point(event.getX(),event.getY());
         if(pressedPoint.equals(releasedPoint))
            setShowLines(releasedPoint);
         else if(!showLinear)
            moveInstance(pressedPoint,releasedPoint);
      }
   }
}