package com.upc.help_system.utils.network;

import com.upc.help_system.model.HelpInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/4/6.
 */

public interface OnGetResponseListener {
    public void onResponse(Call<List<HelpInfo>> call, Response<List<HelpInfo>> response);
    public void onFailure(Call<List<HelpInfo>> call, Throwable t);
}
