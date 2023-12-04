package com.phoenix.phoenixNest;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.phoenix.phoenicnest.R;
import com.phoenix.phoenixNest.dto.AppDto;
import com.phoenix.phoenixNest.util.Env;
import com.phoenix.phoenixNest.util.GetAppService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyApps extends Fragment {

    Bundle extra;
    List<AppDto> apps;
    float loc_X = 0;
    float loc_y = 0;
    View v;
    FragmentManager fm;
    LoadingFragment loader = LoadingFragment.getLoader();

    Map<FrameLayout, View> hm = new HashMap();

    View view;
    View v1;
    View mid;
    View vi1;

    float v1_locX;
    float v1_locY;

    MotionEvent e;

    public MyApps() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(com.phoenix.phoenicnest.R.layout.fragment_my_apps, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        v1 = inflater.inflate(R.layout.layout_pop, view.findViewById(R.id.fcccc), false);
        mid = v1.findViewById(R.id.mid);
        vi1 = v1.findViewById(R.id.v1);
        view.findViewById(R.id.categoryApps).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FrameLayout fl = view.findViewById(R.id.fcccc);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (hm.size() != 0) {
                            hm.keySet().forEach(fll -> {
                                v1.setY(0);
                                v1.setX(0);
                                fl.removeView(hm.get(fll));
                                View mid = hm.get(fll).findViewById(R.id.mid);
                                View vi1 = hm.get(fll).findViewById(R.id.v1);
                                SpringAnimation s = new SpringAnimation(vi1, DynamicAnimation.Y, 0);
                                SpringAnimation s1 = new SpringAnimation(vi1, DynamicAnimation.X, 0);
                                s.start();
                                s1.start();
                            });


                        }


                }


                return false;
            }
        });
        v = view;
        fm = getActivity().getSupportFragmentManager();


        loadMyApps();

        extra = getArguments();
        ((TextView) view.findViewById(R.id.categoryTitle)).setText("My Apps");


        view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction()
                        .setReorderingAllowed(true).addToBackStack("AddApps")
                        .replace(R.id.fragmentContainer, AddAnAppFragment.class, null)
                        .commit();
            }
        });


    }

    private void loadMyApps() {
        if (!loader.isLoading) {
            loader.show(fm, "Loader");
        }
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Env.get(getContext(), "app.url")).build();
        GetAppService getAppService = retrofit.create(GetAppService.class);
        Call<List<AppDto>> call = getAppService.getApps(FirebaseAuth.getInstance().getCurrentUser().getUid());
        System.out.println(Env.get(getContext(), "app.url") + "app/getApps/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

        call.enqueue(new Callback<List<AppDto>>() {
            @Override
            public void onResponse(Call<List<AppDto>> call, Response<List<AppDto>> response) {
                if (response.isSuccessful()) {
                    apps = response.body();
                    RecyclerView rec = v.findViewById(R.id.categoryApps);
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                    rec.setLayoutManager(staggeredGridLayoutManager);
                    rec.setAdapter(new Adapter());

                }
                if (loader.isLoading) {
                    loader.dismiss();
                }


            }

            @Override
            public void onFailure(Call<List<AppDto>> call, Throwable t) {
                if (loader.isLoading) {
                    loader.dismiss();
                }
            }
        });
    }


    class Vh extends RecyclerView.ViewHolder {

        TextView tw;
        View v;
        ImageView iv;
        ImageView appicon;

        FragmentContainerView fv;

        public Vh(@NonNull View itemView) {
            super(itemView);
            tw = itemView.findViewById(R.id.textView18);
            v = itemView;
            iv = itemView.findViewById(R.id.bg);

            appicon = itemView.findViewById(R.id.appIcon);
        }
    }

    class Adapter extends RecyclerView.Adapter<MyApps.Vh> {

        @NonNull
        @Override
        public MyApps.Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inf = LayoutInflater.from(parent.getContext());
            View v = inf.inflate(R.layout.layout_item, parent, false);
            return new Vh(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyApps.Vh holder, int position) {

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
//                    holder.v.setOnLongClickListener(new View.OnLongClickListener() {
//                        @Override
//                        public boolean onLongClick(View v) {
//
//                            if (hm.size() != 0) {
//                                hm.keySet().forEach(fl -> {
//
//                                    fl.removeView(hm.get(fl));
//                                });
//                            }
//
//
//                            FrameLayout fl = view.findViewById(R.id.fcccc);
//
//                            hm.put(fl, v1);
//                            fl.addView(v1);
//
//
//                            v1.setY(holder.v.getY() + view.findViewById(R.id.categoryApps).getY() + loc_y - 10);
//                            v1.setX(holder.v.getX() + view.findViewById(R.id.categoryApps).getX() + loc_X - 10);
//
//
//                            v1_locX = loc_X + 30;
//                            v1_locY = loc_y + 30;
////                            v1.findViewById(R.id.fral).setBackgroundColor(Color.rgb(23, 4, 5));
//
//                            FrameLayout f = v1.findViewById(R.id.fral);
//
//
//
//                            SpringAnimation s = new SpringAnimation(vi1, DynamicAnimation.Y, 30);
//                            SpringAnimation s1 = new SpringAnimation(vi1, DynamicAnimation.X, 30);
//                            s.start();
//                            s1.start();
//
//                            return true;
//                        }
//
//
//                    });
                    holder.v.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            e = event;
                            System.out.println("dddd................................");
                            FrameLayout fl = view.findViewById(R.id.fcccc);
                            loc_y = event.getY();
                            loc_X = event.getX();
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_UP:
                                    v1.setY(0);
                                    v1.setX(0);
                                    SpringAnimation s = new SpringAnimation(vi1, DynamicAnimation.Y, 0);
                                    SpringAnimation s1 = new SpringAnimation(vi1, DynamicAnimation.X, 0);
                                    s.start();
                                    s1.start();
                                    hm.remove(fl);
                                    fl.removeView(v1);
                                    break;


                            }

                            return false;
                        }
                    });


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
                    AppDto app = apps.get(pos);

                    Bundle b = new Bundle();
                    b.putString("PackageName", app.getPackageName());
                    if (app.getDescription() != null) {
                        b.putString("description", app.getDescription());
                        b.putStringArrayList("category", (ArrayList<String>) app.getCategoryies());
                    }
                    fm.beginTransaction()
                            .setReorderingAllowed(true).addToBackStack("AppDetails")
                            .replace(R.id.fragmentContainer, AppDetailsFragment.class, b)
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