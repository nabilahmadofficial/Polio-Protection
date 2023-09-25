package com.Polio.Protection.admin.Campaign;

public class AdminCampaignItem {

    String campaign_key, campaign_start_date, campaign_end_date;
    int campaign_no;

    public AdminCampaignItem(String campaign_key, String campaign_start_date, String campaign_end_date, int campaign_no) {
        this.campaign_key = campaign_key;
        this.campaign_start_date = campaign_start_date;
        this.campaign_end_date = campaign_end_date;
        this.campaign_no = campaign_no;
    }

    public String getCampaign_key() {
        return campaign_key;
    }

    public void setCampaign_key(String campaign_key) {
        this.campaign_key = campaign_key;
    }

    public String getCampaign_start_date() {
        return campaign_start_date;
    }

    public void setCampaign_start_date(String campaign_start_date) {
        this.campaign_start_date = campaign_start_date;
    }

    public String getCampaign_end_date() {
        return campaign_end_date;
    }

    public void setCampaign_end_date(String campaign_end_date) {
        this.campaign_end_date = campaign_end_date;
    }

    public int getCampaign_no() {
        return campaign_no;
    }

    public void setCampaign_no(int campaign_no) {
        this.campaign_no = campaign_no;
    }
}
