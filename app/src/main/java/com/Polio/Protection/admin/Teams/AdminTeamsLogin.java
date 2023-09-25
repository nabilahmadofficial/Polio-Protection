package com.Polio.Protection.admin.Teams;

public class AdminTeamsLogin {

    String id, email, type;

    public AdminTeamsLogin(String id, String email, String type) {
        this.id = id;
        this.email = email;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id_teams) {
        this.id = id;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }
}
