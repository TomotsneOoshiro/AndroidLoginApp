package com.example.loginapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.loginapplication.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    
    private ActivityLoginBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Realmの初期化
        RealmManager.initRealm(this);
        
        setupViews();
        
        // 初期フラグメントとしてログインフラグメントを表示
        showLoginFragment();
    }
    
    private void setupViews() {
        // ログインボタンのクリックリスナー
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginFragment();
            }
        });
        
        // 新規登録ボタンのクリックリスナー
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterFragment();
            }
        });
    }
    
    private void showLoginFragment() {
        // ボタンの表示状態を更新
        binding.btnLogin.setVisibility(View.GONE);
        binding.btnRegister.setVisibility(View.VISIBLE);
        
        // ログインフラグメントを表示
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, new LoginFragment())
                .commit();
    }
    
    private void showRegisterFragment() {
        // ボタンの表示状態を更新
        binding.btnLogin.setVisibility(View.VISIBLE);
        binding.btnRegister.setVisibility(View.GONE);
        
        // 新規登録フラグメントを表示
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, new RegisterFragment())
                .commit();
    }
} 