package com.oneness.fdxmerchant.Models.RestaurantDataModels;

public class ChangePasswordRequestModel {

    public String restaurant_id = "";
    public String old_password = "";
    public String password = "";

    public ChangePasswordRequestModel(String restaurant_id, String old_password, String password) {
        this.restaurant_id = restaurant_id;
        this.old_password = old_password;
        this.password = password;
    }
}
