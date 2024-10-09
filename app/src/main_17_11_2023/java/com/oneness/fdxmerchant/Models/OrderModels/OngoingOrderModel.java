package com.oneness.fdxmerchant.Models.OrderModels;

import com.oneness.fdxmerchant.Models.RestaurantDataModels.RestaurantDataModel;

import java.util.ArrayList;
import java.util.List;

public class OngoingOrderModel {

    public String id = "";
    public String unique_id = "";
    public String restaurant_id = "";
    public String delivery_boy_id = "";
    public String user_id = "";
    public String name = "";
    public String mobile = "";
    public String email = "";
    public String delivery_address = "";
    public String delivery_landmark = "";
    public String delivery_country = "";
    public String delivery_city = "";
    public String delivery_pin = "";
    public String delivery_lat = "";
    public String delivery_lng = "";
    public String amount = "";
    public String coupon_code = "";
    public String discounted_amount = "";
    public String delivery_charge = "";
    public String packing_price = "";
    public String cutlery_charge = "";
    public String tax_amount = "";
    public String total_amount = "";
    public String restaurant_commission = "";
    public String status = "";
    public String preparation_time = "";
    public String cancellation_reason = "";
    public String transaction_id = "";
    public String payment_status = "";
    public String is_deleted = "";
    public String created_at = "";
    public String updated_at = "";
    public String notes = "";
    public String time_diff = "";
    public String is_ready = "";
    public String is_dispatched = "";
    public String dispatch_otp = "";
    public String order_type = "";
    public String restaurant_share_commission = "";

    public RestaurantDataModel restaurant;
    public BoyModel boy;
    public List<OrderItemsModel> items = new ArrayList<>();

}
