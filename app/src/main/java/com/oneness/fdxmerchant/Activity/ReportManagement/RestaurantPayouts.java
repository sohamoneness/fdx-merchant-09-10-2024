package com.oneness.fdxmerchant.Activity.ReportManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneness.fdxmerchant.Adapters.PayoutsAdapter;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportRequestModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.PayoutDataModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.PayoutResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantPayouts extends AppCompatActivity {

    ImageView ivBack;
    RecyclerView payoutRv;
    Prefs prefs;
    ApiManager manager = new ApiManager();
    DialogView dialogView;
    List<PayoutDataModel> payoutList = new ArrayList<>();
    PayoutsAdapter pAdapter;

    TextView tvFrom, tvTo;
    RelativeLayout btnSearch;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_payouts);

        prefs = new Prefs(RestaurantPayouts.this);
        dialogView = new DialogView();

        ivBack = findViewById(R.id.ivBack);
        payoutRv = findViewById(R.id.payoutRv);
        tvFrom = findViewById(R.id.tvFrom);
        tvTo = findViewById(R.id.tvTo);
        btnSearch = findViewById(R.id.btnSearch);

        getCurrentDate();

        //getPayoutList(prefs.getData(Constants.REST_ID));

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RestaurantPayouts.this, date1, myCalendar
                        .get(android.icu.util.Calendar.YEAR), myCalendar.get(android.icu.util.Calendar.MONTH),
                        myCalendar.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.show();


            }
        });

        tvTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RestaurantPayouts.this, date2, myCalendar1
                        .get(android.icu.util.Calendar.YEAR), myCalendar1.get(android.icu.util.Calendar.MONTH),
                        myCalendar1.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*DateWiseOrderReportRequestModel dateWiseOrderReportRequestModel = new DateWiseOrderReportRequestModel(
                        tvFrom.getText().toString(),
                        tvTo.getText().toString(),
                        prefs.getData(Constants.REST_ID)
                );
                getOrderReport(dateWiseOrderReportRequestModel);*/

                getPayoutList(prefs.getData(Constants.REST_ID));

            }
        });


    }

    private void getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        Calendar day1 = Calendar.getInstance();
        day1.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        //String formattedDate1 = df.format(day1.getTime());
        //tvFrom.setText(formattedDate1);
        tvFrom.setText(formattedDate);
        tvTo.setText(formattedDate);

        /*DateWiseOrderReportRequestModel dateWiseOrderReportRequestModel = new DateWiseOrderReportRequestModel(
                tvFrom.getText().toString(),
                tvTo.getText().toString(),
                prefs.getData(Constants.REST_ID)
        );*/
        getPayoutList(prefs.getData(Constants.REST_ID));
    }

    final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(android.icu.util.Calendar.YEAR, year);
            myCalendar.set(android.icu.util.Calendar.MONTH, monthOfYear);
            myCalendar.set(android.icu.util.Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(tvFrom);
        }

    };

    final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar1.set(android.icu.util.Calendar.YEAR, year);
            myCalendar1.set(android.icu.util.Calendar.MONTH, monthOfYear);
            myCalendar1.set(android.icu.util.Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel1(tvTo);
        }

    };
    private void updateLabel(TextView editText) {
        //   String myFormat = "yyyy-MM-dd"; //In which you need put here
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));

    }

    private void updateLabel1(TextView editText) {
        //   String myFormat = "yyyy-MM-dd"; //In which you need put here
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar1.getTime()));

    }

    private void getPayoutList(String data) {
        dialogView.showCustomSpinProgress(RestaurantPayouts.this);
        manager.service.getPayouts(data).enqueue(new Callback<PayoutResponseModel>() {
            @Override
            public void onResponse(Call<PayoutResponseModel> call, Response<PayoutResponseModel> response) {
                if (response.isSuccessful()){
                    PayoutResponseModel prm = response.body();
                    if (!prm.error){
                        dialogView.dismissCustomSpinProgress();

                        payoutList = prm.data;

                        if (payoutList.size()>0){
                            pAdapter = new PayoutsAdapter(RestaurantPayouts.this, payoutList);
                            payoutRv.setLayoutManager(new LinearLayoutManager(RestaurantPayouts.this, LinearLayoutManager.VERTICAL, false));
                            payoutRv.setAdapter(pAdapter);
                        }

                    }else {

                    }
                }else {

                }
            }

            @Override
            public void onFailure(Call<PayoutResponseModel> call, Throwable t) {

            }
        });
    }
}