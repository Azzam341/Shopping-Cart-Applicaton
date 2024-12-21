package com.example.testingwithfx;

class Address {
    private String address;
    private String town;
    private String city;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Address(String address, String town, String city) {
        this.address = address;
        this.town = town;
        this.city = city;
    }
}