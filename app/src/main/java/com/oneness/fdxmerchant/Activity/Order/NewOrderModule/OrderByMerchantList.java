package com.oneness.fdxmerchant.Activity.Order.NewOrderModule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.oneness.fdxmerchant.Activity.Order.OrderActivity;
import com.oneness.fdxmerchant.Adapters.MerchantOrderListAdapter;
import com.oneness.fdxmerchant.R;

import java.util.ArrayList;
import java.util.List;

public class OrderByMerchantList extends AppCompatActivity {

    RelativeLayout createOrderRL;
    MerchantOrderListAdapter merchantOrderListAdapter;
    RecyclerView orderListRV;
    List<String> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_by_merchant_list);

        createOrderRL = findViewById(R.id.createOrderRL);
        orderListRV = findViewById(R.id.orderListRV);

        createOrderRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderByMerchantList.this, BillingAddressSelectorActivity.class));
            }
        });

        getOrderList();

    }

    private void getOrderList() {
        merchantOrderListAdapter = new MerchantOrderListAdapter(OrderByMerchantList.this, orderList);
        orderListRV.setLayoutManager(new LinearLayoutManager(OrderByMerchantList.this, LinearLayoutManager.VERTICAL, false));
        orderListRV.setAdapter(merchantOrderListAdapter);
    }
}