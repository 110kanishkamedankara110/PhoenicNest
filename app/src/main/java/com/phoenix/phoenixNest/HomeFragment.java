package com.phoenix.phoenixNest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.phoenix.phoenicnest.R;
import com.phoenix.phoenixNest.dto.AppDto;
import com.phoenix.phoenixNest.dto.CategoryDto;
import com.phoenix.phoenixNest.util.CategoryService;
import com.phoenix.phoenixNest.util.Env;
import com.phoenix.phoenixNest.util.GetAppService;
import com.phoenix.phoenixNest.util.Notifications;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {
    List<AppDto> apps;
    ViewGroup vg;

    FragmentManager fm;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            user.reload();
            user = auth.getCurrentUser();
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vg = container;
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        Notifications.registerNotificationChannel(getActivity());
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // create a dialog to ask yes no questions whether or not the user wants to exit
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        fm = getActivity().getSupportFragmentManager();
        setCategory(view);
        setPopular(view);
        loadMyApps(view);



    }

    private void setCategory(View container) {

        LinearLayout l = container.findViewById(R.id.cardTest);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Env.get(getContext(), "app.url"))
                .addConverterFactory(GsonConverterFactory.create())

                .build();
        CategoryService service = retrofit.create(CategoryService.class);
        Call<List<CategoryDto>> category = service.getCategory();

        category.enqueue(new Callback<List<CategoryDto>>() {
            @Override
            public void onResponse(Call<List<CategoryDto>> call, Response<List<CategoryDto>> response) {
                List<CategoryDto> categoryDtos = response.body();
                LinearLayout l = container.findViewById(R.id.categorytext);
                categoryDtos.forEach(c -> {

                    LayoutInflater inf = LayoutInflater.from(container.getContext());

                    View v = inf.inflate(R.layout.category_card, l, false);
                    TextView tw = v.findViewById(R.id.cardTest);
                    ImageView iw = v.findViewById(R.id.categoryImage);
                    tw.setText(c.getCategory());
                    TextView co = v.findViewById(R.id.count);
                    Picasso.get()
                            .load(Uri.parse(Env.get(getContext(), "app.url") + "image/category/" + c.getImages().get(0)))
                            .into(iw, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    e.printStackTrace();
                                }
                            });


                    l.addView(v);
                });

            }

            @Override
            public void onFailure(Call<List<CategoryDto>> call, Throwable t) {
                t.printStackTrace();
            }
        });


    }

    private void setPopular(View container) {
        LinearLayout l = container.findViewById(R.id.popular);
        for (int i = 0; i < 10; i++) {
            LayoutInflater inf = LayoutInflater.from(container.getContext());
            inf.inflate(R.layout.popular_card, l, true);
        }
    }

    private void loadMyApps(View container) {
        LoadingFragment loader=LoadingFragment.getLoader();
        if (!loader.isLoading) {
            loader.show(fm, "Loader");
        }
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Env.get(getContext(), "app.url")).build();
        GetAppService getAppService = retrofit.create(GetAppService.class);
        Call<List<AppDto>> call = getAppService.getAllApps();

        call.enqueue(new Callback<List<AppDto>>() {
            @Override
            public void onResponse(Call<List<AppDto>> call, Response<List<AppDto>> response) {
                if (response.isSuccessful()) {
                    System.out.println("sucess................");
                    apps = response.body();
                    RecyclerView rec =container.findViewById(R.id.new_items);
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                    rec.setLayoutManager(staggeredGridLayoutManager);
                    rec.setAdapter(new HomeFragment.Adapter());

                }
                if (loader.isLoading) {
                    System.out.println("Failed...............");
                    loader.dismiss();
                }


            }

            @Override
            public void onFailure(Call<List<AppDto>> call, Throwable t) {
                if (loader.isLoading) {
                    loader.dismiss();
                }
                t.printStackTrace();
            }
        });
    }

    class Vh extends RecyclerView.ViewHolder {

        TextView tw;
        View v;
        ImageView iv;
        ImageView appicon;

        public Vh(@NonNull View itemView) {
            super(itemView);
            tw = itemView.findViewById(R.id.textView18);
            v = itemView;
            iv = itemView.findViewById(R.id.bg);
            appicon = itemView.findViewById(R.id.appIcon);
        }
    }

    class Adapter extends RecyclerView.Adapter<HomeFragment.Vh> {

        @NonNull
        @Override
        public HomeFragment.Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inf = LayoutInflater.from(parent.getContext());
            View v = inf.inflate(R.layout.layout_item, parent, false);
            return new HomeFragment.Vh(v);
        }

        @Override
        public void onBindViewHolder(@NonNull HomeFragment.Vh holder, int position) {

            int pos = position;
            holder.tw.post(new Runnable() {
                @Override
                public void run() {
                    ImageView iv = holder.iv;
                    ImageView appi = holder.appicon;


                    int width = apps.get(pos).getWidth();
                    int height = apps.get(pos).getHeight();

                    int containerWidth = holder.tw.getWidth();

                    int containerheight = (containerWidth / width) * height;


                    holder.tw.setHeight(containerheight);
                    iv.setClipToOutline(true);
                    appi.setClipToOutline(true);
                    appi.setElevation(100);
                    Picasso.get()
                            .load(Env.get(getContext(), "app.url") + "image/appBanner/" + apps.get(pos).getPackageName() + "/" + apps.get(pos).getAppBanner())
                            .resize(containerWidth, containerheight)
                            .centerCrop()
                            .into(iv);
                    Picasso.get()
                            .load(Env.get(getContext(), "app.url") + "image/appIcon/" + apps.get(pos).getPackageName() + "/" + apps.get(pos).getAppIcon())
                            .resize(appi.getWidth(), appi.getHeight())
                            .centerCrop()
                            .into(appi);








                }

            });
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppDto app=apps.get(pos);

                    Bundle b = new Bundle();
                    b.putString("packageName",app.getPackageName());
                    b.putStringArrayList("categoryies",(ArrayList<String>) app.getCategoryies());
                    b.putStringArrayList("screenshots",(ArrayList<String>)app.getScreenShots());
                    b.putString("appBanner",app.getAppBanner());
                    b.putString("apk",app.getApk());
                    b.putString("appIcon",app.getAppIcon());
                    b.putString("appTitle",app.getAppTitle());
                    b.putString("appDescription",app.getDescription());
                    b.putString("version",app.getVersion());
                    b.putString("versionCode",app.getVersionCode());
                    b.putInt("width",app.getWidth());
                    b.putInt("height",app.getHeight());
                    b.putString("mainActivity",app.getMainActivity());


                    fm.beginTransaction()
                            .setReorderingAllowed(true).addToBackStack("SingleAppView")
                            .replace(R.id.fragmentContainer, SingleViewFragment.class,b)
                            .commit();


                }
            });

        }

        @Override
        public int getItemCount() {
            if (apps != null) {
                return apps.size();

            } else {
                return 0;

            }
        }


    }
}