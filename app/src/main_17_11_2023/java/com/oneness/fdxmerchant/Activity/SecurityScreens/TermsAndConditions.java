package com.oneness.fdxmerchant.Activity.SecurityScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.oneness.fdxmerchant.R;

public class TermsAndConditions extends AppCompatActivity {

    ImageView ivBack;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        ivBack = findViewById(R.id.ivBack);
        webView = findViewById(R.id.webView);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        webView.loadUrl("http://demo91.co.in/dev/fdx/public/terms.html");
    }
}