/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encryption;

import java.util.Arrays;
import org.jasypt.util.text.StrongTextEncryptor;

/**
 *
 * @author Connor
 */
public class DataEncryptor {
    private static StrongTextEncryptor enc;
    
    public static void init(byte[] password) {
        enc = new StrongTextEncryptor();
        enc.setPassword(Arrays.toString(password));
    }
    
    public static void init(String password) {
        enc = new StrongTextEncryptor();
        enc.setPassword(password);
    }
    
    public static String encrypt(String message) {
      return enc.encrypt(message);
    }
    
    public static String decrypt(String message) {
      return enc.decrypt(message);
    }
}
