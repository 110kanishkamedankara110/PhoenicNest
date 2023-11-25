package com.phoenix.phoenixNest;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.phoenix.phoenicnest.R;
import com.phoenix.phoenixNest.dto.AppMain;
import com.phoenix.phoenixNest.dto.Message;
import com.phoenix.phoenixNest.util.AddAppService;
import com.phoenix.phoenixNest.util.Env;
import com.phoenix.phoenixNest.util.Part;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AddAnAppFragment extends Fragment {

    private FragmentManager fm;
    private Uri appIcon;
    private Uri appBanner;
    LoadingFragment loader = LoadingFragment.getLoader();
    public AddAnAppFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_an_app, container, false);
    }

    private void clearIcons() {
        appIcon = null;
        appBanner = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ImageView) view.findViewById(R.id.appicon)).setClipToOutline(true);
        ((ImageView) view.findViewById(R.id.appbanner)).setClipToOutline(true);

        fm = getActivity().getSupportFragmentManager();
        view.findViewById(R.id.cancelAppAdd).setOnClickListener(v -> fm.beginTransaction()
                .replace(R.id.fragmentContainer, MyApps.class, null)
                .commit());

        view.findViewById(R.id.nextappAdd).setOnClickListener(v -> {

            EditText appTitle = view.findViewById(R.id.appTitle);
            EditText pakageStructure = view.findViewById(R.id.pkgStr);
            EditText mainActivity = view.findViewById(R.id.mainAct);
            Uri appIcon = AddAnAppFragment.this.appIcon;
            Uri appBanner = AddAnAppFragment.this.appBanner;

            if (appTitle.getText().toString().isEmpty()) {
                new AlertDialog.Builder(getContext()).setTitle("Empty AppTitle").setMessage("Please Enter App Title").show();
            } else if (pakageStructure.getText().toString().isEmpty()) {

                new AlertDialog.Builder(getContext()).setTitle("Empty Package Name").setMessage("Please Enter Package name").show();

            } else if (pakageStructure.getText().toString().trim().contains(" ")) {

                new AlertDialog.Builder(getContext()).setTitle("Invalid Package Name").setMessage("Package Name Cannot Contain Any Spaces").show();

            } else if (mainActivity.getText().toString().isEmpty()) {
                new AlertDialog.Builder(getContext()).setTitle("Empty Main Activity Name").setMessage("Please Enter Main Activity Name").show();

            } else if (appIcon == null) {
                new AlertDialog.Builder(getContext()).setTitle("App Icon Not Selected").setMessage("Please Select App Icon").show();

            } else if (appBanner == null) {
                new AlertDialog.Builder(getContext()).setTitle("App Banner Not Selected").setMessage("Please Select App Banner").show();

            } else {
                AppMain appMain = new AppMain();
                appMain.setAppName(appTitle.getText().toString());
                appMain.setPakgeName(pakageStructure.getText().toString());
                appMain.setMainActivity(mainActivity.getText().toString());


                try {

                    MultipartBody.Part appIconPart = new Part().getPart(view.getContext(), appIcon, "appIcon");
                    MultipartBody.Part appBannerPart = new Part().getPart(view.getContext(), appBanner, "appBanner");

                    if (!loader.isLoading) {
                        loader.show(fm, "Loader");
                    }
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Env.get(getContext(), "app.url"))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    AddAppService service = retrofit.create(AddAppService.class);
                    Call<Message> appMainCall = service.addApp(appIconPart, appBannerPart, appMain);

                    appMainCall.enqueue(new retrofit2.Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {

                            if (response.isSuccessful()) {
                                Message message = response.body();
                                if (message.getMessage().equals("Sucess")) {
                                    appTitle.setText("");
                                    appTitle.setText("");
                                    pakageStructure.setText("");
                                    mainActivity.setText("");

                                    clearIcons();
                                    ImageButton appI = view.findViewById(R.id.appicon);
                                    ImageButton appB = view.findViewById(R.id.appbanner);

                                    appI.setImageBitmap(null);
                                    appB.setImageBitmap(null);

                                } else {

                                    new AlertDialog.Builder(getContext()).setTitle("Message").setMessage(message.getMessage()).show();
                                }


                            }
                            if (loader.isLoading) {
                                loader.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            t.printStackTrace();
                            if (loader.isLoading) {
                                loader.dismiss();
                            }
                        }
                    });

                } catch (Exception e) {
                    if (loader.isLoading) {
                        loader.dismiss();
                    }
                    e.printStackTrace();
                }


//


            }
        });

        ActivityResultLauncher<PickVisualMediaRequest> picAppImage = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);

                loader.show(fm, "Loader");
                AddAnAppFragment.this.appIcon = uri;
                if (!loader.isLoading) {
                    loader.show(fm, "Loader");
                }
                ImageButton img = view.findViewById(R.id.appicon);
                Picasso.get()
                        .load(uri)
                        .into(img, new Callback() {
                            @Override
                            public void onSuccess() {
                                if (loader.isLoading) {
                                    loader.dismiss();
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                if (loader.isLoading) {
                                    loader.dismiss();
                                }
                                Toast.makeText(view.getContext(), "Image Cannot be load", Toast.LENGTH_LONG).show();
                            }
                        });

            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });

        ActivityResultLauncher<PickVisualMediaRequest> picAppBanner = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                LoadingFragment loader = LoadingFragment.getLoader();
                loader.show(fm, "Loader");
                AddAnAppFragment.this.appBanner = uri;
                if (!loader.isLoading) {
                    loader.show(fm, "Loader");
                }
                ImageButton img = view.findViewById(R.id.appbanner);
                Picasso.get()
                        .load(uri)
                        .into(img, new Callback() {
                            @Override
                            public void onSuccess() {
                                if (loader.isLoading) {
                                    loader.dismiss();
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                if (loader.isLoading) {
                                    loader.dismiss();
                                }
                                Toast.makeText(view.getContext(), "Image Cannot be load", Toast.LENGTH_LONG).show();
                            }
                        });

            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });
        view.findViewById(R.id.appicon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                picAppImage.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());

            }
        });

        view.findViewById(R.id.appbanner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                picAppBanner.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());

            }
        });


    }


}