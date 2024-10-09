package com.oneness.fdxmerchant.Utils.TablayoutAdapter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.oneness.fdxmerchant.Fragments.TabFragments.OrderReportTabs.CustomOrderReportFragment;
import com.oneness.fdxmerchant.Fragments.TabFragments.OrderReportTabs.TodayOrderReportFragment;
import com.oneness.fdxmerchant.Fragments.TabFragments.OrderTabs.CanceledOrdersFragment;
import com.oneness.fdxmerchant.Fragments.TabFragments.OrderTabs.DeliveredOrdersFragment;
import com.oneness.fdxmerchant.Fragments.TabFragments.OrderTabs.NewOrdersFragment;
import com.oneness.fdxmerchant.Fragments.TabFragments.OrderTabs.OngoingOrdersFragment;

public class OrderReportTabLayoutAdapter extends FragmentPagerAdapter {

    Context mContext;
    int mTotalTabs;

    public OrderReportTabLayoutAdapter(FragmentManager fragmentManager, int tabCount) {
        super(fragmentManager);
        // this.mContext = fragmentManager;
        this.mTotalTabs = tabCount;
    }


    //String uId;




    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d("asasas" , position + "");
        switch (position) {
            case 0:
                return new TodayOrderReportFragment();
            case 1:
                return new CustomOrderReportFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return mTotalTabs;
    }
}
