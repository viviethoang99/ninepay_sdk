package com.npsdk.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.npsdk.LibListener;
import com.npsdk.jetpack_sdk.PasswordActivity;
import com.npsdk.jetpack_sdk.WebviewActivity;
import com.npsdk.jetpack_sdk.WebviewComposeActivity;
import com.npsdk.module.NPayLibrary;
import com.npsdk.module.model.Bank;
import com.npsdk.module.model.SdkConfig;
import com.npsdk.module.utils.*;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "MainActivityLOG";
    TextView userInfo;
    TextView txtMoney;
    WebView webViewGate;
    LinearLayout layoutGate;
    boolean isShow = false;
    Toolbar toolbar;
    EditText edtUrlPaygate;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout llRutTien = findViewById(R.id.ll_quet_ma);
        LinearLayout llNapTien = findViewById(R.id.ll_nap_tien);
        LinearLayout llChuyenTien = findViewById(R.id.ll_chuyen_tien);
        LinearLayout llTienIch = findViewById(R.id.ll_lich_su);
        LinearLayout llMuaTheDt = findViewById(R.id.ll_thanh_toan_hoa_don);
        LinearLayout llNapTheDT = findViewById(R.id.ll_nap_tien_dt);
        LinearLayout llNapData = findViewById(R.id.ll_mua_the_game);
        LinearLayout ll_mua_the_dich_vu = findViewById(R.id.ll_mua_the_dich_vu);
        LinearLayout ll_mua_the_dt = findViewById(R.id.ll_mua_the_dt);
        LinearLayout ll_nap_data = findViewById(R.id.ll_nap_data);
        LinearLayout testClick = findViewById(R.id.test_click);
        LinearLayout layout_sdv = findViewById(R.id.layout_sdv);
        RelativeLayout rlInfo = findViewById(R.id.rl_info);
        ImageView btn_eyes = findViewById(R.id.btn_eyes);
        Button btn_bank_link_manage = findViewById(R.id.btn_bank_link_manage);
        Button btn_bank_link_add = findViewById(R.id.btn_bank_link_add);
        edtUrlPaygate = findViewById(R.id.edt_url_paygate);
        Button btnThanhToan = findViewById(R.id.btn_thanh_toan);
        Button btnThanhToan2 = findViewById(R.id.btn_thanh_toan2);
        Button btnThanhToan3 = findViewById(R.id.btn_thanh_toan3);
        View btnClose = findViewById(R.id.btnClose);
        txtMoney = findViewById(R.id.txt_money);
        userInfo = findViewById(R.id.txt_name);
        webViewGate = findViewById(R.id.webView_gate);
        layoutGate = findViewById(R.id.layout_web_gate);
        toolbar = findViewById(R.id.toolbar);
        btnClose = findViewById(R.id.btnClose);

        txtMoney.setTransformationMethod(new AsteriskPasswordTransformationMethod());

        llRutTien.setOnClickListener(this);
        llNapTien.setOnClickListener(this);
        llChuyenTien.setOnClickListener(this);
        llTienIch.setOnClickListener(this);
        llMuaTheDt.setOnClickListener(this);
        llNapTheDT.setOnClickListener(this);
        llNapData.setOnClickListener(this);
        ll_mua_the_dich_vu.setOnClickListener(this);
        ll_mua_the_dt.setOnClickListener(this);
        ll_nap_data.setOnClickListener(this);
        btn_bank_link_manage.setOnClickListener(this);
        btn_bank_link_add.setOnClickListener(this);
        layout_sdv.setOnClickListener(this);
        btn_eyes.setOnClickListener(this);
        rlInfo.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnThanhToan.setOnClickListener(this);
        btnThanhToan2.setOnClickListener(this);
        btnThanhToan3.setOnClickListener(this);
        testClick.setOnClickListener(this);
        // Create flavor by packagen name test
        String flavorEnv = Flavor.setEnvTest(this);
        SdkConfig sdkConfig = new SdkConfig.Builder(this).merchantCode("sdk_test").uid(null).env(flavorEnv).brandColor(0xff15AE62).build();
        initSdk(sdkConfig);

//		Intent intent = new Intent(this, PasswordActivity.class);
//		intent.putExtra("url", url);
//		startActivity(intent);
    }


    private void initSdk(SdkConfig sdkConfig) {
        NPayLibrary.getInstance().init(MainActivity.this, sdkConfig, new LibListener() {
            @Override
            public void onLoginSuccessful() {

            }

            @Override
            public void onPaySuccessful() {

            }

            @Override
            public void getInfoSuccess(String phone, String balance, String ekycStatus, List<Bank> listBank) {
                userInfo.setText("Hi," + phone);
                txtMoney.setText(balance + "đ");
            }

            @Override
            public void onError(int errorCode, String message) {

            }

            @Override
            public void onLogoutSuccessful() {
                System.out.println("Logout success!");
            }

            @Override
            public void onCloseSDK() {

            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        String url = edtUrlPaygate.getText().toString();
        if (url.isEmpty())
            url = "https://dev-payment.9pay.mobi/portal?baseEncode=eyJtZXJjaGFudEtleSI6Ik5yeDl3VyIsInRpbWUiOjE2ODcyNDA2MDQsImludm9pY2Vfbm8iOiJ4NzRTT216bSIsImFtb3VudCI6MTAwMDAwLCJkZXNjcmlwdGlvbiI6Ik11YSBoYW5nIDlQYXkiLCJyZXR1cm5fdXJsIjoiaHR0cDovL2ZjZGNjNDc2N2FjYi5uZ3Jvay5pby8iLCJiYWNrX3VybCI6Imh0dHA6Ly9mY2RjYzQ3NjdhY2Iubmdyb2suaW8vIiwibWV0aG9kIjoiOVBBWSIsImlzX2N1c3RvbWVyX3BheV9mZWUiOjF9&signature=H9B%2B0ujl3D4joXSr3Eno5u0L%2FgHPnXn8obKNbqaHW1I%3D";
        switch (v.getId()) {
            case R.id.ll_quet_ma:
                Log.d(TAG, "onClick: ll_rut_tien");
                NPayLibrary.getInstance().openWallet(Actions.WITHDRAW);
                break;
            case R.id.ll_nap_tien:
                Log.d(TAG, "onClick: ll_nap_tien");
                String phoneDeposit = Preference.getString(this, Flavor.prefKey + Constants.PHONE, "");
                NPayLibrary.getInstance().openWallet(Actions.deposit(phoneDeposit, null));
                break;
            case R.id.ll_chuyen_tien:
                NPayLibrary.getInstance().openWallet(Actions.TRANSFER);
                Log.d(TAG, "onClick: ll_chuyen_tien");
                break;
            case R.id.ll_lich_su:
                Log.d(TAG, "onClick: ll_lich_su");
                NPayLibrary.getInstance().openWallet(Actions.HISTORY);
                break;
            case R.id.ll_thanh_toan_hoa_don:
                Log.d(TAG, "onClick: ll_thanh_toan_hoa_don");
                NPayLibrary.getInstance().openWallet(Actions.SHOP);

                break;
            case R.id.ll_nap_tien_dt:
                Log.d(TAG, "onClick: ll_nap_tien_dt");
                NPayLibrary.getInstance().openWallet(Actions.TOPUP);
                break;
            case R.id.ll_mua_the_game:
                Log.d(TAG, "onClick: ll_mua_the_game");
                NPayLibrary.getInstance().openWallet(Actions.GAME);
                break;
            case R.id.ll_mua_the_dich_vu:
                Log.d(TAG, "onClick: ll_mua_the_dich_vu");
                NPayLibrary.getInstance().openWallet(Actions.SERVICE_CARD);
                break;
            case R.id.ll_mua_the_dt:
                Log.d(TAG, "onClick: ll_mua_the_dt");
                NPayLibrary.getInstance().openWallet(Actions.PHONE_CARD);
                break;
            case R.id.ll_nap_data:
                Log.d(TAG, "onClick: ll_nap_data");
                NPayLibrary.getInstance().openWallet(Actions.DATA_CARD);
                break;
            case R.id.btn_bank_link_manage:
                Log.d(TAG, "onClick: btn_bank_link_manage");
//                NPayLibrary.getInstance().openWallet(Actions.BANK_LINK_MANAGE);
                break;
            case R.id.btn_bank_link_add:
                Log.d(TAG, "onClick: btn_bank_link_add");
//                NPayLibrary.getInstance().openWallet(Actions.ADD_LINK_BANK);
                break;
            case R.id.btn_eyes:
                Log.d(TAG, "onClick: btn_eyes");
                if (isShow) {
                    txtMoney.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    txtMoney.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                }
                isShow = !isShow;
                break;
            case R.id.layout_sdv:
            case R.id.rl_info:
                Log.d(TAG, "onClick: layout_sdv");
                NPayLibrary.getInstance().openWallet(Actions.OPEN_WALLET);
//				String url = "https://dev-payment.9pay.mobi/portal?baseEncode=eyJtZXJjaGFudEtleSI6Ik5yeDl3VyIsInRpbWUiOjE2ODU2MzExNzQsImludm9pY2Vfbm8iOiJCaVJSWnhzRSIsImFtb3VudCI6MTAwMDAsImRlc2NyaXB0aW9uIjoiVGhpcyBpcyBkZXNjcmlwdGlvbiIsInJldHVybl91cmwiOiJodHRwOi8vZmNkY2M0NzY3YWNiLm5ncm9rLmlvLyIsImJhY2tfdXJsIjoiaHR0cDovL2ZjZGNjNDc2N2FjYi5uZ3Jvay5pby8iLCJtZXRob2QiOiI5UEFZIn0%3D&signature=btYIvja%2B3ca4m%2Fy7g%2FtcIxxhzHgrJ7FM46seHsfTSWY%3D";
//				Intent intent = new Intent(v.getContext(), OrderActivity.class);
//				intent.putExtra("url", url);
//				startActivity(intent);
                break;
            case R.id.btnClose:
                layoutGate.setVisibility(View.GONE);
                break;
            case R.id.btn_thanh_toan:
                //paste url thanh toán vào hàm pay

                NPayLibrary.getInstance().payWithWallet(url, Constants.WALLET);
                edtUrlPaygate.setText("");
                break;
            case R.id.btn_thanh_toan2:
                NPayLibrary.getInstance().payWithWallet(url, Constants.ATM_CARD);
                edtUrlPaygate.setText("");
                break;
            case R.id.btn_thanh_toan3:
                //paste url thanh toán vào hàm pay
                NPayLibrary.getInstance().payWithWallet(url, Constants.CREDIT_CARD);
                break;
            case R.id.test_click:
                NPayLibrary.getInstance().payWithWallet(url, null);
//                NPayLibrary.getInstance().openWallet(url);
//                String old = Preference.getString(this, Flavor.prefKey + Constants.ACCESS_TOKEN, "");
//                Preference.save(this, NPayLibrary.getInstance().sdkConfig.getEnv() + Constants.ACCESS_TOKEN, old + "a");
//                Intent i = new Intent(this, WebviewComposeActivity.class);
//                i.putExtra("url", "https://zing.vn");
//                startActivity(i);
                break;
        }
        edtUrlPaygate.setText("");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}

class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private final CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            mSource = source; // Store char sequence
        }

        public char charAt(int index) {
            return '*'; // This is the important part
        }

        public int length() {
            return mSource.length(); // Return default
        }

        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }
}