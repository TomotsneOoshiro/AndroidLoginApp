package com.example.loginapplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User extends RealmObject {
    @PrimaryKey
    @Required
    private String personID = "";
    
    @Required
    private String password = "";
    
    private boolean isDeleted = false;
    
    private int userType = 1; // 0=管理者, 1=一般, 2=ゲスト
    
    private boolean isSkipConsent = false;

    // デフォルトコンストラクタ
    public User() {}

    // Getter and Setter methods
    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public boolean isSkipConsent() {
        return isSkipConsent;
    }

    public void setSkipConsent(boolean skipConsent) {
        isSkipConsent = skipConsent;
    }
} 