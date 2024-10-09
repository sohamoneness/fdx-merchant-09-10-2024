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

import com.oneness.fdxmerchant.Adapters.NewOrdersAdapter;
import com.oneness.fdxmerchant.Adapters.OrdersAdapter;
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


public class NewOrdersFragment extends Fragment {
    RecyclerView newOrderRv;
    TextView noOrderNew;

    ApiManager manager =new ApiManager();
    DialogView dialogView;
    Prefs prefs;

    NewOrdersAdapter ordersAdapter;

    List<NewOrdersModel> newOrderList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_orders_fragment, container, false);

        dialogView = new DialogView();
        prefs = new Prefs(getActivity());

        newOrderRv = v.findViewById(R.id.newOrderRv);
        noOrderNew = v.findViewById(R.id.noOrderNew);

        getNewOrders(prefs.getData(Constants.REST_ID));

        return v;
    }

    private void getNewOrders(String id) {

        dialogView.showCustomSpinProgress(getActivity());

        manager.service.getNewOrders(id).enqueue(new Callback<NewOrderResponseModel>() {
            @Override
            public void onResponse(Call<NewOrderResponseModel> call, Response<NewOrderResponseModel> response) {

                if (response.isSuccessful()){
                    NewOrderResponseModel norm = response.body();
                    if (!norm.error){
                        dialogView.dismissCustomSpinProgress();
                        newOrderList = norm.orders;


                        if (newOrderList.size() > 0){
                            noOrderNew.setVisibility(View.GONE);
                            newOrderRv.setVisibility(View.VISIBLE);
                            ordersAdapter = new NewOrdersAdapter(getActivity(), newOrderList);
                            newOrderRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                            newOrderRv.setAdapter(ordersAdapter);

                        }else{

                            noOrderNew.setVisibility(View.VISIBLE);
                            newOrderRv.setVisibility(View.GONE);

                        }

                    }else{
                        dialogView.dismissCustomSpinProgress();

                    }

                }else{
                    dialogView.dismissCustomSpinProgress();
                }

            }

            @Override
            public void onFailure(Call<NewOrderResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }

}
