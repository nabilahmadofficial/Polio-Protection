package com.Polio.Protection.admin.ViewResult;

public class AdminViewResultChildrenItem {

    String children_name, children_key, father_cnic, mark_date, mark_time, campaign_key;
    int children_no;

    public AdminViewResultChildrenItem(String children_name, String children_key, String father_cnic, String mark_date, String mark_time, String campaign_key, int children_no) {
        this.children_name = children_name;
        this.children_key = children_key;
        this.father_cnic = father_cnic;
        this.mark_date = mark_date;
        this.mark_time = mark_time;
        this.campaign_key = campaign_key;
        this.children_no = children_no;
    }

    public String getChildren_name() {
        return children_name;
    }

    public void setChildren_name(String children_name) {
        this.children_name = children_name;
    }

    public String getChildren_key() {
        return children_key;
    }

    public void setChildren_key(String children_key) {
        this.children_key = children_key;
    }

    public String getFather_cnic() {
        return father_cnic;
    }

    public void setFather_cnic(String father_cnic) {
        this.father_cnic = father_cnic;
    }

    public String getMark_date() {
        return mark_date;
    }

    public void setMark_date(String mark_date) {
        this.mark_date = mark_date;
    }

    public String getMark_time() {
        return mark_time;
    }

    public void setMark_time(String mark_time) {
        this.mark_time = mark_time;
    }

    public String getCampaign_key() {
        return campaign_key;
    }

    public void setCampaign_key(String campaign_key) {
        this.campaign_key = campaign_key;
    }

    public int getChildren_no() {
        return children_no;
    }

    public void setChildren_no(int children_no) {
        this.children_no = children_no;
    }
}
