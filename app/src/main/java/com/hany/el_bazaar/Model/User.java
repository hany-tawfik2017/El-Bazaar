package com.hany.el_bazaar.Model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Map;

/**
 * Created by Hany on 10/27/2018.
 */
@IgnoreExtraProperties
public class User {
    public String userType;
    public String name;
    public String email;
    public String password;
    public String address;
    public String mobile;
    public Map<String,String> aboutMap;
    public String brandName;
    public long userRate;

    public User() {

    }

    public User(String... userAttributes) {
        if (userAttributes.length > 2) {
            this.name = userAttributes[0];
            this.email = userAttributes[1];
            this.password = userAttributes[2];
            this.address = userAttributes[3];
            this.mobile = userAttributes[4];
            this.userType = userAttributes[5];
        } else {
            this.name = userAttributes[0];
            this.email = userAttributes[1];
        }
    }

    public long getUserRate() {
        return userRate;
    }

    public void setUserRate(long userRate) {
        this.userRate = userRate;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Map<String, String> getAboutMap() {
        return aboutMap;
    }

    public void setAboutMap(Map<String, String> aboutMap) {
        this.aboutMap = aboutMap;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
