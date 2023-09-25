package com.Polio.Protection.team.Children;

public class TeamFatherModal {

    String Father_key, Father_Name, Father_Cnic, Address;

    public TeamFatherModal() {

    }

    public TeamFatherModal(String fatherkey, String fatherName, String fatherCnic, String address) {
        Father_key = fatherkey;
        Father_Name = fatherName;
        Father_Cnic = fatherCnic;
        Address = address;
    }

    public String getFatherkey() {
        return Father_key;
    }

    public String getFatherName() {
        return Father_Name;
    }

    public String getFatherCnic() {
        return Father_Cnic;
    }

    public String getAddress() {
        return Address;
    }
}
