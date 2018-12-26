package com.hany.el_bazaar.Model;

import com.google.firebase.database.IgnoreExtraProperties;

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
}
