package ObjectTracker;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/**
 *  This class receives information about the running target
 *  program and sends it to the appropriate panel to display it.
 *  It displays these panels in internal frames, which are kept
 *  on the PatternDisplayer desktop.
 *
 *  @author  Eoin O'Connor
 *  @see ObjectClass
 *  @see MethodClass
 *  @see InstanceClass
 *  @see FieldClass
 *  @see ObjectInteractionPanel
 *  @see MethodInteractionPanel
 *  @see InstanceTrackerPanel
 */
public class PatternDisplayer extends JDesktopPane
{
   /**
    *  Used to add internal frames to the desktop.
    */
   private Container jifContainer;
   /**
    *  Displays object interaction.
    */
   private ObjectInteractionPanel objectInteractionPanel;
   /**
    *  Displays method interaction.
    */
   private MethodInteractionPanel methodInteractionPanel;
   /**
    *  Displays the instances modified in the program.
    */
   private InstanceTrackerPanel instanceTrackerPanel;
   /**
    *  Used to scroll the ObjectInteractionPanel.
    */
   private JScrollPane objectInteractionPanelScroller;
   /**
    *  Used to scroll the MethodInteractionPanel.
    */
   private JScrollPane methodInteractionPanelScroller;
   /**
    *  Used to scroll the InstanceTrackerPanel.
    */
   private JScrollPane instanceTrackerPanelScroller;
   /**
    *  Contains the ObjectInteractionPanel.
    */
   private JInternalFrame objectInteractionFrame;
   /**
    *  Contains the MethodInteractionPanel.
    */
   private JInternalFrame methodInteractionFrame;
   /**
    *  Contains the InstanceTrackerPanel.
    */
   private JInternalFrame instanceTrackerFrame;
   /**
    *  The initial width of each internal frame.
    */
   private int width;
   /**
    *  The initial height of each internal frame.
    */
   private int height;
   /**
    *  Displays information in the ObjectInteractionPanel
    */
   private JTextField objectInteractionInfoField;
   /**
    *  Displays information in the MethodInteractionPanel
    */
   private JTextField methodInteractionInfoField;
   /**
    *  Displays information in the InstanceTrackerPanel
    */
   private JTextField instanceTrackerInfoField;
   /**
    *  The search Pattern Tracker menu option.
    */
   private JMenuItem patternMenuItem;
   /**
    *  The Search Pattern Tracker frame.
    */
   private JInternalFrame findFrame;
   /**
    *  The Find button in the Search Pattern Tracker frame.
    */
   private JButton findNextButton;
   /**
    *  The Close button in the Search Pattern Tracker frame.
    */
   private JButton closeButton;
   /**
    *  The text field in the Search Pattern Tracker frame.
    */
   private JTextField findField;
   /**
    *  The combo box in the Search Pattern Tracker frame.
    */
   private JComboBox threadCombo;
   /**
    *  The panel chosen to search in.
    */
   private String panelName;
   /**
    *  The string to search for.
    */
   private String searchString;
   /**
    *  The main panel in the Search Pattern Tracker frame.
    */
   private JPanel findPanel;
   /**
    *  A panel in the Search Pattern Tracker frame.
    *  Contains the combo box.
    */
   private JPanel threadPanel;
   /**
    *  A panel in the Search Pattern Tracker frame.
    *  Contains the combo box.
    */
   private JPanel threadLayerPanel;
   /**
    *  A panel in the Search Pattern Tracker frame.
    *  Contains the text field.
    */
   private JPanel inputPanel;
   /**
    *  A panel in the Search Pattern Tracker frame.
    *  Contains the text field.
    */
   private JPanel inputLayerPanel;
   /**
    *  A panel in the Search Pattern Tracker frame.
    *  Contains the Find and Close buttons.
    */
   private JPanel buttonPanel;
   /**
    *  A panel in the Search Pattern Tracker frame.
    *  Contains the Find and Close buttons.
    */
   private JPanel buttonLayerPanel;

   /**
    *  Constructor: creates a new PatternDisplayer. Initializes
    *  the internal frame and adds them to the desktop.
    */
   public PatternDisplayer()
   {
      width = 230; // initial width of JInternalFrames
      height = 400; // initial height of JInternalFrames

      // set up the object interaction internal frame:
      objectInteractionInfoField = new JTextField();
      objectInteractionInfoField.setEditable(false);

      objectInteractionPanel = new ObjectInteractionPanel(objectInteractionInfoField);
      objectInteractionPanelScroller = new JScrollPane(objectInteractionPanel);
      objectInteractionPanel.setScrollPane(objectInteractionPanelScroller);

      objectInteractionFrame = new JInternalFrame();
      // set JInternalFrame properties:
      objectInteractionFrame.setClosable(true);
      objectInteractionFrame.setMaximizable(true);
      objectInteractionFrame.setIconifiable(true);
      objectInteractionFrame.setResizable(true);
      objectInteractionFrame.setSize(width,height/2);
      objectInteractionFrame.setTitle("Object Interaction");
      objectInteractionFrame.getContentPane().add(objectInteractionPanelScroller,"Center");
      objectInteractionFrame.getContentPane().add(objectInteractionInfoField,"North");
      objectInteractionFrame.show();

      add(objectInteractionFrame,new Integer(1));

      try
      {
         objectInteractionFrame.setIcon(true);
      }
      catch(java.beans.PropertyVetoException e)
      {
         e.printStackTrace();
      }

      // set up the method interaction internal frame:
      methodInteractionInfoField = new JTextField();
      methodInteractionInfoField.setEditable(false);

      methodInteractionPanel = new MethodInteractionPanel(methodInteractionInfoField);
      methodInteractionPanelScroller = new JScrollPane(methodInteractionPanel);
      methodInteractionPanel.setScrollPane(methodInteractionPanelScroller);

      methodInteractionFrame = new JInternalFrame();
      // set JInternalFrame properties:
      methodInteractionFrame.setClosable(true);
      methodInteractionFrame.setMaximizable(true);
      methodInteractionFrame.setIconifiable(true);
      methodInteractionFrame.setResizable(true);
      methodInteractionFrame.setSize(width,height/2);
      methodInteractionFrame.setTitle("Method Interaction");
      methodInteractionFrame.getContentPane().add(methodInteractionPanelScroller,"Center");
      methodInteractionFrame.getContentPane().add(methodInteractionInfoField,"North");
      methodInteractionFrame.show();

      add(methodInteractionFrame,new Integer(1));

      try
      {
         methodInteractionFrame.setIcon(true);
      }
      catch(java.beans.PropertyVetoException e)
      {
         e.printStackTrace();
      }

      // set up InstanceTrackerPanel:
      instanceTrackerInfoField = new JTextField();
      instanceTrackerInfoField.setEditable(false);

      instanceTrackerPanel = new InstanceTrackerPanel(instanceTrackerInfoField);
      instanceTrackerPanelScroller = new JScrollPane(instanceTrackerPanel);
      instanceTrackerPanel.setScrollPane(instanceTrackerPanelScroller);

      instanceTrackerFrame = new JInternalFrame();
      // set JInternalFrame properties:
      instanceTrackerFrame.setClosable(true);
      instanceTrackerFrame.setMaximizable(true);
      instanceTrackerFrame.setIconifiable(true);
      instanceTrackerFrame.setResizable(true);
      instanceTrackerFrame.setSize(width,height/2);
      instanceTrackerFrame.setTitle("Instance Tracker");
      instanceTrackerFrame.getContentPane().add(instanceTrackerPanelScroller,"Center");
      instanceTrackerFrame.getContentPane().add(instanceTrackerInfoField,"North");

      instanceTrackerFrame.show();

      add(instanceTrackerFrame,new Integer(1));

      try
      {
         instanceTrackerFrame.setIcon(true);
      }
      catch(java.beans.PropertyVetoException e)
      {
         e.printStackTrace();
      }
   }

   /**
    *  Returns the ObjectInteractionPanel.
    *
    *  @return The ObjectInteractionPanel.
    */
   public ObjectInteractionPanel getObjectInteractionPanel()
   {
      return objectInteractionPanel;
   }

   /**
    *  Returns the MethodInteractionPanel.
    *
    *  @return The MethodInteractionPanel.
    */
   public MethodInteractionPanel getMethodInteractionPanel()
   {
      return methodInteractionPanel;
   }

   /**
    *  Returns the InstanceTrackerPanel.
    *
    *  @return The InstanceTrackerPanel.
    */
   public InstanceTrackerPanel getInstanceTrackerPanel()
   {
      return instanceTrackerPanel;
   }

   /**
    *  Sets the search Pattern Tracker menu item.
    *
    *  @param  item  The search Pattern Tracker menu item.
    */
   public void setPatternMenuItem(JMenuItem item)
   {
      patternMenuItem = item;
      patternMenuItem.addActionListener(new FindButtonListener());
   }

   /**
    *  Sets the Show Connection Lines menu item.
    *
    *  @param  item  The Show Connection Lines menu item.
    */
   public void setShowLinesMenuItem(JMenuItem item)
   {
      instanceTrackerPanel.setShowLinesMenuItem(item);
   }

   /**
    *  Sets the Show Instances in Linear Form menu item.
    *
    *  @param  item  The Show Instances in Linear Form menu item.
    */
   public void setShowLinearMenuItem(JMenuItem item)
   {
      instanceTrackerPanel.setShowLinearMenuItem(item);
   }

   /**
    *  Sets the Reduce Instance Tracker Size menu item.
    *
    *  @param  item  The Reduce Instance Tracker Size menu item.
    */
   public void setReduceMenuItem(JMenuItem item)
   {
      instanceTrackerPanel.setReduceMenuItem(item);
   }

   /**
    *  Sets the Increase Instance Tracker Size menu item.
    *
    *  @param  item  The Increase Instance Tracker Size menu item.
    */
   public void setIncreaseMenuItem(JMenuItem item)
   {
      instanceTrackerPanel.setIncreaseMenuItem(item);
   }

   /**
    *  Searches the method names of a chosen panel for a search string.
    *  If a match is found, then the panel window is selected
    *  and the scrollbars are scrolled down to focus on the match.
    *  If no (more) matches are found, an appropriate message
    *  is displayed.
    */
   private void find()
   {
      panelName = (String)threadCombo.getSelectedItem();
      searchString = findField.getText();

      if(panelName.equals("Object Interaction")) // search the object interaction frame
      {
         // search for the string
         if(searchString.length()>0)
         {
            boolean foundObject = objectInteractionPanel.findObject(searchString);

            if(foundObject)
            {
               // show the object interaction frame
               try
               {
                  if(objectInteractionFrame.isIcon())
                     objectInteractionFrame.setIcon(false);
                  if(!objectInteractionFrame.isSelected())
                     objectInteractionFrame.setSelected(true);
               }
               catch(java.beans.PropertyVetoException e)
               {
                  e.printStackTrace();
               }
            }
            else // object not found
            {
               JOptionPane.showMessageDialog(this,
                  "There were no matches found for \"" + searchString + "\" in the Object Interaction Frame",
                  "No matches found",
                  JOptionPane.PLAIN_MESSAGE);
            }
         }
      }
      else if(panelName.equals("Method Interaction")) // search the method interaction panel
      {
         // search for the string
         if(searchString.length()>0)
         {
            boolean foundMethod = methodInteractionPanel.findMethod(searchString);

            if(foundMethod)
            {
               // show the object interaction frame
               try
               {
                  if(methodInteractionFrame.isIcon())
                     methodInteractionFrame.setIcon(false);
                  if(!methodInteractionFrame.isSelected())
                     methodInteractionFrame.setSelected(true);
               }
               catch(java.beans.PropertyVetoException e)
               {
                  e.printStackTrace();
               }
            }
            else // method not found
            {
               JOptionPane.showMessageDialog(this,
                  "There were no matches found for \"" + searchString + "\" in the Method Interaction Frame",
                  "No matches found",
                  JOptionPane.PLAIN_MESSAGE);
            }
         }
      }
      else // search the instance tracker frame
      {
         // search for the string
         if(searchString.length()>0)
         {
            boolean foundMethod = instanceTrackerPanel.findInstance(searchString);

            if(foundMethod)
            {
               // show the object interaction frame
               try
               {
                  if(instanceTrackerFrame.isIcon())
                     instanceTrackerFrame.setIcon(false);
                  if(!instanceTrackerFrame.isSelected())
                     instanceTrackerFrame.setSelected(true);
               }
               catch(java.beans.PropertyVetoException e)
               {
                  e.printStackTrace();
               }
            }
            else // method not found
            {
               JOptionPane.showMessageDialog(this,
                  "There were no matches found for \"" + searchString + "\" in the Instance Tracker Frame",
                  "No matches found",
                  JOptionPane.PLAIN_MESSAGE);
            }
         }
      }
   }

   /**
    *  Displays the Search Pattern Tracker frame.
    */
   private void startFindFrame()
   {
      findFrame = new JInternalFrame();
      findFrame.setSize(225,135);
      findFrame.setTitle("Search Pattern Tracker");
      findFrame.setClosable(true);
      jifContainer = findFrame.getContentPane();

         findPanel = new JPanel();
         findPanel.setLayout(new GridLayout(3,1));

            threadPanel = new JPanel();
            threadPanel.setLayout(new BorderLayout());
               threadLayerPanel = new JPanel();
               threadLayerPanel.setLayout(new FlowLayout());
            threadPanel.add(threadLayerPanel,"West");
               threadLayerPanel.add(new Label("Frame:"));
                  threadCombo = new JComboBox();
                  threadCombo.addItem("Object Interaction");
                  threadCombo.addItem("Method Interaction");
                  threadCombo.addItem("Instance Tracker");
               threadLayerPanel.add(threadCombo);

            inputPanel = new JPanel();
            inputPanel.setLayout(new BorderLayout());
               inputLayerPanel = new JPanel();
               inputLayerPanel.setLayout(new FlowLayout());
            inputPanel.add(inputLayerPanel,"West");
               inputLayerPanel.add(new Label("Find:"));
                  findField = new JTextField(10);
               inputLayerPanel.add(findField);

            buttonPanel = new JPanel();
            buttonPanel.setLayout(new BorderLayout());
               buttonLayerPanel = new JPanel();
               buttonLayerPanel.setLayout(new FlowLayout());
            buttonPanel.add(buttonLayerPanel,"West");
                  findNextButton = new JButton("Find");
                  findNextButton.addActionListener(new FindButtonListener());
               buttonLayerPanel.add(findNextButton);
                  closeButton = new JButton("Close");
                  closeButton.addActionListener(new FindButtonListener());
               buttonLayerPanel.add(closeButton);

         findPanel.add(threadPanel);
         findPanel.add(inputPanel);
         findPanel.add(buttonPanel);

      jifContainer.add(findPanel,"Center");
      findFrame.show();
      add(findFrame,new Integer(2));
      try
      {
         findFrame.setSelected(true);
      }
      catch(java.beans.PropertyVetoException e)
      {
         e.printStackTrace();
      }
   }

   /**
    *  This class listens for a button to be pressed.
    */
   private class FindButtonListener implements ActionListener
   {
      /**
       *  If the search Pattern Tracker menu option is
       *  selected, the Search Pattern Tracker frame is
       *  displayed. If the Find button is pressed, the
       *  chosen frame in the combo box is searched for
       *  a string matching the one in the text field.
       *  If the Close button is pressed, the Search
       *  Pattern Tracker frame is closed.
       */
      public void actionPerformed(ActionEvent event)
      {
         Object source = event.getSource();

         if(source==patternMenuItem)
         {
            startFindFrame();
         }
         else if(source==findNextButton)
         {
            find();
         }
         else if(source==closeButton)
         {
            findFrame.dispose();
         }
      }
   }
}