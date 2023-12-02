package com.phoenix.phoenixNest;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.phoenix.phoenicnest.R;

public class DrawerFragmernt extends Fragment {
    boolean viewShown = false;
    FirebaseAuth mAuth;
    FirebaseUser user;
    boolean searchVisible = false;
    float height;
    float start;
    FragmentManager fm;
    int heightKeyboard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_drawer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        if(user==null){
            LinearLayout l=getActivity().findViewById(R.id.UserContainer);
            l.setVisibility(View.GONE);
            ((Button)view.findViewById(R.id.logout)).setText("Sign In");
        }
        view.findViewById(R.id.guesture_bar).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        start = event.getY();

                        // Here u can write code which is executed after the user touch on the screen
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        if (view.findViewById(R.id.guesture_bar).getTranslationY() < height) {
                            SpringAnimation s = new SpringAnimation(view.findViewById(R.id.guesture_bar), DynamicAnimation.TRANSLATION_Y, 0);
                            s.start();
                        } else {

                            SpringAnimation s = new SpringAnimation(view.findViewById(R.id.guesture_bar), DynamicAnimation.TRANSLATION_Y, height);
                            s.start();
                        }
                        // Here u can write code which is executed after the user release the touch on the screen
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        System.out.println("Height " + height + " , Loc " + view.findViewById(R.id.guesture_bar).getTranslationY());

                        view.findViewById(R.id.guesture_bar).setY(event.getRawY());

                        // Here u can write code which is executed when user move the finger on the screen
                        break;
                    }
                }
                return true;
            }
        });

        fm = getActivity().getSupportFragmentManager();

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                heightKeyboard = view.getRootView().getHeight() - view.getHeight();
            }
        });


        super.onViewCreated(view, savedInstanceState);
        ConstraintLayout c = view.findViewById(R.id.mainContainer);

        ViewTreeObserver vto = c.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                height = c.getMeasuredHeight() + 30;
                view.findViewById(R.id.guesture_bar).setTranslationY(height);

            }
        });
        ImageButton b = view.findViewById(R.id.more);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (view.findViewById(R.id.guesture_bar).getTranslationY() == height) {

                    SpringAnimation s = new SpringAnimation(view.findViewById(R.id.guesture_bar), DynamicAnimation.Y, view.findViewById(R.id.guesture_bar).getY() - height);
                    s.start();
                    viewShown = true;
                } else {
                    SpringAnimation s = new SpringAnimation(view.findViewById(R.id.guesture_bar), DynamicAnimation.Y, view.findViewById(R.id.guesture_bar).getY() + height);
                    s.start();
                    viewShown = false;
                }

            }
        });
        ImageButton b2 = view.findViewById(R.id.search);
        b2.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                if (!searchVisible) {
                    fm.beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.search, Search.class, null)
                            .commit();
                    searchVisible = true;
                } else {
                    fm.beginTransaction()
                            .setReorderingAllowed(true)
                            .remove(fm.findFragmentById(R.id.search))
                            .commit();
                    searchVisible = false;
                }


            }
        });


        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        view.findViewById(R.id.myapps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =
                        fm.findFragmentById(R.id.fragmentContainer);
                if(!(fragment instanceof MyApps)){
                    fm.beginTransaction()
                            .setReorderingAllowed(true).addToBackStack("MyApps")
                            .replace(R.id.fragmentContainer, MyApps.class, null)
                            .commit();
                }


            }
        });
        view.findViewById(R.id.myprofile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                Fragment fragment =
                        fm.findFragmentById(R.id.fragmentContainer);
                if(!(fragment instanceof UserProfileFragment)) {
                    fm.beginTransaction()
                            .setReorderingAllowed(true).addToBackStack("MyProfile")
                            .replace(R.id.fragmentContainer, UserProfileFragment.class, null)
                            .commit();
                }
            }
        });
        view.findViewById(R.id.homeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =
                        fm.findFragmentById(R.id.fragmentContainer);
                if(!(fragment instanceof HomeFragment)) {
                    fm.beginTransaction()
                            .setReorderingAllowed(true).addToBackStack("Home")
                            .replace(R.id.fragmentContainer, HomeFragment.class, null)
                            .commit();
                }
            }
        });

    }
}