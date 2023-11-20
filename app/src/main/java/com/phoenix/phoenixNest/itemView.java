package com.phoenix.phoenixNest;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

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
import android.widget.TextView;

import com.phoenix.phoenicnest.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class itemView extends Fragment {

    Bundle extra;
    int[] name = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 3, 4, 34, 34, 34, 43, 443, 4, 3, 43, 43, 4, 2, 4, 2};

    FragmentManager fm;

    public itemView() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(com.phoenix.phoenicnest.R.layout.fragment_item_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fm = getActivity().getSupportFragmentManager();
        extra = getArguments();
        ((TextView) view.findViewById(R.id.pageTitle)).setText(extra.getString("title"));
        RecyclerView rec = view.findViewById(R.id.itemView);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        rec.setLayoutManager(staggeredGridLayoutManager);
        rec.setAdapter(new Adapter());


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
            iv=itemView.findViewById(R.id.bg);
            appicon=itemView.findViewById(R.id.appIcon);
        }
    }

    class Adapter extends RecyclerView.Adapter<itemView.Vh> {
        @NonNull
        @Override
        public itemView.Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inf = LayoutInflater.from(parent.getContext());
            View v = inf.inflate(R.layout.layout_item, parent, false);
            return new Vh(v);
        }

        @Override
        public void onBindViewHolder(@NonNull itemView.Vh holder, int position) {

            int pos = position;
            holder.tw.post(new Runnable() {
                @Override
                public void run() {
                    System.out.println(holder.tw.getWidth());
                    ImageView iv=holder.iv;
                    ImageView appi = holder.appicon;
                    Picasso.get().load(R.drawable.testimage).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                            int width = bitmap.getWidth();
                            int height = bitmap.getHeight();

                            int containerWidth=holder.tw.getWidth();

                            int containerheight=(containerWidth/width)*height;


//                            holder.tw.setHeight(containerheight);
                            iv.setClipToOutline(true);
                            appi.setClipToOutline(true);
                            Picasso.get()
                                    .load(R.drawable.testimage)
                                    .resize(containerWidth,containerheight)
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