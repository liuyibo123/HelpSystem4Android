package com.upc.help_system.utils.network;

import com.upc.help_system.model.HelpInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by Administrator on 2017/4/2.
 */

public interface RequestServices {

    @Headers({"Content-Type:application:json"})
    @POST("/{url}")
    Call<String> post(@Path("url") String url,@Body HelpInfo info);
    @GET("/{url}")
    Call<List<HelpInfo>> get(@Path("url") String url);
    @GET("helpinfo/get")
    Call<String> getStr();
}
