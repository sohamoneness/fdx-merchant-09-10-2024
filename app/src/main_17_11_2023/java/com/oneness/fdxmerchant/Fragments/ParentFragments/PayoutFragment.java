package com.oneness.fdxmerchant.Fragments.ParentFragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Activity.ReportManagement.RestaurantPayouts;
import com.oneness.fdxmerchant.Adapters.PayoutsAdapter;
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

public class PayoutFragment extends Fragment {

    ImageView ivBack;
    RecyclerView payoutRv;
    Prefs prefs;
    ApiManager manager = new ApiManager();
    DialogView dialogView;
    List<PayoutDataModel> payoutList = new ArrayList<>();
    PayoutsAdapter pAdapter;

    TextView tvFrom, tvTo, tvNoPayout;
    RelativeLayout btnSearch;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar1 = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.payout_fragment, container, false);
        prefs = new Prefs(getActivity());
        dialogView = new DialogView();

        //ivBack = v.findViewById(R.id.ivBack);
        payoutRv = v.findViewById(R.id.payoutRv);
        tvFrom = v.findViewById(R.id.tvFrom);
        tvTo = v.findViewById(R.id.tvTo);
        btnSearch = v.findViewById(R.id.btnSearch);
        tvNoPayout = v.findViewById(R.id.tvNoPayout);

        getCurrentDate();


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

                getPayoutList(prefs.getData(Constants.REST_ID));

            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                startActivity(new Intent(getActivity(), Dashboard.class));
                getActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        return v;
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
        dialogView.showCustomSpinProgress(getActivity());
        manager.service.getPayouts(data).enqueue(new Callback<PayoutResponseModel>() {
            @Override
            public void onResponse(Call<PayoutResponseModel> call, Response<PayoutResponseModel> response) {
                if (response.isSuccessful()){
                    PayoutResponseModel prm = response.body();
                    if (!prm.error){
                        dialogView.dismissCustomSpinProgress();

                        payoutList = prm.data;

                        if (payoutList.size()>0){
                            tvNoPayout.setVisibility(View.GONE);
                            payoutRv.setVisibility(View.VISIBLE);
                            pAdapter = new PayoutsAdapter(getActivity(), payoutList);
                            payoutRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            payoutRv.setAdapter(pAdapter);
                        }else {
                            tvNoPayout.setVisibility(View.VISIBLE);
                            payoutRv.setVisibility(View.GONE);
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
