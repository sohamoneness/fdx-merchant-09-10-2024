package com.oneness.fdxmerchant.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oneness.fdxmerchant.Activity.ItemManagement.ItemListActivity;
import com.oneness.fdxmerchant.Models.ItemManagementModels.ItemStatusUpdateResponseModel;
import com.oneness.fdxmerchant.Models.ItemManagementModels.ItemsModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageItemMenuAdapter extends RecyclerView.Adapter<ManageItemMenuAdapter.Hold> {

    List<ItemsModel> cmList;
    Context context;
    ApiManager manager = new ApiManager();
    DialogView dialogView;
    String catId = "";
    String catName = "";


    public ManageItemMenuAdapter(ItemListActivity itemListActivity, List<ItemsModel> itemList, String catID, String catName) {
        this.context = itemListActivity;
        this.cmList = itemList;
        this.catId = catID;
        this.catName = catName;
    }


    @NonNull
    @Override
    public ManageItemMenuAdapter.Hold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_item_menu_row, parent, false);
        return new Hold(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageItemMenuAdapter.Hold holder, int position) {
        dialogView = new DialogView();
        ItemsModel cmm = cmList.get(position);
        holder.tvItemName.setText(cmm.name);
        //holder.tvExtraTxt.setText(cmm.description);
        if (cmm.description != null) {
            if (!cmm.description.equals("")) {
                holder.tvExtraTxt.setText(Html.fromHtml(cmm.description));
            }
        }
        
        holder.tvPrice.setText("₹ " + cmm.price);
        if (cmm.image != null) {
            if (cmm.image.equals("")) {
                Glide.with(context).load(R.drawable.no_image).into(holder.ivFood);
            } else {
                Glide.with(context).load(cmm.image).into(holder.ivFood);
            }
        } else {
            Glide.with(context).load(R.drawable.no_image).into(holder.ivFood);
        }

        if (cmm.in_stock.equals("0")) {
            holder.toggleStock.setChecked(false);
            holder.tvStockStat.setText("Stock Out");
        } else {
            holder.toggleStock.setChecked(true);
            holder.tvStockStat.setText("Stock In");
        }


        holder.toggleStock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("CHECK>>", "" + isChecked);
                if (isChecked) {
                   /* RestaurantStatusRequestModel restaurantStatusRequestModel = new RestaurantStatusRequestModel(
                            prefs.getData(Constants.REST_ID),
                            "0"

                    );
                    updateRestStat(restaurantStatusRequestModel, "on");*/
                    updateStatus(cmm.id, "1");
                    holder.tvStockStat.setText("Stock In");
                } else {
                    updateStatus(cmm.id, "0");
                    holder.tvStockStat.setText("Stock Out");
                    /*RestaurantStatusRequestModel restaurantStatusRequestModel = new RestaurantStatusRequestModel(
                            prefs.getData(Constants.REST_ID),
                            "1"

                    );
                    updateRestStat(restaurantStatusRequestModel, "off");*/
                }
            }
        });


    }

    private void updateStatus(String id, String s) {
        dialogView.showCustomSpinProgress(context);
        manager.service.itemStockUpdate(id, s).enqueue(new Callback<ItemStatusUpdateResponseModel>() {
            @Override
            public void onResponse(Call<ItemStatusUpdateResponseModel> call, Response<ItemStatusUpdateResponseModel> response) {
                if (response.isSuccessful()) {
                    ItemStatusUpdateResponseModel isurm = response.body();
                    if (!isurm.error) {
                        dialogView.dismissCustomSpinProgress();
                        ItemListActivity.catID = catId;
                        ItemListActivity.catName = catName;
                        ((Activity) context).startActivity(new Intent(((Activity) context), ItemListActivity.class));
                        ((Activity) context).overridePendingTransition(0, 0);
                        ((Activity) context).finish();
                        ((Activity) context).overridePendingTransition(0, 0);
                        Toast.makeText(context, isurm.message, Toast.LENGTH_SHORT).show();
                    } else {
                        dialogView.dismissCustomSpinProgress();
                        Toast.makeText(context, isurm.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialogView.dismissCustomSpinProgress();
                    Toast.makeText(context, "Something went wrong! Please try again!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ItemStatusUpdateResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
                Toast.makeText(context, "Network error! Please check your internet connection and try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cmList.size();
    }

   /* @Override
    public long getItemId(int position) {
        return position;
    }*/

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class Hold extends RecyclerView.ViewHolder {

        TextView tvItemName, tvPrice, tvExtraTxt;
        CircleImageView ivFood;
        Switch toggleStock;
        TextView tvStockStat;

        public Hold(@NonNull View itemView) {
            super(itemView);

            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvExtraTxt = itemView.findViewById(R.id.tvExtraTxt);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivFood = itemView.findViewById(R.id.ivFood);
            toggleStock = itemView.findViewById(R.id.toggleStock);
            tvStockStat = itemView.findViewById(R.id.tvStockStat);

        }
    }
}
