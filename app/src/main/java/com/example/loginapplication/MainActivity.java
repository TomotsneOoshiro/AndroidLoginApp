package com.example.loginapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    
    private ActivityMainBinding binding;
    private String currentPersonID;
    private int currentUserType;
    private static final int LOGIN_REQUEST_CODE = 1001;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Realmの初期化（Contextを渡す）
        RealmManager.initRealm(this);
        
        // Intentからユーザー情報を取得
        Intent intent = getIntent();
        if (intent != null) {
            currentPersonID = intent.getStringExtra("PERSON_ID");
            currentUserType = intent.getIntExtra("USER_TYPE", 1);
        }
        
        // ログイン情報がない場合はLoginActivityに遷移
        if (currentPersonID == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, LOGIN_REQUEST_CODE);
            return;
        }
        
        setupViews();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                // ログイン成功
                currentPersonID = data.getStringExtra("PERSON_ID");
                currentUserType = data.getIntExtra("USER_TYPE", 1);
                setupViews();
            } else {
                // ログインキャンセルまたは失敗
                finish();
            }
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // onResumeでもログイン情報をチェック（LoginActivityから戻ってきた場合）
        if (currentPersonID == null) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, LOGIN_REQUEST_CODE);
            return;
        }
        
        // ログイン情報がある場合はUIを更新
        if (binding != null) {
            setupViews();
        }
    }
    
    private void setupViews() {
        // ユーザー情報を表示
        String userTypeText = getUserTypeText(currentUserType);
        binding.tvWelcome.setText("ようこそ " + currentPersonID + "さん (" + userTypeText + ")");
        
        // ログアウトボタンのクリックリスナー
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "ログアウトしました", Toast.LENGTH_SHORT).show();
                // ログイン情報をクリアしてLoginActivityに遷移
                currentPersonID = null;
                currentUserType = 1;
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, LOGIN_REQUEST_CODE);
            }
        });
        
        // ユーザー管理ボタン（管理者のみ表示）
        if (currentUserType == 0) {
            binding.btnUserManagement.setVisibility(View.VISIBLE);
            binding.btnUserManagement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "ユーザー管理画面に遷移します", Toast.LENGTH_SHORT).show();
                    // ここでユーザー管理画面に遷移
                }
            });
        } else {
            binding.btnUserManagement.setVisibility(View.GONE);
        }
        
        // 設定ボタン
        binding.btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "設定画面に遷移します", Toast.LENGTH_SHORT).show();
                // ここで設定画面に遷移
            }
        });
    }
    
    private String getUserTypeText(int userType) {
        switch (userType) {
            case 0:
                return "管理者";
            case 1:
                return "一般";
            case 2:
                return "ゲスト";
            default:
                return "不明";
        }
    }
} 