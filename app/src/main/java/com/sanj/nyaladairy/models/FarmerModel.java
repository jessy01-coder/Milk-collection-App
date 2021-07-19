package com.sanj.nyaladairy.models;

public class FarmerModel {
    private String name, nid, phone, randomPic;

    public FarmerModel() {
    }

    public FarmerModel(String name, String nid, String phone, String randomPic) {
        this.name = name;
        this.nid = nid;
        this.phone = phone;
        this.randomPic = randomPic;
    }

    public String getName() {
        return name;
    }

    public String getNid() {
        return nid;
    }

    public String getPhone() {
        return phone;
    }

    public String getRandomPic() {
        return randomPic;
    }
}
