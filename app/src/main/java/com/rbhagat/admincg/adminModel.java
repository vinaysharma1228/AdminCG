package com.rbhagat.admincg;

public class adminModel {

    String email,password,city,adminUid;
    int count;

    public adminModel(int count) {
        this.count = count;
    }



    public adminModel(String email, String password, String city, String adminUid) {
        this.email = email;
        this.password = password;
        this.city = city;
        this.adminUid = adminUid;
    }

    public adminModel(){}

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdminUid() {
        return adminUid;
    }

    public void setAdminUid(String adminUid) {
        this.adminUid = adminUid;
    }
}
