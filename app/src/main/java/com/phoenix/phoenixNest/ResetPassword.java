package com.phoenix.phoenixNest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.phoenix.phoenicnest.R;


public class ResetPassword extends DialogFragment {

    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_reset_password, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user!=null) {
            view.findViewById(R.id.resetEmail).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.reload();
                    user = mAuth.getCurrentUser();
                    if (user.isEmailVerified()) {
                        EditText op = view.findViewById(R.id.emailresemail);
                        EditText np = view.findViewById(R.id.emailrespassword);

                        String oldPassword = op.getText().toString();
                        String newPassword = np.getText().toString();
                        if (oldPassword.isEmpty()) {
                            builder.setMessage("Enter Old Password").setTitle("Empty Old Password").show();
                        }
                        else if (newPassword.isEmpty()) {
                            builder.setMessage("Enter New Password").setTitle("Empty New Password").show();
                        }else {
                            LoadingFragment loader=LoadingFragment.getLoader();
                            loader.show(getActivity().getSupportFragmentManager(),"Loader");


                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(user.getEmail(), oldPassword);


                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                user = mAuth.getCurrentUser();
                                                user.updatePassword(newPassword)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                loader.dismiss();
                                                                if (task.isSuccessful()) {
                                                                    mAuth.signOut();
                                                                    startActivity(new Intent(getContext(),LoginActivity.class));
                                                                }


                                                            }
                                                        });
                                            } else {
                                                loader.dismiss();
                                                builder.setMessage("Invalid Password").setTitle("Password is invalid").show();

                                            }
                                        }
                                    });

                        }
                    } else {
                        builder.setMessage("Verify Your Email").setTitle("Email Not Verified").show();
                    }

                }
            });
        }
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


}