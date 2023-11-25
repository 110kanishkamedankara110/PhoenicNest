package com.phoenix.phoenixNest.util;

import com.phoenix.phoenixNest.dto.AppMain;
import com.phoenix.phoenixNest.dto.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AddAppService {
    @POST("app/addApp")
    @Multipart


     Call<Message> addApp(@Part MultipartBody.Part appIcon, @Part MultipartBody.Part appBanner, @Part("appMain") AppMain appMain);

}
