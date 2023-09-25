package com.Polio.Protection.team.AddDropChildren.Add;

public class Father {

   String Father_key, Father_Name, Father_Cnic , Address;

   public Father() {

   }

    public Father(String fatherkey, String fatherName, String fatherCnic, String address) {
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
