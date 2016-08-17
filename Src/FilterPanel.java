package ObjectTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 *  This class represents a panel. The purpose of
 *  the panel is to allow the user to enter class
 *  names into a list. This list will be used to
 *  filter out all classes that are not present in
 *  in the list from the Graphical Java Displayer.
 *
 *  @author  Eoin O'Connor
 *  @see UserOptionsPanel
 *  @see ObjectTracker
 */
public class FilterPanel extends JPanel
{
   /**
    *  The width of the panel.
    */
   private int width;
   /**
    *  The height of the panel.
    */
   private int height;
   /**
    *  The button used to add a class name to
    *  the list.
    */
   private JButton addButton;
   /**
    *  The button used to remove a class name
    *  from the list.
    */
   private JButton removeButton;
   /**
    *  A JList used to manage the real list.
    */
   private JList list;
   /**
    *  A temporary list.
    */
   private DefaultListModel listModel;
   /**
    *  Used to enter class names.
    */
   private JTextField textField;
   /**
    *  The list of class names.
    */
   private ArrayList classList;

   /**
    *  Constuctor: initializes the panel and
    *  the lists. Sets the button listeners.
    */
   public FilterPanel()
   {
      width = 240;
      height = 200;
      ButtonListener listener = new ButtonListener();
      classList = new ArrayList();

      setLayout(new GridLayout(1,2));

         JPanel mainPanel = new JPanel();
         mainPanel.setLayout(new BorderLayout());

            JPanel listLabelPanel = new JPanel();
            listLabelPanel.setLayout(new FlowLayout());

               JLabel listLabel = new JLabel("Filter List:");
            listLabelPanel.add(listLabel);
               addButton = new JButton("Add");
               addButton.addActionListener(listener);
            listLabelPanel.add(addButton);
               removeButton = new JButton("Remove");
               removeButton.addActionListener(listener);
            listLabelPanel.add(removeButton);

         mainPanel.add(listLabelPanel,"North");

            JPanel listPanel = new JPanel();
            listPanel.setLayout(new FlowLayout());

               list = new JList();
               list.setVisibleRowCount(5);
               listModel = new DefaultListModel();
               JScrollPane listScroller = new JScrollPane(list);
               list.setModel(listModel);

            listPanel.add(listScroller);

         mainPanel.add(listPanel,"Center");

            JPanel fieldPanel = new JPanel();
            fieldPanel.setLayout(new FlowLayout());
               textField = new JTextField(20);
               textField.setText("Enter class name");
            fieldPanel.add(textField);

         mainPanel.add(fieldPanel,"South");

      add(mainPanel);
   }

   /**
    *  Adds a class name from the text field
    *  to the JList and to the ArrayList of class
    *  names. Resets the contents of the text field.
    */
   private void addClass()
   {
      String className = textField.getText();
      if(className.length() > 0)
      {
         if(!classList.contains(className))
         {
            listModel.addElement(className);
            list.setModel(listModel);
            classList.add(className);
         }
         textField.setText("");
      }
   }

   /**
    *  Removes the selected class name from
    *  the JList and from the ArrayList of
    *  class names.
    */
   private void removeClass()
   {
      int index = list.getSelectedIndex();
      if(index > -1)
      {
         String className = (String)list.getSelectedValue();
         classList.remove(className);
         listModel.remove(index);
         list.setModel(listModel);
      }
   }

   /**
    *  Returns the list of class names.
    *
    *  @return The list of class names.
    */
   public ArrayList getClassList()
   {
      return classList;
   }

   /**
    *  This class listens for the add or remove
    *  button to be pressed.
    */
   private class ButtonListener implements ActionListener
   {
      /**
       *  If the add button is pressed then the
       *  addClass() method is called. If the remove
       *  button is pressed then the removeClass()
       *  method is called.
       *
       *  @param  event The ActionEvent that has occurred.
       */
      public void actionPerformed(ActionEvent event)
      {
         Object source = event.getSource();

         if(source==addButton)
            addClass();
         else if(source==removeButton)
            removeClass();
      }
   }
}