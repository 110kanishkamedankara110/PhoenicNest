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
import com.phoenix.phoenixNest.util.Error;
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

                Error.setErrorFiled(appTitle,getContext(),view.findViewById(R.id.errorText2),"Please Enter App Title");
                Error.removeErrorFiled(pakageStructure,getContext());
                Error.removeErrorFiled(mainActivity,getContext());

            } else if (pakageStructure.getText().toString().isEmpty()) {

                Error.setErrorFiled(pakageStructure,getContext(),view.findViewById(R.id.errorText2),"Please Enter Package name");
                Error.removeErrorFiled(appTitle,getContext());
                Error.removeErrorFiled(mainActivity,getContext());

            } else if (pakageStructure.getText().toString().trim().contains(" ")) {

                Error.setErrorFiled(pakageStructure,getContext(),view.findViewById(R.id.errorText2),"Package Name Cannot Contain Any Spaces");
                Error.removeErrorFiled(appTitle,getContext());
                Error.removeErrorFiled(mainActivity,getContext());

            } else if (mainActivity.getText().toString().isEmpty()) {

                Error.setErrorFiled(mainActivity,getContext(),view.findViewById(R.id.errorText2),"Please Enter Main Activity Name");
                Error.removeErrorFiled(pakageStructure,getContext());
                Error.removeErrorFiled(appTitle,getContext());

            } else if (appIcon == null) {

                Error.displayErrorMessage(view.findViewById(R.id.errorText2),getContext(),"Please Select App Icon");
                Error.removeErrorFiled(appTitle,getContext());
                Error.removeErrorFiled(pakageStructure,getContext());
                Error.removeErrorFiled(mainActivity,getContext());
            } else if (appBanner == null) {

                Error.displayErrorMessage(view.findViewById(R.id.errorText2),getContext(),"Please Select App Banner");
                Error.removeErrorFiled(appTitle,getContext());
                Error.removeErrorFiled(pakageStructure,getContext());
                Error.removeErrorFiled(mainActivity,getContext());

            } else {
                Error.removeErrorFiled(appTitle,getContext());
                Error.removeErrorFiled(pakageStructure,getContext());
                Error.removeErrorFiled(mainActivity,getContext());

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
                                    Error.removeErrorText(view.findViewById(R.id.errorText2),getContext());

                                    ImageButton appI = view.findViewById(R.id.appicon);
                                    ImageButton appB = view.findViewById(R.id.appbanner);
                                    Bundle b = new Bundle();
                                    b.putString("PackageName",pakageStructure.getText().toString());

                                    appTitle.setText("");
                                    appTitle.setText("");
                                    pakageStructure.setText("");
                                    mainActivity.setText("");

                                    clearIcons();

                                    appI.setImageBitmap(null);
                                    appB.setImageBitmap(null);
                                    fm.beginTransaction()
                                            .setReorderingAllowed(true)
                                            .replace(R.id.fragmentContainer, AppDetailsFragment.class, b)
                                            .commit();

                                } else {
                                    Error.displayErrorMessage(view.findViewById(R.id.errorText2),getContext(),message.getMessage());
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
                                Error.displayErrorMessage(view.findViewById(R.id.errorText2),getContext(),"Request TimeOut");
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

                                Error.displayErrorMessage(view.findViewById(R.id.errorText2),getContext(),"Image Cannot be load");

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
                                Error.displayErrorMessage(view.findViewById(R.id.errorText2),getContext(),"Image Cannot be load");

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