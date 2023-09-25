package com.Polio.Protection.team.MarkChildren;

public class ChildrenItem {

    String children_no, children_name, children_id;
    Boolean check;

    public ChildrenItem(String children_id, String children_no, String children_name, Boolean check) {
        this.children_id = children_id;
        this.children_no = children_no;
        this.children_name = children_name;
        this.check = check;
    }

    public String getChildren_id() {
        return children_id;
    }

    public void setChildren_id(String children_id) {
        this.children_id = children_id;
    }

    public String getChildren_no() {
        return children_no;
    }

    public void setChildren_no(String children_no) {
        this.children_no = children_no;
    }

    public String getChildren_name() {
        return children_name;
    }

    public void setChildren_name(String children_name) {
        this.children_name = children_name;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
}
