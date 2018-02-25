/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import main.Main;
import datamanaging.MenuOptionData;
import java.awt.Dimension;

/**
 *
 * @author Connor
 */
public class MainScreen {
    private JFrame frame;
    private JPanel container;
    private JScrollPane sitePane;
    private JPanel viewPanel;
    private HashMap<String, HashMap<String, String>> passlist;
    private static ArrayList<MenuOptionData> list;
    public static JLabel[] attributeLabels;
    public static JPanel editOptions;
    private static MenuOptionData selectedOption;
    private static MenuOtherListener sideListener;
    
    public MainScreen() {
        setupDataTable(false);
    }
    
    private void setupDataTable(boolean isRefresh) {
        if (isRefresh) {
            container.removeAll();
        } else {
            frame = new JFrame();
            container = new JPanel();
        }
        
        sitePane = new JScrollPane();
        viewPanel = new JPanel();
        passlist = Main.getSaveManager().getPassList();
        
        attributeLabels = new JLabel[5];
        for (int i = 0; i < 4; i++) {
            attributeLabels[i] = new JLabel();
            viewPanel.add(attributeLabels[i]);
        }
        editOptions = new JPanel();
        sideListener = new MenuOtherListener();
    }
    
    public void makePopup(boolean isRefresh) {
        frame.setLayout(null);
        container.setLayout(null);
        frame.getContentPane().setPreferredSize(new Dimension(500, 400));
        container.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        buildHashMap();
        sitePane.setLayout(null);
        sitePane.setSize(150, 400);
        for (JButton i : displayMenuOptions()) {
            sitePane.add(i);
        }
        
        JButton edit = new JButton();
        edit.setText("Edit");
        edit.setActionCommand("edit");
        edit.addActionListener(sideListener);
        JButton delete = new JButton();
        delete.setText("Delete");
        delete.setActionCommand("delete");
        delete.addActionListener(sideListener);
        editOptions = new JPanel();
        editOptions.setLayout(new GridLayout(1,2));
        editOptions.add(edit);
        editOptions.add(delete);
        
        viewPanel.setLayout(new GridLayout(5, 1));
        viewPanel.setSize(350, 400);
        viewPanel.setLocation(150, 0);
        viewPanel.add(editOptions);
        editOptions.setVisible(false);
        
        container.add(sitePane);
        container.add(viewPanel);
        frame.add(container);
        frame.pack();
        frame.setLocationRelativeTo(null);
        //frame.validate();
        frame.setVisible(true);
    }
    
    public void refreshScreen() {
        setupDataTable(true);
        makePopup(true);
    }
    
    private void buildHashMap() {
        list = new ArrayList<>();
        if (passlist.size() > 0) {
            Iterator it = passlist.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry kv = (Map.Entry) it.next();
                HashMap<String, String> val = (HashMap<String, String>) kv.getValue();
                String key = (String) kv.getKey();
                MenuOptionData mod = new MenuOptionData(key, val.get("userName"), val.get("password"), val.get("url"), val.get("notes"));
                list.add(mod);
            }
        }
    }
    
    private JButton[] displayMenuOptions() {
        MenuOptionListener mol = new MenuOptionListener();
        JButton[] buttonList = new JButton[1+list.size()];
        JButton newPass = new JButton();
        newPass.setText("New Entry");
        newPass.setActionCommand("new");
        newPass.addActionListener(sideListener);
        newPass.setSize(sitePane.getWidth() - 10, 30);
        newPass.setLocation(5, 5);
        buttonList[0] = newPass;
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                int k = list.size()-i-1;
                int vLocation = (k+1)*35+5;
                int hLocation = 5;
                int vSize = 30;
                int hSize = sitePane.getWidth() - 10;
                
                JButton button = new JButton();
                button.setText(list.get(i).getSiteName());
                button.setActionCommand(list.get(i).getSiteName());
                button.addActionListener(mol);
                button.setSize(hSize, vSize);
                button.setLocation(hLocation, vLocation);
                buttonList[i+1] = button;
            }
        }
        return buttonList;
    }
    
    public static ArrayList<MenuOptionData> getList() {
        return list;
    }
    
    public static void updateJLabels(String[] labelTexts) {
        for (int i = 0; i < 4; i++) {
            attributeLabels[i].setText(labelTexts[i]);
        }
    }
    
    public static void setSelectedOption(MenuOptionData mod) {
        selectedOption = mod;
        editOptions.setVisible(true);
    }
    
    private class MenuOptionListener implements ActionListener {
        
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            MenuOptionData selectedOption = new MenuOptionData();
            boolean optionFound = false;
            for (MenuOptionData mod : MainScreen.getList()) {
                if (mod.getSiteName().equals(e.getActionCommand())) {
                    optionFound = true;
                    MainScreen.setSelectedOption(mod);
                    selectedOption = mod;
                    break;
                }
            }
            int curLabelPos = 0;
            if (optionFound) {
                String[] labelTexts = new String[4];
                if (!selectedOption.getUserName().equals("")) {
                    labelTexts[curLabelPos] = "User Name: "+selectedOption.getUserName();
                    curLabelPos++;
                }
                if (!selectedOption.getPassword().equals("")) {
                    labelTexts[curLabelPos] = "Password: "+selectedOption.getPassword();
                    curLabelPos++;
                }
                if (!selectedOption.getURL().equals("")) {
                    labelTexts[curLabelPos] = "URL: "+selectedOption.getURL();
                    curLabelPos++;
                }
                if (!selectedOption.getNotes().equals("")) {
                    labelTexts[curLabelPos] = "Notes: "+selectedOption.getNotes();
                    curLabelPos++;
                }
                MainScreen.updateJLabels(labelTexts);
            } else {
                System.err.println("Data read error: option DNE");
            }
        }
        
    }
    private class MenuOtherListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("new"))
                new MenuOptionEditor();
            else if (e.getActionCommand().equals("edit")) {
                new MenuOptionEditor(selectedOption);
            } else if (e.getActionCommand().equals("delete"))
                new DeletePrompt(selectedOption.getSiteName());
        }
        
    }
}
