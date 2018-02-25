/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import datamanaging.SaveFileManager;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import main.Main;

/**
 *
 * @author Connor
 */
public class PasswordScreen {
    public static final int MODE_CREATE = 0;
    public static final int MODE_ACCESS = 1;
    
    private int mode;
    private String password;
    private boolean hasError;
    private PasswordScreenListener psl;
    private JFrame frame;
    private JPasswordField passField;
    private JLabel errorLabel;
    
    public PasswordScreen() {
        mode = MODE_ACCESS;
    }
    
    public PasswordScreen(int mode) {
        this.mode = mode;
    }
    
    public void showScreen() {
        hasError = false;
        psl = new PasswordScreenListener();
        
        frame = new JFrame();
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(psl);
        JLabel label = new JLabel();
        label.addKeyListener(psl);
        JButton submit = new JButton();
        submit.addKeyListener(psl);
        passField = new JPasswordField();
        passField.addKeyListener(psl);
        errorLabel = new JLabel();
        errorLabel.addKeyListener(psl);
        JPanel centerPanel = new JPanel();
        centerPanel.addKeyListener(psl);
        centerPanel.setLayout(new BorderLayout());
        
        frame.setLayout(new BorderLayout());
        if (mode == MODE_CREATE){
            label.setText("<html><p>Set your password.</p></hyml>");
            submit.setText("Submit");
        } else {
            label.setText("<html><p>Type your password.</p></html>");
            submit.setText("Login");
        }
        frame.add(label, BorderLayout.NORTH);
        centerPanel.add(passField, BorderLayout.NORTH);
        centerPanel.add(submit, BorderLayout.SOUTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        errorLabel.setVisible(false);
        frame.add(errorLabel, BorderLayout.SOUTH);
        submit.addActionListener(psl);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public void setError(String text) {
        errorLabel.setVisible(true);
        errorLabel.setText("<html><p style='color:red'>"+text+"</p></html>");
        hasError = true;
    }
    
    public void removeError() {
        errorLabel.setVisible(false);
        errorLabel.setText("");
        hasError = false;
    }
    
    private void nextStep() {
        char[] pass = passField.getPassword();
        boolean cont = true;
        if (mode == MODE_CREATE) {
            if (pass.length <= 7) {
                setError("Password must be atleast 8 characters");
                cont = false;
            }
            if (pass.length > 30) {
                setError("Password must be 30 characters or less");
                cont = false;
            }
            if (cont) {
                removeError();
                SaveFileManager sfm = Main.getSaveManager();
                sfm.createPassFile(new String(passField.getPassword()).getBytes());
                Main.getPasswordEncryptor().setPassword(new String(passField.getPassword()).getBytes());
                Main.getSaveManager().loadList();
                MainScreen screen = new MainScreen();
                screen.makePopup(false);
                frame.dispose();
            }
        } else {
            Main.getPasswordEncryptor().setPassword(new String(passField.getPassword()).getBytes());
            if (Main.getSaveManager().checkPassword()) {
                Main.getSaveManager().loadList();
                Main.createMainScreen();
                Main.getMainScreen().makePopup(false);
                frame.dispose();
            } else {
                setError("Password is incorrect");
            }
        }
    }
    
    private class PasswordScreenListener implements ActionListener, KeyListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // clicked button
            nextStep();
        }

        @Override
        public void keyTyped(KeyEvent e) {
            //do nothing
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // pressed enter
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                nextStep();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            //do nothing
        }
        
    }
}
