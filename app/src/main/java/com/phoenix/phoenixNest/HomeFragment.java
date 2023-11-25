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
import com.phoenix.phoenixNest.dto.CategoryDto;
import com.phoenix.phoenixNest.util.CategoryService;
import com.phoenix.phoenixNest.util.Env;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {
    int[] name = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 3, 4, 34, 34, 34, 43, 443, 4, 3, 43, 43, 4, 2, 4, 2};
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
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // create a dialog to ask yes no questions whether or not the user wants to exit
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        super.onViewCreated(view, savedInstanceState);
        fm = getActivity().getSupportFragmentManager();
        setCategory(view);
        setPopular(view);
        loadNew(view);


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
                    System.out.println(Env.get(getContext(), "app.url") + "image/" + c.getImages().get(0));
                    Picasso.get()
                            .load(Uri.parse(Env.get(getContext(), "app.url") + "image/" + c.getImages().get(0)))
//                            .load(R.drawable.person)
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


    private void loadNew(View container) {
        RecyclerView recyclerView = container.findViewById(R.id.new_items);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(new HomeFragment.Adapter());
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
            View v = inf.inflate(com.phoenix.phoenicnest.R.layout.layout_item, parent, false);
            return new Vh(v);
        }

        @Override
        public void onBindViewHolder(@NonNull HomeFragment.Vh holder, int position) {

            int pos = position;
            holder.tw.post(new Runnable() {
                @Override
                public void run() {
                    System.out.println(holder.tw.getWidth());
                    ImageView iv = holder.iv;
                    ImageView appi = holder.appicon;
                    Picasso.get().load(R.drawable.testimage).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();

                            int containerWidth = holder.tw.getWidth();

                            int containerheight = (containerWidth / width) * height;


//                            holder.tw.setHeight(containerheight);
                            iv.setClipToOutline(true);
                            appi.setClipToOutline(true);

                            Picasso.get()
                                    .load(R.drawable.testimage)
                                    .resize(containerWidth, containerheight)
                                    .centerCrop()
                                    .into(iv);

                            Picasso.get()
                                    .load(R.drawable.testappicon)
                                    .resize(appi.getWidth(), appi.getHeight())
                                    .centerCrop()
                                    .into(appi);

                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });

                }
            });
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle b = new Bundle();
                    b.putString("name", String.valueOf(name[pos]));

                    fm.beginTransaction()
                            .setReorderingAllowed(true).addToBackStack("singleView")
                            .replace(R.id.fragmentContainer, SingleViewFragment.class, b)
                            .commit();


                    Log.i("U", String.valueOf(name[pos]));
                }
            });

        }

        @Override
        public int getItemCount() {
            return name.length;
        }
    }
}