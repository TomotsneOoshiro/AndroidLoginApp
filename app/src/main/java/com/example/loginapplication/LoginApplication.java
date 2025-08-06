package com.example.loginapplication;

import android.app.Application;
import android.util.Log;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObjectSchema;

public class LoginApplication extends Application {
    
    private static final String TAG = "LoginApplication";
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Application created");
        
        // Realmの初期化
        try {
            Realm.init(this);
            Log.d(TAG, "Realm.init() completed");
            
            // シンプルなRealm設定
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .name("login_app.realm")
                    .schemaVersion(1)
                    .deleteRealmIfMigrationNeeded() // 開発中は便利
                    .allowWritesOnUiThread(true) // UIスレッドでの書き込みを許可
                    .build();
            Realm.setDefaultConfiguration(config);
            Log.d(TAG, "Realm configuration set");
            
            // デバッグ用：スキーマを確認
            try {
                Realm testRealm = Realm.getDefaultInstance();
                Log.d(TAG, "Realm schema size: " + testRealm.getSchema().getAll().size());
                for (RealmObjectSchema schema : testRealm.getSchema().getAll()) {
                    Log.d(TAG, "Schema class: " + schema.getClassName());
                    Log.d(TAG, "Schema fields: " + schema.getFieldNames());
                }
                testRealm.close();
                Log.d(TAG, "Realm schema check completed");
            } catch (Exception e) {
                Log.e(TAG, "Error checking schema: " + e.getMessage(), e);
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error initializing Realm: " + e.getMessage(), e);
        }
    }
} 