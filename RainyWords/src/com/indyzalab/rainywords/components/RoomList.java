package com.indyzalab.rainywords.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.indyzalab.rainywords.utils.RoomListListener;

public class RoomList extends JPanel {

  JList<String> list;

  DefaultListModel<String> model;
  RoomListListener listener = new RoomListListener() {
	
	@Override
	public void onClickResetButton(int index) {
		// TODO Auto-generated method stub
		
	}
  };
  
  public RoomList() {
    setLayout(new BorderLayout());
    model = new DefaultListModel<String>();
   
    list = new JList<String>(model);
    JScrollPane pane = new JScrollPane(list);
    JButton resetButton = new JButton("Reset");
    final JTextField timeBox = new JTextField("Time...");
    timeBox.addMouseListener(new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e){
            timeBox.setText("");
        }
    });
    

    JButton setTime = new JButton("Set time");

    resetButton.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
    	      int index = getSelectedIndex();
    	      listener.onClickResetButton(index);
    	 }
    });

    add(pane, BorderLayout.NORTH);
    add(resetButton, BorderLayout.WEST);
    add(timeBox);
    add(setTime, BorderLayout.EAST);
  }

  
  
  public void setListener(RoomListListener listener) {
	this.listener = listener;
  }



public int getSelectedIndex(){
	  int selected[] = list.getSelectedIndices();
      int index_return = 0;
      for (int i = 0; i < selected.length; i++) {
        String element = (String) list.getModel().getElementAt(selected[i]);
        int index = selected[i];
        System.out.print(element + "  " + index);
        index_return = selected[i];
        break;
      }
      return index_return;
  }
  public void setListData(ArrayList<String> arrayList){
	  model = new DefaultListModel<String>();
	  for(String s:arrayList){
		  model.addElement(s);
	  }
	  list.setModel(model);
  }
  public static void main(String s[]) {
    JFrame frame = new JFrame("Choose the Room");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(new RoomList());
    frame.setSize(260, 200);
    frame.setVisible(true);
  }
}
