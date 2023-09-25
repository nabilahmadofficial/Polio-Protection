package com.Polio.Protection.team.MarkChildren;

public class TeamMarkChildrenSearchItem {

    String children_key, children_name, campaign_key,campaign_id, father_cnic;
    int children_no;
    String check;

    public TeamMarkChildrenSearchItem(String children_key, int children_no, String children_name, String check, String campaign_key, String campaign_id, String father_cnic) {
        this.children_key = children_key;
        this.children_no = children_no;
        this.children_name = children_name;
        this.check = check;
        this.campaign_key = campaign_key;
        this.campaign_id = campaign_id;
        this.father_cnic = father_cnic;
    }

    public String getChildren_key() {
        return children_key;
    }

    public void setChildren_key(String children_key) {
        this.children_key = children_key;
    }

    public int getChildren_no() {
        return children_no;
    }

    public void setChildren_no(int children_no) {
        this.children_no = children_no;
    }

    public String getChildren_name() {
        return children_name;
    }

    public void setChildren_name(String children_name) {
        this.children_name = children_name;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
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

    public String getFather_cnic() {
        return father_cnic;
    }

    public void setFather_cnic(String father_cnic) {
        this.father_cnic = father_cnic;
    }
}
