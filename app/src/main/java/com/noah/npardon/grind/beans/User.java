package com.noah.npardon.grind.beans;

public class User {
    private String username;
    private String password;
    private String first_name;
    private String email;
    private String token;

    public User(String username, String password, String first_name, String email) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.email = email;
    }

    public User(String username, String first_name, String email) {
        this.username = username;
        this.first_name = first_name;
        this.email = email;
    }




    public String getUsername() {
        return username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
