package com.phoenix.phoenixNest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.phoenix.phoenixNest.util.Error;
import com.phoenix.phoenixNest.util.StatusBar;
import com.phoenix.phoenicnest.R;

import java.util.regex.Pattern;

public class SigninActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(com.phoenix.phoenicnest.R.layout.activity_signin);

        StatusBar.hideStatusBar(this);

        Button b = findViewById(R.id.login_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SigninActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });


        findViewById(R.id.signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailText = findViewById(R.id.email);
                EditText password1Text = findViewById(R.id.password);
                EditText password2Text = findViewById(R.id.password2);
                TextView errorText = findViewById(R.id.errorText);


                String email = String.valueOf(emailText.getText());
                String password1 = String.valueOf(password1Text.getText());
                String password2 = String.valueOf(password2Text.getText());
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


                if (email.isEmpty()) {
                    Error.setErrorFiled(emailText,SigninActivity.this,errorText,"Enter Your Email");
                    Error.removeErrorFiled(password1Text,SigninActivity.this);
                    Error.removeErrorFiled(password2Text,SigninActivity.this);

                } else if (!email.matches(emailPattern)) {
                    Error.setErrorFiled(emailText,SigninActivity.this,errorText,"Email Is Not Valid");
                    Error.removeErrorFiled(password1Text,SigninActivity.this);
                    Error.removeErrorFiled(password2Text,SigninActivity.this);
                } else if (password1.isEmpty()) {
                    Error.setErrorFiled(password1Text,SigninActivity.this,errorText,"Enter Password");
                    Error.removeErrorFiled(emailText,SigninActivity.this);
                    Error.removeErrorFiled(password2Text,SigninActivity.this);
                } else if (password2.isEmpty()) {
                    Error.setErrorFiled(password2Text,SigninActivity.this,errorText,"Confirm Password");
                    Error.removeErrorFiled(emailText,SigninActivity.this);
                    Error.removeErrorFiled(password1Text,SigninActivity.this);
                } else if (!password1.equals(password2)) {
                    Error.setErrorFiled(password2Text,SigninActivity.this,errorText,"Password Doesn't Match");
                    Error.removeErrorFiled(emailText,SigninActivity.this);
                    Error.setErrorFiled(password1Text,SigninActivity.this);
                } else {
                    Error.removeErrorFiled(password2Text,SigninActivity.this);
                    Error.removeErrorFiled(emailText,SigninActivity.this);
                    Error.removeErrorFiled(password1Text,SigninActivity.this);
                    Error.removeErrorText(errorText,SigninActivity.this);
                    firebaseAuth(email.trim(), password1);
                }

            }


        });

    }

    private void firebaseAuth(String email, String password) {
        LoadingFragment loader=LoadingFragment.getLoader();
        loader.show(getSupportFragmentManager(),"Loader");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            loader.dismiss();
                            updateUI(user);
                        } else {
                            loader.dismiss();
                            Error.displayErrorMessage(findViewById(R.id.errorText),SigninActivity.this,"Authentication failed");

                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(SigninActivity.this, HomeActivity.class));
                }
            }
        });
    }
}