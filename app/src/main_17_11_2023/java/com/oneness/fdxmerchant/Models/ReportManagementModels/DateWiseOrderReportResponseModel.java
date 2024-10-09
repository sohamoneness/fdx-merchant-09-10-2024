package com.oneness.fdxmerchant.Models.ReportManagementModels;

import java.util.ArrayList;
import java.util.List;

public class DateWiseOrderReportResponseModel {
    public boolean error = false;
    public String message = "";
    public String total_order_count = "";
    public String total_order_amount = "";
    public List<DateWiseOrderReportModel> orders = new ArrayList<>();
    public String delivered_order_count = "";
    public String cancelled_order_count = "";
}
