package com.example.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatapplication.databinding.ActivityAuthenticationBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthenticationActivity extends AppCompatActivity {

    ActivityAuthenticationBinding binding;
    String userName,emailId,passwordData;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAuthenticationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference= FirebaseDatabase.getInstance().getReference("users");

       binding.registerLogin.setOnClickListener(v -> {
           Intent intent=new Intent(AuthenticationActivity.this,LoginActivity.class);
           startActivity(intent);
       });

        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName=binding.name.getText().toString();
                emailId=binding.emailInput.getText().toString();
                passwordData=binding.passwordInput.getText().toString();
                register();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
            finish();
        }
    }

    private void register(){
        userName=binding.name.getText().toString().trim();
        if(userName.isEmpty()) {
            binding.name.setError("Required Field");
            return;
        }
        emailId=binding.emailInput.getText().toString().trim();
        if(emailId.isEmpty()) {
            binding.emailInput.setError("Required Field");
            return;
        }
        passwordData=binding.passwordInput.getText().toString().trim();
        if(passwordData.isEmpty()) {
            binding.passwordInput.setError("Required Field");
            return;
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailId.trim(),passwordData).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                UserProfileChangeRequest userProfileChangeRequest= new UserProfileChangeRequest.Builder().setDisplayName(userName).build();
                FirebaseUser firebaseUser =FirebaseAuth.getInstance().getCurrentUser();
                firebaseUser.updateProfile(userProfileChangeRequest);
                UserModel userModel=new UserModel(FirebaseAuth.getInstance().getUid(),userName, passwordData, emailId);
                databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(userModel);
                startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
                Toast.makeText(AuthenticationActivity.this,"Registration Successfull",Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(AuthenticationActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        });
    }
}