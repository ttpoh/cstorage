package com.nova.cstorage.map;

public class MapPlaceData {
    String placeName;
    String category;
    String address;
    String phone;
    String url;
    String x, y;

    public MapPlaceData(String mapPlace, String mapPhone) {
        this.placeName = mapPlace;
        this.category = category;
        this.placeName = mapPlace;
        this.phone = mapPhone;
    }

    public MapPlaceData() {
//        this.placeName = mapPlace;
//        this.phone = mapPhone;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getY() {
        return y;
    }

    public void sety(String y) {
        this.y = y;
    }

    public String getX() {
        return x;
    }

    public void setx(String x) {
        this.x = x;
    }
}