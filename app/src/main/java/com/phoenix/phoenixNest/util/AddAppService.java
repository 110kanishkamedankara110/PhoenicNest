package com.phoenix.phoenixNest.util;

import com.phoenix.phoenixNest.dto.AppDetailsDto;
import com.phoenix.phoenixNest.dto.AppDto;
import com.phoenix.phoenixNest.dto.AppMain;
import com.phoenix.phoenixNest.dto.AppReleaseDto;
import com.phoenix.phoenixNest.dto.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AddAppService {
    @POST("app/addApp")
    @Multipart


     Call<Message> addApp(@Part MultipartBody.Part appIcon, @Part MultipartBody.Part appBanner, @Part("appMain") AppMain appMain);
    @POST("app/addAppDetails")
    Call<Message> addAppDetails(@Body AppDetailsDto appDetailsDto);

    @POST("app/updateAppDetails")
    Call<Message> updateAppDetails(@Body AppDetailsDto appDetailsDto);

    @POST("app/release")
    @Multipart
    Call<Message> addAppRelease(@Part MultipartBody.Part apk,@Part List<MultipartBody.Part> screenshots, @Part("appRelease") AppReleaseDto appRelease);
}
