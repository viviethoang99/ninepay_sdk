package com.npsdk.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.npsdk.LibListener;
import com.npsdk.R;
import com.npsdk.jetpack_sdk.DataOrder;
import com.npsdk.jetpack_sdk.InputCardActivity;
import com.npsdk.jetpack_sdk.OrderActivity;
import com.npsdk.jetpack_sdk.repository.CallbackOrder;
import com.npsdk.jetpack_sdk.repository.CheckValidatePayment;
import com.npsdk.jetpack_sdk.repository.model.ValidatePaymentModel;
import com.npsdk.jetpack_sdk.repository.model.validate_payment.Methods;
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
    private static boolean isLoginComplete = false;
    private static boolean isCheckOrderComplete = false;
    private Dialog progressDialog;

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

    private void initDialogLoading() {
        progressDialog = new Dialog(activity);
        progressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ProgressBar progressBar = new ProgressBar(activity);
        progressBar.getIndeterminateDrawable().setColorFilter(activity.getResources().getColor(R.color.green), PorterDuff.Mode.SRC_IN);
        progressBar.setIndeterminate(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setContentView(progressBar);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnKeyListener((dialog, keyCode, event) -> false);
    }

    private final Runnable showProgressRunnable = new Runnable() {
        public void run() {
            if (progressDialog != null) {
                progressDialog.show();
            }
        }
    };

    private final Runnable hideProgressRunnable = new Runnable() {
        public void run() {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    };

    public void payWithWallet(String url, @Nullable String type) {
        initDialogLoading();
        if (progressDialog.isShowing()) return;
        if (Preference.getString(activity, Flavor.prefKey + Constants.ACCESS_TOKEN, "").isEmpty()) {
            // Gọi sang webview login
            NPayLibrary.getInstance().openWallet(Actions.LOGIN);
            return;
        }
        String token = Preference.getString(activity, Flavor.prefKey + Constants.ACCESS_TOKEN, "");
        String deviceId = DeviceUtils.getDeviceID(activity);
        String UID = DeviceUtils.getUniqueID(activity);

        showProgressRunnable.run();
        DataOrder.Companion.setUrlData(url);
        // Lấy thông tin đơn hàng
        new CheckValidatePayment().check(activity, DataOrder.Companion.getUrlData(), new CallbackOrder() {
            @Override
            public void onSuccess(ValidatePaymentModel data) {
                DataOrder.Companion.setDataOrderSaved(data);
                if (type != null && !type.isEmpty()) {
                    for (Methods methods : data.getData().getMethods()) {
                        if (methods.getCode().equals(type)) {
                            DataOrder.Companion.setSelectedItemDefault(methods);
                            DataOrder.Companion.setSelectedItemMethod(methods);
                            break;
                        }
                    }
                } else {
                    DataOrder.Companion.setSelectedItemDefault(null);
                }
                isCheckOrderComplete = true;
                checkComplete(type);
            }
        });


        if (type == null || type.equals("ATM_CARD") || type.equals("CREDIT_CARD")) {
            isLoginComplete = true;
        } else {
            // Get user info
            GetInfoTask getInfoTask = new GetInfoTask(activity, "Bearer " + token, new GetInfoTask.OnGetInfoListener() {
                @Override
                public void onGetInfoSuccess(String balance, String status, String phone) {
                    isLoginComplete = true;
                    DataOrder.Companion.setBalance(Integer.parseInt(balance));
                    checkComplete(type);
                }

                @Override
                public void onError(int errorCode, String message) {
                    if (errorCode == 403 || message.contains("đã hết hạn") || message.toLowerCase().contains("không tìm thấy")) {
                        refreshToken(deviceId, UID, new Runnable() { // Refresh success
                            @Override
                            public void run() {
                                payWithWallet(url, type);
                            }
                        });
                    }

                }
            });
            getInfoTask.execute();
        }
    }


    private void checkComplete(@Nullable String type) {
        try {
            if (isCheckOrderComplete && isLoginComplete) {
                isCheckOrderComplete = false;
                isLoginComplete = false;
                hideProgressRunnable.run();
                // Call after 2 function called done
                ValidatePaymentModel data = DataOrder.Companion.getDataOrderSaved();
                if (data == null) return;
                if (type != null && DataOrder.Companion.getSelectedItemDefault() == null) {
                    Toast.makeText(activity, "Phương thức thanh toán không được hỗ trợ.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent;
                Methods methods = DataOrder.Companion.getSelectedItemDefault();
                if (methods == null || methods.getCode().equals("WALLET")) {
                    intent = new Intent(activity, OrderActivity.class);
                    intent.putExtra("url", DataOrder.Companion.getUrlData());

                } else {
                    intent = new Intent(activity, InputCardActivity.class);
                }
                activity.startActivity(intent);
            }
        } catch (Exception e) {
            hideProgressRunnable.run();
            Toast.makeText(activity, "Đã có lỗi xảy ra...", Toast.LENGTH_SHORT).show();
        }
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
        header.put("brand_color", String.valueOf(SdkConfig.getBrandColor()));
        header.put("platform", "android");
        header.put("device", DeviceUtils.getDevice());
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
