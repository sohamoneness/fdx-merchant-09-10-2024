package com.oneness.fdxmerchant.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oneness.fdxmerchant.Models.GoogleAddressModels.GoogleAddressBean;
import com.oneness.fdxmerchant.R;

import java.util.ArrayList;


/**
 * This class extends {@link BaseAdapter} to show the addresses based on the
 * user input and show the row of the list view in the list view.
 *
 */
public class GoogleAddressAdapter extends BaseAdapter
{
	private ArrayList<GoogleAddressBean> arrayList;
	private LayoutInflater inflater;
	Activity activity;
	/**
	 * This class extends {@link BaseAdapter} to show the addresses based on the
	 * user input and show the row of the list view in the list view.
	 */
	public GoogleAddressAdapter(ArrayList<GoogleAddressBean> arrayList, Context context)
	{
		this.arrayList = arrayList;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		return arrayList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = inflater.inflate(R.layout.inflate_google_address, parent, false);
		GoogleAddressBean googleAddressBean = arrayList.get(position);

		TextView name = (TextView) view.findViewById(R.id.listAddName);
		name.setText( googleAddressBean.getAddress());
//		CToast.show(activity,googleAddressBean.getAddress());

		return view;
	}

}
