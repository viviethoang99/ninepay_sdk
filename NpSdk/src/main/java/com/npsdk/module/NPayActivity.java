package com.npsdk.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.window.OnBackInvokedDispatcher;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.npsdk.R;
import com.npsdk.module.utils.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Map;

public class NPayActivity extends AppCompatActivity {
    public static final String TAG = NPayActivity.class.getName();
    @SuppressLint("StaticFieldLeak")
    public static WebView webView, webView2;
    Map<String, String> headerWebView = NPayLibrary.getInstance().getHeader();
    private View btnClose;
    private Toolbar toolbar;
    private LinearProgressIndicator progressBar;
    private BroadcastReceiver changeUrlBR;
    private JsHandler jsHandler;

    boolean isProgressDeposit = false;
    private ValueCallback<Uri[]> fileUploadCallback;
    private static final int FILE_CHOOSER_REQUEST_CODE = 10000;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npay);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT, () -> {
                    }
            );
        }

        findView();
        // Set color progress bar webview loading
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF15AE62, PorterDuff.Mode.SRC_IN);
        progressBar.getProgressDrawable().setColorFilter(0xFF15AE62, PorterDuff.Mode.SRC_IN);
        closeButtonWebview();
        jsHandler = new JsHandler(this);
        String data = getIntent().getStringExtra("data");
        Log.d(TAG, "onCreate: data ==   " + data);

        IntentFilter filter = new IntentFilter();
        filter.addAction("webViewBroadcast");
        filter.addAction("nativeBroadcast");
        listentChangeUrlBroadcast();

        LocalBroadcastManager.getInstance(this).registerReceiver(changeUrlBR, filter);

        settingWebview(webView);
        settingWebview(webView2);

        setUpweb1Client();
        setUpWeb2Client();
        setCookieRefreshToken();

        try {
            Uri.Builder builder = new Uri.Builder();
            JSONObject jsonObject = new JSONObject(data);
            String route = jsonObject.getString("route");
            String orderId = "";
            if (jsonObject.has("order_id")) {
                orderId = jsonObject.getString("order_id");
            }

            // Chống load lặp màn cũ.
            webView.loadUrl("javascript:document.open();document.close();");

            // Các route thuộc danh mục hóa đơn.
            if (Actions.listAllServices().contains(route)) {
                webView2.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(Utils.getUrlActionShop(route), headerWebView);
                System.out.println("Webview 1 load url " + Utils.getUrlActionShop(route));
                showOrHideToolbar();
            } else {
                if (Actions.listActionSdk().contains(route)) {
                    webView2.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    webView.loadUrl(Utils.getUrlActionSdk(route), headerWebView);
                    System.out.println("Webview 1 load url " + Utils.getUrlActionSdk(route));
                    return;
                }
                if (route.startsWith("http")) {
                    webView.loadUrl(route, headerWebView);
                    System.out.println("Webview 1 load url " + route);
                    return;
                }
                builder.scheme("https")
                        .encodedAuthority(Flavor.baseUrl.replaceAll("https://", ""))
                        .appendPath("v1")
                        .appendQueryParameter("route", route)
                        .appendQueryParameter("Merchant-Code", jsonObject.getString("Merchant-Code"))
                        .appendQueryParameter("Merchant-Uid", jsonObject.getString("Merchant-Uid"))
                        .appendQueryParameter("brand_color", NPayLibrary.getInstance().sdkConfig.getBrandColor())
                        .appendQueryParameter("platform", "android")
                        .appendQueryParameter("device-name", DeviceUtils.getDeviceName());
                if (jsonObject.has("order_id")) {
                    builder.appendQueryParameter("order_id", Utils.convertUrlToOrderId(orderId));
                }
                Log.d(TAG, "onCreate: Flavor.baseUrl ==   " + builder);
                clearWebview2NonToolbar();
                webView.loadUrl(builder.toString(), headerWebView);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setCookieRefreshToken() {
        String accessToken = Preference.getString(this, Flavor.prefKey + Constants.ACCESS_TOKEN, "");
        String refreshToken = Preference.getString(this, Flavor.prefKey + Constants.REFRESH_TOKEN, "");
        String publicKey = Preference.getString(this, Flavor.prefKey + Constants.PUBLIC_KEY, "");
        publicKey = (URLEncoder.encode(publicKey)).replace("++++", "");

        CookieManager cookieManager = CookieManager.getInstance();

        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);
        cookieManager.setAcceptThirdPartyCookies(webView2, true);

        if (!accessToken.isEmpty() && !refreshToken.isEmpty() && !publicKey.isEmpty()) {
            String cookieAcccessToken = "actk=" + accessToken + "; path=/v1";
            cookieManager.setCookie(Flavor.baseUrl.replaceAll("https://", ""), cookieAcccessToken);

            String cookieRefreshToken = "rtk=" + refreshToken + "; path=/v1";
            cookieManager.setCookie(Flavor.baseUrl.replaceAll("https://", ""), cookieRefreshToken);

            String publicKeyString = "pk=" + publicKey + "; path=/v1";
            cookieManager.setCookie(Flavor.baseUrl.replaceAll("https://", ""), publicKeyString);
        }
    }

    private void setUpweb1Client() {
        webView.setWebViewClient(new WebViewClient() {

//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();
//            }


            @Override
            public void onPageFinished(WebView view, String url) {
                CookieManager.getInstance().flush();
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                String url = request.getUrl().toString();

                System.out.println("shouldOverrideUrlLoading 1: " + url);

                if (!url.contains(Flavor.baseUrl) && !url.contains(Flavor.baseShop)) {
                    webView.setVisibility(View.GONE);
                    clearWebview2NonToolbar();
                    webView2.setVisibility(View.VISIBLE);
                    webView2.loadUrl(url, headerWebView);
                    return false;
                }
                if (url.contains("/merchant/payment/") || url.contains("/thanh-toan-qr/")) {
                    try {
                        Uri.Builder builder = new Uri.Builder();

                        builder.scheme("https")
                                .encodedAuthority(Flavor.baseUrl.replaceAll("https://", ""))
                                .appendPath("v1")
                                .appendPath("payment")
                                .appendQueryParameter("route", Constants.VERIFY_PAYMENT_ROUTE)
                                .appendQueryParameter("Merchant-Code", NPayLibrary.getInstance().sdkConfig.getMerchantCode())
                                .appendQueryParameter("Merchant-Uid", NPayLibrary.getInstance().sdkConfig.getUid())
                                .appendQueryParameter("brand_color", NPayLibrary.getInstance().sdkConfig.getBrandColor())
                                .appendQueryParameter("platform", "android")
                                .appendQueryParameter("order_id", Utils.convertUrlToOrderId(url))
                                .appendQueryParameter("device", DeviceUtils.getDeviceName());
                        clearWebview2NonToolbar();
                        webView2.setVisibility(View.GONE);
                        webView.setVisibility(View.VISIBLE);
                        webView.loadUrl(builder.toString(), headerWebView);

                    } catch (Exception ignored) {
                        System.out.println("Error webiew " + ignored);
                    }
                } else {
                    view.loadUrl(request.getUrl().toString(), headerWebView);
                }
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showOrHideToolbar();
                super.onPageStarted(view, url, favicon);
            }

        });
    }

    private void setUpWeb2Client() {
        webView2.setWebViewClient(new WebViewClient() {

//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();
//            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                Log.d(TAG, "shouldOverrideUrlLoading 2: url ==   " + url);

                if (url.endsWith("close-webview")) {
                    clearWebview2WithToolbar();
                    return false;
                }

                if (url.startsWith(Flavor.baseUrl)) {
                    clearWebview2WithToolbar();
                    return false;
                }
                webView2.loadUrl(url, headerWebView);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                CookieManager.getInstance().flush();
                super.onPageFinished(view, url);
            }
        });
    }


    private void listentChangeUrlBroadcast() {
        changeUrlBR = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive", "onReceive:  ==   " + intent.getAction());
                if (intent.getAction().equals("webViewBroadcast")) {
                    if (webView2 != null) {
                        clearWebview2NonToolbar();
                    }
                    String getURL = intent.getStringExtra("url");
                    String nameAction = intent.getStringExtra("name");
                    if (nameAction != null) {
                        isProgressDeposit = nameAction.equals("napas-deposit");
                    }
                    if (!getURL.startsWith(Flavor.baseUrl) && !getURL.startsWith(Flavor.baseShop)) {
                        webView.setVisibility(View.GONE);
                        webView2.setVisibility(View.VISIBLE);
                        webView2.loadUrl(getURL, headerWebView);
                    }
                    showOrHideToolbar();

                }
                if (intent.getAction().equals("nativeBroadcast")) {
                    if (intent.getStringExtra("action").equals("close")) {
                        finish();
                    }
                }
            }
        };
    }

    private void findView() {
        webView = findViewById(R.id.webView);
        webView2 = findViewById(R.id.webView2);
        toolbar = findViewById(R.id.toolbar);
        btnClose = findViewById(R.id.btnClose);
        progressBar = findViewById(R.id.progressBar);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void settingWebview(WebView webView) {
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.addJavascriptInterface(jsHandler, "JsHandler");
        WebSettings webSettings = webView.getSettings();
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                fileUploadCallback = filePathCallback;
                openFileChooser();
                return true;
            }
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 95) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), FILE_CHOOSER_REQUEST_CODE);
    }

    public void clearWebview2WithToolbar() {
        if (webView2.getVisibility() == View.VISIBLE) {
            clearWebview2NonToolbar();
            webView2.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }
        showOrHideToolbar();
    }

    public void clearWebview2NonToolbar() {
        webView2.clearHistory();
        webView2.clearCache(true);
        webView2.clearFormData();
        webView2.loadUrl("javascript:document.open();document.close();");
    }


    void closeButtonWebview() {
        btnClose.setOnClickListener(view -> {
            if (isProgressDeposit) {
                webView.loadUrl("javascript: window.sendEventDismissScreen()");
                isProgressDeposit = false;
            }
            clearWebview2WithToolbar();
        });
    }

    void showOrHideToolbar() {
        if (webView2.getVisibility() == View.VISIBLE) {
            toolbar.setVisibility(View.VISIBLE);
            return;
        }
        if (toolbar.getVisibility() == View.VISIBLE) {
            toolbar.setVisibility(View.GONE);
            closeCamera();
        }

        if (webView.getUrl() == null) {
            finish();
        }
    }

    void closeCamera() {
        try {
            Camera.open().release();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == JsHandler.PERMISSION_CAMERA_REQUEST_CODE) {
            JsHandler.sendStatusCamera(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
        } else if (requestCode == JsHandler.PERMISSION_STORAGE_REQUEST_CODE) {
            JsHandler.sendStatusStorage(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            if (fileUploadCallback == null) return;
            Uri[] results = null;

            if (resultCode == Activity.RESULT_OK && data != null) {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }

            fileUploadCallback.onReceiveValue(results);
            fileUploadCallback = null;
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        if (changeUrlBR != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(changeUrlBR);
            changeUrlBR = null;
        }
        clearWebview2NonToolbar();
        closeCamera();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}