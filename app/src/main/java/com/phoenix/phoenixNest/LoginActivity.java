package com.phoenix.phoenixNest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.phoenix.phoenixNest.util.Error;
import com.phoenix.phoenixNest.util.StatusBar;
import com.phoenix.phoenicnest.R;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);

        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
        }
        setTheme(com.phoenix.phoenicnest.R.style.Base_Theme_PhoenicNest);


        setContentView(R.layout.activity_login);
        StatusBar.hideStatusBar(this);

        Button b = findViewById(R.id.signin_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SigninActivity.class);
                startActivity(i);
            }
        });
        Button b2 = findViewById(R.id.signIn);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();

                String email = ((EditText) findViewById(R.id.email)).getText().toString();
                String password = ((EditText) findViewById(R.id.password)).getText().toString();

                if (!email.isEmpty() && !password.isEmpty()) {
                    firebaseAuth(email.trim(), password);
                }


            }
        });
        findViewById(R.id.fogetpw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText d = findViewById(R.id.email);
                String email = d.getText().toString();
                if (email.isEmpty()) {

                    Error.setErrorFiled(d, LoginActivity.this,findViewById(R.id.errorText3), "Enter Your Email");
                } else {
                    Error.removeErrorFiled(d,LoginActivity.this);
                    Error.removeErrorText(findViewById(R.id.errorText3),LoginActivity.this);
                    LoadingFragment loader = LoadingFragment.getLoader();
                    loader.show(getSupportFragmentManager(), "Loader");
                    mAuth.sendPasswordResetEmail(email.trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        loader.dismiss();
                                        Toast.makeText(getApplicationContext(), "Password Reset Email Sent", Toast.LENGTH_SHORT).show();
                                    } else {
                                        loader.dismiss();
                                        Toast.makeText(getApplicationContext(), "Email not found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });


    }

    private void firebaseAuth(String email, String password) {
        LoadingFragment loader = LoadingFragment.getLoader();
        loader.show(getSupportFragmentManager(), "Loader");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            loader.dismiss();
                            updateUI(user);

                        } else {

                            Log.w("u", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_LONG).show();
                            loader.dismiss();
                            updateUI(null);

                        }
                    }


                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
        }
    }

}