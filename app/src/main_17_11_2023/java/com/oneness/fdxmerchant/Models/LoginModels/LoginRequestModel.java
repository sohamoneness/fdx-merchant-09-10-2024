package com.oneness.fdxmerchant.Models.LoginModels;

public class LoginRequestModel {
    public String mobile = "";
    public String password = "";
    public String device_token = "";

    public LoginRequestModel(String mobile, String password, String device_token) {
        this.mobile = mobile;
        this.password = password;
        this.device_token = device_token;
    }
}
