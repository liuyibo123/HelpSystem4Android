package com.upc.help_system.model;

import android.util.Log;

import com.upc.help_system.utils.network.HttpCon;
import com.upc.help_system.utils.network.httpconimpl.RetrofitUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/4/27.
 */

public class ExpressModel {
    public void postExpress(String url, Express express, MainTableModel.MainTable mainTable){
        RetrofitUtil retrofitUtil = new RetrofitUtil();

        retrofitUtil.setPostListener(new HttpCon.OnResponseListener() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("ExpressModel", "response:" + response);
            }
            @Override
            public void onFailure(Call call, Throwable throwable) {
                Log.d("ExpressModel", "throwable:" + throwable);
            }
        });
        retrofitUtil.doPost(url,express, mainTable);
    };
   public class Express{
       int id=0;
       public Express(String express_company, String take_number, String name, String phone, String express_description) {
           this.express_company = express_company;
           this.take_number = take_number;
           this.name = name;
           this.phone = phone;
           this.express_description = express_description;
       }

       String express_company;
       String take_number;
       String name;
       String phone;
       String express_description;
       public int getId() {
           return id;
       }

       public void setId(int id) {
           this.id = id;
       }

       public String getExpress_company() {
           return express_company;
       }

       public void setExpress_company(String express_company) {
           this.express_company = express_company;
       }

       public String getTake_number() {
           return take_number;
       }

       public void setTake_number(String take_number) {
           this.take_number = take_number;
       }

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }

       public String getPhone() {
           return phone;
       }

       public void setPhone(String phone) {
           this.phone = phone;
       }

       public String getExpress_description() {
           return express_description;
       }

       public void setExpress_description(String express_description) {
           this.express_description = express_description;
       }

   }
}
