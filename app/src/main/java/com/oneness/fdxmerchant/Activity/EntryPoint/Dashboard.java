package com.oneness.fdxmerchant.Activity.EntryPoint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.oneness.fdxmerchant.Activity.Order.OrderDetails;
import com.oneness.fdxmerchant.Activity.Profile.Profile;
import com.oneness.fdxmerchant.Activity.Profile.UpdateProfile;
import com.oneness.fdxmerchant.Activity.ReportManagement.RestaurantPayouts;
import com.oneness.fdxmerchant.Activity.SecurityScreens.PrivacyPolicy;
import com.oneness.fdxmerchant.Activity.SecurityScreens.TermsAndConditions;
import com.oneness.fdxmerchant.Fragments.ParentFragments.CouponManagementFragment;
import com.oneness.fdxmerchant.Fragments.ParentFragments.HomeFragment;
import com.oneness.fdxmerchant.Fragments.ParentFragments.ItemManagementFragment;
import com.oneness.fdxmerchant.Fragments.ParentFragments.NotificationManagementFragment;
import com.oneness.fdxmerchant.Fragments.ParentFragments.OrderManagementFragment;
import com.oneness.fdxmerchant.Fragments.ParentFragments.PayoutFragment;
import com.oneness.fdxmerchant.Fragments.ParentFragments.ReportManagementFragment;
import com.oneness.fdxmerchant.Models.RestaurantDataModels.RestaurantDataModel;
import com.oneness.fdxmerchant.Models.RestaurantOpenCloseModels.RestaurantStatusRequestModel;
import com.oneness.fdxmerchant.Models.RestaurantOpenCloseModels.RestaurantStatusResponseModel;
import com.oneness.fdxmerchant.Network.ApiManager;
import com.oneness.fdxmerchant.Network.Constants;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static Dashboard dashboard;
    DrawerLayout drawer;
    NavigationView navigationView;
    ImageView ivHam, ivUser, ivNoti, ivSearch, tvEdit;
    TextView tvHeader, tvId, tvUser, tvOpen;
    ImageView ivUserPrf;
    public static RestaurantDataModel restaurantDataModel = new RestaurantDataModel();
    LinearLayout logoutLL, openLL;
    Switch toggleOpen;
    int REQUEST_PHONE_CALL = 100;

    ApiManager manager = new ApiManager();

    Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        dashboard = Dashboard.this;
        prefs = new Prefs(dashboard);
        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        ivHam = findViewById(R.id.ivHam);
        ivUser = findViewById(R.id.ivUser);
        ivNoti = findViewById(R.id.ivNoti);
        ivSearch = findViewById(R.id.ivSearch);
        tvHeader = findViewById(R.id.tvHeader);
        logoutLL = findViewById(R.id.logoutLL);
        openLL = findViewById(R.id.openLL);
        toggleOpen = (Switch) findViewById(R.id.toggleOpen);
        tvOpen = findViewById(R.id.tvOpen);

        tvUser = navigationView.getHeaderView(0).findViewById(R.id.tvUser);
        tvId = navigationView.getHeaderView(0).findViewById(R.id.tvId);
        ivUserPrf = navigationView.getHeaderView(0).findViewById(R.id.ivUserPrf);
        tvEdit = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.tvEdit);
        //tvId = findViewById(R.id.tvId);
        //tvEdit = findViewById(R.id.tvEdit);
        //tvUser = findViewById(R.id.tvUser);

        if (prefs.getData(Constants.REST_TAKING).equals("1")){
            //toggleOpen.setActivated(false);
            toggleOpen.setChecked(false);
        }else {
           // toggleOpen.setActivated(true);
            toggleOpen.setChecked(true);
        }

        toggleOpen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("CHECK>>", ""+isChecked);
                if (isChecked){
                    RestaurantStatusRequestModel restaurantStatusRequestModel = new RestaurantStatusRequestModel(
                            prefs.getData(Constants.REST_ID),
                            "0"

                    );
                    updateRestStat(restaurantStatusRequestModel, "on");
                }else {
                    RestaurantStatusRequestModel restaurantStatusRequestModel = new RestaurantStatusRequestModel(
                            prefs.getData(Constants.REST_ID),
                            "1"

                    );
                    updateRestStat(restaurantStatusRequestModel, "off");
                }
            }
        });

        tvUser.setText(prefs.getData(Constants.REST_NAME));
        tvId.setText(prefs.getData(Constants.REST_EMAIL));

        if (prefs.getData(Constants.REST_LOGO).equals("")){
            Glide.with(Dashboard.this).load(prefs.getData(Constants.REST_BANNER)).into(ivUserPrf);
        }else {
            Glide.with(Dashboard.this).load(prefs.getData(Constants.REST_LOGO)).into(ivUserPrf);
        }

       // Glide.with(Dashboard.this).load(prefs.getData(Constants.REST_LOGO)).into(ivUserPrf);

        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Profile.restaurantDataModel = restaurantDataModel;
                startActivity(new Intent(Dashboard.this, Profile.class));
            }
        });



        navigationView.setNavigationItemSelectedListener(this);

        logoutLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutPopup();
            }
        });

        ivNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvHeader.setVisibility(View.VISIBLE);
                tvHeader.setText("Notification Management");
                ivNoti.setVisibility(View.GONE);
                ivUser.setVisibility(View.GONE);
                ivSearch.setVisibility(View.GONE);
                openLL.setVisibility(View.GONE);
                NotificationManagementFragment notiManagementFragment = new NotificationManagementFragment();
                pushFragmentsStatic(notiManagementFragment, true, null);
            }
        });

        ivHam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawer.openDrawer(navigationView);

            }
        });

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile.restaurantDataModel = restaurantDataModel;
                startActivity(new Intent(Dashboard.this, UpdateProfile.class));
            }
        });

        if (ContextCompat.checkSelfPermission(Dashboard.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Dashboard.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        }

        homeLandingPage();


    }

    private void updateRestStat(RestaurantStatusRequestModel restaurantStatusRequestModel, String stat) {
        manager.service.restaurantStatUpdate(restaurantStatusRequestModel).enqueue(new Callback<RestaurantStatusResponseModel>() {
            @Override
            public void onResponse(Call<RestaurantStatusResponseModel> call, Response<RestaurantStatusResponseModel> response) {
                if (response.isSuccessful()){
                    RestaurantStatusResponseModel rsrm = response.body();
                    if (!rsrm.error){
                        if (stat.equals("off")){
                            prefs.saveData(Constants.REST_TAKING, "1");
                        }else {
                            prefs.saveData(Constants.REST_TAKING, "0");
                        }

                        Toast.makeText(Dashboard.this, rsrm.message, Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(Dashboard.this, rsrm.message, Toast.LENGTH_SHORT).show();
                    }
                }else {

                }
            }

            @Override
            public void onFailure(Call<RestaurantStatusResponseModel> call, Throwable t) {

            }
        });
    }

    private void homeLandingPage() {
        if (Constants.fromAddItem == true){
            tvHeader.setVisibility(View.VISIBLE);
            tvHeader.setText("Category & Item Management");
            ivNoti.setVisibility(View.GONE);
            ivUser.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);
            openLL.setVisibility(View.GONE);
            Constants.fromAddItem = false;
            ItemManagementFragment itemManagementFragment = new ItemManagementFragment();
            pushFragmentsStatic(itemManagementFragment, true, null);
        }else{
            openLL.setVisibility(View.VISIBLE);
            HomeFragment landingHome = new HomeFragment();
            pushFragmentsStatic(landingHome, true, null);
        }

    }

    public static void pushFragmentsStatic(Fragment fragment, boolean shouldAdd, String tag) {
        //backCount = 0;
        FragmentManager manager = dashboard.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        //ft.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        ft.replace(R.id.fragment_container, fragment, tag);

        if (shouldAdd) {
            ft.addToBackStack("ScreenStack");
        } else {
            manager.popBackStack(null, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        ft.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.navDashboard) {
            //Log.d("CLICKED 1>>", "OKK1");
            drawer.closeDrawer(navigationView);
            tvHeader.setVisibility(View.GONE);
            ivNoti.setVisibility(View.VISIBLE);
            ivUser.setVisibility(View.VISIBLE);
            ivSearch.setVisibility(View.GONE);
            openLL.setVisibility(View.VISIBLE);
            HomeFragment landingHome = new HomeFragment();
            pushFragmentsStatic(landingHome, true, null);
        } else if (id == R.id.navOrder) {
            drawer.closeDrawer(GravityCompat.START);
            Log.d("CLICKED 2>>", "OKK2");

            tvHeader.setVisibility(View.VISIBLE);
            tvHeader.setText("Order Management");
            ivNoti.setVisibility(View.GONE);
            ivUser.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);
            openLL.setVisibility(View.GONE);
            OrderManagementFragment orderManagementFragment = new OrderManagementFragment();
            pushFragmentsStatic(orderManagementFragment, true, null);
        } /*else if (id == R.id.navCategory) {
            drawer.closeDrawer(GravityCompat.START);
            Log.d("CLICKED 2>>", "OKK2");
            tvHeader.setVisibility(View.VISIBLE);
            tvHeader.setText("Category Management");
            ivNoti.setVisibility(View.GONE);
            ivUser.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);
            CategoryManagementFragment catManagementFragment = new CategoryManagementFragment();
            pushFragmentsStatic(catManagementFragment, true, null);

        }*/ else if (id == R.id.navItem) {

            drawer.closeDrawer(GravityCompat.START);
            Log.d("CLICKED 2>>", "OKK2");
            tvHeader.setVisibility(View.VISIBLE);
            tvHeader.setText("Category & Item Management");
            ivNoti.setVisibility(View.GONE);
            ivUser.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);
            openLL.setVisibility(View.GONE);
            ItemManagementFragment itemManagementFragment = new ItemManagementFragment();
            pushFragmentsStatic(itemManagementFragment, true, null);

        } else if (id == R.id.navNotification){
            drawer.closeDrawer(GravityCompat.START);
            Log.d("CLICKED 2>>", "OKK2");
            tvHeader.setVisibility(View.VISIBLE);
            tvHeader.setText("Notification Management");
            ivNoti.setVisibility(View.GONE);
            ivUser.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);
            openLL.setVisibility(View.GONE);
            NotificationManagementFragment notiManagementFragment = new NotificationManagementFragment();
            pushFragmentsStatic(notiManagementFragment, true, null);
        } else if (id == R.id.navCupon){
            drawer.closeDrawer(GravityCompat.START);
            //Log.d("CLICKED 2>>", "OKK2");
            tvHeader.setVisibility(View.VISIBLE);
            tvHeader.setText("Coupon Management");
            ivNoti.setVisibility(View.GONE);
            ivUser.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);
            openLL.setVisibility(View.GONE);
            CouponManagementFragment couponManagementFragment = new CouponManagementFragment();
            pushFragmentsStatic(couponManagementFragment, true, null);
        }else if (id == R.id.navReports){
            drawer.closeDrawer(GravityCompat.START);
            //Log.d("CLICKED 2>>", "OKK2");
            tvHeader.setVisibility(View.VISIBLE);
            tvHeader.setText("Reports Management");
            ivNoti.setVisibility(View.GONE);
            ivUser.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);
            openLL.setVisibility(View.GONE);
            ReportManagementFragment.restaurantDataModel = restaurantDataModel;
            ReportManagementFragment reportManagementFragment = new ReportManagementFragment();
            pushFragmentsStatic(reportManagementFragment, true, null);
        }else if (id == R.id.navPayout){
            drawer.closeDrawer(GravityCompat.START);
            //Log.d("CLICKED 2>>", "OKK2");
            tvHeader.setVisibility(View.VISIBLE);
            tvHeader.setText("Payouts");
            ivNoti.setVisibility(View.GONE);
            ivUser.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);
            openLL.setVisibility(View.GONE);
            //startActivity(new Intent(Dashboard.this, RestaurantPayouts.class));
           // ReportManagementFragment.restaurantDataModel = restaurantDataModel;
            PayoutFragment payoutFragment = new PayoutFragment();
            pushFragmentsStatic(payoutFragment, true, null);
        }else if (id == R.id.navChangePsw){
            drawer.closeDrawer(GravityCompat.START);
            //Log.d("CLICKED 2>>", "OKK2");
            /*tvHeader.setVisibility(View.VISIBLE);
            tvHeader.setText("Reports Management");
            ivNoti.setVisibility(View.GONE);
            ivUser.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);
            ReportManagementFragment.restaurantDataModel = restaurantDataModel;
            ReportManagementFragment reportManagementFragment = new ReportManagementFragment();
            pushFragmentsStatic(reportManagementFragment, true, null);*/
            startActivity(new Intent(Dashboard.this, ChangePassword.class));
        } else if (id == R.id.navPrivacy){
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(Dashboard.this, PrivacyPolicy.class));
        }else if (id == R.id.navTerms){
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(Dashboard.this, TermsAndConditions.class));
        }/*else if (id == R.id.navLogout) {
            drawer.closeDrawer(GravityCompat.START);
            showLogoutPopup();
        }*/


        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void showLogoutPopup() {
        Dialog dialog = new Dialog(Dashboard.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.inflate_custom_alert_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        /*LayoutInflater layoutInflater = LayoutInflater.from(Dashboard.this);
        View promptView = layoutInflater.inflate(R.layout.inflate_custom_alert_dialog, null);

        final AlertDialog alertD = new AlertDialog.Builder(Dashboard.this).create();*/
        TextView tvHeader = (TextView) dialog.findViewById(R.id.tvHeader);
        tvHeader.setText(getResources().getString(R.string.app_name));
        tvHeader.setVisibility(View.GONE);
        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        tvMsg.setText("Do you want to logout ?");

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setText("Cancel");
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        //btnOk.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        btnOk.setText("Ok");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //deleteFromCart(id, pos);
                prefs.clearAllData();
                startActivity(new Intent(Dashboard.this, EmailLogin.class));
                finishAffinity();
                dialog.dismiss();

            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        /*alertD.setView(promptView);
        try {*/
        dialog.show();
        /*} catch (WindowManager.BadTokenException e) {
            //use a log message
        }*/
    }
}