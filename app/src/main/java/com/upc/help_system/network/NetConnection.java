package com.upc.help_system.network;

import com.upc.help_system.model.HelpInfo;

/**
 * Created by Administrator on 2017/4/6.
 */

public interface NetConnection {

    public void get(String url);
    public void post(String url, HelpInfo helpInfo);
    public void delete(String url);
    public void update(String url);
}
