package com.Polio.Protection.team.Children;

public class TeamChildrenModal {

    String Child_key, Child_self_key, Child_Name,Father_Cnic, DateofBirth, Gender, Team_key, Child_Add_Date, Location_latitude, Location_longitude, Team_key_update, Child_update_date;

    public TeamChildrenModal() {

    }

    public TeamChildrenModal(String child_key, String child_self_key, String child_Name, String Father_CNIC, String dateofBirth, String gender, String team_key, String child_Add_Date, String location_latitude, String location_longitude, String team_key_update, String child_update_date) {
        Child_key = child_key;
        Child_self_key = child_self_key;
        Child_Name = child_Name;
        Father_Cnic= Father_CNIC;
        DateofBirth = dateofBirth;
        Gender = gender;
        Team_key = team_key;
        Location_latitude = location_latitude;
        Location_longitude = location_longitude;
        Child_Add_Date = child_Add_Date;
        Team_key_update = team_key_update;
        Child_update_date = child_update_date;
    }

    public String getChild_key() {
        return Child_key;
    }

    public String getChild_self_key() {
        return Child_self_key;
    }

    public String getChild_Name() {
        return Child_Name;
    }

    public String getFather_CNIC() {
        return Father_Cnic;
    }

    public String getDateofBirth() {
        return DateofBirth;
    }

    public String getGender() {
        return Gender;
    }

    public String getTeam_key() {
        return Team_key;
    }

    public String getLocation_latitude() {
        return Location_latitude;
    }

    public String getLocation_longitude() {
        return Location_longitude;
    }

    public String getChild_Add_Date() {
        return Child_Add_Date;
    }

    public String getTeam_key_update() {
        return Team_key_update;
    }

    public String getChild_update_date() {
        return Child_update_date;
    }
}
