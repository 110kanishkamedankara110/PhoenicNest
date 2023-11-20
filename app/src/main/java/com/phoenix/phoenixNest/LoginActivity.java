package com.phoenix.phoenixNest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        if(mAuth.getCurrentUser()!=null){
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

                    if(!email.isEmpty() && !password.isEmpty()){
                        firebaseAuth(email, password);
                    }






            }
        });


    }

    private void firebaseAuth(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("u", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
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