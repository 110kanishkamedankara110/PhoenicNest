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


        Uri img = user.getPhotoUrl();
        if (img == null) {
            Picasso.get()
                    .load(R.drawable.person)
                    .resize(200, 200)
                    .centerCrop()
                    .into((ImageView) view.findViewById(R.id.profilepic));
        } else {
            Picasso.get()
                    .load(img)
                    .resize(200, 200)
                    .centerCrop()
                    .into((ImageView) view.findViewById(R.id.profilepic));
        }


        ((ImageView) view.findViewById(R.id.profilepic)).setClipToOutline(true);

        db.collection("user").document(user.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                ds = snapshot;
                if (e != null) {


                }

                if (snapshot != null && snapshot.exists()) {

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
                } else {

                }
            }
        });
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                    }
//                });
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);

                        Pimage = uri;
                        Picasso.get()
                                .load(uri)
                                .resize(200, 200)
                                .centerCrop()
                                .into((ImageView) view.findViewById(R.id.profilepic));

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
                ImageView image = view.findViewById(R.id.profilepic);

                String userName = name.getText().toString();
                String birthYear = year.getText().toString();
                String birthmonth = month.getText().toString();
                String birthday = day.getText().toString();


                if (!ds.exists()) {
                    if (userName.isEmpty()) {
                        Toast.makeText(view.getContext(), "Please Enter User Name", Toast.LENGTH_LONG).show();
                    } else if (birthYear.isEmpty()) {
                        Toast.makeText(view.getContext(), "Please Enter Birth Day", Toast.LENGTH_LONG).show();
                    }else {
                        Map<String, String> data = new HashMap();
                        String dob = birthYear + "/" + birthmonth + "/" + birthday;
                        data.put("name", userName);
                        data.put("dob", dob);
                        db.collection("user").document(user.getUid()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(view.getContext(), "Data Successfully Added", Toast.LENGTH_LONG).show();
                                        fm.beginTransaction()
                                                .replace(R.id.fragmentContainer, UserProfileFragment.class, null)
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
                        dor.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(view.getContext(), "Details Updated", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }
                }


                if (Pimage != null) {


                    String imagename = Pimage.getLastPathSegment();


                    StorageReference imageref = storageRef.child("images/" + System.currentTimeMillis() + imagename);
                    UploadTask uploadTask = imageref.putFile(Pimage);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
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
                                                        Toast.makeText(view.getContext(), "Image Change Success", Toast.LENGTH_LONG).show();
                                                        user.reload();
                                                        user = mAuth.getCurrentUser();
                                                        fm.beginTransaction()
                                                                .replace(R.id.fragmentContainer, UserProfileFragment.class, null)
                                                                .commit();

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