package com.example.loginapplication;

import android.content.Context;
import android.util.Log;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import java.util.List;
import java.util.UUID;

public class RealmManager {
    
    private static final String TAG = "RealmManager";
    
    public static void initRealm(Context context) {
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("login_app.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded() // 開発中は便利
                .allowWritesOnUiThread(true) // UIスレッドでの書き込みを許可
                .build();
        Realm.setDefaultConfiguration(config);
        Log.d(TAG, "Realm initialized successfully");
    }
    
    public static boolean createUser(String personID, String password, int userType) {
        Realm realm = Realm.getDefaultInstance();
        try {
            Log.d(TAG, "Creating user: personID=" + personID + ", userType=" + userType);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    User user = realm.createObject(User.class);
                    user.setPersonID(personID);
                    user.setPassword(password); // 実際のアプリではハッシュ化すべき
                    user.setUserType(userType);
                    user.setDeleted(false);
                    user.setSkipConsent(false);
                    Log.d(TAG, "User created in transaction: " + user.getPersonID());
                }
            });
            Log.d(TAG, "User creation successful");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error creating user: " + e.getMessage(), e);
            return false;
        } finally {
            realm.close();
        }
    }
    
    public static User authenticateUser(String personID, String password) {
        Realm realm = Realm.getDefaultInstance();
        try {
            Log.d(TAG, "Authenticating user: personID=" + personID);
            User user = realm.where(User.class)
                    .equalTo("personID", personID)
                    .equalTo("password", password)
                    .equalTo("isDeleted", false)
                    .findFirst();
            
            if (user != null) {
                Log.d(TAG, "Authentication successful for user: " + user.getPersonID());
            } else {
                Log.d(TAG, "Authentication failed - user not found or password incorrect");
                // デバッグ用：全ユーザーを確認
                RealmResults<User> allUsers = realm.where(User.class).findAll();
                Log.d(TAG, "Total users in database: " + allUsers.size());
                for (User u : allUsers) {
                    Log.d(TAG, "User in DB: personID=" + u.getPersonID() + 
                          ", password=" + u.getPassword() + 
                          ", userType=" + u.getUserType() + 
                          ", isDeleted=" + u.isDeleted());
                }
            }
            return user;
        } catch (Exception e) {
            Log.e(TAG, "Error authenticating user: " + e.getMessage(), e);
            return null;
        } finally {
            realm.close();
        }
    }
    
    public static User getUserByPersonID(String personID) {
        Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(User.class)
                    .equalTo("personID", personID)
                    .equalTo("isDeleted", false)
                    .findFirst();
        } finally {
            realm.close();
        }
    }
    
    public static List<User> getAllUsers() {
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<User> results = realm.where(User.class)
                    .equalTo("isDeleted", false)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }
    
    public static List<User> getUsersByType(int userType) {
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<User> results = realm.where(User.class)
                    .equalTo("userType", userType)
                    .equalTo("isDeleted", false)
                    .findAll();
            return realm.copyFromRealm(results);
        } finally {
            realm.close();
        }
    }
    
    public static boolean deleteUser(String personID) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    User user = realm.where(User.class)
                            .equalTo("personID", personID)
                            .findFirst();
                    if (user != null) {
                        user.setDeleted(true);
                    }
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            realm.close();
        }
    }
    
    public static boolean updateUserType(String personID, int userType) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    User user = realm.where(User.class)
                            .equalTo("personID", personID)
                            .findFirst();
                    if (user != null) {
                        user.setUserType(userType);
                    }
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            realm.close();
        }
    }
    
    public static boolean updateSkipConsent(String personID, boolean skipConsent) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    User user = realm.where(User.class)
                            .equalTo("personID", personID)
                            .findFirst();
                    if (user != null) {
                        user.setSkipConsent(skipConsent);
                    }
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            realm.close();
        }
    }
} 