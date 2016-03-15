package com.example.woekun.toeiconline.models;

public class User {
    private String email;
    private String name;
    private String avatar;
    private String level;
    private String address;
    private String phone;

    public User() {
    }

    // user upgrade level
    public User(String email, String level) {
        createUser(email, null, null, level, null, null);
    }

    public User(String email, String name, String avatar, String level,
                String address, String phone) {
        createUser(email, name, avatar, level, address, phone);
    }

    private void createUser(String email, String name, String avatar, String level, String address, String phone) {
        this.email = email;
        this.name = name;
        this.avatar = avatar;
        this.level = level;
        this.address = address;
        this.phone = phone;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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
}
