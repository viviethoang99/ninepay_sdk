package com.npsdk.module.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.npsdk.jetpack_sdk.DataOrder;
import com.npsdk.jetpack_sdk.OrderActivity;
import com.npsdk.jetpack_sdk.ResultPayment;
import com.npsdk.module.NPayActivity;
import com.npsdk.module.NPayLibrary;
import com.npsdk.module.PaymentMethod;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.CLIPBOARD_SERVICE;

public class JsHandler {

    public static final int PERMISSION_CAMERA_REQUEST_CODE = 999;
    public static final int PERMISSION_STORAGE_REQUEST_CODE = 888;
    private static final String TAG = JsHandler.class.getSimpleName();

    private final Activity activity;

    public JsHandler(Activity activity) {
        this.activity = activity;
    }

    public static void sendStatusCamera(boolean status) {
        String jsExcute = "javascript: window.sendStatusCamera(" + status + ")";
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = () -> {
            NPayActivity.webView.loadUrl(jsExcute);
        };
        mainHandler.post(myRunnable);
    }

    public static void sendStatusStorage(boolean status) {
        String jsExcute = "javascript: window.getRequestGallery(" + status + ")";
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = () -> {
            NPayActivity.webView.loadUrl(jsExcute);
        };
        mainHandler.post(myRunnable);
    }

    @JavascriptInterface
    public void executeFunction(String command, String params) {
        try {
            Log.i(TAG, "executeFunctionP: command = " + command + "; params = " + params);
            JSONObject paramJson = new JSONObject(params);
            switch (switchCommandJS.valueOf(command)) {
                case open9PayApp:
                    String appPackageName = "vn.ninepay.ewallet";
                    Intent intent = activity.getPackageManager().getLaunchIntentForPackage(appPackageName);
                    if (intent == null) {
                        // Bring user to the market or let them choose an app?
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=" + appPackageName));
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    break;
                case close:
                    activity.finish();
                    NPayLibrary.getInstance().listener.onCloseSDK();
                    break;
                case backToApp:
                    activity.finish();
                    try {
                        if (paramJson.has("name")) {
                            String screenName = paramJson.getString("name");
                            NPayLibrary.getInstance().callbackBackToAppfrom(screenName);
                        }
                    } catch (Exception e) {
                        System.out.println("ERROR backToApp " + e.getMessage());
                    }
                    break;
                case getDeviceID:
                    break;
                case share:
                    ShareCompat.IntentBuilder.from(activity).setType("text/plain").setChooserTitle("9Pay").setText(paramJson.getString("text")).startChooser();
                    break;
                case copy:
                    ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("", paramJson.getString("text"));
                    clipboard.setPrimaryClip(clip);
                    break;
                case openOtherUrl:
                    Intent intentOpenWebView = new Intent();
                    intentOpenWebView.setAction("webViewBroadcast");
                    intentOpenWebView.putExtra("url", paramJson.getString("url"));
                    intentOpenWebView.putExtra("token", paramJson.getString("token"));
                    intentOpenWebView.putExtra("name", paramJson.getString("name"));
                    LocalBroadcastManager.getInstance(activity).sendBroadcast(intentOpenWebView);
                    break;
                case call:
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse(paramJson.getString("text")));
                    activity.startActivity(callIntent);
                    break;
                case message:
                    Intent messageIntent = new Intent(Intent.ACTION_VIEW);
                    messageIntent.setData(Uri.parse(paramJson.getString("text")));
                    activity.startActivity(messageIntent);
                    break;
                case onLoggedInSuccess:
                    NPayLibrary.getInstance().callBackToMerchant(
                            NameCallback.LOGIN, "onSuccess", null);
                    break;
                case onPaymentSuccess:
                    NPayLibrary.getInstance().callBackToMerchant(
                            NameCallback.SDK_PAYMENT, true, null);
                    break;
                case onError:
                    int errorCode = paramJson.getInt("error_code");
                    String message = paramJson.getString("message");
                    NPayLibrary.getInstance().listener.onError(errorCode, message);
                    break;
                case clearToken:
                    Preference.remove(activity, NPayLibrary.getInstance().sdkConfig.getEnv() + Constants.ACCESS_TOKEN);
                    Preference.remove(activity, NPayLibrary.getInstance().sdkConfig.getEnv() + Constants.REFRESH_TOKEN);
                    Preference.remove(activity, NPayLibrary.getInstance().sdkConfig.getEnv() + Constants.PUBLIC_KEY);
                    break;
                case getAllToken:
                    Preference.save(activity, NPayLibrary.getInstance().sdkConfig.getEnv() + Constants.ACCESS_TOKEN, paramJson.getString("access_token"));
                    Preference.save(activity, NPayLibrary.getInstance().sdkConfig.getEnv() + Constants.REFRESH_TOKEN, paramJson.getString("refresh_token"));
                    Preference.save(activity, NPayLibrary.getInstance().sdkConfig.getEnv() + Constants.PUBLIC_KEY, paramJson.getString("public_key"));
                    break;
                case requestCamera:
                    requestCamera(activity);
                    break;
                case openSchemaApp:
                    openSchemaApp(paramJson.getString("schema"));
                    break;
                case logout:
                    NPayLibrary.getInstance().logout();
                    break;
                case requestGallery:
                    requestStorage();
                    break;
                case callbackToApp:
                    getCallbackFromJs(paramJson);
                    break;
                case send_email:
                    try {
                        String email = paramJson.getString("email");
                        Intent intentEmail = new Intent(Intent.ACTION_SENDTO);
                        intentEmail.setData(Uri.parse("mailto:" + email));
                        activity.startActivity(intentEmail);
                    } catch (Exception e) {
                    }
                    break;
                case result_payment_token:
                    handleCallbackPaymentToken(paramJson);
                    break;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCallbackPaymentToken(JSONObject jsonObject) {
        activity.finish();

        Intent intentResult = new Intent(activity, ResultPayment.class);
        String status = Constants.SUCCESS;
        try {
            if (jsonObject != null) {
                status = jsonObject.getString("order_status");
                if (status.contains(Constants.FAIL)) {
                    intentResult.putExtra("message", jsonObject.getString("message"));
                }
            }
        } catch (JSONException exception) {
            status = Constants.FAIL;
        }

        intentResult.putExtra("status", status);
        if (DataOrder.Companion.isShowResultScreen()) {
            activity.startActivity(intentResult);
        } else {
            Boolean isSuccess = status.contains(Constants.SUCCESS);
            NPayLibrary.getInstance().callBackToMerchant(
                    NameCallback.SDK_PAYMENT, isSuccess, null);
            NPayLibrary.getInstance().listener.onCloseSDK();
        }

    }

    private void getCallbackFromJs(JSONObject jsonObject) {
        try {
            System.out.println("params " + jsonObject);
            String nameCallback = jsonObject.getString("name");
            Object statusCallback = jsonObject.getString("status");
            String params = null;
            if (jsonObject.has("params")) {
                params = jsonObject.getString("params");
            }

            // Send callback
            NPayLibrary.getInstance().callBackToMerchant(
                    nameCallback, statusCallback, params);


            // Back to SDK
            Boolean isResetPassword = nameCallback.equals("RESET_PASSWORD");
            Boolean isDeposit = nameCallback.equals("DEPOSIT");
            Boolean isLoginSuccess = nameCallback.equals("LOGIN") && (jsonObject.has("status") && jsonObject.getString("status").equals("onSuccess"));


            if (isResetPassword || isDeposit || isLoginSuccess) {

                // Kiem tra co click tu native mobile payment hay khong.
                if (DataOrder.Companion.isProgressing()) {
                    DataOrder.Companion.setProgressing(false);
                    if (isDeposit || !DataOrder.Companion.isStartScreen()) {
                        NPayLibrary.getInstance().getUserInfoSendToPayment(new Runnable() {
                            @Override
                            public void run() {
                                activity.finish();
                            }
                        });
                    } else if (isLoginSuccess) {
                        if (DataOrder.Companion.isStartScreen()) {
                            // Co the la login den tu viec nap tien
                            Intent intent = new Intent(activity, OrderActivity.class);
                            intent.putExtra("method", PaymentMethod.WALLET);
                            intent.putExtra("url", DataOrder.Companion.getUrlData());
                            activity.startActivity(intent);
                        }
                    }
                }
            }
        } catch (JSONException jsonException) {
            System.out.println(jsonException);
        }
    }

    private void requestCamera(Activity activity) {
        if (isHavePermissionCamera()) {
            sendStatusCamera(true);
            return;
        }
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST_CODE);
    }

    private void requestStorage() {
        if (isHavePermissionStorage()) {
            sendStatusStorage(true);
            return;
        }

        String[] typePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13
            typePermission = new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO};
        }

        ActivityCompat.requestPermissions(activity, typePermission, PERMISSION_STORAGE_REQUEST_CODE);
    }

    private boolean isHavePermissionStorage() {
        int checked = PackageManager.PERMISSION_DENIED;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13
            checked = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES);
        } else {
            checked = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        return checked == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isHavePermissionCamera() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    void openSchemaApp(String schema) {
        if (schema == null || schema.isEmpty()) return;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(schema));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Log.d("OPEN APP", ex.getMessage());
        }
    }

    private enum switchCommandJS {
        open9PayApp, close, logout, openOtherUrl, share, copy,
        call, message, clearToken, onLoggedInSuccess, onPaymentSuccess,
        onError, getAllToken, getDeviceID, requestCamera,
        openSchemaApp, requestGallery,
        backToApp,
        callbackToApp, send_email,
        result_payment_token
    }
}