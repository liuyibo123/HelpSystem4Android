package com.upc.help_system.events;

/**
 * Created by Liuyibo on 2017/5/4.
 */
/*
*   服务器推送有新订单事件
*   TODO(7) 部分解析方式由json替换为int （liuyibo）
* */
public  class NewOrderEvent {
    private int id;
    public NewOrderEvent(int id) {
        this.id = id;
    }
    public int getId(){
        return id;
    }
}
