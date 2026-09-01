package com.civicconnect.model;

import java.io.Serializable;

/**
 * JavaBean representing a Citizen user in the CivicConnect system.
 * Adheres to MVC Model specification.
 */
public class Citizen implements Serializable {
    private static final long serialVersionUID = 1L;

    private String citizenId;
    private String name;
    private String mobile;
    private String email;
    private String password;
    private String pincode;

    public Citizen() {}

    public Citizen(String citizenId, String name, String mobile, String email, String password, String pincode) {
        this.citizenId = citizenId;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.pincode = pincode;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    @Override
    public String toString() {
        return "Citizen [citizenId=" + citizenId + ", name=" + name + ", mobile=" + mobile + ", email=" + email + "]";
    }
}
