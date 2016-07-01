package com.twopole.bean;

public class MyLocationBean {
    private int id;
    private double longitude;
    private double latitude;
    private String address;
    private double direction;
    private double radius;
    private double speed;
    private String road_name;
    private String road_voice;
    private boolean is_speak;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getRoad_name() {
        return road_name;
    }

    public void setRoad_name(String road_name) {
        this.road_name = road_name;
    }

    public String getRoad_voice() {
        return road_voice;
    }

    public void setRoad_voice(String road_voice) {
        this.road_voice = road_voice;
    }

    public boolean is_speak() {
        return is_speak;
    }

    public void setIs_speak(boolean is_speak) {
        this.is_speak = is_speak;
    }
}
