package com.Polio.Protection.team.AddDropChildren.Add;

public class Children {

    String Child_key, Child_Name, DateofBirth, Gender, Team_key, Location_latitude, Location_longitude, Child_Add_Date, Team_key_update, Child_update_date;

    public Children() {

    }

    public Children(String child_key, String child_Name, String dateofBirth, String gender, String team_key, String location_latitude, String location_longitude, String child_Add_Date, String team_key_update, String child_update_date) {
        Child_key = child_key;
        Child_Name = child_Name;
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

    public String getChild_Name() {
        return Child_Name;
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
