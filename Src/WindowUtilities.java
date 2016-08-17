package ObjectTracker;

import javax.swing.*;
import java.awt.*;

/**
 *  This class contains any methods that are needed
 *  to change the look and feel of the GUI components.
 *
 *  @author  Eoin O'Connor
 *  @see UserOptionsPanel
 *  @see Browser
 */
public class WindowUtilities
{
   /**
    *  Tells the system to use native look and feel, as in previous
    *  releases. Metal (Java) LAF is the default otherwise.
    */
   public static void setNativeLookAndFeel()
   {
      try
      {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch(Exception e)
      {
         System.err.println("Error setting native LAF: " + e);
      }
   }
}