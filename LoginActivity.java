package com.example.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatapplication.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    String emailId,passwordData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.register.setOnClickListener(v -> {
            Intent intent=new Intent(LoginActivity.this,AuthenticationActivity.class);
            startActivity(intent);
        });
        binding.login.setOnClickListener(v -> {
            login_redirect();
        });
    }

    private void login_redirect(){
        emailId=binding.email.getText().toString().trim();
        if(emailId.isEmpty()) {
            binding.email.setError("Required Field");
            return;
        }
        passwordData=binding.pass.getText().toString().trim();
        if(passwordData.isEmpty()) {
            binding.pass.setError("Required Field");
            return;
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailId.trim(),passwordData).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Toast.makeText(LoginActivity.this,"Login Successfull",Toast.LENGTH_LONG).show();
                    finish();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(LoginActivity.this,"User not found....Register yourself please!",Toast.LENGTH_LONG).show();
        });
    }
}