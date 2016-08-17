package ObjectTracker;

import java.io.*;

/**
 *  This class handles the output and error streams of a
 *  running program.
 *
 *  @author  Eoin O'Connor
 *  @see ObjectTracker
 *  @see InputStreamCopier
 */
public class OutputStreamCopier implements Runnable
{
   /**
    *  The size of the buffer.
    */
   private static int BUFFER_LENGTH = 64;
   /**
    *  The input stream that is read from.
    */
   private InputStream from;
   /**
    *  Used to buffer the stream of bytes
    *  before they are sent to the output
    *  panel.
    */
   private byte[] buffer;
   /**
    *  The panel that will display the output.
    */
   private IOPanel ioPanel;

   /**
    *  Constructor: sets the input stream and
    *  and the buffer.
    *
    *  @param  from The input stream to attach to.
    *  @param  ioPanel The panel that will display the output.
    */
   public OutputStreamCopier(InputStream from,IOPanel ioPanel)
   {
      buffer = new byte[BUFFER_LENGTH];
      this.from = from;
      this.ioPanel = ioPanel;
   }

   /**
    *  Reads the bytes from the input stream
    *  into the buffer. Then it sends the bytes
    *  in the buffer to the output panel to be
    *  displayed.
    */
   public void run()
   {
      int read;
      String line;

      try
      {
         while((read = from.read(buffer)) != -1)
         {
            line = "";
            for(int i=0;i<read;i++)
               line = line + (char)buffer[i];
            ioPanel.writeOutput(line);
         }
      }
      catch(IOException e)
      {
         // give up
         e.printStackTrace();
      }
   }
}