package com.Polio.Protection.team.MarkChildren;

public class TeamMarkChildrenMarkModal {

    String children_key, children_name, father_cnic, location_latitude, location_longitude, date_time, picture, team_key;

    public TeamMarkChildrenMarkModal(String children_key, String children_name, String father_cnic, String location_latitude, String location_longitude, String date_time, String picture, String team_key) {
        this.children_key = children_key;
        this.children_name = children_name;
        this.father_cnic = father_cnic;
        this.location_latitude = location_latitude;
        this.location_longitude = location_longitude;
        this.date_time = date_time;
        this.picture = picture;
        this.team_key = team_key;
    }

    public String getChildren_key() {
        return children_key;
    }

    public void setChildren_key(String children_key) {
        this.children_key = children_key;
    }

    public String getChildren_name() {
        return children_name;
    }

    public void setChildren_name(String children_name) {
        this.children_name = children_name;
    }

    public String getFather_cnic() {
        return father_cnic;
    }

    public void setFather_cnic(String father_cnic) {
        this.father_cnic = father_cnic;
    }

    public String getLocation_latitude() {
        return location_latitude;
    }

    public void setLocation_latitude(String location_latitude) {
        this.location_latitude = location_latitude;
    }

    public String getLocation_longitude() {
        return location_longitude;
    }

    public void setLocation_longitude(String location_longitude) {
        this.location_longitude = location_longitude;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTeam_key() {
        return team_key;
    }

    public void setTeam_key(String team_key) {
        this.team_key = team_key;
    }

}
