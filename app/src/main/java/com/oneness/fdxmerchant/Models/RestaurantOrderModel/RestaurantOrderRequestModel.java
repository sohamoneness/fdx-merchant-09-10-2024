package com.oneness.fdxmerchant.Models.RestaurantOrderModel;

public class RestaurantOrderRequestModel {
    public String restaurant_id = "";
    public String pickup_location = "";
    public String pickup_lat = "";
    public String pikcup_lng = "";
    public String drop_location = "";
    public String drop_address = "";
    public String drop_lat = "";
    public String drop_lng = "";
    public String name = "";
    public String mobile = "";
    public String item_price = "";
    public String payment_type = "";

    public RestaurantOrderRequestModel(String restaurant_id, String pickup_location, String pickup_lat, String pikcup_lng,
                                       String drop_location, String drop_lat, String drop_lng, String drop_address,
                                       String name, String mobile, String item_price, String payment_type){

        this.restaurant_id = restaurant_id;
        this.pickup_location = pickup_location;
        this.pickup_lat = pickup_lat;
        this.pikcup_lng = pikcup_lng;
        this.drop_location = drop_location;
        this.drop_lat = drop_lat;
        this.drop_lng = drop_lng;
        this.drop_address = drop_address;
        this.name = name;
        this.mobile = mobile;
        this.item_price = item_price;
        this.payment_type = payment_type;
    }
}
