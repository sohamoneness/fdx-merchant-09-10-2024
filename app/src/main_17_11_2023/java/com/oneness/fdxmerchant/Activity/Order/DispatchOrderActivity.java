package com.oneness.fdxmerchant.Activity.Order;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.oneness.fdxmerchant.Activity.EntryPoint.Dashboard;
//import com.oneness.fdxmerchant.Models.LoginModels.LoginRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.DispatchOrderRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.DispatchOrderResponseModel;
import com.oneness.fdxmerchant.Models.OrderModels.ReadyOrderRequestModel;
import com.oneness.fdxmerchant.Models.OrderModels.ReadyOrderResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DispatchOrderActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText etNum1, etNum2, etNum4;
    private Button btnSubmit;
    private ImageView ivBack;
    private String otp = "";
    DialogView dialogView;
    ApiManager manager = new ApiManager();
    String TAG = "DispatchOrderActivity";
    public static String order_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch_order);

        dialogView = new DialogView();

        etNum1 = (EditText) findViewById(R.id.etNum1);
        etNum2 = (EditText) findViewById(R.id.etNum2);
        etNum4 = (EditText) findViewById(R.id.etNum4);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        ivBack = (ImageView) findViewById(R.id.ivBack);

        etNum1.addTextChangedListener(new GenericTextWatcher(etNum1));
        etNum2.addTextChangedListener(new GenericTextWatcher(etNum2));
        etNum4.addTextChangedListener(new GenericTextWatcher(etNum4));


        btnSubmit.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.btnSubmit:
                otp = etNum1.getText().toString()+etNum2.getText().toString()+etNum4.getText().toString();

                DispatchOrderRequestModel dispatchOrderRequestModel = new DispatchOrderRequestModel(
                        order_id,
                        otp
                );
                dispatchOrder(dispatchOrderRequestModel);

                break;
        }
    }

    private void dispatchOrder(DispatchOrderRequestModel dispatchOrderRequestModel) {

        dialogView.showCustomSpinProgress(DispatchOrderActivity.this);

        manager.service.dispatchOrder(dispatchOrderRequestModel).enqueue(new Callback<DispatchOrderResponseModel>() {
            @Override
            public void onResponse(Call<DispatchOrderResponseModel> call, Response<DispatchOrderResponseModel> response) {
                if (response.isSuccessful()) {
                    dialogView.dismissCustomSpinProgress();
                    DispatchOrderResponseModel dispatchOrderResponseModel = response.body();

                    if (!dispatchOrderResponseModel.error) {
                        Toast.makeText(DispatchOrderActivity.this, "Order dispatched!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DispatchOrderActivity.this, Dashboard.class));
                        finish();

                    } else {
                        Toast.makeText(DispatchOrderActivity.this, dispatchOrderResponseModel.message, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    dialogView.dismissCustomSpinProgress();
                }
            }

            @Override
            public void onFailure(Call<DispatchOrderResponseModel> call, Throwable t) {

                dialogView.dismissCustomSpinProgress();

            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        /*if(etNum4.getText().toString().length()==0){
            etNum2.requestFocus();
        }
        if(etNum2.getText().toString().length()==0){
            etNum1.requestFocus();
        }*/
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(etNum1.getText().toString().length()==1){
            etNum2.requestFocus();
        }

        if(etNum2.getText().toString().length()==1){
            etNum4.requestFocus();
        }
        /*if (etNum4.getText().toString().length()==0){
            etNum2.requestFocus();
        }*/

    }

    @Override
    public void afterTextChanged(Editable editable) {
        /*if(etNum4.getText().toString().length()==0){
            etNum2.requestFocus();
        }
        if(etNum2.getText().toString().length()==0){
            etNum1.requestFocus();
        }*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DispatchOrderActivity.this, Dashboard.class));
        finish();
    }

    public class GenericTextWatcher implements TextWatcher
    {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch(view.getId())
            {
                case R.id.etNum1:
                    if(text.length()==1)
                        etNum2.requestFocus();
                    break;
                case R.id.etNum2:
                    if(text.length()==1)
                        etNum4.requestFocus();
                    else if(text.length()==0)
                        etNum1.requestFocus();
                    break;
                case R.id.etNum4:
                    if(text.length()==1)
                        etNum4.requestFocus();
                    else if(text.length()==0)
                        etNum2.requestFocus();
                    break;

            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }

}