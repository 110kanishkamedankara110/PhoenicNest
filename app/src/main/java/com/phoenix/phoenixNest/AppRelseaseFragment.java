package com.phoenix.phoenixNest;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.phoenix.phoenicnest.R;
import com.phoenix.phoenixNest.util.AppDetails;
import com.phoenix.phoenixNest.util.Error;
import com.phoenix.phoenixNest.util.ListListener;
import com.phoenix.phoenixNest.util.ListWithListener;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;


public class AppRelseaseFragment extends Fragment {

    FragmentManager fm;
    Bundle extra;
    List<Uri> screenshots;

    int maxItems=4;
    Uri apk;

    public AppRelseaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_app_relsease, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout ss=view.findViewById(R.id.SS);
        screenshots=new ListWithListener<Uri>(new ListListener() {
            @Override
            public void onAdd(Object item) {
                View cat = LayoutInflater.from(getContext()).inflate(R.layout.screen_shots_layout, ss, false);



                ImageButton ib = cat.findViewById(R.id.delBut);
                ib.setImageResource(R.drawable.baseline_close_24);

                Picasso.get()
                        .load((Uri) item)
                        .resize(100, 200)
                        .centerCrop()
                        .into((ImageView)cat.findViewById(R.id.ssImage));

                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ss.removeView(cat);

                        screenshots.remove(item);

                    }
                });
                ss.addView(cat);
            }

            @Override
            public void onRemove() {

            }

            @Override
            public void onRemoveAll() {

            }
        });

        ImageButton ib=view.findViewById(R.id.AddSs);
        ib.setImageResource(R.drawable.baseline_add_24);
        fm = getActivity().getSupportFragmentManager();
        extra = getArguments();
        ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
                registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(maxItems), uris -> {
                    // Callback is invoked after the user selects media items or closes the
                    // photo picker.
                    if (!uris.isEmpty()) {
                        for(int i=0;i<(uris.size()<maxItems?uris.size():maxItems);i++){
                            screenshots.add(uris.get(i));
                        }

                        screenshots.forEach(c->{
                            System.out.println(c);
                        });
                    } else {

                    }
                });
        ActivityResultLauncher<Intent> pickApp = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        Intent data = result.getData();
                        if (data != null) {
                            Uri uri = data.getData();
                            apk = uri;

                            PackageInfo atb = AppDetails.getPackageInfo(uri, getContext());

                            if(atb.packageName.equals(extra.getString("PackageName"))){
                                TextView fileText=view.findViewById(R.id.fileText);
                                EditText version=view.findViewById(R.id.versionText);
                                EditText versioNCode=view.findViewById(R.id.vetsionCodeText);

                                fileText.setText(atb.packageName+" "+atb.versionName+" ("+atb.getLongVersionCode()+")");
                                version.setText(atb.versionName);
                                versioNCode.setText(String.valueOf(atb.getLongVersionCode()));
                            }else{
                                Error.displayErrorMessage(view.findViewById(R.id.ErrorMessageAppRelease),getContext(),"Invalid Package Name In Apk");
                            }





                        }
                    }

                });

        // Start the file picker intent

        view.findViewById(R.id.testB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickApp.launch(new Intent(Intent.ACTION_OPEN_DOCUMENT)
                        .setType("application/vnd.android.package-archive"));
            }
        });
        view.findViewById(R.id.AddSs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maxItems=4-screenshots.size();
                if(screenshots.size()!=4){
                    pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build());
                }

            }
        });


    }


}