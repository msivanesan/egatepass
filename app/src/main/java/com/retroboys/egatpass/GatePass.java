package com.retroboys.egatpass;

public class GatePass {
    private String id;
    private String userName;
    private String name;
    private String messsage;
    private String hostel;
    private String email;
    private String requestDate;
    private String requestTime;
    private String aproveTime;
    private String outTime;

    private String remark;
    private String code;
    private String phNumber;
    private String status;
    public GatePass() {
    }

    public GatePass(String name,String requestDate) {
        this.requestDate = requestDate;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public GatePass(String id, String userName, String name, String messsage, String hostel, String email, String requestDate, String requestTime, String status, String phNumber) {
        this.userName=userName;
        this.id=id;
        this.name = name;
        this.messsage = messsage;
        this.hostel = hostel;
        this.email = email;
        this.requestDate = requestDate;
        this.requestTime=requestTime;
        this.status = status;
        this.phNumber = phNumber;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMesssage() {
        return messsage;
    }

    public void setMesssage(String messsage) {
        this.messsage = messsage;
    }

    public String getHostel() {
        return hostel;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getAproveTime() {
        return aproveTime;
    }

    public void setAproveTime(String aproveTime) {
        this.aproveTime = aproveTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
