package com.oneness.fdxmerchant.Fragments.TabFragments.OrderTabs;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.ReportManagement.RestaurantPayouts;
import com.oneness.fdxmerchant.Adapters.DeliveredOrderAdapter;
import com.oneness.fdxmerchant.Adapters.NewOrdersAdapter;
import com.oneness.fdxmerchant.Models.OrderModels.DeliveredOrderModel;
import com.oneness.fdxmerchant.Models.OrderModels.DeliveredOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.NewOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.NewOrdersModel;
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

public class DeliveredOrdersFragment extends Fragment {
    RecyclerView delOrderRv;

    ApiManager manager =new ApiManager();
    DialogView dialogView;
    Prefs prefs;

    DeliveredOrderAdapter ordersAdapter;

    List<DeliveredOrderModel> delOrderList = new ArrayList<>();
    TextView noOrderDeliver, tvFrom, tvTo;
    RelativeLayout btnSearch;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();
    String start = "", end = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.delivered_orders_fragment, container, false);

        dialogView = new DialogView();
        prefs = new Prefs(getActivity());

        delOrderRv = v.findViewById(R.id.delOrderRv);
        //v.setNestedScrollingEnabled(false);
        noOrderDeliver = v.findViewById(R.id.noOrderDeliver);
        tvFrom = v.findViewById(R.id.tvFrom);
        tvTo = v.findViewById(R.id.tvTo);
        btnSearch = v.findViewById(R.id.btnSearch);

        getCurrentDate();



        //getDeliveredOrders(prefs.getData(Constants.REST_ID));

        tvFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date1, myCalendar
                        .get(android.icu.util.Calendar.YEAR), myCalendar.get(android.icu.util.Calendar.MONTH),
                        myCalendar.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.show();


            }
        });

        tvTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date2, myCalendar1
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

                getDeliveredOrders(prefs.getData(Constants.REST_ID), start, end);

            }
        });

        return v;
    }

    private void getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        Calendar day1 = Calendar.getInstance();
        day1.set(Calendar.DAY_OF_MONTH, 1);
        //System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        String formattedDate1 = df1.format(c);
        //String formattedDate1 = df.format(day1.getTime());
        //tvFrom.setText(formattedDate1);
        tvFrom.setText(formattedDate1);
        tvTo.setText(formattedDate1);
        start = formattedDate;
        end = formattedDate;

        /*DateWiseOrderReportRequestModel dateWiseOrderReportRequestModel = new DateWiseOrderReportRequestModel(
                tvFrom.getText().toString(),
                tvTo.getText().toString(),
                prefs.getData(Constants.REST_ID)
        );*/
        getDeliveredOrders(prefs.getData(Constants.REST_ID), start, end);
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
        String myFormat1 = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);

        editText.setText(sdf1.format(myCalendar.getTime()));
        start = sdf.format(myCalendar.getTime());

    }

    private void updateLabel1(TextView editText) {
        //   String myFormat = "yyyy-MM-dd"; //In which you need put here
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        String myFormat1 = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);

        editText.setText(sdf1.format(myCalendar1.getTime()));
        end = sdf.format(myCalendar1.getTime());

    }

    private void getDeliveredOrders(String id, String from, String to) {

       // dialogView.showCustomSpinProgress(getActivity());

        manager.service.getDeliveredOrders(id, from, to).enqueue(new Callback<DeliveredOrderResponseModel>() {
            @Override
            public void onResponse(Call<DeliveredOrderResponseModel> call, Response<DeliveredOrderResponseModel> response) {

                if (response.isSuccessful()){
                    DeliveredOrderResponseModel norm = response.body();

                    if (!norm.error){
                        //dialogView.dismissCustomSpinProgress();
                        delOrderList = norm.orders;
                        //dialogView.dismissCustomSpinProgress();

                        if (delOrderList.size() > 0){
                            noOrderDeliver.setVisibility(View.GONE);
                            delOrderRv.setVisibility(View.VISIBLE);
                            ordersAdapter = new DeliveredOrderAdapter(getActivity(), delOrderList);
                           // ViewCompat.setNestedScrollingEnabled(delOrderRv, false);
                            delOrderRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            delOrderRv.setAdapter(ordersAdapter);
                            //delOrderRv.setItemViewCacheSize(50);
                            //delOrderRv.hasFixedSize();
                           //delOrderRv.setDrawingCacheEnabled(true);

                        }else{

                            noOrderDeliver.setVisibility(View.VISIBLE);
                            delOrderRv.setVisibility(View.GONE);

                        }

                    }else{
                        //dialogView.dismissCustomSpinProgress();
                    }

                }else{
                   // dialogView.dismissCustomSpinProgress();
                }

            }

            @Override
            public void onFailure(Call<DeliveredOrderResponseModel> call, Throwable t) {

                //dialogView.dismissCustomSpinProgress();

            }
        });

    }
}
