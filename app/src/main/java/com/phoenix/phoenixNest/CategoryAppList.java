package com.phoenix.phoenixNest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.phoenix.phoenicnest.R;
import com.phoenix.phoenixNest.dto.AppDto;
import com.phoenix.phoenixNest.util.CategoryService;
import com.phoenix.phoenixNest.util.Env;
import com.phoenix.phoenixNest.util.GetAppService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryAppList extends Fragment {

    Bundle extra;
    List<AppDto> apps;

    FragmentManager fm;

    public CategoryAppList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_category_app_list, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        extra = getArguments();
        fm = getActivity().getSupportFragmentManager();
        TextView tv = view.findViewById(R.id.categoryTitle);
        tv.setText(extra.getString("category"));

        load(view);

    }


    private void load(View container) {
        LoadingFragment loader = LoadingFragment.getLoader();
        if (!loader.isLoading) {
            loader.show(fm, "Loader");
        }
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Env.get(getContext(), "app.url")).build();
        CategoryService categoryService = retrofit.create(CategoryService.class);
        Call<List<AppDto>> call = categoryService.getCategoryApps(extra.getString("category"));

        call.enqueue(new Callback<List<AppDto>>() {
            @Override
            public void onResponse(Call<List<AppDto>> call, Response<List<AppDto>> response) {
                if (response.isSuccessful()) {
                    System.out.println("sucess................");
                    apps = response.body();
                    RecyclerView rec = container.findViewById(R.id.categoryApps);
                    StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                    rec.setLayoutManager(staggeredGridLayoutManager);
                    rec.setAdapter(new CategoryAppList.Adapter());

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


    class Adapter extends RecyclerView.Adapter<CategoryAppList.Vh> {

        @NonNull
        @Override
        public CategoryAppList.Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inf = LayoutInflater.from(parent.getContext());
            View v = inf.inflate(R.layout.layout_item, parent, false);
            return new CategoryAppList.Vh(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryAppList.Vh holder, int position) {

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
                    AppDto app = apps.get(pos);

                    Bundle b = new Bundle();
                    b.putString("packageName", app.getPackageName());
                    b.putString("MaxColor", app.getMaxColor());
                    b.putString("MinColor", app.getMinColor());
                    b.putStringArrayList("categoryies", (ArrayList<String>) app.getCategoryies());
                    b.putStringArrayList("screenshots", (ArrayList<String>) app.getScreenShots());
                    b.putString("appBanner", app.getAppBanner());
                    b.putString("apk", app.getApk());
                    b.putString("appIcon", app.getAppIcon());
                    b.putString("appTitle", app.getAppTitle());
                    b.putString("appDescription", app.getDescription());
                    b.putString("version", app.getVersion());
                    b.putString("versionCode", app.getVersionCode());
                    b.putInt("width", app.getWidth());
                    b.putInt("height", app.getHeight());
                    b.putString("mainActivity", app.getMainActivity());


                    fm.beginTransaction()
                            .setReorderingAllowed(true).addToBackStack("SingleAppView")
                            .replace(R.id.fragmentContainer, SingleViewFragment.class, b)
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