package ObjectTracker;

import java.awt.*;
import java.util.*;

/**
 *  This class represents a field of an
 *  instance of an object.
 *
 *  @author  Eoin O'Connor
 *  @see InstanceTrackerPanel
 *  @see InstanceClass
 */
public class FieldClass
{
   /**
    *  The identifier name of the field.
    */
   private String name;
   /**
    *  The type nameo of the field.
    */
   private String type;
   /**
    *  The value of the field.
    */
   private Object value;
   /**
    *  The colour associated with the field. This
    *  colour is the same colour as the colour
    *  associated with the instance that the field
    *  belongs to.
    */
   private Color colour;
   /**
    *  Indicates whether the field is a reference to
    *  a instance.
    */
   private boolean isInstance;
   /**
    *  The field's x-coordinate in the InstanceTrackerPanel.
    */
   private int width;
   /**
    *  The field's y-coordinate in the InstanceTrackerPanel.
    */
   private int height;

   /**
    *  Constructor: initializes a field with name n,
    *  type t and value v.
    *
    *  @param  n  The identifier name of the field.
    *  @param  t  The type name of the field.
    *  @param  v  The value of the field.
    */
   public FieldClass(String n,String t,Object v)
   {
      name = n;
      type = t;
      value = v;
      isInstance = false;
      width = 0;
      height = 0;
   }

   /**
    *  Returns the identifier name of the field.
    *
    *  @return The identifier name of the field.
    */
   public String getName()
   {
      return name;
   }

   /**
    *  Returns the type name of the field.
    *
    *  @return The type name of the field.
    */
   public String getType()
   {
      return type;
   }

   /**
    *  Returns the value of the field.
    *
    *  @return The value of the field.
    */
   public Object getValue()
   {
      return value;
   }

   /**
    *  Sets the value of the field.
    *
    *  @param  s  The new value of the field.
    */
   public void setValue(Object s)
   {
      value = s;
   }

   /**
    *  Sets whether or not the field is a
    *  reference to an instance.
    *
    *  @param  b  The new boolean value.
    */
   public void setIsInstance(boolean b)
   {
      isInstance = b;
   }

   /**
    *  Returns true if the field is a reference
    *  to an instance.
    *
    *  @return True if the field is a
    *          a reference to an instance.
    */
   public boolean getIsInstance()
   {
      return isInstance;
   }

   /**
    *  Returns a string representation of the field.
    *
    *  @return A string representation of the
    *          field.
    */
   public String toString()
   {
      return type + " " + name + " " + value.toString();
   }

   /**
    *  Returns true if the field is equal to obj.
    *
    *  @return   True if the field is equal to
    *            another field.
    *
    *  @param  obj   The field to compare this
    *                field to.
    */
   public boolean equals(FieldClass obj)
   {
      return name.equals(obj.getName());
   }

   /**
    *  Sets the colour of the field.
    *
    *  @param  c  The new colour of the field.
    */
   public void setColour(Color c)
   {
      colour = c;
   }

   /**
    *  Returns the colour of the field.
    *
    *  @return The colour of the field.
    */
   public Color getColour()
   {
      return colour;
   }

   /**
    *  Sets the x-coordinate of the field.
    *
    *  @param  n  The new x-coordinate.
    */
   public void setWidth(int n)
   {
      width = n;
   }

   /**
    *  Returns the x-coordinate of the field.
    *
    *  @return The x-coordinate of the field.
    */
   public int getWidth()
   {
      return width;
   }
   /**
    *  Sets the y-coordinate of the field.
    *
    *  @param  n  The new y-coordinate.
    */
   public void setHeight(int n)
   {
      height = n;
   }

   /**
    *  Returns the y-coordinate of the field.
    *
    *  @return The y-coordinate of the field.
    */
   public int getHeight()
   {
      return height;
   }
}