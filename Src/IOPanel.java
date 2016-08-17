package ObjectTracker;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/**
 *  This class implements a panel that handles the
 *  target program's input and output.
 *
 *  @author  Eoin O'Connor
 *  @see InputStreamCopier
 *  @see OutputStreamCopier
 *  @see ObjectTracker
 */
public class IOPanel extends JPanel
{
   /**
    *  Used to display the program output.
    */
   private JTextArea outputArea;
   /**
    *  Stores the text to be output.
    */
   private String outputText;
   /**
    *  Used to scroll the output panel.
    */
   private JScrollPane outputScroller;
   /**
    *  Used to enter input text.
    */
   private JTextField commandLine;
   /**
    *  Stores the input text.
    */
   private String cmdLineText;
   /**
    *  The JVM's process.
    */
   private Process process;
   /**
    *  Handles the program output.
    */
   private OutputStreamCopier out;
   /**
    *  Handles the program's error stream.
    */
   private OutputStreamCopier err;
   /**
    *  Handles the program's input.
    */
   private InputStreamCopier in;

   /**
    *  Constructor: initializes the panel. Sets a
    *  listener for the input field.
    */
   public IOPanel()
   {
      outputText = "";
      outputArea = new JTextArea();
      outputScroller = new JScrollPane(outputArea); // enables scoll bars
      outputScroller.isWheelScrollingEnabled();
      outputArea.setEditable(false);

      commandLine = new JTextField();
      commandLine.addActionListener(new TextFieldListener());

      JPanel titlePanel = new JPanel();
      titlePanel.add(new JLabel("Program Input and Output"));

      this.setLayout(new BorderLayout());
      add(titlePanel,"North");
      add(outputScroller,"Center");
      add(commandLine,"South");
   }

   /**
    *  Sets the process that will be executed.
    *  Starts the output threads to handle the
    *  output and error streams.
    *
    *  @param  process  The JVM's process.
    */
   public void set(Process process)
   {
      this.process = process;
      startOutputThreads();
   }

   /**
    *  Starts the output threads to handle the
    *  output and error streams of the program.
    */
   private void startOutputThreads()
   {
      Thread thread;

      out =  new OutputStreamCopier(process.getInputStream(),this);
      err =  new OutputStreamCopier(process.getErrorStream(),this);
      thread = new Thread(out);
      thread.setDaemon(true);
      thread.start();
      thread = new Thread(err);
      thread.setDaemon(true);
      thread.start();
   }

   /**
    *  Starts the input thread to handle the
    *  input stream of the program.
    *
    *  @param  buffer   Contains the buffered input.
    */
   private void startInputThread(byte [] buffer)
   {
      Thread thread;

      in =  new InputStreamCopier(process.getOutputStream(),buffer);
      thread = new Thread(in);
      thread.setDaemon(true);
      thread.start();
   }

   /**
    *  Displays the output in the output field.
    *
    *  @param  s  Contains the output text.
    */
   public void writeOutput(String s)
   {
      outputText += s;
      outputArea.setText(outputText);

      // makes the scroller follow the output
      int scollBarHeight = outputScroller.getVerticalScrollBar().getMaximum();
      outputScroller.getVerticalScrollBar().setValue(scollBarHeight);
   }

   /**
    *  Returns the output scrollpane.
    *
    *  @return The output scrollpane.
    */
   public JScrollPane getOutputScroller()
   {
      return outputScroller;
   }

   /**
    *  Returns the command-line field.
    *
    *  @return The command-line field.
    */
   public JTextField getCommandLine()
   {
      return commandLine;
   }

   /**
    *  This class listens for text to be entered into
    *  the input field.
    */
   private class TextFieldListener implements ActionListener
   {
      /**
       *  Takes the text that was entered into the text
       *  field and sends it to the input of the program.
       *
       *  @param  event The ActionEvent that occurred.
       */
      public void actionPerformed(ActionEvent event)
      {
         cmdLineText = commandLine.getText() + "\n";
         byte [] buffer = new byte[cmdLineText.length()];
         for(int i=0;i<buffer.length;i++)
            buffer[i] = (byte)cmdLineText.charAt(i);
         startInputThread(buffer);
         commandLine.setText("");
      }
   }
}