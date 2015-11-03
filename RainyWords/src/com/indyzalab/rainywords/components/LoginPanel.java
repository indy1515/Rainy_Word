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

public class LoginPanel extends JPanel {
    public final static String
        LOG_IN  = "Login",
        LOG_OUT = "Logout";
    protected JButton logButt;
    public JButton getLogButton() { return logButt; }
    private final static int DEFAULT_PSWD_CHARS = 10;
    private JLabel logonameField = new JLabel("Rainy Words", SwingConstants.CENTER); 
    private JTextField nameField = new JTextField(DEFAULT_PSWD_CHARS);
    public String getUserName() { return nameField.getText(); }

    /**
     * override this method to return true if approved, false otherwise.
     * default is true.
     */
    public boolean approveLogin(String uname) {
        return true;
    }

    /**
     * override this method to learn about logout events.
     */
    public void loggedOut(String uname) {
    }

    public LoginPanel() {
        this(false);
    }

    public LoginPanel(final boolean clearPasswords) {
        this(clearPasswords, true, null, null);
    }

    /**
     * @param clearPasswords if true, clears password field on successful login.
     * @param initial_user optional default text to load into the 'user' type-in.
     * @param initial_password optional default text to load into the 'password' type-in.
     */
    public LoginPanel(final boolean clearPasswords, final boolean displayFailures, String initial_user, String initial_password) {
//        final JPasswordField pswdField = new JPasswordField(DEFAULT_PSWD_CHARS);
        logButt = new JButton(LOG_IN);
        KeyListener quickLogin = new KeyAdapter() {
            public void keyTyped(KeyEvent ke) {
                if(ke.getKeyChar() == KeyEvent.VK_ENTER) {
                    logButt.doClick();
                    logButt.requestFocus();
                }
            }
        };
        nameField.setText(initial_user);
//        pswdField.setText(initial_password);
        logButt.setName(LOG_IN);
        nameField.addKeyListener(quickLogin);
        nameField.setSize(100, 100);
//        pswdField.addKeyListener(quickLogin);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(255,255,0));
        titlePanel.add(logonameField, BorderLayout.CENTER);
        titlePanel.setPreferredSize(new Dimension(1000, 300));
        titlePanel.setSize(1000, 300);
        JPanel containerGrid = new JPanel(new BorderLayout());
        containerGrid.setBackground(new Color(255,0,0));
        containerGrid.setPreferredSize(new Dimension(1000, 300));
        JPanel nameGrid = new JPanel();
//        nameGrid.setLayout(new BoxLayout(nameGrid, BoxLayout.X_AXIS));
        nameGrid.setBackground(new Color(255,255,255));
        nameGrid.add(new JLabel("User Name"));
        nameGrid.add(nameField);
        nameGrid.setPreferredSize(new Dimension(50, 100));
//        nameGrid.setSize(new Dimension(50, 50));
        
        containerGrid.add(nameGrid ,BorderLayout.CENTER);
//        grid.add(new JLabel("Password"));
//        grid.add(pswdField);

        // create login button row
        JPanel row = new JPanel();
        row.setBorder(new EmptyBorder(5, 0, 5, 0));
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setPreferredSize(new Dimension(1000, 300));
        row.add(logButt);
        logButt.setBackground(new Color(220,220,220));

        logButt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(logButt.getText().equals(LOG_IN)) {
                    // seek login approval from derived class
                    if(approveLogin(nameField.getText())) {
                        // note: must set logout text *before* clearing password
                        // otherwise component dependancy handler will disable the
                        // login button w/out password text before later setting logout text
                        // this closes bug #2336
                        logButt.setText(LOG_OUT);
              
                        nameField.setEnabled(false);
//                        pswdField.setEnabled(false);
                        fireLoginEvent(nameField.getText(), true);
                    }
                    else
                        if(displayFailures)
                            JOptionPane.showMessageDialog(LoginPanel.this, "Login Denied", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    logButt.setText(LOG_IN);
                    loggedOut(nameField.getText());
                    nameField.setEnabled(true);
//                    pswdField.setEnabled(true);
                    fireLoginEvent(nameField.getText(), false);
                }
            }
        });

        // implement component dependancies
        new ComponentDependencyHandler(nameField) {
            public void dependencyNotification() {
                String
                    logtext = logButt.getText(),
                    nametext = nameField.getText();
                boolean newstate = logtext.equalsIgnoreCase(LOG_OUT) ||
                    (nameField.getText() != null && nametext.length() > 0); // has login text?
                logButt.setEnabled(newstate);
            }
        };

        // construct final layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(titlePanel);
        add(containerGrid);
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