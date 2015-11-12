package com.indyzalab.rainywords.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class RoomList extends JPanel {

  JList<String> list;

  DefaultListModel<String> model;
  
  String labels[] = { "Room 1", "Room 2", "Room 3", "Room 4", "Room 5", "Room 6", "Room 7" };

  public RoomList() {
    setLayout(new BorderLayout());
    model = new DefaultListModel<String>();
    for (int i = 0, n = labels.length; i < n; i++) {
        model.addElement(labels[i]);
    }
   
    list = new JList<String>(model);
    JScrollPane pane = new JScrollPane(list);
    JButton resetButton = new JButton("Reset");

    resetButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    	      int selected[] = list.getSelectedIndices();
    	      
    	      for (int i = 0; i < selected.length; i++) {
    	        String element = (String) list.getModel().getElementAt(selected[i]);
    	        int index = selected[i]+1;
    	        System.out.print(element + "  " + index);
    	      }
    	    }
    });

    add(pane, BorderLayout.NORTH);
    add(resetButton, BorderLayout.WEST);
  }

  public static void main(String s[]) {
    JFrame frame = new JFrame("Choose the Room");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(new RoomList());
    frame.setSize(260, 200);
    frame.setVisible(true);
  }
}
