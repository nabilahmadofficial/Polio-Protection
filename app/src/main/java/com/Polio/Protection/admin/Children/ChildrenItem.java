package com.Polio.Protection.admin.Children;

public class ChildrenItem  {

    String children_key, children_name, father_cnic;
    int children_no;

    public ChildrenItem(String children_key, String children_name, String father_cnic, int children_no) {
        this.children_key = children_key;
        this.children_name = children_name;
        this.father_cnic = father_cnic;
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

    public String getFather_cnic() {
        return father_cnic;
    }

    public void setFather_cnic(String father_cnic) {
        this.father_cnic = father_cnic;
    }

    public int getChildren_no() {
        return children_no;
    }

    public void setChildren_no(int children_no) {
        this.children_no = children_no;
    }
}
