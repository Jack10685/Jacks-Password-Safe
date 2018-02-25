package gui;


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import main.Main;
import datamanaging.SaveFileManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Connor Jack
 */
public class NumPad {
    public static final int MODE_CREATE = 0;
    public static final int MODE_ACCESS = 1;
    
    private byte[] keycode;
    private NumPadButtonListener codeListener;
    private JFrame frame;
    private JLabel codeLabel;
    private JLabel errorLabel;
    private JPanel keypad;
    private String message;
    private boolean hasError;
    private int mode;
    
    public NumPad() {
        keycode = new byte[10];
        codeListener = new NumPadButtonListener();
        for (int i = 0; i < 10; i++) {
            keycode[i] = -1;
        }
        message = " ";
        mode = MODE_ACCESS;
        hasError = false;
    }
    
    public NumPad(int mode) {
        keycode = new byte[10];
        codeListener = new NumPadButtonListener();
        for (int i = 0; i < 10; i++) {
            keycode[i] = -1;
        }
        this.mode = mode;
        if (mode == MODE_CREATE) {
            message = "Enter a PIN";
        } else {
            message = " ";
        }
        hasError = false;
    }
    
    public void showNumPad() {
        frame = new JFrame();
        frame.addKeyListener(codeListener);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        
        codeLabel = new JLabel(message, JLabel.CENTER);
        codeLabel.setSize(300, 75);
        codeLabel.setFocusable(true);
        codeLabel.addKeyListener(codeListener);
        codeLabel.setFont(new Font(codeLabel.getFont().getName(), Font.PLAIN, 30));
        frame.add(codeLabel, BorderLayout.NORTH);
        
        keypad = new JPanel();
        keypad.setLayout(new GridLayout(4, 3));
        keypad.addKeyListener(codeListener);
        
        for (int i = 1; i <= 9; i++) {
            JButton button = new JButton();
            button.setText(String.valueOf(i));
            button.setActionCommand(String.valueOf(i));
            button.addActionListener(codeListener);
            button.addKeyListener(codeListener);
            keypad.add(button);
        }
        
        JButton back = new JButton();
        back.setText("←");
        back.setActionCommand("back");
        back.addActionListener(codeListener);
        back.addKeyListener(codeListener);
        keypad.add(back);
        
        JButton zero = new JButton();
        zero.setText("0");
        zero.setActionCommand("0");
        zero.addActionListener(codeListener);
        zero.addKeyListener(codeListener);
        keypad.add(zero);
        
        JButton enter = new JButton();
        enter.setText("Enter");
        enter.setActionCommand("enter");
        enter.addActionListener(codeListener);
        enter.addKeyListener(codeListener);
        keypad.add(enter);
        frame.add(keypad, BorderLayout.CENTER);
        
        errorLabel = new JLabel("", JLabel.CENTER);
        errorLabel.addKeyListener(codeListener);
        errorLabel.setFocusable(true);
        frame.add(errorLabel, BorderLayout.SOUTH);
        
        frame.setVisible(true);
    }
    
    private void pushNumPadLabelText() {
        long shownum = 0;
        for (int i = 0; i < 10; i++) {
            if (keycode[i] == -1) {
                break;
            }
            shownum *= 10;
            shownum += keycode[i];
        }
        String strform = String.valueOf(shownum);
        String push = "";
        for (int i = 0; i < strform.length(); i++) {
            push+="•";
        }
        if (keycode[0] == -1)
            codeLabel.setText(message);
        else
            codeLabel.setText(push);
    }
    
    private void addKeyCodeNumber(int number) {
        for (int i = 0; i < 10; i++) {
            if (keycode[i] == -1) {
                keycode[i] = (byte) number;
                break;
            }
        }
        pushNumPadLabelText();
    }
    
    private void subtractKeyCodeNumber() {
        if (keycode[9] != -1) {
            keycode[9] = -1;
        } else if (keycode[0] != -1) {
            for (int i = 0; i < 10; i++) {
                if (keycode[i] == -1 && i > 0) {
                    keycode[i-1] = -1;
                    break;
                }
            }
        }
        pushNumPadLabelText();
    }
    
    private JButton makeButton(int number) {
        JButton button = new JButton();
        button.setText(Integer.toString(number));
        button.setActionCommand(Integer.toString(number));
        button.addActionListener(codeListener);
        return button;
    }
    
    private int verifyKeyPressed(String number) {
        try {
            int numberConverted = Integer.parseInt(number);
            return numberConverted;
        } catch (NumberFormatException exception) {

        }
        return -1;
    }
    
    private void setError(String error) {
        if (!hasError)
            frame.setSize(frame.getWidth(), frame.getHeight()+16);
        hasError = true;
        errorLabel.setText(error);
    }
    
    private void removeError() {
        if (hasError)
            frame.setSize(frame.getWidth(), frame.getHeight()-16);
        hasError = false;
        errorLabel.setText("");
    }
    
    private void userPressEnter() {
        if (keycode[3] == -1 && mode == MODE_CREATE) {
            setError("PIN must have atleast 4 numbers");
        } else if (mode == MODE_CREATE) {
            removeError();
            SaveFileManager sfm = Main.getSaveManager();
            sfm.createPINFile(keycode);
            Main.getPasswordEncryptor().setPassword(keycode);
            Main.getSaveManager().loadList();
            MainScreen screen = new MainScreen();
            screen.makePopup(false);
            frame.dispose();
        } else if (mode == MODE_ACCESS) {
            Main.getPasswordEncryptor().setPassword(keycode);
            if (Main.getSaveManager().checkPassword()) {
                Main.getSaveManager().loadList();
                Main.createMainScreen();
                Main.getMainScreen().makePopup(false);
                frame.dispose();
            } else {
                setError("PIN is incorrect");
            }
        }
    }
    
    private class NumPadButtonListener implements ActionListener, KeyListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int key = verifyKeyPressed(e.getActionCommand());
            if (key != -1) {
                addKeyCodeNumber(key);
            } else if (e.getActionCommand().equals("enter")) {
                userPressEnter();
            } else if (e.getActionCommand().equals("back")) {
                subtractKeyCodeNumber();
            }
        }

       @Override
        public void keyTyped(KeyEvent e) {
            String key = Character.toString(e.getKeyChar());
            int number = verifyKeyPressed(key);
            if (number != -1) {
                addKeyCodeNumber(number);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                userPressEnter();
            } else if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DECIMAL) {
                subtractKeyCodeNumber();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // do nothing
        }
        
    }
}
