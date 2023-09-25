package com.Polio.Protection.team.Children;

public class TeamChildrenItem {

    String children_key, children_name, children_gender, father_id_card;
    int children_no;

    public TeamChildrenItem(String children_key, String children_name, String children_gender, String father_id_card, int children_no) {
        this.children_key = children_key;
        this.children_name = children_name;
        this.children_gender = children_gender;
        this.father_id_card = father_id_card;
        this.children_no = children_no;
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

    public String getChildren_gender() {
        return children_gender;
    }

    public void setChildren_gender(String children_gender) {
        this.children_gender = children_gender;
    }

    public String getFather_id_card() {
        return father_id_card;
    }

    public void setFather_id_card(String father_id_card) {
        this.father_id_card = father_id_card;
    }

    public int getChildren_no() {
        return children_no;
    }

    public void setChildren_no(int children_no) {
        this.children_no = children_no;
    }
}
