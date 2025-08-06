package com.example.loginapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Realmの初期化
        RealmManager.initRealm()
        
        setupViews()
    }
    
    private fun setupViews() {
        // ログインボタンのクリックリスナー
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "ユーザー名とパスワードを入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val user = RealmManager.authenticateUser(username, password)
            if (user != null) {
                Toast.makeText(this, "ログイン成功！ようこそ ${user.username}さん", Toast.LENGTH_SHORT).show()
                // ここでダッシュボード画面に遷移
            } else {
                Toast.makeText(this, "ユーザー名またはパスワードが間違っています", Toast.LENGTH_SHORT).show()
            }
        }
        
        // 登録ボタンのクリックリスナー
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val email = binding.etEmail.text.toString()
            
            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "すべての項目を入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // ユーザー名の重複チェック
            val existingUser = RealmManager.getUserByUsername(username)
            if (existingUser != null) {
                Toast.makeText(this, "このユーザー名は既に使用されています", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val success = RealmManager.createUser(username, email, password)
            if (success) {
                Toast.makeText(this, "ユーザー登録が完了しました", Toast.LENGTH_SHORT).show()
                clearInputs()
            } else {
                Toast.makeText(this, "ユーザー登録に失敗しました", Toast.LENGTH_SHORT).show()
            }
        }
        
        // 登録モード切り替えボタン
        binding.btnToggleMode.setOnClickListener {
            if (binding.btnRegister.visibility == android.view.View.VISIBLE) {
                // ログインモードに切り替え
                binding.btnRegister.visibility = android.view.View.GONE
                binding.etEmail.visibility = android.view.View.GONE
                binding.btnToggleMode.text = "新規登録"
            } else {
                // 登録モードに切り替え
                binding.btnRegister.visibility = android.view.View.VISIBLE
                binding.etEmail.visibility = android.view.View.VISIBLE
                binding.btnToggleMode.text = "ログイン"
            }
        }
    }
    
    private fun clearInputs() {
        binding.etUsername.text.clear()
        binding.etPassword.text.clear()
        binding.etEmail.text.clear()
    }
} 