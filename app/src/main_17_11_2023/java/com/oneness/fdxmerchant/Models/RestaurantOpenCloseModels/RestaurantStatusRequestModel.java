package com.oneness.fdxmerchant.Models.RestaurantOpenCloseModels;

public class RestaurantStatusRequestModel {
    public String id = "";
    public String is_not_taking_orders = "";

    public RestaurantStatusRequestModel(String id, String is_not_taking_orders) {
        this.id = id;
        this.is_not_taking_orders = is_not_taking_orders;
    }
}
