package com.Polio.Protection.admin.Teams;

public class AdminTeamsItem {

    String team_key, team_id;
    int team_no;

    public AdminTeamsItem(String team_key, String team_id, int team_no) {
        this.team_key = team_key;
        this.team_id = team_id;
        this.team_no = team_no;
    }

    public String getTeam_key() {
        return team_key;
    }

    public void setTeam_key(String team_key) {
        this.team_key = team_key;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public int getTeam_no() {
        return team_no;
    }

    public void setTeam_no(int team_no) {
        this.team_no = team_no;
    }
}
