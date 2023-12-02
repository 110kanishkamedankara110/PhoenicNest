package com.phoenix.phoenixNest.util;

import com.phoenix.phoenixNest.Search;
import com.phoenix.phoenixNest.dto.AppDto;
import com.phoenix.phoenixNest.dto.CategoryDto;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryService {
    @GET("category")
    Call<List<CategoryDto>> getCategory();

    @GET("category/getApps/{category}")
    Call<List<AppDto>> getCategoryApps(@Path("category") String category);

}
