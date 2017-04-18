package com.upc.help_system.model;


import java.util.Date;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Observable;

/**
 * Created by Liuyibo on 2017/3/22.
 *      帮助信息实体类
 */

public class HelpInfo{

    private int id;  //编号,自增
    private int category = 1;//种类
    private String content = "无";//内容
    private String destination_from = "无";//目的地(从)
    private String destination_to = "无";//目的地(去)
    private float tip = 0;//悬赏金额
    private int accepter_id;//接单人id
    private int creator_id;//创建人id
    private String deadline;//最迟完成时间
    private int state_num;//状态字段

    public Map get_links() {
        return _links;
    }

    public void set_links(Map _links) {
        this._links = _links;
    }

    private Map _links;

    public int getNeed_person() {
        return need_person;
    }

    public void setNeed_person(int need_person) {
        this.need_person = need_person;
    }

    private int need_person;//需要多少人
    public int getId() {
        return id;
    }
    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDestination_from() {
        return destination_from;
    }

    public void setDestination_from(String destination_from) {
        this.destination_from = destination_from;
    }

    public String getDestination_to() {
        return destination_to;
    }

    public void setDestination_to(String destination_to) {
        this.destination_to = destination_to;
    }

    public float getTip() {
        return tip;
    }

    public void setTip(float tip) {
        this.tip = tip;
    }

    public int getAccepter_id() {
        return accepter_id;
    }

    public void setAccepter_id(int accepter_id) {
        this.accepter_id = accepter_id;
    }


    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getState_num() {
        return state_num;
    }

    public void setState_num(int state_num) {
        this.state_num = state_num;
    }

    @Override
    public String toString() {
        return "HelpInfo{" +
                "id=" + id +
                ", category=" + category +
                ", content='" + content + '\'' +
                ", destination_from='" + destination_from + '\'' +
                ", destination_to='" + destination_to + '\'' +
                ", tip=" + tip +
                ", accepter_id=" + accepter_id +
                ", creator_id=" + creator_id +
                ", deadline=" + deadline +
                ", state_num=" + state_num +
                ", need_person=" + need_person +
                '}';
    }
}
