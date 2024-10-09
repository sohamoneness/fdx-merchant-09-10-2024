package com.oneness.fdxmerchant.Fragments.TabFragments.OrderTabs;

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

import com.oneness.fdxmerchant.Adapters.CanceledOrderAdapter;
import com.oneness.fdxmerchant.Adapters.NewOrdersAdapter;
import com.oneness.fdxmerchant.Models.OrderModels.CanceledOrderModel;
import com.oneness.fdxmerchant.Models.OrderModels.CanceledOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.NewOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.NewOrdersModel;
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

public class CanceledOrdersFragment extends Fragment {

    RecyclerView cancelOrderRv;

    ApiManager manager =new ApiManager();
    DialogView dialogView;
    Prefs prefs;

    CanceledOrderAdapter ordersAdapter;

    List<CanceledOrderModel> cancelOrderList = new ArrayList<>();
    TextView noOrderCancel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cancelled_orders_fragment, container, false);

        dialogView = new DialogView();
        prefs = new Prefs(getActivity());

        cancelOrderRv = v.findViewById(R.id.cancelOrderRv);
        noOrderCancel = v.findViewById(R.id.noOrderCancel);

        getCancelledOrder(prefs.getData(Constants.REST_ID));




        return v;
    }

    private void getCancelledOrder(String id) {
        //dialogView.showCustomSpinProgress(getActivity());
        manager.service.getCancelledOrders(id).enqueue(new Callback<CanceledOrderResponseModel>() {
            @Override
            public void onResponse(Call<CanceledOrderResponseModel> call, Response<CanceledOrderResponseModel> response) {

                if (response.isSuccessful()){

                    CanceledOrderResponseModel norm = response.body();

                    if (!norm.error){
                       // dialogView.dismissCustomSpinProgress();
                        cancelOrderList = norm.orders;

                        if (cancelOrderList.size() > 0){
                            noOrderCancel.setVisibility(View.GONE);
                            cancelOrderRv.setVisibility(View.VISIBLE);

                            ordersAdapter = new CanceledOrderAdapter(getActivity(), cancelOrderList);
                            cancelOrderRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            cancelOrderRv.setAdapter(ordersAdapter);

                        }else{
                            noOrderCancel.setVisibility(View.VISIBLE);
                            cancelOrderRv.setVisibility(View.GONE);

                        }

                    }else{
                       // dialogView.dismissCustomSpinProgress();

                    }

                }else{
                    //dialogView.dismissCustomSpinProgress();
                }

            }

            @Override
            public void onFailure(Call<CanceledOrderResponseModel> call, Throwable t) {

                //dialogView.dismissCustomSpinProgress();

            }
        });

    }
}
