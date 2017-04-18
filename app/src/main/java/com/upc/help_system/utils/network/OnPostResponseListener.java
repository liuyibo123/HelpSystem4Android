package com.upc.help_system.utils.network;

import com.upc.help_system.model.HelpInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/4/6.
 */

public interface OnPostResponseListener {
    public void onResponse(Call<String> call, Response<String> response);
    public void onFailure(Call<String> call, Throwable t);
}
