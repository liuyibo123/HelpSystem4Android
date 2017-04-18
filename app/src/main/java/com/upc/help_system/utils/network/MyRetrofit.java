package com.upc.help_system.utils.network;

import com.upc.help_system.model.HelpInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/4/5.
 */

public class MyRetrofit implements NetConnection {
    private Retrofit retrofit;
    private RequestServices requestServices;
    private OnGetResponseListener listener;
    private OnPostResponseListener listener_post;
    public void setOnPostResponseListener(OnPostResponseListener listener_post){
        this.listener_post = listener_post;
    }
    public void setOnGetResponseListener(OnGetResponseListener listener){
        this.listener = listener;
    }
    private RequestServices getRequestServices(){
        if(requestServices == null){
            return getRetrofit().create(RequestServices.class);
        }
        else
            return requestServices;
    }
    private Retrofit getRetrofit(){
        if(retrofit == null) {
            return new Retrofit.Builder()
                   .baseUrl("http://112.74.209.39:8080/")
 //                   .baseUrl("http://172.25.208.125:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        else return retrofit;
    }
    @Override
    public void get(String url) {
        Call<List<HelpInfo>> call = getRequestServices().get(url);
        call.enqueue(new Callback<List<HelpInfo>>() {
            @Override
            public void onResponse(Call<List<HelpInfo>> call, Response<List<HelpInfo>> response) {
                listener.onResponse(call,response);
            }
            @Override
            public void onFailure(Call<List<HelpInfo>> call, Throwable t) {
                listener.onFailure(call,t);
            }
        });
     }


    @Override
    public  void post(String url, HelpInfo helpInfo) {
        Call<String> call = getRequestServices().post(url,helpInfo);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                listener_post.onResponse(call,response);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener_post.onFailure(call,t);
            }

        });
    }

    @Override
    public void delete(String url) {

    }

    @Override
    public void update(String url) {

    }
}
