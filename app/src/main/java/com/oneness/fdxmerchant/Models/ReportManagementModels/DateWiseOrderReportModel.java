package com.oneness.fdxmerchant.Models.ReportManagementModels;

import java.util.ArrayList;
import java.util.List;

public class DateWiseOrderReportModel {
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
    public String restaurant_commission = "";
    public String discounted_amount = "";
    public String delivery_charge = "";
    public String packing_price = "";
    public String tax_amount = "";
    public String total_amount = "";
    public String status = "";
    public String preparation_time = "";
    public String cancellation_reason = "";
    public String transaction_id = "";
    public String payment_status = "";
    public String is_deleted = "";
    public String created_at = "";
    public String updated_at = "";
    public String total_item_count = "";
    public String boy_name = "";
    public String restaurant_share_commission = "";
    public String cutlery_charge = "";
    public List<DateWiseOrderItemModel> items = new ArrayList<>();

}
