package com.oneness.fdxmerchant.Activity.Order.NewOrderModule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
import com.oneness.fdxmerchant.Activity.EntryPoint.EmailLogin;
import com.oneness.fdxmerchant.Models.LoginModels.LoginRequestModel;
import com.oneness.fdxmerchant.Models.LoginModels.LoginResponseModel;
import com.oneness.fdxmerchant.Models.RestaurantDataModels.RestaurantDataModel;
import com.oneness.fdxmerchant.Models.RestaurantOrderModel.RestaurantOrderRequestModel;
import com.oneness.fdxmerchant.Models.RestaurantOrderModel.RestaurantOrderResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOrderByMerchant extends AppCompatActivity {
    EditText etName, etContact, etAdr, etAmount, etAdr2;
    RadioGroup rgPay;
    RadioButton rbCod, rbOnline;
    Button btnCreate;
    RadioButton rb;
    String payType = "";
    String lat = "", lng = "", pin = "", city = "";
    ImageView ivBack;

    DialogView dialogView;
    Prefs prefs;
    ApiManager manager = new ApiManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order_by_merchant);

        dialogView = new DialogView();
        prefs = new Prefs(AddOrderByMerchant.this);

        etName = findViewById(R.id.etName);
        etContact = findViewById(R.id.etContact);
        etAdr = findViewById(R.id.etAdr);
        etAdr2= findViewById(R.id.etAdr2);
        etAmount = findViewById(R.id.etAmount);
        rgPay = findViewById(R.id.rgPay);
        rbCod = findViewById(R.id.rbCod);
        rbOnline = findViewById(R.id.rbOnline);
        btnCreate = findViewById(R.id.btnCreate);
        ivBack = findViewById(R.id.ivBack);

        Toast.makeText(this, getIntent().getStringExtra("SELECT_ADDRESS"), Toast.LENGTH_SHORT).show();
        lat = getIntent().getStringExtra("ADR_LAT");
        lng = getIntent().getStringExtra("ADR_LON");

        etAdr.setText(getIntent().getStringExtra("SELECT_ADDRESS"));

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBlankValidate()){
                    RestaurantOrderRequestModel restaurantOrderRequestModel = new RestaurantOrderRequestModel(
                            prefs.getData(Constants.REST_ID),
                            prefs.getData(Constants.REST_LOC),
                            prefs.getData(Constants.REST_LAT),
                            prefs.getData(Constants.REST_LNG),
                            etAdr.getText().toString(),
                            lat,
                            lng,
                            etAdr2.getText().toString(),
                            etName.getText().toString(),
                            etContact.getText().toString(),
                            etAmount.getText().toString(),
                            payType
                    );

                    callCreateOrderTask(restaurantOrderRequestModel);
                }
            }
        });

        rgPay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = group.findViewById(checkedId);
                payType = rb.getText().toString();
                Toast.makeText(AddOrderByMerchant.this, payType, Toast.LENGTH_SHORT).show();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void createOrder(String name, String contact, String adr, String amount, String payType) {
        Toast.makeText(this, "NEED TO CALL API HERE TO PLACE THE ORDER!!!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean isBlankValidate() {
        if (etName.getText().toString().equals("")){
            Toast.makeText(this, "Please enter customer name!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (etContact.getText().toString().equals("")){
            Toast.makeText(this, "Please enter customer contact!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (etAdr.getText().toString().equals("")){
            Toast.makeText(this, "Please enter customer address!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (etAmount.getText().toString().equals("")){
            Toast.makeText(this, "Please enter order amount!", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }

    }

    private void callCreateOrderTask(RestaurantOrderRequestModel restaurantOrderRequestModel) {

        dialogView.showCustomSpinProgress(AddOrderByMerchant.this);

        manager.service.restaurantOrderCreate(restaurantOrderRequestModel).enqueue(new Callback<RestaurantOrderResponseModel>() {
            @Override
            public void onResponse(Call<RestaurantOrderResponseModel> call, Response<RestaurantOrderResponseModel> response) {
                if (response.isSuccessful()){
                    dialogView.dismissCustomSpinProgress();
                    RestaurantOrderResponseModel restaurantOrderResponseModel = response.body();
                    if (!restaurantOrderResponseModel.error){
                        Toast.makeText(AddOrderByMerchant.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }else{
                        etAdr.setText("");
                        etAdr2.setText("");
                        etName.setText("");
                        etContact.setText("");
                        etAmount.setText("");
                        dialogView.showSingleButtonDialog(AddOrderByMerchant.this, getResources().getString(R.string.app_name), "Your request has been submitted successfully");
                    }
                }else{
                    dialogView.dismissCustomSpinProgress();
                    Toast.makeText(AddOrderByMerchant.this, "error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestaurantOrderResponseModel> call, Throwable t) {
                dialogView.dismissCustomSpinProgress();
                Toast.makeText(AddOrderByMerchant.this, "Unable to connect to the server! Please make sure your internet connection is on and try again!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}