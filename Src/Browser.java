package ObjectTracker;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

/**
 *  This class implements an internet browser.
 *
 *  @author  Eoin O'Connor
 *  @see UserOptionsPanel
 */
public class Browser extends JFrame implements HyperlinkListener,ActionListener
{
   /**
    *  This text field is used to enter a URL and to
    *  display the current URL.
    */
   private JTextField urlField;
   /**
    *  The main window displaying the content of the
    *  web page.
    */
   private JEditorPane htmlPane;
   /**
    *  The URL that the browser goes to initially once
    *  loaded.
    */
   private String initialURL;

   /**
    *  Constructor: attempts to launch the browser
    *  by trying to connect to initialURL. If there
    *  is a problem connecting either to the web
    *  or to the URL a warning message is displayed.
    *  Sets listeners for the URL field and for the
    *  hyperlinks.
    *
    *  @param  initialURL  A string containing the URL
    *                      to browse.
    */
   public Browser(String initialURL)
   {
      setTitle("Graphical Java Displayer Help");
      this.initialURL = initialURL;
      this.setDefaultCloseOperation(HIDE_ON_CLOSE);
      WindowUtilities.setNativeLookAndFeel();

      JPanel topPanel = new JPanel();
      topPanel.setBackground(Color.lightGray);
      JLabel urlLabel = new JLabel("URL:");
      urlField = new JTextField(30);
      urlField.setText(initialURL);
      urlField.addActionListener(this);
      topPanel.add(urlLabel);
      topPanel.add(urlField);
      getContentPane().add(topPanel, BorderLayout.NORTH);

      try
      {
         htmlPane = new JEditorPane(initialURL);
         htmlPane.setEditable(false);
         htmlPane.addHyperlinkListener(this);
         JScrollPane scrollPane = new JScrollPane(htmlPane);
         scrollPane.isWheelScrollingEnabled();
         getContentPane().add(scrollPane, BorderLayout.CENTER);
      }
      catch(IOException ioe)
      {
         warnUser("Can't build HTML pane for " + initialURL + ": " + ioe);
      }

      Dimension screenSize = getToolkit().getScreenSize();
      int width = screenSize.width * 8 / 10;
      int height = screenSize.height * 8 / 10;
      setBounds(width/8, height/8, width, height);
   }

   /**
    *  Listens for a URL to be entered into the
    *  URL field. When [Return] is pressed the
    *  browser attempts to connect to the URL
    *  in the URL field. Displays a warning
    *  message if there is a problem connecting.
    *
    *  @param  event The ActionEvent that has occurred.
    */
   public void actionPerformed(ActionEvent event)
   {
      String url;
      if(event.getSource() == urlField) 
         url = urlField.getText();
      else
         url = initialURL;
      try
      {
         htmlPane.setPage(new URL(url));
         urlField.setText(url);
      }
      catch(IOException ioe)
      {
         warnUser("Can't follow link to " + url + ": " + ioe);
      }
   }

   /**
    *  Listens for a link to be pressed on the web page.
    *  When this happens the browser is updated to
    *  reflect this change. A warning message is displayed
    *  if there is a problem connecting to the new web
    *  page.
    *
    *  @param  event The HyperlinkEvent that has occurred.
    */
   public void hyperlinkUpdate(HyperlinkEvent event)
   {
      if(event.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
      {
         try
         {
            htmlPane.setPage(event.getURL());
            urlField.setText(event.getURL().toExternalForm());
         }
         catch(IOException ioe)
         {
            warnUser("Can't follow link to " + event.getURL().toExternalForm() + ": " + ioe);
         }
      }
   }

   /**
    *  Displays a warning message to the user.
    *
    *  @param  message   A string containing the
    *                    message to be displayed.
    */
   private void warnUser(String message)
   {
      JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
   }
}