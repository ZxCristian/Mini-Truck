package com.example.addtotruck;

public class ShippingAdd {
    String fullname;
    String phonenum;
    String RDC;
    String street;
    String landmark;

    ShippingAdd(){

    }

    public ShippingAdd(String fullname, String phonenum, String RDC, String street, String landmark) {
        this.fullname = fullname;
        this.phonenum = phonenum;
        this.RDC = RDC;
        this.street = street;
        this.landmark = landmark;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getRDC() {
        return RDC;
    }

    public void setRDC(String RDC) {
        this.RDC = RDC;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}
