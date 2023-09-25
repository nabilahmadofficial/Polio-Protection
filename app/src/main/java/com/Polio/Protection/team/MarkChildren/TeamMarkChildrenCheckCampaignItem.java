package com.Polio.Protection.team.MarkChildren;

public class TeamMarkChildrenCheckCampaignItem {

    String campaign_key, campaign_id, campaign_star_date, campaign_end_date;
    int campaign_no;

    public TeamMarkChildrenCheckCampaignItem(String campaign_key, int campaign_no, String campaign_id, String campaign_star_date, String campaign_end_date) {
        this.campaign_key = campaign_key;
        this.campaign_no = campaign_no;
        this.campaign_id = campaign_id;
        this.campaign_star_date = campaign_star_date;
        this.campaign_end_date = campaign_end_date;
    }

    public String getCampaign_key() {
        return campaign_key;
    }

    public void setCampaign_key(String campaign_key) {
        this.campaign_key = campaign_key;
    }

    public String getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(String campaign_id) {
        this.campaign_id = campaign_id;
    }

    public String getCampaign_star_date() {
        return campaign_star_date;
    }

    public void setCampaign_star_date(String campaign_star_date) {
        this.campaign_star_date = campaign_star_date;
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
