package com.oneness.fdxmerchant.Models.OrderModels;

public class DispatchOrderRequestModel {
    public String id = "";
    public String dispatch_otp = "";

    public DispatchOrderRequestModel(String id, String dispatch_otp) {
        this.id = id;
        this.dispatch_otp = dispatch_otp;
    }
}
