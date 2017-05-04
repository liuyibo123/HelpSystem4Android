package com.upc.help_system.model;

import android.util.Log;

import com.upc.help_system.utils.network.HttpCon;
import com.upc.help_system.utils.network.httpconimpl.RetrofitUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/4/27.
 */

public class UserModel {
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    User user = null;
    HttpCon con;

    public void add(User user){
        con = new RetrofitUtil();

        con.setGetListener(new HttpCon.OnResponseListener() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d("UserModel", "response:" + response);
            }

            @Override
            public void onFailure(Call call, Throwable throwable) {
                Log.d("UserModel", "throwable:" + throwable);
            }
        });
        con.doPost("add_user",user,null);
    }
    public class User {
        public User(int id, String name, String passwd, String phone_number) {
            this.id = id;
            this.name = name;
            this.passwd = passwd;
            this.phone_number = phone_number;
        }

        private int id;
        private String name;
        private String passwd;
        private String phone_number;
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPasswd() {
            return passwd;
        }

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

    }
}
