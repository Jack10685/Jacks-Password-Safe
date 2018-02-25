/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanaging;

import encryption.DataEncryptor;
import encryption.PasswordEncryptor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import main.Main;

/**
 *
 * @author Connor
 */
public class SaveFileManager {
    public static final int PIN_FILE = 0;
    public static final int PASS_FILE = 1;
    public static final int NO_FILE = 3;
    
    private static final String PIN = "TMP-PI.JPW";
    private static final String PASS = "TMP-PA.JPW";
    
    private File save;
    private int saveType;
    private HashMap<String, HashMap<String, String>> passList;
    
    public SaveFileManager() {
        save = new File(PIN);
        if (save.exists() && !save.isDirectory()) {
            saveType = PIN_FILE;
        } else {
            save = new File(PASS);
            if (save.exists() && !save.isDirectory()) {
                saveType = PASS_FILE;
            } else {
                saveType = NO_FILE;
            }
        }
    }
    
    public boolean fileExists() {
        return saveType != NO_FILE;
    }
    
    public boolean isPINFile() {
        return saveType == PIN_FILE;
    }
    
    public boolean isPasswordFile() {
        return saveType == PASS_FILE;
    }
    
    public void createPINFile(byte[] pin) {
        save = new File(PIN);
        try {
        FileWriter fw = new FileWriter(save);
        BufferedWriter bw = new BufferedWriter(fw);
        
        PasswordEncryptor pwe = Main.getPasswordEncryptor();
        pwe.setPassword(pin);
        bw.write(pwe.getStoredPassword());
        
        bw.close();
        fw.close();
        saveType = PIN_FILE;
        } catch(IOException e) {
            System.err.println("Could not create PIN File");
        }
    }
    
     public void createPassFile(byte[] pass) {
        save = new File(PASS);
        try {
        FileWriter fw = new FileWriter(save);
        BufferedWriter bw = new BufferedWriter(fw);
        
        PasswordEncryptor pwe = Main.getPasswordEncryptor();
        pwe.setPassword(pass);
        bw.write(pwe.getStoredPassword());
        
        bw.close();
        fw.close();
        saveType = PIN_FILE;
        } catch(IOException e) {
            System.err.println("Could not create Password File");
        }
    }
    
    public boolean checkPassword() {
        boolean passwordCorrect = false;
        try {
        FileReader fr = new FileReader(save);
        BufferedReader br = new BufferedReader(fr);
        String thing = br.readLine();
        if (thing.equals(Main.getPasswordEncryptor().getStoredPassword()))
            passwordCorrect = true;
        
        br.close();
        fr.close();
        
        } catch(FileNotFoundException e) {
            System.err.println("Password file not found");
        } catch(IOException e) {
            System.err.println("Cannot read from file");
        }
        return passwordCorrect;
    }
    
    public void loadList() {
        if (saveType != NO_FILE && checkPassword()) {
            passList = new HashMap<String, HashMap<String, String>>();
            try {
            FileReader fr = new FileReader(save);
            BufferedReader br = new BufferedReader(fr);
            DataEncryptor.init(Main.getPasswordEncryptor().getActivePasswordBytes());
            br.readLine();
            String line = br.readLine();
            boolean inData = false;
            String inDataLocation = "";
            while (line != null) {
                line = DataEncryptor.decrypt(line);
                if (!inData && line.startsWith("{")) {
                    inData = true;
                } else if(!inData && line.startsWith("\"")) {
                    inDataLocation = line.substring(1, line.length()-1);
                    passList.put(inDataLocation, new HashMap<>());
                    if (line.endsWith("{")) {
                        inData = true;
                    }
                } else if(inData && line.startsWith("\"")) {
                    String[] keyAndVal = line.split("\":\"");
                    String key = keyAndVal[0].substring(1);
                    String value = keyAndVal[1].substring(0, keyAndVal[1].length()-1);
                    passList.get(inDataLocation).put(key, value);
                    if (line.endsWith("}")) {
                        inData = false;
                        inDataLocation = "";
                    }
                } else if(line.startsWith("}")) {
                    inData = false;
                    inDataLocation = "";
                }
                line = br.readLine();
            }
            br.close();
            fr.close();
            } catch (FileNotFoundException e) {
                System.err.println("Password file not found");
            } catch (IOException e) {
                System.err.println("Cannot read password file");
            }
        }
    }
    
    public void saveList() {
        try {
            if (save.exists()) {
                save.delete();
            }
            FileWriter fw = new FileWriter(save);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(Main.getPasswordEncryptor().getStoredPassword());
            bw.newLine();
            //Iterator it = passList.entrySet().iterator();
            String[] keys1 = passList.keySet().toArray(new String[0]);
                //ArrayList<HashMap<String, String>> values1 = new ArrayList<>(passList.values().toArray(new HashMap<String, String>[0]));
            for (String firstKeys : keys1) {
                String[] keys2 = passList.get(firstKeys).keySet().toArray(new String[0]);
                String[] values = passList.get(firstKeys).values().toArray(new String[0]);
                bw.write(DataEncryptor.encrypt("\"" + firstKeys + "\""));
                bw.newLine();
                bw.write(DataEncryptor.encrypt("{"));
                bw.newLine();
                for(int k = 0; k < keys2.length; k++) {
                    bw.write(DataEncryptor.encrypt("\""+keys2[k]+"\":\""+values[k]+"\""));
                    bw.newLine();
                }   
                bw.write(DataEncryptor.encrypt("}"));
                bw.newLine();
                
            }
        bw.close();
        fw.close();
        } catch(IOException e) {
            System.err.println("Could not save Password file");
        }
    }
    
    public HashMap<String, HashMap<String, String>> getPassList() {
        return passList;
    }
    
    public MenuOptionData getSingleSiteData(String siteName) {
        MenuOptionData mod = new MenuOptionData();
        Iterator it = passList.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry KV = (Map.Entry)it.next();
            if (passList.get((String) KV.getValue()).get("siteName").equals(siteName)) {
                HashMap<String, String> modData = passList.get((String) KV.getValue());
                mod = new MenuOptionData(modData.get("siteName"), modData.get("userName"), modData.get("password"), modData.get("url"), modData.get("notes"));
                break;
            }
        }
        return mod;
    }
    
    public boolean updateSingleSiteData(MenuOptionData mod) {
        if (passList.containsKey(mod.getSiteName())) {
            HashMap<String, String> item = new HashMap<>();
            item.put("userName", mod.getUserName());
            item.put("password", mod.getPassword());
            item.put("url", mod.getURL());
            item.put("notes", mod.getNotes());
            passList.replace(mod.getSiteName(), item);
            saveList();
            return true;
        } else {
            System.err.println("Site data does not exist, creating...");
            addSingleSite(mod);
            return false;
        }
    }
    
    public boolean addSingleSite(MenuOptionData mod) {
        if (!passList.containsKey(mod.getSiteName())) {
            HashMap<String, String> item = new HashMap<>();
            item.put("userName", mod.getUserName());
            item.put("password", mod.getPassword());
            item.put("url", mod.getURL());
            item.put("notes", mod.getNotes());
            passList.put(mod.getSiteName(), item);
            saveList();
            return true;
        } else {
            System.err.println("Site data exists, remove first or update section");
            return false;
        }
    }
    
    public boolean removeSiteData(String siteName) {
        if (passList.containsKey(siteName)) {
            passList.remove(siteName);
            saveList();
            return true;
        } else {
            System.err.println("Tried to remove entry which does not exist");
            return false;
        }
    }
    
    public boolean moveSiteData(String siteName, String newSiteName) {
        if (!passList.containsKey(newSiteName) || siteName.equals(newSiteName)) {
            passList.put(newSiteName, passList.get(siteName));
            passList.remove(siteName);
            return true;
        } else {
            System.err.println(newSiteName+" already exists as an entry");
            return false;
        }
    }
    
    public void acceptNewPassList(HashMap<String, HashMap<String, String>> passList) {
        this.passList = passList;
        saveList();
    }
}
