package com.Polio.Protection.admin.Campaign;

public class AdminCampaignModal {

    String  campaign_id, start_date, end_date, admin_key, add_date, update_admin_key, update_date, campaign_key;

    public AdminCampaignModal() {

    }

    public AdminCampaignModal(String campaign_id, String start_date, String end_date, String admin_key, String add_date, String update_admin_key, String update_date, String campaign_key) {
        this.campaign_id = campaign_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.admin_key = admin_key;
        this.add_date = add_date;
        this.update_admin_key = update_admin_key;
        this.update_date = update_date;
        this.campaign_key = campaign_key;
    }


    public String getCampaign_id() {
        return campaign_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getAdmin_key() {
        return admin_key;
    }

    public String getAdd_date() {
        return add_date;
    }

    public String getUpdate_admin_key() {
        return update_admin_key;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public String getCampaign_key() {
        return campaign_key;
    }
}
