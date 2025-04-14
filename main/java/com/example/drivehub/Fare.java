package com.example.drivehub;

public class Fare {
    private String serviceName;
    private String cabType;
    private String fareAmount;

    public Fare(String serviceName, String cabType, String fareAmount) {
        this.serviceName = serviceName;
        this.cabType = cabType;
        this.fareAmount = fareAmount;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getCabType() {
        return cabType;
    }

    public String getFareAmount() {
        return fareAmount;
    }
}



