package com.hany.el_bazaar.Model;

import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by Hany on 11/24/2018.
 */
@IgnoreExtraProperties
public class Service implements Comparable<Service> {

    public String serviceName,serviceId;
    public List<String> serviceUsers;
    boolean serviceChecked;

    public Service(){

    }

    public Service(String serviceName){
        this.serviceName = serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<String> getServiceUsers() {
        return serviceUsers;
    }

    public void setServiceUsers(List<String> serviceUsers) {
        this.serviceUsers = serviceUsers;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public boolean isServiceChecked() {
        return serviceChecked;
    }

    public void setServiceChecked(boolean serviceChecked) {
        this.serviceChecked = serviceChecked;
    }

    @Override
    public int compareTo(@NonNull Service o) {
        return 0;
    }
}
