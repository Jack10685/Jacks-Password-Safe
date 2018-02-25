/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import encryption.DataEncryptor;
import encryption.PasswordEncryptor;
import gui.NPOrKBPrompt;
import gui.NumPad;
import datamanaging.SaveFileManager;
import gui.MainScreen;
import gui.PasswordScreen;

/**
 *
 * @author Connor Jack
 */
public class Main {
    private static SaveFileManager psfm;
    private static PasswordEncryptor pwe;
    private static DataEncryptor data;
    private static MainScreen screen;
    
    public static void main(String[] args) {
        psfm = new SaveFileManager();
        pwe = new PasswordEncryptor();
        data = new DataEncryptor();
        if (psfm.fileExists()) {
            if (psfm.isPINFile()) {
                NumPad numpad = new NumPad(NumPad.MODE_ACCESS);
                numpad.showNumPad();
            } else {
                PasswordScreen pws = new PasswordScreen(PasswordScreen.MODE_ACCESS);
                pws.showScreen();
            }
        } else {
            NPOrKBPrompt setpass = new NPOrKBPrompt();
        }
    }
    
    public static void createMainScreen() {
        screen = new MainScreen();
    }
    
    public static MainScreen getMainScreen() {
       if (screen == null)
           screen = new MainScreen();
       return screen; 
    }
    
    public static SaveFileManager getSaveManager() {
        return psfm;
    }
    
    public static PasswordEncryptor getPasswordEncryptor() {
        return pwe;
    }
}
