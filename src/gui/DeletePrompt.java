/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import main.Main;

/**
 *
 * @author Connor
 */
public class DeletePrompt {
    private String siteName;
    private JFrame frame;
    
    public DeletePrompt(String siteName) {
        this.siteName = siteName;
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        JLabel text = new JLabel("<html><p style='text-align:center;'>Are you sure you want to delete data for the site \""+siteName+"\"?</p></html>");
        frame.add(text, BorderLayout.CENTER);
        
        DeletePromptListener dpl = new DeletePromptListener();
        JButton yes = new JButton("Yes");
        yes.setActionCommand("yes");
        yes.addActionListener(dpl);
        JButton no = new JButton("No");
        no.setActionCommand("no");
        no.addActionListener(dpl);
        JPanel optionPanel = new JPanel();
        optionPanel.setLayout(new GridLayout(1,2));
        optionPanel.add(no);
        optionPanel.add(yes);
        
        frame.add(optionPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    
    private class DeletePromptListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("yes")) {
                Main.getSaveManager().removeSiteData(siteName);
                frame.dispose();
                Main.getMainScreen().refreshScreen();
            } else
                frame.dispose();
        }
        
    }
}
