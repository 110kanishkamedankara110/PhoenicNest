package com.phoenix.phoenixNest.util;

import com.phoenix.phoenixNest.dto.CategoryDto;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {
    @GET("category")
    Call<List<CategoryDto>> getCategory();
}
