package com.indyzalab.rainywords.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.indyzalab.rainywords.utils.ComponentDependencyHandler;

import java.awt.*;
import java.awt.event.*;
import java.util.*;


/**
 * Title:        Login Panel
 * Description:  A simple yet complete login/logout panel with user callbacks
 *               for approving login attempts and getting notification of logouts.
 * Copyright:    Copyright (c) 2004
 * Company:      Superliminal Software
 * @author Melinda Green
 * @version 1.0
 */

public class ServiceLoginPanel2 extends JPanel {
    public final static String
        LOG_IN  = "Login",
        LOG_OUT = "Log out",
        HOST = "host";
    protected JButton logButt;
    protected JButton hostBtn;
    public JButton getLogButton() { return logButt; }
    public JButton getHostButton() { return hostBtn; }
    private final static int DEFAULT_PSWD_CHARS = 10;
    private JTextField nameField = new JTextField(DEFAULT_PSWD_CHARS);
    public String getUserName() { return nameField.getText(); }

    
    public void onClickHostBtn(){
    	
    }
    
    /**
     * override this method to return true if approved, false otherwise.
     * default is true.
     */
    public boolean approveLogin(String uname, String pswd) {
        return true;
    }

    /**
     * override this method to learn about logout events.
     */
    public void loggedOut(String uname) {
    }

    public ServiceLoginPanel2() {
        this(false);
    }

    public ServiceLoginPanel2(final boolean clearPasswords) {
        this(clearPasswords, true, null, null);
    }

    /**
     * @param clearPasswords if true, clears password field on successful login.
     * @param initial_user optional default text to load into the 'user' type-in.
     * @param initial_password optional default text to load into the 'password' type-in.
     */
    public ServiceLoginPanel2(final boolean clearPasswords, final boolean displayFailures, String initial_user, String initial_password) {
        final JTextField pswdField = new JTextField(DEFAULT_PSWD_CHARS);
        logButt = new JButton(LOG_IN);
        KeyListener quickLogin = new KeyAdapter() {
            public void keyTyped(KeyEvent ke) {
                if(ke.getKeyChar() == KeyEvent.VK_ENTER) {
                    logButt.doClick();
                    logButt.requestFocus();
                }
            }
        };
        
        hostBtn = new JButton(HOST);
       
        nameField.setText(initial_user);
        pswdField.setText(initial_password);
        logButt.setName(LOG_IN);
        nameField.addKeyListener(quickLogin);
        pswdField.addKeyListener(quickLogin);
        // create the grid
        JPanel grid = new JPanel(new GridLayout(2, 2));
        grid.setBackground(new Color(255,255,255));
        grid.add(new JLabel("IP"));
        grid.add(nameField);
        grid.add(new JLabel("Port"));
        grid.add(pswdField);

        // create login button row
        JPanel row = new JPanel();
        row.setBorder(new EmptyBorder(5, 0, 5, 0));
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.add(hostBtn);
        row.add(logButt);
        
        hostBtn.setBackground(new Color(220,220,220));

        hostBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	onClickHostBtn();
            }
        });
        
        logButt.setBackground(new Color(220,220,220));

        logButt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(logButt.getText().equals(LOG_IN)) {
                    // seek login approval from derived class
                    if(approveLogin(nameField.getText(), new String(pswdField.getText()))) {
                        // note: must set logout text *before* clearing password
                        // otherwise component dependancy handler will disable the
                        // login button w/out password text before later setting logout text
                        // this closes bug #2336
                        logButt.setText(LOG_OUT);
                        if(clearPasswords)
                            pswdField.setText(null);
                        nameField.setEnabled(false);
                        pswdField.setEnabled(false);
                        fireLoginEvent(nameField.getText(), true);
                    }
                    else
                        if(displayFailures)
                            JOptionPane.showMessageDialog(ServiceLoginPanel2.this, "Login Denied", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    logButt.setText(LOG_IN);
                    loggedOut(nameField.getText());
                    nameField.setEnabled(true);
                    pswdField.setEnabled(true);
                    fireLoginEvent(nameField.getText(), false);
                }
            }
        });

        // implement component dependancies
        new ComponentDependencyHandler(nameField, pswdField) {
            public void dependencyNotification() {
                String
                    logtext = logButt.getText(),
                    nametext = nameField.getText(),
                    pswdtext = pswdField.getText();
                boolean newstate = logtext.equalsIgnoreCase(LOG_OUT) ||
                    (nameField.getText() != null && nametext.length() > 0 // has login text?
                     && pswdtext.length() > 0);  // has password text?
                logButt.setEnabled(newstate);
            }
        };

        // construct final layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(grid);
        add(row);
    }

    public interface LoginListener {
        void loggedIn(String uname);
        void loggedOut(String uname);
    }
    public static class LoginAdapter implements LoginListener {
        public void loggedIn(String uname){}
        public void loggedOut(String uname){}
    }
    private Vector loginListeners = new Vector();
    public void addLoginListener(LoginListener ll) { loginListeners.add(ll); }
    public void removeLoginListener(LoginListener ll) { loginListeners.remove(ll); }
    protected void fireLoginEvent(String uname, boolean in) {
        for(Enumeration e=loginListeners.elements(); e.hasMoreElements(); ) {
            LoginListener ll = (LoginListener)e.nextElement();
            if(in)
                ll.loggedIn(uname);
            else
                ll.loggedOut(uname);
        }
    }


}