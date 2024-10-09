package com.oneness.fdxmerchant.Utils.TablayoutAdapter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.oneness.fdxmerchant.Fragments.TabFragments.ItemReportFragments.ItemReportCustomFragment;
import com.oneness.fdxmerchant.Fragments.TabFragments.ItemReportFragments.ItemReportTodayFragment;
import com.oneness.fdxmerchant.Fragments.TabFragments.OrderReportTabs.CustomOrderReportFragment;
import com.oneness.fdxmerchant.Fragments.TabFragments.OrderReportTabs.TodayOrderReportFragment;

public class ItemReportTabAdapter extends FragmentPagerAdapter {

    FragmentManager mContext;
    int mTotalTabs;

    public ItemReportTabAdapter(FragmentManager fragmentManager, int tabCount) {
        super(fragmentManager);
        //this.mContext = fragmentManager;
        this.mTotalTabs = tabCount;
    }


    //String uId;




    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d("asasas" , position + "");
        switch (position) {
            case 0:
                return new ItemReportTodayFragment();
            case 1:
                return new ItemReportCustomFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return mTotalTabs;
    }
}
