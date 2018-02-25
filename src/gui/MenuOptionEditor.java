/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;


import datamanaging.MenuOptionData;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import main.Main;
/**
 *
 * @author Connor Jack
 */
public class MenuOptionEditor {
    public static final int MODE_CREATE = 0;
    public static final int MODE_EDIT = 1;
    
    private JFrame frame;
    private JLabel[] labels;
    private JTextField[] fields;
    private JButton enter;
    private JButton cancel;
    private JPanel labelFieldPanel;
    private boolean isCreating;
    private OptionEditorListener oel;
    private MenuOptionData oldData;
    private MenuOptionData newData;
    
    public MenuOptionEditor(MenuOptionData mod) {
        createLabelArray(mod);
        oldData = mod;
        oel = new OptionEditorListener();
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(oel);
        
        labelFieldPanel = new JPanel();
        labelFieldPanel.addKeyListener(oel);
        labelFieldPanel.setLayout(new GridLayout(5,2));
        
        for (int i = 0; i < 5; i++) {
            fields[i].setFocusable(true);
            fields[i].addKeyListener(oel);
            labels[i].addKeyListener(oel);
            labelFieldPanel.add(labels[i]);
            labelFieldPanel.add(fields[i]); 
        }
        isCreating = false;
        frame.add(labelFieldPanel, BorderLayout.CENTER);
        
        JButton create = new JButton();
        create.setText("Create");
        create.addActionListener(oel);
        create.setActionCommand("create");
        create.addKeyListener(oel);
        frame.add(create, BorderLayout.SOUTH);
        
        frame.setVisible(true);
    }
    
    public MenuOptionEditor() {
        createLabelArray();
        oel = new OptionEditorListener();
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(oel);
        
        labelFieldPanel = new JPanel();
        labelFieldPanel.addKeyListener(oel);
        labelFieldPanel.setLayout(new GridLayout(5,2));
        
        for (int i = 0; i < 5; i++) {
            fields[i].setFocusable(true);
            fields[i].addKeyListener(oel);
            labels[i].addKeyListener(oel);
            labelFieldPanel.add(labels[i]);
            labelFieldPanel.add(fields[i]); 
        }
        isCreating = true;
        frame.add(labelFieldPanel, BorderLayout.CENTER);
        
        JButton create = new JButton();
        create.setText("Create");
        create.addActionListener(new OptionEditorListener());
        create.setActionCommand("create");
        create.addKeyListener(oel);
        frame.add(create, BorderLayout.SOUTH);
        
        frame.setVisible(true);
    }
    
    private void createLabelArray() {
        fields = new JTextField[5];
        labels = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            fields[i] = new JTextField();
            labels[i] = new JLabel();
        }
        labels[0].setText("Website:");
        labels[1].setText("User Name:");
        labels[2].setText("Password:");
        labels[3].setText("URL:");
        labels[4].setText("Notes:");
    }
    
    private void createLabelArray(MenuOptionData mod) {
        fields = new JTextField[5];
        labels = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            fields[i] = new JTextField();
            labels[i] = new JLabel();
        }
        labels[0].setText("Website:");
        labels[1].setText("User Name:");
        labels[2].setText("Password:");
        labels[3].setText("URL:");
        labels[4].setText("Notes:");
        
        fields[0].setText(mod.getSiteName());
        fields[1].setText(mod.getUserName());
        fields[2].setText(mod.getPassword());
        fields[3].setText(mod.getURL());
        fields[4].setText(mod.getNotes());
    }
    
    private class OptionEditorListener implements ActionListener, KeyListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("create")) {
                String[] options = new String[5];
                for (int i = 0; i < 5; i++) {
                    options[i] = fields[i].getText();
                }
                if (options[0].equals("")) {
                    System.err.println("No site name specified");
                    frame.dispose();
                } else {
                    MenuOptionData mod = new MenuOptionData(options[0], options[1], options[2], options[3], options[4]);
                    Main.getSaveManager().addSingleSite(mod);
                    Main.getMainScreen().refreshScreen();
                    frame.dispose();
                }
            } else if (e.getActionCommand().equals("edit")) {
                String[] options = new String[5];
                for (int i = 0; i < 5; i++) {
                    options[i] = fields[i].getText();
                }
                newData = new MenuOptionData(options[0], options[1], options[2], options[3], options[4]);
                boolean siteNameChange = false;
                if (options[0].equals("")) {
                    System.err.println("No site name specified");
                    frame.dispose();
                } else {
                    if (newData.getSiteName() != oldData.getSiteName()) {
                        siteNameChange = true;
                        Main.getSaveManager().removeSiteData(oldData.getSiteName());
                        Main.getSaveManager().addSingleSite(newData);
                    } else
                        Main.getSaveManager().updateSingleSiteData(newData);
                }
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
            // do nothing
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (isCreating) {
                    String[] options = new String[5];
                    for (int i = 0; i < 5; i++) {
                        options[i] = fields[i].getText();
                    }
                    MenuOptionData mod = new MenuOptionData(options[0], options[1], options[2], options[3], options[4]);
                    Main.getSaveManager().addSingleSite(mod);
                    Main.getMainScreen().refreshScreen();
                    frame.dispose();
                } else {
                    String[] options = new String[5];
                for (int i = 0; i < 5; i++) {
                    options[i] = fields[i].getText();
                }
                newData = new MenuOptionData(options[0], options[1], options[2], options[3], options[4]);
                boolean siteNameChange = false;
                if (newData.getSiteName().equals(oldData.getSiteName())) {
                    siteNameChange = true;
                    Main.getSaveManager().removeSiteData(oldData.getSiteName());
                    Main.getSaveManager().addSingleSite(newData);
                } else
                    Main.getSaveManager().updateSingleSiteData(newData);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // do nothing
        }
        
    }
}
