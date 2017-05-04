package com.upc.help_system.utils.network;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/4/26.
 */

public interface HttpCon {
    OnResponseListener getListener = null;
    OnResponseListener postListener = null;

    public  void setGetListener(OnResponseListener getListener);
    public void setPostListener(OnResponseListener postListener);
    public void doGet(String url);
    public void doPost(String url, Object body,Object obj1);
    public interface OnResponseListener{
        public void onResponse(Call call, Response response);
        public void onFailure(Call call, Throwable throwable) ;
    }

}
