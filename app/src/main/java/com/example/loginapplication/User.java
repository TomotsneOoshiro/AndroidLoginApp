package com.example.loginapplication;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import io.realm.annotations.RealmClass;

@RealmClass
public class User extends RealmObject {
    @PrimaryKey
    @Required
    private String personID = "";
    
    @Required
    private String password = "";
    
    private boolean isDeleted = false;
    
    private int userType = 1; // 0=管理者, 1=一般, 2=ゲスト
    
    private boolean isSkipConsent = false;

    // デフォルトコンストラクタ（Realmが必要）
    public User() {
        this.personID = "";
        this.password = "";
        this.isDeleted = false;
        this.userType = 1;
        this.isSkipConsent = false;
    }

    // パラメータ付きコンストラクタ
    public User(String personID, String password, int userType) {
        this.personID = personID;
        this.password = password;
        this.userType = userType;
        this.isDeleted = false;
        this.isSkipConsent = false;
    }

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