package ObjectTracker;

import java.io.*;

/**
 *  This class contains the main method. It
 *  runs the ObjectTracker and handles its
 *  error stream.
 *
 *  @author  Eoin O'Connor
 *  @see ObjectTracker
 */
public class Tracker
{
   /**
    *  Sets the error stream to write to a file
    *  named "ErrorFile.txt". Initializes the
    *  ObjectTracker and runs it.
    *
    *  @param  args  An array of command-line arguments.
    */
   public static void main(String [] args)
   {
      try
      {
         PrintStream errorStream = new PrintStream(new FileOutputStream("ErrorFile.txt"));
         System.setErr(errorStream);

         ObjectTracker tracker = new ObjectTracker();
         tracker.run();

         errorStream.close();
      }
      catch(FileNotFoundException e)
      {
         e.printStackTrace();
      }
   }
}