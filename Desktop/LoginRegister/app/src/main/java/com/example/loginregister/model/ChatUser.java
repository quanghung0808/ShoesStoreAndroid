package com.example.loginregister.model;

import java.io.Serializable;

public class ChatUser implements Serializable {
    private String email;
    private String name;
    private String image;

    public ChatUser() {
    }

    public ChatUser(String email, String name, String image) {
        this.email = email;
        this.name = name;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ChatUser{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
