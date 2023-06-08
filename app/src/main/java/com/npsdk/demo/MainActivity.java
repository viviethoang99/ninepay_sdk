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
import com.npsdk.jetpack_sdk.WebviewActivity;
import com.npsdk.module.NPayLibrary;
import com.npsdk.module.model.SdkConfig;
import com.npsdk.module.utils.Actions;
import com.npsdk.module.utils.Flavor;

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
            public void getInfoSuccess(String phone, String balance, String ekycStatus) {
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
        switch (v.getId()) {
            case R.id.ll_quet_ma:
                Log.d(TAG, "onClick: ll_rut_tien");
                NPayLibrary.getInstance().openWallet(Actions.QR);
                break;
            case R.id.ll_nap_tien:
                Log.d(TAG, "onClick: ll_nap_tien");
                NPayLibrary.getInstance().openWallet(Actions.DEPOSIT);
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
                NPayLibrary.getInstance().openWallet(Actions.TOPUP_PHONE_CARD);
                break;
            case R.id.ll_mua_the_game:
                Log.d(TAG, "onClick: ll_mua_the_game");
                NPayLibrary.getInstance().openWallet(Actions.MG_CARD);
                break;
            case R.id.ll_mua_the_dich_vu:
                Log.d(TAG, "onClick: ll_mua_the_dich_vu");
                NPayLibrary.getInstance().openWallet(Actions.BUY_SERVICES_CARD);
                break;
            case R.id.ll_mua_the_dt:
                Log.d(TAG, "onClick: ll_mua_the_dt");
                NPayLibrary.getInstance().openWallet(Actions.BUY_PHONE_CARD);
                break;
            case R.id.ll_nap_data:
                Log.d(TAG, "onClick: ll_nap_data");
                NPayLibrary.getInstance().openWallet(Actions.TOPUP_DATA_CARD);
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
                String url = edtUrlPaygate.getText().toString();
                if (url.isEmpty())
                    url = "https://dev-payment.9pay.mobi/portal?baseEncode=eyJtZXJjaGFudEtleSI6Ik5yeDl3VyIsInRpbWUiOjE2ODU0OTkzMDMsImludm9pY2Vfbm8iOiJkOEFFMGJKdyIsImFtb3VudCI6MTAwMDAsImRlc2NyaXB0aW9uIjoiVGhpcyBpcyBkZXNjcmlwdGlvbiIsInJldHVybl91cmwiOiJodHRwOi8vZmNkY2M0NzY3YWNiLm5ncm9rLmlvLyIsImJhY2tfdXJsIjoiaHR0cDovL2ZjZGNjNDc2N2FjYi5uZ3Jvay5pby8iLCJtZXRob2QiOiI5UEFZIn0%3D&signature=LcASMO3nwXivlpUrIiG%2B%2FEwCZAXOIcGYTtC99nk3BRk%3D";
                NPayLibrary.getInstance().payWithWallet(url, "WALLET");
                edtUrlPaygate.setText("");
                break;
            case R.id.btn_thanh_toan2:
                //paste url thanh toán vào hàm pay
                String url2 = edtUrlPaygate.getText().toString();
                if (url2.isEmpty())
                    url2 = "https://dev-payment.9pay.mobi/portal?baseEncode=eyJtZXJjaGFudEtleSI6Ik5yeDl3VyIsInRpbWUiOjE2ODU0OTkzMDMsImludm9pY2Vfbm8iOiJkOEFFMGJKdyIsImFtb3VudCI6MTAwMDAsImRlc2NyaXB0aW9uIjoiVGhpcyBpcyBkZXNjcmlwdGlvbiIsInJldHVybl91cmwiOiJodHRwOi8vZmNkY2M0NzY3YWNiLm5ncm9rLmlvLyIsImJhY2tfdXJsIjoiaHR0cDovL2ZjZGNjNDc2N2FjYi5uZ3Jvay5pby8iLCJtZXRob2QiOiI5UEFZIn0%3D&signature=LcASMO3nwXivlpUrIiG%2B%2FEwCZAXOIcGYTtC99nk3BRk%3D";
                NPayLibrary.getInstance().payWithWallet(url2, "ATM_CARD");
                edtUrlPaygate.setText("");
                break;
            case R.id.btn_thanh_toan3:
                //paste url thanh toán vào hàm pay
                String url3 = edtUrlPaygate.getText().toString();
                if (url3.isEmpty())
                    url3 = "https://dev-payment.9pay.mobi/portal?baseEncode=eyJtZXJjaGFudEtleSI6Ik5yeDl3VyIsInRpbWUiOjE2ODU0OTkzMDMsImludm9pY2Vfbm8iOiJkOEFFMGJKdyIsImFtb3VudCI6MTAwMDAsImRlc2NyaXB0aW9uIjoiVGhpcyBpcyBkZXNjcmlwdGlvbiIsInJldHVybl91cmwiOiJodHRwOi8vZmNkY2M0NzY3YWNiLm5ncm9rLmlvLyIsImJhY2tfdXJsIjoiaHR0cDovL2ZjZGNjNDc2N2FjYi5uZ3Jvay5pby8iLCJtZXRob2QiOiI5UEFZIn0%3D&signature=LcASMO3nwXivlpUrIiG%2B%2FEwCZAXOIcGYTtC99nk3BRk%3D";
                NPayLibrary.getInstance().payWithWallet(url3, "CREDIT_CARD");
                edtUrlPaygate.setText("");

                break;
            case R.id.test_click:
                String url4 = edtUrlPaygate.getText().toString();
                if (url4.isEmpty()) url4 = "https://dev-payment.9pay.mobi/portal?baseEncode=eyJtZXJjaGFudEtleSI6Ik5yeDl3VyIsInRpbWUiOjE2ODYxODg2MTIsImludm9pY2Vfbm8iOiJTODc1WlI5QyIsImFtb3VudCI6MTAwMDAsImRlc2NyaXB0aW9uIjoiVGhpcyBpcyBkZXNjcmlwdGlvbiIsInJldHVybl91cmwiOiJodHRwOi8vZmNkY2M0NzY3YWNiLm5ncm9rLmlvLyIsImJhY2tfdXJsIjoiaHR0cDovL2ZjZGNjNDc2N2FjYi5uZ3Jvay5pby8iLCJtZXRob2QiOiI5UEFZIn0%3D&signature=BNofxRWOLe51atSN5COIzMCdBv2wIiGho9Q2pfHrlvs%3D";
                NPayLibrary.getInstance().payWithWallet(url4, null);
                edtUrlPaygate.setText("");
//                Intent i = new Intent(this, WebviewActivity.class);
//                i.putExtra("url", "https://zing.vn");
//                startActivity(i);
                break;

        }

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