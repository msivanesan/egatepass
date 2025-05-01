package com.retroboys.egatpass;

public class Users {
    public Users() {
    }

    private String userName;
    private String name;
    private String email;
    private String roll;
    private String hostel;

    private String phNumber;
    private String password;

    public Users(String userName, String name, String email, String roll, String hostel,  String phNumber, String password) {
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.roll = roll;
        this.hostel = hostel;

        this.phNumber = phNumber;
        this.password = password;
    }

    public Users(String userName, String name, String hostel) {
        this.userName = userName;
        this.name = name;
        this.hostel = hostel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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



    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
