package com.phoenix.phoenixNest.util;


import com.phoenix.phoenixNest.dto.AppDto;

import org.checkerframework.checker.units.qual.C;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetAppService {

    @GET("app/getApps/{user}")
    Call<List<AppDto>> getApps(@Path("user") String user);

    @GET("app/getAllApps")
    Call<List<AppDto>> getAllApps();

    @GET("app/getAllApps/{key}")
    Call<List<AppDto>> getAllApps(@Path("key") String key);

    @GET("app/getPopular")
    Call<List<AppDto>> getPopular();
}
