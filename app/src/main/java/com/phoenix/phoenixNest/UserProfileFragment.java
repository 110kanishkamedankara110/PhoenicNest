package com.phoenix.phoenixNest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.os.Message;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.phoenix.phoenicnest.R;
import com.phoenix.phoenixNest.dto.User;
import com.phoenix.phoenixNest.util.Error;
import com.phoenix.phoenixNest.util.Success;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class UserProfileFragment extends Fragment {


    FirebaseAuth mAuth;
    FirebaseUser user;

    User userdet;
    FirebaseFirestore db;

    Uri Pimage;
    FirebaseStorage storage;

    FragmentManager fm;
    DocumentSnapshot ds;


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        EditText y;
        EditText m;
        EditText d;

        public DatePickerFragment(EditText year, EditText month, EditText day) {
            y = year;
            d = day;
            m = month;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker.
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog d = new DatePickerDialog(requireContext(), this, year, month, day);
            d.getDatePicker().getTouchables().get(0).performClick();


            return d;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            y.setText(String.valueOf(year));
            d.setText(String.valueOf(day));
            m.setText(String.valueOf(month));
        }

    }

    public UserProfileFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LoadingFragment loader = LoadingFragment.getLoader();
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        fm = getActivity().getSupportFragmentManager();
        storage = FirebaseStorage.getInstance();




        StorageReference storageRef = storage.getReference();


        EditText year = view.findViewById(R.id.year);
        year.setShowSoftInputOnFocus(false);
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText year = view.findViewById(R.id.year);
                EditText month = view.findViewById(R.id.month);
                EditText day = view.findViewById(R.id.day);
                new DatePickerFragment(year, month, day).show(fm, "date picker");
            }
        });

        EditText month = view.findViewById(R.id.month);
        month.setShowSoftInputOnFocus(false);
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText year = view.findViewById(R.id.year);
                EditText month = view.findViewById(R.id.month);
                EditText day = view.findViewById(R.id.day);
                new DatePickerFragment(year, month, day).show(fm, "date picker");
            }
        });
        EditText day = view.findViewById(R.id.day);
        day.setShowSoftInputOnFocus(false);
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText year = view.findViewById(R.id.year);
                EditText month = view.findViewById(R.id.month);
                EditText day = view.findViewById(R.id.day);
                new DatePickerFragment(year, month, day).show(fm, "date picker");
            }
        });

        view.findViewById(R.id.resetpw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ResetPassword().show(fm, "resetpw");
            }
        });
        view.findViewById(R.id.changeEm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EmailChangeFragment().show(fm, "changeEm");
            }
        });

        Uri img = user.getPhotoUrl();
        loader.show(fm, "Loader");
        if (img == null) {


            Picasso.get()
                    .load(R.drawable.person)
                    .resize(200, 200)
                    .centerCrop()
                    .into((ImageView) view.findViewById(R.id.profilepic), new Callback() {
                        @Override
                        public void onSuccess() {

                            loader.dismiss();
                        }

                        @Override
                        public void onError(Exception e) {
                            loader.dismiss();
                            Error.displayErrorMessage(view.findViewById(R.id.Error3),getContext(),"Image Cannot Load");
                        }
                    });

        } else {
            Picasso.get()
                    .load(img)
                    .resize(200, 200)
                    .centerCrop()
                    .into((ImageView) view.findViewById(R.id.profilepic), new Callback() {
                        @Override
                        public void onSuccess() {
                            loader.dismiss();
                        }

                        @Override
                        public void onError(Exception e) {
                            loader.dismiss();
                            Error.displayErrorMessage(view.findViewById(R.id.Error3),getContext(),"Image Cannot Load");
                        }
                    });

        }


        ((ImageView) view.findViewById(R.id.profilepic)).setClipToOutline(true);

        LoadingFragment loader2 = LoadingFragment.getLoader();
        loader2.show(fm, "Loader");
        db.collection("user").document(user.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if(!loader2.isLoading){
                    loader2.show(fm, "Loader");
                }
                ds = snapshot;
                if (e != null) {
                    if (loader2.isLoading) {
                        loader2.dismiss();
                    }
                }

                if(snapshot != null && snapshot.exists()) {

                    userdet = snapshot.toObject(User.class);
                    String[] dob = userdet.getDob().split("/");


                    EditText name = view.findViewById(R.id.name);
                    EditText year = view.findViewById(R.id.year);
                    EditText month = view.findViewById(R.id.day);
                    EditText day = view.findViewById(R.id.month);

                    name.setText(userdet.getName());
                    year.setText(dob[0]);
                    month.setText(dob[1]);
                    day.setText(dob[2]);
                    if (loader2.isLoading) {
                        loader2.dismiss();

                    }
                } else {
                    if (loader2.isLoading) {
                        loader2.dismiss();

                    }
                }
            }
        });

        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);

                        Pimage = uri;
                        if(!loader.isLoading){
                            loader.show(fm,"Loader");
                        }
                        Picasso.get()
                                .load(uri)
                                .resize(200, 200)
                                .centerCrop()
                                .into((ImageView) view.findViewById(R.id.profilepic), new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        if(loader.isLoading){
                                            loader.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        if(loader.isLoading){
                                            loader.dismiss();
                                        }
                                        Error.displayErrorMessage(view.findViewById(R.id.Error3),getContext(),"Image Cannot Load");
                                    }
                                });

                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });
        view.findViewById(R.id.chooseImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });
        view.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText name = view.findViewById(R.id.name);
                EditText year = view.findViewById(R.id.year);
                EditText month = view.findViewById(R.id.day);
                EditText day = view.findViewById(R.id.month);

                String userName = name.getText().toString();
                String birthYear = year.getText().toString();
                String birthmonth = month.getText().toString();
                String birthday = day.getText().toString();


                if (!ds.exists()) {
                    if (userName.isEmpty()) {
                        Error.setErrorFiled(name,getContext(),view.findViewById(R.id.Error3),"Please Enter User Name");
                        Error.removeErrorFiled(year,getContext());
                        Error.removeErrorFiled(month,getContext());
                        Error.removeErrorFiled(day,getContext());

                    } else if (birthYear.isEmpty()) {
                        Error.setErrorFiled(year,getContext(),view.findViewById(R.id.Error3),"Please Select Date Of Birth");
                        Error.removeErrorFiled(name,getContext());
                        Error.setErrorFiled(month,getContext());
                        Error.setErrorFiled(day,getContext());
                    } else {
                        Error.removeErrorFiled(name,getContext());
                        Error.setErrorFiled(month,getContext());
                        Error.setErrorFiled(day,getContext());
                        Error.setErrorFiled(year,getContext());
                        Error.removeErrorText(view.findViewById(R.id.Error3),getContext());

                        Map<String, String> data = new HashMap();
                        String dob = birthYear + "/" + birthmonth + "/" + birthday;
                        data.put("name", userName);
                        data.put("dob", dob);
                        db.collection("user").document(user.getUid()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Success.displaySuccessMessage(view.findViewById(R.id.Error3),getContext(),"Data Successfully Added");
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Success.removeSuccessText(view.findViewById(R.id.Error3),getContext());
                                            }
                                        },10000);


                                        fm.beginTransaction()
                                                .replace(R.id.fragmentContainer, UserProfileFragment.class, null)
                                                .commit();

                                        fm.beginTransaction()
                                                .replace(R.id.fragmentDrawer,DrawerFragmernt.class,null)
                                                .commit();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {


                                    }
                                });
                    }

                } else {
                    DocumentReference dor = db.collection("user").document(user.getUid());
                    Map<String, Object> data = new HashMap();
                    if (!userName.isEmpty()) {
                        if (!userName.equals(userdet.getName())) {
                            data.put("name", userName);
                        }
                    }

                    if (!birthYear.isEmpty()) {
                        String dob = birthYear + "/" + birthmonth + "/" + birthday;
                        if (!dob.equals(userdet.getDob())) {
                            data.put("dob", dob);
                        }
                    }

                    if (data.size() > 0) {
                        if(!loader.isLoading){
                            loader.show(fm,"Loader");
                        }
                        dor.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Success.displaySuccessMessage(view.findViewById(R.id.Error3),getContext(),"Details Updated");
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Success.removeSuccessText(view.findViewById(R.id.Error3),getContext());
                                            }
                                        },10000);
                                        if(loader.isLoading){
                                            loader.dismiss();
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        if(loader.isLoading){
                                            loader.dismiss();
                                        }
                                    }
                                });
                    }
                }


                if (Pimage != null) {
                    if(!loader2.isLoading){
                        loader2.show(fm,"Loader");
                    }

                    String imagename = Pimage.getLastPathSegment();


                    StorageReference imageref = storageRef.child("images/" + System.currentTimeMillis() + imagename);
                    UploadTask uploadTask = imageref.putFile(Pimage);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            if(loader2.isLoading){
                                loader2.dismiss();
                            }
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setPhotoUri(uri)
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Success.displaySuccessMessage(view.findViewById(R.id.Error3),getContext(),"Image Change Success");
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Success.removeSuccessText(view.findViewById(R.id.Error3),getContext());
                                                            }
                                                        },10000);
                                                        user.reload();
                                                        user = mAuth.getCurrentUser();
                                                        if(loader2.isLoading){
                                                            loader2.dismiss();
                                                        }
                                                    }
                                                }
                                            });
                                }
                            });


                        }
                    });


                }


            }
        });
    }
}