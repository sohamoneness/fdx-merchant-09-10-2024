package com.oneness.fdxmerchant.Fragments.TabFragments.ItemReportFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oneness.fdxmerchant.Activity.ReportManagement.DateWiseOrderReport;
import com.oneness.fdxmerchant.Adapters.ItemReportAdapter;
import com.oneness.fdxmerchant.Models.ReportManagementModels.DateWiseOrderReportRequestModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.ItemReportModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.ItemReportRequestModel;
import com.oneness.fdxmerchant.Models.ReportManagementModels.ItemReportResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

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

public class ItemReportTodayFragment extends Fragment {

    Prefs prefs;
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    TextView tvToday, tvNoOrder, tvRevenue, tvTotItem, tvTotItemOrdered;
    List<ItemReportModel> itemReportList = new ArrayList<>();
    List<ItemReportModel> itemReportShortList = new ArrayList<>();
    ItemReportAdapter itemReportAdapter;
    RecyclerView itemReportRv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_report_today_fragment, container, false);

        prefs = new Prefs(getActivity());
        dialogView = new DialogView();

        tvToday = v.findViewById(R.id.tvToday);
        itemReportRv = v.findViewById(R.id.itemReportRv);
        tvNoOrder = v.findViewById(R.id.tvNoOrder);
        tvRevenue = v.findViewById(R.id.tvRevenue);
        tvTotItem = v.findViewById(R.id.tvTotItem);
        tvTotItemOrdered = v.findViewById(R.id.tvTotItemOrdered);

        getCurrentDate();

        return v;
    }



    private void getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat df1 = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        String formattedDate1 = df1.format(c);
        tvToday.setText(formattedDate1);
        /*tvTo.setText(formattedDate);*/

        ItemReportRequestModel itemReportRequestModel = new ItemReportRequestModel(
                formattedDate,
                formattedDate,
                prefs.getData(Constants.REST_ID)
        );
        getItemReport(itemReportRequestModel);
    }

    private void getItemReport(ItemReportRequestModel itemReportRequestModel) {
        //dialogView.showCustomSpinProgress(getActivity());
        manager.service.getItemWiseReport(itemReportRequestModel).enqueue(new Callback<ItemReportResponseModel>() {
            @Override
            public void onResponse(Call<ItemReportResponseModel> call, Response<ItemReportResponseModel> response) {
                if (response.isSuccessful()) {
                    // dialogView.dismissCustomSpinProgress();
                    ItemReportResponseModel itemReportResponseModel = response.body();
                    if (!itemReportResponseModel.error) {

                        DecimalFormat formatter1 = new DecimalFormat("#,##,###.00");
                        String formatted = formatter1.format(Double.parseDouble(itemReportResponseModel.total_order_amount));

                        if (formatted.equals(".00")){
                            formatted = "0.00";
                        }

                        tvRevenue.setText("\u20B9 " + formatted);
                        tvTotItem.setText(itemReportResponseModel.total_items);
                        tvTotItemOrdered.setText(itemReportResponseModel.total_item_count);

                        itemReportList = itemReportResponseModel.items;
                        for (int i = 0; i < itemReportList.size(); i++){
                            if (!itemReportList.get(i).total_order_count.equals("0")){
                                ItemReportModel itemReportModel = itemReportList.get(i);
                                itemReportShortList.add(itemReportModel);
                            }

                        }

                        if (itemReportShortList.size() > 0) {
                            tvNoOrder.setVisibility(View.GONE);
                            itemReportRv.setVisibility(View.VISIBLE);
                            itemReportAdapter = new ItemReportAdapter(getActivity(), itemReportShortList);
                            itemReportRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            itemReportRv.setHasFixedSize(true);
                            itemReportRv.setAdapter(itemReportAdapter);

                            /*itemReportRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                    if (!recyclerView.canScrollVertically(1))
                                        onScrolledToBottom();
                                }
                            });*/

                        } else {
                            tvNoOrder.setVisibility(View.VISIBLE);
                            itemReportRv.setVisibility(View.GONE);
                        }

                    } else {

                    }

                } else {
                    // dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<ItemReportResponseModel> call, Throwable t) {
                // dialogView.dismissCustomSpinProgress();
            }
        });
    }

    private void onScrolledToBottom() {
        if (itemReportShortList.size() < itemReportList.size()) {
            int x, y;
            if ((itemReportList.size() - itemReportShortList.size()) >= 50) {
                x = itemReportShortList.size();
                y = x + 50;
            } else {
                x = itemReportShortList.size();
                y = x + itemReportList.size() - itemReportShortList.size();
            }
            for (int i = x; i < y; i++) {
                itemReportShortList.add(itemReportList.get(i));
            }
            itemReportAdapter.notifyDataSetChanged();
        }
    }

}
