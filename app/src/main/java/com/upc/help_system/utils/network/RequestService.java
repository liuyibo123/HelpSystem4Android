package com.upc.help_system.utils.network;

import com.upc.help_system.model.Express;
import com.upc.help_system.model.MainTable;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/5/10.
 */

public interface RequestService {
    @POST("/maintable/add_new")
    Call<MyResponse> addMainTable(@Body MainTable table);

    @POST("/express/add")
    Call<Void> addExpress(@Body Express express);
}
