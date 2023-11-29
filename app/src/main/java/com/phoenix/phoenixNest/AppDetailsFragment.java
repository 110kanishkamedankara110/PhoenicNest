package com.phoenix.phoenixNest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.phoenix.phoenicnest.R;
import com.phoenix.phoenixNest.dto.AppDetailsDto;
import com.phoenix.phoenixNest.dto.CategoryDto;
import com.phoenix.phoenixNest.dto.Message;
import com.phoenix.phoenixNest.util.AddAppService;
import com.phoenix.phoenixNest.util.CategoryService;
import com.phoenix.phoenixNest.util.Env;
import com.phoenix.phoenixNest.util.Error;
import com.phoenix.phoenixNest.util.ListListener;
import com.phoenix.phoenixNest.util.ListWithListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AppDetailsFragment extends Fragment {

    FragmentManager fm;
    Bundle extra;
    String[] categoryies;
    List<String> selectedCat;

    public AppDetailsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_app_details, container, false);
    }

    private boolean searchCategory(String cat) {

        return Arrays.asList(categoryies).contains(cat);

    }

    private boolean isSimilar(List<String> firstList, List<String> secondList) {
        boolean isSimilar = true;
        for (String l : firstList) {

            if (secondList.contains(l)) {

            } else {
                isSimilar = false;
                break;
            }
        }
        for (String l : secondList) {

            if (firstList.contains(l)) {

            } else {
                isSimilar = false;
                break;
            }
        }
        return isSimilar;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);


        fm = getActivity().getSupportFragmentManager();
        LoadingFragment loader = LoadingFragment.getLoader();
        if (!loader.isLoading) {
            loader.show(fm, "Loader");
        }
        Retrofit rf = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Env.get(getContext(), "app.url"))
                .build();

        CategoryService service = rf.create(CategoryService.class);
        Call<List<CategoryDto>> category = service.getCategory();

        category.enqueue(new Callback<List<CategoryDto>>() {
            @Override
            public void onResponse(Call<List<CategoryDto>> call, Response<List<CategoryDto>> response) {
                if (response.isSuccessful()) {
                    String[] cat = new String[response.body().size()];
                    for (int i = 0; i < response.body().size(); i++) {
                        cat[i] = response.body().get(i).getCategory();
                    }

                    extra = getArguments();
                    categoryies = cat;


                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, categoryies);


                    ImageButton b = view.findViewById(R.id.addButton);
                    b.setEnabled(false);
                    FlexboxLayout fb = view.findViewById(R.id.fb);

                    AutoCompleteTextView atv = view.findViewById(R.id.catView);
                    atv.setAdapter(adapter);
                    atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (!selectedCat.contains(atv.getText().toString())) {
                                b.setClickable(true);
                                b.setEnabled(true);
                            }

                        }
                    });
                    atv.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {


                            b.setClickable(searchCategory(atv.getText().toString().trim()) && !selectedCat.contains(atv.getText().toString().trim()));
                            b.setEnabled(searchCategory(atv.getText().toString().trim()) && !selectedCat.contains(atv.getText().toString().trim()));

                        }
                    });
                    selectedCat = new ListWithListener<String>(new ListListener() {
                        @Override
                        public void onAdd(Object item) {
                            View cat = LayoutInflater.from(getContext()).inflate(R.layout.layout_category_item, fb, false);

                            TextView tv = cat.findViewById(R.id.catText);
                            ImageButton ib = cat.findViewById(R.id.catCanclebut);
                            ib.setImageResource(R.drawable.baseline_close_24);
                            tv.setText((String) item);
                            atv.setText("");

                            ib.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fb.removeView(cat);

                                    selectedCat.remove(tv.getText().toString());

                                }
                            });
                            fb.addView(cat);
                        }

                        @Override
                        public void onRemove() {

                        }

                        @Override
                        public void onRemoveAll() {

                        }
                    });

                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View vi) {
                            selectedCat.add(atv.getText().toString());
                        }
                    });

                    if (extra.getString("description") != null) {

                        EditText et = view.findViewById(R.id.appDetails);
                        String des = extra.getString("description");
                        List<String> cato = extra.getStringArrayList("category");
                        System.out.println(des);
                        et.setText(des);
                        cato.forEach(c -> {

                            selectedCat.add(c);
                        });
                    }


                }


                if (loader.isLoading) {
                    loader.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryDto>> call, Throwable t) {
                if (loader.isLoading) {
                    loader.dismiss();
                }
            }
        });


        view.findViewById(R.id.cancelAppAdd2).setOnClickListener(v ->
                fm.popBackStack());
        view.findViewById(R.id.nextappAdd2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = view.findViewById(R.id.appDetails);
                List<String> list = selectedCat;

                if (et.getText().toString().isEmpty()) {
                    Error.setErrorFiled(et, getContext(), view.findViewById(R.id.error4), "Enter Description");
                } else if (list.size() <= 0) {
                    Error.removeErrorFiled(et, getContext());
                    Error.displayErrorMessage(view.findViewById(R.id.error4), getContext(), "Add Categories");
                } else {
                    Error.removeErrorFiled(et, getContext());
                    Error.removeErrorText(view.findViewById(R.id.error4), getContext());

                    AppDetailsDto appDetailsDto = new AppDetailsDto();
                    appDetailsDto.setDescription(et.getText().toString());
                    appDetailsDto.setCategories(list);
                    appDetailsDto.setPackageName(extra.getString("PackageName"));

                    if (!loader.isLoading) {
                        loader.dismiss();
                    }
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Env.get(getContext(), "app.url"))
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();


                    AddAppService AddAppService = retrofit.create(AddAppService.class);

                    if (extra.getString("description") != null) {
                        if (!extra.getString("description").equals(et.getText().toString()) || !isSimilar(extra.getStringArrayList("category"), selectedCat)) {
                            AddAppService.updateAppDetails(appDetailsDto).

                                    enqueue(new retrofit2.Callback<Message>() {
                                        @Override
                                        public void onResponse(Call<Message> call, Response<Message> response) {

                                            if (response.isSuccessful()) {
                                                Message message = response.body();
                                                if (message.getMessage().equals("Success")) {
                                                    Error.removeErrorText(view.findViewById(R.id.error4), getContext());
                                                    et.setText("");
                                                    selectedCat = new LinkedList<String>();
                                                    FlexboxLayout fb = view.findViewById(R.id.fb);
                                                    fb.removeAllViews();
                                                } else {
                                                    Error.displayErrorMessage(view.findViewById(R.id.error4), getContext(), message.getMessage());
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
                                                Error.displayErrorMessage(view.findViewById(R.id.error4), getContext(), "Request TimeOut");
                                                loader.dismiss();
                                            }
                                        }
                                    });
                        }

                    } else {
                        AddAppService.addAppDetails(appDetailsDto).

                                enqueue(new retrofit2.Callback<Message>() {
                                    @Override
                                    public void onResponse(Call<Message> call, Response<Message> response) {

                                        if (response.isSuccessful()) {
                                            Message message = response.body();
                                            if (message.getMessage().equals("Success")) {
                                                Error.removeErrorText(view.findViewById(R.id.error4), getContext());
                                                et.setText("");
                                                selectedCat = new LinkedList<String>();
                                                FlexboxLayout fb = view.findViewById(R.id.fb);
                                                fb.removeAllViews();
                                            } else {
                                                Error.displayErrorMessage(view.findViewById(R.id.error4), getContext(), message.getMessage());
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
                                            Error.displayErrorMessage(view.findViewById(R.id.error4), getContext(), "Request TimeOut");
                                            loader.dismiss();
                                        }
                                    }
                                });

                    }


                    //redirect to add relese page
                    fm.beginTransaction()
                            .setReorderingAllowed(true).addToBackStack("Release")
                            .replace(R.id.fragmentContainer, AppRelseaseFragment.class, extra)
                            .commit();

                }


            }
        });

    }
}