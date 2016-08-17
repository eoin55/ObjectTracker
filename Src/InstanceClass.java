package ObjectTracker;

import java.awt.*;
import java.util.*;

/**
 *  This class represents an instance of
 *  an object.
 *
 *  @author  Eoin O'Connor
 *  @see InstanceTrackerPanel
 *  @see FieldClass
 */
public class InstanceClass implements Comparable
{
   /**
    *  The object name of the instance.
    */
   private String objectName;
   /**
    *  The unique ID number of the instance.
    */
   private long idNum;
   /**
    *  The list of fields belonging to the
    *  instance.
    */
   private ArrayList fields;
   /**
    *  The number of fields belongin to the
    *  instance.
    */
   private int numFields;
   /**
    *  The colour associated with the instance.
    */
   private Color colour;
   /**
    *  The x-coordinate of the instance in the
    *  InstanceTrackerPanel.
    */
   private int width;
   /**
    *  The y-coordinate of the instance in the
    *  InstanceTrackerPanel.
    */
   private int height;
   /**
    *  True if its connection lines should be
    *  shown.
    */
   private boolean showLines;
   /**
    *  True if the user has moved this instance.
    */
   private boolean wasMoved;

   /**
    *  Constructor: Creates a new InstanceClass.
    *  Initializes its member variables to their
    *  default values.
    *
    *  @param  name  The object name of the instance.
    *  @param  id The unique ID number of the instance.
    */
   public InstanceClass(String name,long id)
   {
      objectName = name;
      idNum = id;
      fields = new ArrayList();
      numFields = 0;
      width = 0;
      height = 0;
      showLines = true;
      wasMoved = false;
   }

   /**
    *  Returns the object name of the instance.
    *
    *  @return The object name of the instance.
    */
   public String getName()
   {
      return objectName;
   }

   /**
    *  Returns the unique ID of the instance.
    *
    *  @return The unique ID number of the instance.
    */
   public long getIDNum()
   {
      return idNum;
   }

   /**
    *  Sets the object name of the instance.
    *
    *  @param  s  The new object name of the instance.
    */
   public void setName(String s)
   {
      objectName = s;
   }

   /**
    *  Sets the unique ID number of the instance.
    *
    *  @param  l  The new ID number of the instance.
    */
   public void getIDNum(long l)
   {
      idNum = l;
   }

   /**
    *  Returns the number of fields belonging to the instance.
    *
    *  @return The number of fields belonging
    *          to the instance.
    */
   public int getNumFields()
   {
      return numFields;
   }

   /**
    *  Returns the specified field in the list.
    *
    *  @param  n  The index number of the field
    *             to return.
    *  @return The specified field from the list.
    */
   public FieldClass getField(int n)
   {
      return (FieldClass)fields.get(n);
   }

   /**
    *  Returns true if the instance equals obj.
    *
    *  @param  obj   The instance to be compared to.
    *  @return True if this instance is equal to
    *          obj.
    */
   public boolean equals(InstanceClass obj)
   {
      return idNum == obj.getIDNum();
   }

   /**
    *  Adds a field to the list of fields.
    *  @param  field The field to add to the list.
    */
   public void addField(FieldClass field)
   {
      ListIterator it = fields.listIterator();
      FieldClass temp;

      // check if the field already exists
      boolean foundField = false;
      while(it.hasNext())
      {
         temp = (FieldClass)it.next();
         if(field.equals(temp))
         {
            foundField = true;
            temp.setValue(field.getValue()); // update the fields value
            break;
         }
      }
      if(!foundField) // new field
      {
         fields.add(field);
         numFields++;
      }
   }

   /**
    *  Sets the colour of the instance
    *
    *  @param  c  The new colour of the instance.
    */
   public void setColour(Color c)
   {
      colour = c;
   }

   /**
    *  Returns the colour of the instance.
    *
    *  @return The colour of the instance.
    */
   public Color getColour()
   {
      return colour;
   }

   /**
    *  Returns a string representation of the instance.
    *
    *  @return A string representation of the
    *          instance.
    */
   public String toString()
   {
      return objectName + " " + idNum;
   }

   /**
    *  Sets the value of a particular field in
    *  the list.
    *
    *  @param  obj   The new value of the field.
    *  @param  n  The index of the field in the
    *             list.
    */
   public void setField(Object obj,int n)
   {
      ((FieldClass)fields.get(n)).setValue(obj);
      ((FieldClass)fields.get(n)).setIsInstance(true);
   }

   /**
    *  Sets the x-coordinate of the instance.
    *
    *  @param  n  The new x-coordinate.
    */
   public void setWidth(int n)
   {
      width = n;
   }

   /**
    *  Returns the x-coordinate of the instance.
    *
    *  @return The x-coordinate of the instance.
    */
   public int getWidth()
   {
      return width;
   }

   /**
    *  Sets the y-coordinate of the instance.
    *
    *  @param  n  The new y-coordinate.
    */
   public void setHeight(int n)
   {
      height = n;
   }

   /**
    *  Returns the y-coordinate of the instance.
    *
    *  @return The y-coordinate of the instance.
    */
   public int getHeight()
   {
      return height;
   }

   /**
    *  Returns the list of fields.
    *
    *  @return The list of fields.
    */
   public ArrayList getFields()
   {
      return fields;
   }

   /**
    *  Sets whether or not to display the instance's
    *  connection lines.
    *
    *  @param  b  The new value showLines.
    */
   public void setShowLines(boolean b)
   {
      showLines = b;
   }

   /**
    *  Returns whether or not to display the instance's
    *  connection lines.
    *
    *  @return Whether or not to display the
    *          instance's connection lines.
    */
   public boolean getShowLines()
   {
      return showLines;
   }

   /**
    *  Returns the number of fields belonging to the
    *  instance that reference other instance.
    *
    *  @return The number of fields belonging to the
    *          instance that reference other intances.
    */
   public int getNumberOfRefs()
   {
      int count = 0;
      ListIterator it = fields.listIterator();
      while(it.hasNext())
      {
         if(((FieldClass)it.next()).getIsInstance())
            count++;
      }
      return count;
   }

   /**
    *  Compares the instance to obj.
    *
    *  @param  obj   The object to compare to.
    *  @return 1 if the instance has more instance references
    *          than obj.
    *  @return -1 if the instance has less instance references
    *          than obj.
    *  @return 0 if the instance has the same number of
    *            instance references as obj.
    */
   public int compareTo(Object obj)
   {
      if(getNumberOfRefs() > ((InstanceClass)obj).getNumberOfRefs())
         return 1;
      else if(getNumberOfRefs() < ((InstanceClass)obj).getNumberOfRefs())
         return -1;
      return 0;
   }

   /**
    *  Sets whether or not the instance was moved by
    *  the user.
    *
    *  @param  b  The new value of wasMoved.
    */
   public void setWasMoved(boolean b)
   {
      wasMoved = b;
   }

   /**
    *  Returns whether or not the instance was
    *  moved by the user.
    *
    *  @return Whether or not the instance was
    *          moved by the user.
    */
   public boolean getWasMoved()
   {
      return wasMoved;
   }
}