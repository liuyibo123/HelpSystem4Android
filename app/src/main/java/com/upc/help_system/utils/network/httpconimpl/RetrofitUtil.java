package com.upc.help_system.utils.network.httpconimpl;

import android.util.Log;

import com.upc.help_system.model.ExpressModel;
import com.upc.help_system.utils.network.HttpCon;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2017/4/27.
 */

public class RetrofitUtil implements HttpCon{
    private static String URL = "http://112.74.209.39:8080";
    private Retrofit retrofit;
    private OnResponseListener getlistener;
    private OnResponseListener postlistener;

    public static void setURL(String url){
        URL = url;
    }
    public RetrofitUtil() {
        if(retrofit==null)
            retrofit = new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Override
    public void setGetListener(OnResponseListener getListener) {
        this.getlistener = getListener;
    }

    @Override
    public void setPostListener(OnResponseListener postListener) {
        this.postlistener = postListener;
    }

    @Override
    public void doGet(String url) {

    }

    @Override
    public void doPost(String url, Object obj,Object obj1) {
       RetrofitUtil.RequestService service = retrofit.create(RequestService.class);
        Call<String> call = service.dopost(url,obj,obj);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("RetrofitUtil", "success");
                postlistener.onResponse(call,response);
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.d("RetrofitUtil", "failure:"+throwable);
                postlistener.onFailure(call,throwable);
            }
        });

    }
    public interface RequestService{
        @Headers("Content-Type: application/json")
        @POST("/{url}")
        Call<String> dopost(@Path("url") String url, @Body Object obj,@Body Object obj1);
    }
}
