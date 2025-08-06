package com.example.loginapplication;

import android.content.Context;
import android.util.Log;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.RealmObjectSchema;

import java.util.List;
import java.util.UUID;

public class RealmManager {
    
    private static final String TAG = "RealmManager";
    
    public static boolean createUser(String personID, String password, int userType) {
        Realm realm = Realm.getDefaultInstance();
        try {
            Log.d(TAG, "Creating user: personID=" + personID + ", userType=" + userType);
            
            // 既存ユーザーの確認
            User existingUser = realm.where(User.class)
                    .equalTo("personID", personID)
                    .findFirst();
            if (existingUser != null) {
                Log.w(TAG, "User already exists: " + personID);
                return false;
            }
            
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        User user = realm.createObject(User.class);
                        user.setPersonID(personID);
                        user.setPassword(password); // 実際のアプリではハッシュ化すべき
                        user.setUserType(userType);
                        user.setDeleted(false);
                        user.setSkipConsent(false);
                        Log.d(TAG, "User created in transaction: " + user.getPersonID());
                    } catch (Exception e) {
                        Log.e(TAG, "Error in transaction: " + e.getMessage(), e);
                        throw e; // トランザクションをロールバック
                    }
                }
            });
            
            // 作成後の確認
            User createdUser = realm.where(User.class)
                    .equalTo("personID", personID)
                    .findFirst();
            if (createdUser != null) {
                Log.d(TAG, "User creation verified: " + createdUser.getPersonID() + 
                      ", userType: " + createdUser.getUserType() + 
                      ", isDeleted: " + createdUser.isDeleted());
            } else {
                Log.e(TAG, "User creation failed - user not found after creation");
            }
            
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
            Log.d(TAG, "=== 認証デバッグ情報 ===");
            Log.d(TAG, "認証試行: personID=" + personID + ", password=" + password);
            
            // まず、指定されたpersonIDのユーザーを検索
            User userByID = realm.where(User.class)
                    .equalTo("personID", personID)
                    .findFirst();
            
            if (userByID != null) {
                Log.d(TAG, "personIDでユーザー発見: " + userByID.getPersonID() + 
                      ", password=" + userByID.getPassword() + 
                      ", isDeleted=" + userByID.isDeleted());
            } else {
                Log.d(TAG, "personIDでユーザーが見つかりません: " + personID);
            }
            
            // 全ユーザーを確認
            RealmResults<User> allUsers = realm.where(User.class).findAll();
            Log.d(TAG, "データベース内の全ユーザー数: " + allUsers.size());
            for (User u : allUsers) {
                Log.d(TAG, "全ユーザー: personID=" + u.getPersonID() + 
                      ", password=" + u.getPassword() + 
                      ", userType=" + u.getUserType() + 
                      ", isDeleted=" + u.isDeleted());
            }
            
            // 実際の認証処理
            User user = realm.where(User.class)
                    .equalTo("personID", personID)
                    .equalTo("password", password)
                    .equalTo("isDeleted", false)
                    .findFirst();
            
            if (user != null) {
                Log.d(TAG, "認証成功: " + user.getPersonID());
            } else {
                Log.d(TAG, "認証失敗 - 条件に合うユーザーが見つかりません");
            }
            return user;
        } catch (Exception e) {
            Log.e(TAG, "認証処理でエラー: " + e.getMessage(), e);
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