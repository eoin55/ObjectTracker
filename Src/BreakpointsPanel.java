package ObjectTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 *  This class represents a panel. The purpose of the panel is to allow the user
 *  to enter and remove method names to and from a list. This can then be used
 *  to set breakpoints in a program.
 *
 *  @author  Eoin O'Connor
 *  @see UserOptionsPanel
 *  @see ObjectTracker
 */
public class BreakpointsPanel extends JPanel
{
   /**
    *  The button to add a method name to the list.
    */
   private JButton addButton;
   /**
    *  The button to remove a method name from the list.
    */
   private JButton removeButton;
   /**
    *  The list of method names.
    */
   private JList list;
   /**
    *  A temporary list. Used to construct the real list.
    */
   private DefaultListModel listModel;
   /**
    *  The field used to enter method names.
    */
   private JTextField textField;
   /**
    *  The list of method names.
    */
   private ArrayList methodList;

   /**
    *  Constructor: initializes the panel and the list.
    *  Sets listeners for the buttons.
    */
   public BreakpointsPanel()
   {
      ButtonListener listener = new ButtonListener();
      methodList = new ArrayList();

      setLayout(new GridLayout(1,2));

         JPanel mainPanel = new JPanel();
         mainPanel.setLayout(new BorderLayout());

            JPanel listLabelPanel = new JPanel();
            listLabelPanel.setLayout(new FlowLayout());

               JLabel listLabel = new JLabel("Breakpoint List:");
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

                  ListIterator it = methodList.listIterator();
                  while(it.hasNext())
                  {
                     listModel.addElement((String)it.next());
                  }

               JScrollPane listScroller = new JScrollPane(list);
               list.setModel(listModel);

            listPanel.add(listScroller);

         mainPanel.add(listPanel,"Center");

            JPanel fieldPanel = new JPanel();
            fieldPanel.setLayout(new FlowLayout());
               textField = new JTextField(20);
               textField.setText("Enter method name");
            fieldPanel.add(textField);

         mainPanel.add(fieldPanel,"South");

      add(mainPanel);
   }

   /**
    *  Adds the method name in the JTextField to the JList and
    *  to the ArrayList. Resets the JTextField.
    */
   private void addMethod()
   {
      String methodName = textField.getText();
      if(methodName.length() > 0)
      {
         if(!methodList.contains(methodName))
         {
            listModel.addElement(methodName);
            list.setModel(listModel);
            methodList.add(methodName);
         }
         textField.setText("");
      }
   }

   /**
    *  Removes the selected method name in the JList
    *  from the ArrayList and from the JList.
    */
   private void removeMethod()
   {
      int index = list.getSelectedIndex();
      if(index > -1)
      {
         String methodName = (String)list.getSelectedValue();
         methodList.remove(methodName);
         listModel.remove(index);
         list.setModel(listModel);
      }
   }

   /**
    *  Returns the methodList.
    *
    *  @return  ArrayList
    */
   public ArrayList getMethodList()
   {
      return methodList;
   }

   /**
    *  This is called if the cancel button is pressed.
    *  It resets the list to what it was before the
    *  user started to change it.
    *
    *  @param   oldList  The original ArrayList
    */
   public void setMethodList(ArrayList oldList)
   {
      methodList = oldList;
      listModel = new DefaultListModel();
      ListIterator it = methodList.listIterator();
      while(it.hasNext())
      {
         listModel.addElement((String)it.next());
      }
      list.setModel(listModel);
   }

   /**
    * This class listens for the add or remove buttons to be pressed.
    */
   private class ButtonListener implements ActionListener
   {
      /**
      *  If the add button is pressed, addMethod() is called.
      *  If the remove button is pressed, removeMethod() is called.
      *
      *  @param   event The ActionEvent that has occurred.
      */
      public void actionPerformed(ActionEvent event)
      {
         Object source = event.getSource();

         if(source==addButton)
            addMethod();
         else if(source==removeButton)
            removeMethod();
      }
   }
}