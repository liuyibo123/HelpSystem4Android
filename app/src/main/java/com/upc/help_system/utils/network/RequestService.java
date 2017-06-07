package com.upc.help_system.utils.network;

import com.upc.help_system.model.Express;
import com.upc.help_system.model.MainTable;
import com.upc.help_system.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/10.
 */

public interface RequestService {
    @POST("/maintable/add_new")
    Call<MyResponse> addMainTable(@Body MainTable table);
    @POST("/express/add")
    Call<Void> addExpress(@Body Express express);

    @GET("/maintable/find_all")
    Call<List<MainTable>> getOrders();

    @GET("/express/getbyid")
    Call<Express> getExpressById(@Query("id") int id);

    @POST("/maintable/findbycontent")
    Call<List<MainTable>> getOrdersByContent(@Query("query") String query);

    @POST("/user/register")
    Call<String> register(@Body User user);

    @POST("/user/login")
    Call<String> login(@Body User user);

    @GET("/user/getuser")
    Call<User> getUser(@Query("name") String name);

    @GET("maintable/acceptorder")
    Call<String> acceptOrder(@Query("id") int id, @Query("name") String username);
}
