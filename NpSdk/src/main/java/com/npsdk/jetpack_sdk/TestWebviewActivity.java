package com.npsdk.jetpack_sdk;

import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.npsdk.R;

public class TestWebviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_webview);

        WebView webView = findViewById(R.id.webview_test);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        CookieManager cookieManager = CookieManager.getInstance();
        webView.loadUrl("https://stg-sdk.9pay.mobi/v1/cookie");
    }
}