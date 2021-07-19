package com.sanj.nyaladairy.models;

public class BarchartModel {
    String month;
    float collections;


    public BarchartModel(String month, float collections) {
        this.month = month;
        this.collections = collections;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public float getCollections() {
        return collections;
    }

    public void setCollections(int collections) {
        this.collections = collections;
    }
}
