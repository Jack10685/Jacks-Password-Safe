/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 *
 * @author Connor
 */
public class PasswordEncryptor {
    private MessageDigest encrypter;
    private byte[] storedPassword;
    private byte[] activePassword;
    private String storedPasswordString;
    private String activePasswordString;
    
    public PasswordEncryptor(byte[] password) {
        try {
            encrypter = MessageDigest.getInstance("SHA-256");
            activePassword = encrypt(password);
            storedPassword = encrypt(activePassword);
            activePasswordString = new String(activePassword);
            storedPasswordString = new String(storedPassword);
            fixPasswords();
        } catch(NoSuchAlgorithmException e) {
            
        }
    }
    
    public PasswordEncryptor() {
        try {
            encrypter = MessageDigest.getInstance("SHA-256");
            activePassword = new byte[10];
            storedPassword = encrypt(activePassword);
            activePasswordString = new String(activePassword);
            storedPasswordString = new String(storedPassword);
            fixPasswords();
        } catch(NoSuchAlgorithmException e) {
            
        }
    }
    
    public void setPassword(byte[] password) {
        activePassword = encrypt(password);
        storedPassword = encrypt(activePassword);
        activePasswordString = new String(activePassword);
        storedPasswordString = new String(storedPassword);
        fixPasswords();
    }
    
    public byte[] fixPassword(byte[] password) {
        String ret = new String(password);
        ret = ret.replace("\n", "").replace("\r", "");
        return ret.getBytes();
    }
    
    public void fixPasswords() {
        storedPasswordString = storedPasswordString.replace("\n", "").replace("\r", "");
        activePasswordString = activePasswordString.replace("\n", "").replace("\r", "");
        activePassword = activePasswordString.getBytes();
        storedPassword = storedPasswordString.getBytes();
    }
    
    public String getStoredPassword() {
        return storedPasswordString;
    }
    
    public String getActivePassword() {
        return activePasswordString;
    }
    
    public byte[] getStoredPasswordBytes() {
        return storedPassword;
    }
    
    public byte[] getActivePasswordBytes() {
        return activePassword;
    }
    
    public byte[] encrypt(byte[] message) {
        return encrypter.digest(message);
    }
    
    public byte[] encrypt(String message) {
        return encrypter.digest(message.getBytes(StandardCharsets.UTF_8));
    }
    
    public String encryptToString(String message) {
        return new String(encrypt(message));
    }
}
