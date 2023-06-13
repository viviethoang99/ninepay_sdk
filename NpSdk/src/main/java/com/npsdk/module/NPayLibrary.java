package com.npsdk.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebStorage;
import android.webkit.WebView;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.android.material.snackbar.Snackbar;
import com.npsdk.LibListener;
import com.npsdk.jetpack_sdk.DataOrder;
import com.npsdk.jetpack_sdk.InputCardActivity;
import com.npsdk.jetpack_sdk.OrderActivity;
import com.npsdk.module.api.GetInfoTask;
import com.npsdk.module.api.RefreshTokenTask;
import com.npsdk.module.model.SdkConfig;
import com.npsdk.module.utils.*;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("StaticFieldLeak")
public class NPayLibrary {
    public static final String STAGING = "staging";
    public static final String SANDBOX = "sandbox";
    public static final String PRODUCTION = "prod";
    private static final String TAG = NPayLibrary.class.getSimpleName();
    public static Flavor flavor;
    public static boolean isKeyboardShowing = false;
    private static NPayLibrary INSTANCE;
    public SdkConfig sdkConfig;
    public Activity activity;
    public LibListener listener;

    public static NPayLibrary getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NPayLibrary();
            flavor = new Flavor();
        }
        return INSTANCE;
    }

    public static void showKeyboard(WebView webView) {
        if (isKeyboardShowing) return;
        webView.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager)
                webView.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static void hideSoftKeyboard(WebView webView) {
        if (!isKeyboardShowing) return;
        InputMethodManager inputMethodManager = (InputMethodManager)
                webView.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                webView.getWindowToken(),
                0
        );
    }

    public void init(Activity activity, SdkConfig sdkConfig, LibListener listener) {
        this.activity = activity;
        this.sdkConfig = sdkConfig;
        this.listener = listener;
        flavor.configFlavor(sdkConfig.getEnv());
    }

    public void openWallet(String actions) {
        Intent intent = new Intent(activity, NPayActivity.class);
        intent.putExtra("data", NPayLibrary.getInstance().walletData(actions));
        activity.startActivity(intent);
    }

    public void pay(String urlPayment) {
        Intent intent = new Intent(activity, NPayActivity.class);
        intent.putExtra("data", NPayLibrary.getInstance().paymentData(urlPayment));
        activity.startActivity(intent);
    }

    private void showMessage(String message) {
        Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    public void payWithWallet(String url, @Nullable String type) {
        DataOrder.Companion.setUrlData(url);

        if (type == null || type.equals("WALLET")) {
            if (type != null && type.equals("WALLET")) {
                if (Preference.getString(activity, Flavor.prefKey + Constants.PUBLIC_KEY, "").isEmpty()) {
                    // Gọi sang webview login
                    NPayLibrary.getInstance().openWallet(Actions.LOGIN);
                    return;
                }
            }
            Intent intent = new Intent(activity, OrderActivity.class);
            if (type != null) intent.putExtra("method", type);
            intent.putExtra("url", url);
            activity.startActivity(intent);
            return;
        }

        // Method other
        Intent intent = new Intent(activity, InputCardActivity.class);
        intent.putExtra("method", type);
        activity.startActivity(intent);
    }

    public void getUserInfoSendToPayment() {
        DataOrder.Companion.setBalance(null);
        String token = Preference.getString(activity, Flavor.prefKey + Constants.ACCESS_TOKEN, "");
        String publickey = Preference.getString(activity, Flavor.prefKey + Constants.PUBLIC_KEY, "");
        String deviceId = DeviceUtils.getDeviceID(activity);
        String UID = DeviceUtils.getUniqueID(activity);
        if (token.isEmpty() || publickey.isEmpty()) return;
        // Get user info
        GetInfoTask getInfoTask = new GetInfoTask(activity, "Bearer " + token, new GetInfoTask.OnGetInfoListener() {
            @Override
            public void onGetInfoSuccess(String balance, String status, String phone) {
                DataOrder.Companion.setBalance(Integer.parseInt(balance));
            }

            @Override
            public void onError(int errorCode, String message) {
                if (errorCode == 403 || message.contains("đã hết hạn") || message.toLowerCase().contains("không tìm thấy")) {
                    refreshToken(deviceId, UID, new Runnable() { // Refresh success
                        @Override
                        public void run() {
                            // Đệ quy
                        }
                    });
                }

            }
        });
        getInfoTask.execute();
    }

    public void getInfoAccount() {
        if (Preference.getString(activity, Flavor.prefKey + Constants.ACCESS_TOKEN, "").isEmpty()) {
            listener.onError(403, "Tài khoản chưa được đăng nhập!");
            return;
        }
        String token = Preference.getString(activity, Flavor.prefKey + Constants.ACCESS_TOKEN, "");
        String deviceId = DeviceUtils.getDeviceID(activity);
        String UID = DeviceUtils.getUniqueID(activity);
        Log.d(TAG, "device id : " + deviceId + " , UID : " + UID);
        GetInfoTask getInfoTask = new GetInfoTask(activity, "Bearer " + token, new GetInfoTask.OnGetInfoListener() {
            @Override
            public void onGetInfoSuccess(String balance, String status, String phone) {
                listener.getInfoSuccess(phone, status, balance);
            }

            @Override
            public void onError(int errorCode, String message) {
                if (errorCode == 403 || message.contains("đã hết hạn") || message.toLowerCase().contains("không tìm thấy")) {
                    refreshToken(deviceId, UID, null);
                    return;
                }
                listener.onError(errorCode, message);
            }
        });
        getInfoTask.execute();
    }

    private void refreshToken(String deviceId, String UID, @Nullable Runnable runnable) {
        RefreshTokenTask refreshTokenTask = new RefreshTokenTask(activity, deviceId, UID, new RefreshTokenTask.OnRefreshListener() {
            @Override
            public void onRefreshSuccess() {
                getInfoAccount();
                if (runnable != null) {
                    runnable.run();
                }
            }

            @Override
            public void onError(int errorCode, String message) {
                listener.onError(errorCode, message);

            }
        }, Preference.getString(activity, NPayLibrary.getInstance().sdkConfig.getEnv() + Constants.REFRESH_TOKEN));
        refreshTokenTask.execute();
    }

    public void logout() {
        WebStorage.getInstance().deleteAllData();
        Preference.remove(activity, NPayLibrary.getInstance().sdkConfig.getEnv() + Constants.ACCESS_TOKEN);
        Preference.remove(activity, NPayLibrary.getInstance().sdkConfig.getEnv() + Constants.REFRESH_TOKEN);
        Preference.remove(activity, NPayLibrary.getInstance().sdkConfig.getEnv() + Constants.PUBLIC_KEY);
        listener.onLogoutSuccessful();
    }

    public void close() {
        Intent intentClose = new Intent();
        intentClose.setAction("nativeBroadcast");
        intentClose.putExtra("action", "close");
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intentClose);
        listener.onCloseSDK();
    }

    public Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Merchant-Code", sdkConfig.getMerchantCode());
        header.put("Merchant-Uid", sdkConfig.getUid());
        header.put("env", sdkConfig.getEnv());
        header.put("App-Type", "SDK");
        header.put("Access-Control-Allow-Origin", "*");
        header.put("brand_color", String.valueOf(SdkConfig.getBrandColor()));
        header.put("platform", "android");
        header.put("device", DeviceUtils.getDevice());
        header.put("User-Agent", DeviceUtils.getDevice());
        return header;
    }

    private String walletData(String route) {
        Map<String, String> data = getHeader();
        data.put("route", route);
        JSONObject obj = new JSONObject(data);
        return obj.toString();
    }

    private String paymentData(String urlPayment) {
        Map<String, String> data = getHeader();
        data.put("route", Constants.VERIFY_PAYMENT_ROUTE);
        data.put("order_id", urlPayment);
        JSONObject obj = new JSONObject(data);
        return obj.toString();
    }
}
