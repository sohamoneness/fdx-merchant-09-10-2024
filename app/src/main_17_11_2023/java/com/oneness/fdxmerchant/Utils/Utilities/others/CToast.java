/*
 * Copyright (c) 2016.
 * Soham Ghosh
 */

package com.oneness.fdxmerchant.Utils.Utilities.others;

import android.content.Context;
import android.widget.Toast;

public class CToast
{
	public static void show(Context context, String msg)
	{
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
}
