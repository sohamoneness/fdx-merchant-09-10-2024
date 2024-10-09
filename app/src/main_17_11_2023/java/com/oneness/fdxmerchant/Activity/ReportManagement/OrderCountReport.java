package com.oneness.fdxmerchant.Activity.ReportManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.oneness.fdxmerchant.Adapters.OrderCountAdapter;
import com.oneness.fdxmerchant.Models.ReportManagementModels.OrderCountDataModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.OrderCountResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderCountReport extends AppCompatActivity {

    BarChart chart;
    BarData barData;
    BarDataSet barDataSet;

    ArrayList barEntriesArrayList;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    Prefs prefs;
    List<OrderCountDataModel> orderCountList = new ArrayList<>();
    List<String> monthList = new ArrayList<>();
    List<BarEntry> valueList = new ArrayList<BarEntry>();
    OrderCountAdapter ocAdapter;
    RecyclerView orderCountRV;
    ImageView ivBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_count_report);

        prefs = new Prefs(OrderCountReport.this);
        dialogView = new DialogView();
        orderCountRV = findViewById(R.id.orderCountRV);
        orderCountRV = findViewById(R.id.orderCountRV);
        ivBack = findViewById(R.id.ivBack);
        //chart = findViewById(R.id.chart);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        //barDataSet = new BarDataSet()
       /* BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        String text  = "Bar Chart";
        Description description = new Description();
        description.setText("BarChart");
        chart.setDescription(description);
        chart.animateXY(2000, 2000);
        chart.invalidate();*/
        getBarEntries(prefs.getData(Constants.REST_ID));

    }

   /* private ArrayList getDataSet() {
        ArrayList dataSets = null;

        ArrayList valueSet1 = new ArrayList();
        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);

        ArrayList valueSet2 = new ArrayList();
        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList getXAxisValues() {
        ArrayList xAxis = new ArrayList();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        return xAxis;
    }*/


    private void getBarEntries(String data) {
        dialogView.showCustomSpinProgress(OrderCountReport.this);
        manager.service.getOrderCount(data).enqueue(new Callback<OrderCountResponseModel>() {
            @Override
            public void onResponse(Call<OrderCountResponseModel> call, Response<OrderCountResponseModel> response) {
                if (response.isSuccessful()){
                    OrderCountResponseModel ocrm = response.body();
                    if (!ocrm.error){
                        dialogView.dismissCustomSpinProgress();
                        orderCountList = ocrm.data;

                        if (orderCountList.size() > 0){
                            ocAdapter = new OrderCountAdapter(OrderCountReport.this, orderCountList);
                            orderCountRV.setLayoutManager(new LinearLayoutManager(OrderCountReport.this, LinearLayoutManager.VERTICAL, false));
                            orderCountRV.setAdapter(ocAdapter);
                        }

                    }else {
                        dialogView.dismissCustomSpinProgress();
                    }
                }else{
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<OrderCountResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
            }
        });

    }
}