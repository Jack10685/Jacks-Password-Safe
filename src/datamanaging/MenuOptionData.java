/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanaging;

import java.util.HashMap;
import main.Main;

/**
 *
 * @author Connor Jack
 */
public class MenuOptionData {
        private String siteName;
        private String password;
        private String userName;
        private String URL;
        private String notes;
        private HashMap<String, HashMap<String, String>> passList;
        
        public MenuOptionData(String siteName, String userName, String password, String URL, String notes) {
            this.siteName = siteName;
            this.userName = userName;
            this.password = password;
            this.URL = URL;
            this.notes = notes;
            passList = Main.getSaveManager().getPassList();
        }
        
        public MenuOptionData() {
            URL = "";
            notes = "";
            password = "";
            userName = "";
            siteName = "";
        }
        
        public void changePassword(String newPassword) {
            password = newPassword;
            passList.get(siteName).replace("password", newPassword);
            Main.getSaveManager().acceptNewPassList(passList);
        }
        
        public void chageUserName(String newUser) {
            userName = newUser;
            passList.get(siteName).replace("user", newUser);
            Main.getSaveManager().acceptNewPassList(passList);
        }
        
        public void changeURL(String URL) {
            this.URL = URL;
            passList.get(siteName).replace("url", URL);
            Main.getSaveManager().acceptNewPassList(passList);
        }
        
        public void changeSiteName(String siteName) {
            passList.put(siteName, passList.get(this.siteName));
            passList.remove(this.siteName);
            this.siteName = siteName;
            Main.getSaveManager().acceptNewPassList(passList);
        }
        
        public void changeNotes(String notes) {
            this.notes = notes;
            passList.get(siteName).replace("notes", notes);
            Main.getSaveManager().acceptNewPassList(passList);
        }
        
        public String getSiteName() {
            return siteName;
        }
        
        public String getPassword() {
            return password;
        }
        
        public String getUserName() {
            return userName;
        }
        
        public String getURL() {
            return URL;
        }
        
        public String getNotes() {
            return notes;
        }
}
