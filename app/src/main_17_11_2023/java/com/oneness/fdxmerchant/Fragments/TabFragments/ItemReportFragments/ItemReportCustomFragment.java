package com.oneness.fdxmerchant.Fragments.TabFragments.ItemReportFragments;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Adapters.ItemReportAdapter;
import com.oneness.fdxmerchant.Adapters.ItemReportCustomAdapter;
import com.oneness.fdxmerchant.Models.ReportManagementModels.ItemReportModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.ItemReportRequestModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.ItemReportResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemReportCustomFragment extends Fragment {

    TextView tvFrom, tvTo, tvNoOrder, tvRevenue, tvTotItem, tvTotItemOrdered;
    Prefs prefs;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    Calendar myCalendar1 = Calendar.getInstance();
    Calendar myCalendar = Calendar.getInstance();

    RelativeLayout btnSearch;
    RecyclerView itemCustomReportRv;
    ItemReportCustomAdapter itemReportAdapter;
    List<ItemReportModel> itemReportList = new ArrayList<>();
    List<ItemReportModel> itemReportShortList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_report_custom_fragment, container, false);

        prefs = new Prefs(getActivity());
        dialogView = new DialogView();

        tvFrom = v.findViewById(R.id.tvCustFrom);
        tvTo = v.findViewById(R.id.tvCustTo);
        btnSearch = v.findViewById(R.id.btnCustSearch);
        itemCustomReportRv = v.findViewById(R.id.itemCustomReportRv);
        tvNoOrder = v.findViewById(R.id.tvNoOrder);
        tvRevenue = v.findViewById(R.id.tvRevenue);
        tvTotItem = v.findViewById(R.id.tvTotItem);
        tvTotItemOrdered = v.findViewById(R.id.tvTotItemOrdered);

        getCurrentDate();

        tvFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date1, myCalendar
                        .get(android.icu.util.Calendar.YEAR), myCalendar.get(android.icu.util.Calendar.MONTH),
                        myCalendar.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();

            }
        });

        tvTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date2, myCalendar1
                        .get(android.icu.util.Calendar.YEAR), myCalendar1.get(android.icu.util.Calendar.MONTH),
                        myCalendar1.get(android.icu.util.Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemReportRequestModel itemReportRequestModel = new ItemReportRequestModel(
                        tvFrom.getText().toString(),
                        tvTo.getText().toString(),
                        prefs.getData(Constants.REST_ID)
                );
                getItemReport(itemReportRequestModel);

            }
        });

        return v;
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

    private void getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        Calendar day1 = Calendar.getInstance();
        day1.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(c);
        String formattedDate1 = df.format(day1.getTime());
        tvFrom.setText(formattedDate1);
        tvTo.setText(formattedDate);

        ItemReportRequestModel itemReportRequestModel = new ItemReportRequestModel(
                tvFrom.getText().toString(),
                tvTo.getText().toString(),
                prefs.getData(Constants.REST_ID)
        );
        getItemReport(itemReportRequestModel);
    }

    private void getItemReport(ItemReportRequestModel itemReportRequestModel) {
        dialogView.showCustomSpinProgress(getActivity());
        manager.service.getItemWiseReport(itemReportRequestModel).enqueue(new Callback<ItemReportResponseModel>() {
            @Override
            public void onResponse(Call<ItemReportResponseModel> call, Response<ItemReportResponseModel> response) {
                if (response.isSuccessful()){

                    ItemReportResponseModel itemReportResponseModel = response.body();
                    if (!itemReportResponseModel.error){
                         dialogView.dismissCustomSpinProgress();

                        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                        String formatted = formatter1.format(Double.parseDouble(itemReportResponseModel.total_order_amount));

                        if (formatted.equals(".00")){
                            formatted = "0.00";
                        }

                        tvRevenue.setText("\u20B9 " + formatted);
                        tvTotItem.setText(itemReportResponseModel.total_items);
                        tvTotItemOrdered.setText(itemReportResponseModel.total_item_count);
                        itemReportShortList = new ArrayList<>();
                        itemReportList = itemReportResponseModel.items;

                        for (int i = 0; i < itemReportList.size(); i++){
                            if (!itemReportList.get(i).total_order_count.equals("0")){
                                ItemReportModel itemReportModel = itemReportList.get(i);
                                itemReportShortList.add(itemReportModel);
                            }

                        }

                        if (itemReportShortList.size()>0){
                            tvNoOrder.setVisibility(View.GONE);
                            itemCustomReportRv.setVisibility(View.VISIBLE);
                            itemReportAdapter = new ItemReportCustomAdapter(getActivity(), itemReportShortList);
                            itemCustomReportRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            itemCustomReportRv.setHasFixedSize(true);
                            itemCustomReportRv.setAdapter(itemReportAdapter);
                        }else {
                            tvNoOrder.setVisibility(View.VISIBLE);
                            itemCustomReportRv.setVisibility(View.GONE);
                        }

                    }else{

                    }

                }else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<ItemReportResponseModel> call, Throwable t) {
                 dialogView.dismissCustomSpinProgress();
            }
        });
    }

}
