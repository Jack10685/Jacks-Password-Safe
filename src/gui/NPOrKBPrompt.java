/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Connor
 */
public class NPOrKBPrompt {
    
    private JFrame frame;
    
    public NPOrKBPrompt() {
        frame = new JFrame();
        frame.setSize(200, 300);
        frame.setLayout(new GridLayout(3,1));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        NOKPromptListener nokpl = new NOKPromptListener();
        
        JLabel label = new JLabel();
        label.setText("<html><div style=\"text-align:center\">Would you like your password file to have a PIN or password?</div></html>");
        frame.add(label);
        
        JButton pin = new JButton();
        pin.setText("PIN (less secure)");
        pin.setActionCommand("pin");
        pin.addActionListener(nokpl);
        frame.add(pin);
        
        JButton pass = new JButton();
        pass.setText("Password (more secure)");
        pass.setActionCommand("pass");
        pass.addActionListener(nokpl);
        frame.add(pass);
        frame.setVisible(true);
    }
    
    private class NOKPromptListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("pin")) {
                NumPad numpad = new NumPad(NumPad.MODE_CREATE);
                frame.dispose();
                numpad.showNumPad();
            } else {
                PasswordScreen pws = new PasswordScreen(PasswordScreen.MODE_CREATE);
                frame.dispose();
                pws.showScreen();
            }
        }
        
    }
}
