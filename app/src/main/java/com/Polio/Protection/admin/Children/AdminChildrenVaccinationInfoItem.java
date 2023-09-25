package com.Polio.Protection.admin.Children;

public class AdminChildrenVaccinationInfoItem {

    String campaign_id, mark_date, mark_time, child_key, campaign_key;
    int no;

    public AdminChildrenVaccinationInfoItem(String campaign_id, String mark_date, String mark_time, String child_key, String campaign_key, int no) {
        this.campaign_id = campaign_id;
        this.mark_date = mark_date;
        this.mark_time = mark_time;
        this.child_key = child_key;
        this.campaign_key = campaign_key;
        this.no = no;
    }

    public String getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(String campaign_id) {
        this.campaign_id = campaign_id;
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

    public String getChild_key() {
        return child_key;
    }

    public void setChild_key(String child_key) {
        this.child_key = child_key;
    }

    public String getCampaign_key() {
        return campaign_key;
    }

    public void setCampaign_key(String campaign_key) {
        this.campaign_key = campaign_key;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }
}
