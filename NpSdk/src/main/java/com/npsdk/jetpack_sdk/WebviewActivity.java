package com.npsdk.jetpack_sdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.*;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.npsdk.module.NPayLibrary;
import com.npsdk.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebviewActivity extends AppCompatActivity {

    private WebView webView;
    private String urlLoad = "";

    private View btnClose;

    private static final Integer WEBVIEW_STATUS_CANCEL = -2;
    private static final Integer WEBVIEW_STATUS_FAIL = -1;
    private static final Integer WEBVIEW_STATUS_PENDING = 0;
    private static final Integer WEBVIEW_STATUS_PROCESSED = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Bundle intent = getIntent().getExtras();

        if (intent != null) {
            if (intent.getString("url") != null) {
                urlLoad = intent.getString("url");
            }
        }
        System.out.println("URLLOAD 1: " + urlLoad);
        webView = findViewById(R.id.webView);
        btnClose = findViewById(R.id.btnClose);
        setupWebview();
        webviewListener();
    }

    private void webviewListener() {
        btnClose.setOnClickListener(view -> finish());
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("URLLOAD 2: " + url);

                if (url.contains("payment_result?status")) {
                    String regex = "status=(-?\\d+)&message=([^&]+)";

                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(url);

                    if (matcher.find()) {
                        String status = matcher.group(1);
                        String message = matcher.group(2);

                        finish();
                        if (Integer.parseInt(status) == WEBVIEW_STATUS_PROCESSED) {
                            if (NPayLibrary.getInstance().listener != null) {
                                NPayLibrary.getInstance().listener.onPaySuccessful();
                                System.out.println("onPaySuccessful new sdk");
                            } else {
                                Toast.makeText(WebviewActivity.this, "Bạn chưa khởi tạo SDK.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Move to error page
                            moveToErrorPage(view.getContext(), message);
                        }
                    }

                    return false;
                }

                if (url.contains("error/payment")) {
                    finish();
                    moveToErrorPage(view.getContext(), "Payment failed");
                    return false;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    private void moveToErrorPage(Context context, String error) {
        // Move to error page
        Intent intent = new Intent(context, ErrorPaymentActivity.class);
        intent.putExtra("message", decodeMessage(error));
        startActivity(intent);
    }
    private void setupWebview() {
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());
            }
        });

        webView.loadUrl(urlLoad);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private static String decodeMessage(String encodedMessage) {
        try {
            String decoded = URLDecoder.decode(encodedMessage, Charset.forName("UTF-8").toString());
            return decoded;
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}