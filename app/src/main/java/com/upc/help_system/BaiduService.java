package com.upc.help_system;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/4/13.
 */

public interface BaiduService {
    @GET("/")
    Flowable<ResponseBody> getText();
}
