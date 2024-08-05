package com.npsdk.demo.util;

public class ServiceModel {
    private String title;
    private int id;
    private ServiceEnum serviceType;

    public ServiceModel(String title, int id, ServiceEnum serviceType) {
        this.title = title;
        this.id = id;
        this.serviceType = serviceType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ServiceEnum getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceEnum serviceType) {
        this.serviceType = serviceType;
    }
}
