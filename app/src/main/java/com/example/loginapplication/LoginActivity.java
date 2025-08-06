package com.example.loginapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loginapplication.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    
    private ActivityLoginBinding binding;
    private static final String ADMIN_PASSWORD = "1234";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Realmの初期化（Contextを渡す）
        RealmManager.initRealm(this);
        
        setupViews();
    }
    
    private void setupViews() {
        // ログインボタンのクリックリスナー
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personID = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();
                
                if (personID.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "ユーザーIDとパスワードを入力してください", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                User user = RealmManager.authenticateUser(personID, password);
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "ログイン成功！", Toast.LENGTH_SHORT).show();
                    // MainActivityに戻る（ログイン情報付き）
                    Intent intent = new Intent();
                    intent.putExtra("PERSON_ID", user.getPersonID());
                    intent.putExtra("USER_TYPE", user.getUserType());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "ユーザーIDまたはパスワードが間違っています", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        // 登録ボタンのクリックリスナー
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personID = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();
                
                if (personID.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "ユーザーIDとパスワードを入力してください", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // 管理者チェックボックスがチェックされている場合の処理
                if (binding.cbAdmin.isChecked()) {
                    String adminPassword = binding.etAdminPassword.getText().toString();
                    if (adminPassword.isEmpty()) {
                        Toast.makeText(LoginActivity.this, "管理者パスワードを入力してください", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!adminPassword.equals(ADMIN_PASSWORD)) {
                        Toast.makeText(LoginActivity.this, "管理者パスワードが間違っています", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                
                // ユーザーIDの重複チェック
                User existingUser = RealmManager.getUserByPersonID(personID);
                if (existingUser != null) {
                    Toast.makeText(LoginActivity.this, "このユーザーIDは既に使用されています", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // ユーザー種別を決定（管理者チェックボックスがチェックされている場合は管理者、そうでなければ一般）
                int userType = binding.cbAdmin.isChecked() ? 0 : 1;
                
                // ユーザー登録
                boolean success = RealmManager.createUser(personID, password, userType);
                if (success) {
                    String userTypeText = userType == 0 ? "管理者" : "一般";
                    Toast.makeText(LoginActivity.this, "ユーザー登録が完了しました（" + userTypeText + "）", Toast.LENGTH_SHORT).show();
                    clearInputs();
                } else {
                    Toast.makeText(LoginActivity.this, "ユーザー登録に失敗しました", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        // 登録モード切り替えボタン
        binding.btnToggleMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btnRegister.getVisibility() == View.VISIBLE) {
                    // ログインモードに切り替え
                    binding.btnRegister.setVisibility(View.GONE);
                    binding.btnLogin.setVisibility(View.VISIBLE);
                    binding.llAdminSection.setVisibility(View.GONE);
                    binding.btnToggleMode.setText("新規登録");
                    binding.btnGuest.setVisibility(View.VISIBLE);
                } else {
                    // 登録モードに切り替え
                    binding.btnRegister.setVisibility(View.VISIBLE);
                    binding.btnLogin.setVisibility(View.GONE);
                    binding.llAdminSection.setVisibility(View.VISIBLE);
                    binding.btnToggleMode.setText("ログイン");
                    binding.btnGuest.setVisibility(View.GONE);
                }
            }
        });
        
        // ゲストで始めるボタンのクリックリスナー
        binding.btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "ゲストとしてログインしました", Toast.LENGTH_SHORT).show();
                // MainActivityに戻る（ゲストユーザー情報付き）
                Intent intent = new Intent();
                intent.putExtra("PERSON_ID", "guest");
                intent.putExtra("USER_TYPE", 2); // ゲストユーザー
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    
    private void clearInputs() {
        binding.etUsername.getText().clear();
        binding.etPassword.getText().clear();
        binding.etAdminPassword.getText().clear();
        binding.cbAdmin.setChecked(false);
    }
} 