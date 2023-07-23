package com.example.billboard;

public class BillboardData {
    String id;
    String billboardName;
    String Name;

    BillboardData(String id, String billboardName, String Name) {
        this.id = id;
        this.billboardName = billboardName;
        this.Name = Name;
    }
    public String getBillboardName(){
        return billboardName;
    }
    public String getId(){
        return id;
    }
}
