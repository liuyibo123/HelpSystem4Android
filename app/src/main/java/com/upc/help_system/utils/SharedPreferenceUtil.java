package com.upc.help_system.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.upc.help_system.MyApplication;

/**
 * Created by Administrator on 2017/6/18.
 */

public class SharedPreferenceUtil {

    static SharedPreferences sharedPreferences ;
    public static String getString(String spname,String key){
        sharedPreferences = MyApplication.getContext().getSharedPreferences(spname, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key,"");
        return value;
    }
}
