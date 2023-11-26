package com.phoenix.phoenixNest.util;


import com.phoenix.phoenixNest.dto.AppDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetAppService {

    @GET("app/getApps/{user}")
    Call<List<AppDto>> getApps(@Path("user") String user);


}
