package com.sanj.nyaladairy.models;

public class DailyCollectionModel {
    private String name, capacity, collectionId, dateTime, farmerNID;

    public DailyCollectionModel() {
    }

    public DailyCollectionModel(String name, String capacity, String collectionId, String dateTime, String farmerNID) {
        this.name = name;
        this.capacity = capacity;
        this.collectionId = collectionId;
        this.dateTime = dateTime;
        this.farmerNID = farmerNID;
    }

    public String getName() {
        return name;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getFarmerNID() {
        return farmerNID;
    }
}
