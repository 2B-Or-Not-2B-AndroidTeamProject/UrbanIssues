package com.example.telerik.urbanissues.models;

import android.graphics.Bitmap;

import com.telerik.everlive.sdk.core.EverliveApp;
import com.telerik.everlive.sdk.core.EverliveAppSettings;
import com.telerik.everlive.sdk.core.model.system.User;

import java.util.Hashtable;
import java.util.UUID;

import static com.example.telerik.urbanissues.common.Constants.APP_ID;

public class BaseViewModel {
    public static EverliveApp urbanIssuesApp;

    private Hashtable<UUID, Bitmap> pictures = new Hashtable<UUID, Bitmap>();
    private Hashtable<UUID, User> users = new Hashtable<UUID, User>();
    private User loggedUser;
    private String selectedAccount;

    private static BaseViewModel instance;

    public void addUser(User user) {
        this.users.put(user.getId(), user);
    }

    public User getUserById(UUID id) {
        return this.users.get(id);
    }

    public void addPicture(UUID id, Bitmap image) {
        this.pictures.put(id, image);
    }

    public Bitmap getPictureById(UUID id) {
        return this.pictures.get(id);
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public String getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(String selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    public static BaseViewModel getInstance() {
        if (instance == null) {
            instance = new BaseViewModel();
        }
        return instance;
    }

    public static void initialize(EverliveApp app) {
        String appId = APP_ID;
        EverliveAppSettings appSettings = new EverliveAppSettings();
        appSettings.setAppId(appId);
        appSettings.setUseHttps(true);

        app = new EverliveApp(appSettings);
    }
}