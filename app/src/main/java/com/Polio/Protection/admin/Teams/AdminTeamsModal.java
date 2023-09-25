package com.Polio.Protection.admin.Teams;

public class AdminTeamsModal {

    String team_key, id_teams, id_one_worker, id_two_worker, id_add, date_add, id_update, date_update;

    public AdminTeamsModal(String team_key,String id_teams, String id_one_worker, String id_two_worker, String id_add, String date_add, String id_update, String date_update) {
        this.team_key = team_key;
        this.id_teams = id_teams;
        this.id_one_worker = id_one_worker;
        this.id_two_worker = id_two_worker;
        this.id_add = id_add;
        this.date_add = date_add;
        this.id_update = id_update;
        this.date_update = date_update;
    }

    public String getTeam_key() {
        return team_key;
    }

    public void setTeam_key(String team_key) {
        this.team_key = team_key;
    }

    public String getId_teams() {
        return id_teams;
    }

    public void setId_teams(String id_teams) {
        this.id_teams = id_teams;
    }

    public String getId_one_worker() {
        return id_one_worker;
    }

    public void setId_one_worker(String id_one_worker) {
        this.id_one_worker = id_one_worker;
    }

    public String getId_two_worker() {
        return id_two_worker;
    }

    public void setId_two_worker(String id_two_worker) {
        this.id_two_worker = id_two_worker;
    }

    public String getId_add() {
        return id_add;
    }

    public void setId_add(String id_add) {
        this.id_add = id_add;
    }

    public String getDate_add() {
        return date_add;
    }

    public void setDate_add(String date_add) {
        this.date_add = date_add;
    }

    public String getId_update() {
        return id_update;
    }

    public void setId_update(String id_update) {
        this.id_update = id_update;
    }

    public String getDate_update() {
        return date_update;
    }

    public void setDate_update(String date_update) {
        this.date_update = date_update;
    }
}
