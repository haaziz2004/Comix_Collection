package com.comix.api.comixapi.requestbody;

public class CreateUserRequestBody {
    private String username;
    private String password;

    public CreateUserRequestBody() {
    }

    public CreateUserRequestBody(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassowrd(String password) {
        this.password = password;
    }
}