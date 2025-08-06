package com.example.loginapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loginapplication.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {
    
    private FragmentLoginBinding binding;
    private static final String TAG = "LoginFragment";
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
    }
    
    private void setupViews() {
        // デバッグ用：テストユーザー作成ボタン
        binding.btnLogin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 長押しでテストユーザーを作成
                boolean success = RealmManager.createUser("test", "test123", 1);
                if (success) {
                    Toast.makeText(getContext(), "テストユーザーを作成しました（test/test123）", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "テストユーザー作成に失敗しました", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        
        // ログインボタンのクリックリスナー
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personID = binding.etUsername.getText().toString();
                String password = binding.etPassword.getText().toString();

                if (personID.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getContext(), "ユーザーIDとパスワードを入力してください", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                User user = RealmManager.authenticateUser(personID, password);
                if (user != null) {
                    Toast.makeText(getContext(), "ログイン成功！", Toast.LENGTH_SHORT).show();
                    // MainActivityに戻る（ログイン情報付き）
                    Intent intent = new Intent();
                    intent.putExtra("PERSON_ID", user.getPersonID());
                    intent.putExtra("USER_TYPE", user.getUserType());
                    if (getActivity() != null) {
                        getActivity().setResult(getActivity().RESULT_OK, intent);
                        getActivity().finish();
                    }
                } else {
                    Toast.makeText(getContext(), "ユーザーIDまたはパスワードが間違っています", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        // ゲストで始めるボタンのクリックリスナー
        binding.btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "ゲストとして開始します", Toast.LENGTH_SHORT).show();
                // MainActivityに戻る（ゲスト情報付き）
                Intent intent = new Intent();
                intent.putExtra("PERSON_ID", "guest");
                intent.putExtra("USER_TYPE", 2); // ゲスト
                if (getActivity() != null) {
                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                }
            }
        });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 