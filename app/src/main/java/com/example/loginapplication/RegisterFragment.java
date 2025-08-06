package com.example.loginapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loginapplication.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {
    
    private FragmentRegisterBinding binding;
    private static final String TAG = "RegisterFragment";
    private static final String ADMIN_PASSWORD = "1234";
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
    }
    
    private void setupViews() {
        // 初期状態の設定
        binding.tilAdminPassword.setVisibility(View.GONE); // TextInputLayoutを非表示
        binding.cbAdmin.setChecked(false); // 初期状態ではオフ
        
        // 管理者チェックボックスの状態変更リスナー
        binding.cbAdmin.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                // 管理者チェックボックスがチェックされている場合のみ管理者パスワードフィールドを表示
                binding.tilAdminPassword.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                if (!isChecked) {
                    // チェックが外れた場合は管理者パスワードをクリア
                    binding.etAdminPassword.setText("");
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
                    Toast.makeText(getContext(), "ユーザーIDとパスワードを入力してください", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // 管理者チェックボックスがチェックされている場合の処理
                if (binding.cbAdmin.isChecked()) {
                    String adminPassword = binding.etAdminPassword.getText().toString();
                    if (adminPassword.isEmpty()) {
                        Toast.makeText(getContext(), "管理者パスワードを入力してください", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!adminPassword.equals(ADMIN_PASSWORD)) {
                        Toast.makeText(getContext(), "管理者パスワードが間違っています", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                
                // ユーザーIDの重複チェック
                User existingUser = RealmManager.getUserByPersonID(personID);
                if (existingUser != null) {
                    Toast.makeText(getContext(), "このユーザーIDは既に使用されています", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // ユーザー種別を決定（管理者チェックボックスがチェックされている場合は管理者、そうでなければ一般）
                int userType = binding.cbAdmin.isChecked() ? 0 : 1;
                
                // ユーザー登録
                boolean success = RealmManager.createUser(personID, password, userType);
                if (success) {
                    String userTypeText = userType == 0 ? "管理者" : "一般";
                    Toast.makeText(getContext(), "ユーザー登録が完了しました（" + userTypeText + "）", Toast.LENGTH_SHORT).show();
                    clearInputs();
                } else {
                    Toast.makeText(getContext(), "ユーザー登録に失敗しました", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    private void clearInputs() {
        binding.etUsername.setText("");
        binding.etPassword.setText("");
        binding.etAdminPassword.setText("");
        binding.cbAdmin.setChecked(false);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 