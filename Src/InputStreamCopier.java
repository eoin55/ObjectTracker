package ObjectTracker;

import java.io.*;

/**
 *  This class handles the input stream of a
 *  running program.
 *
 *  @author  Eoin O'Connor
 *  @see ObjectTracker
 *  @see OutputStreamCopier
 */
public class InputStreamCopier implements Runnable
{
   /**
    *  The output stream that is written to.
    */
   private OutputStream to;
   /**
    *  Used to buffer the stream of bytes
    *  before they are sent to the output
    *  stream.
    */
   private byte [] buffer;

   /**
    *  Constructor: sets the output stream and
    *  and the buffer.
    *
    *  @param  to The output stream to attach to.
    *  @param  bytes The bytes to buffer.
    */
   public InputStreamCopier(OutputStream to,byte [] bytes)
   {
      this.to = to;
      buffer = bytes;
   }

   /**
    *  Sends the bytes in the buffer to the
    *  output stream and flushes the output
    *  stream.
    */
   public void run()
   {
      try
      {
         to.write(buffer,0,buffer.length);
         to.flush();
      }
      catch(IOException e)
      {
         // give up
         e.printStackTrace();
      }
   }
}