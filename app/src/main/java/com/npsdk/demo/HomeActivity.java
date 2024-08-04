package com.npsdk.demo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.npsdk.LibListener;
import com.npsdk.demo.databinding.ActivityHomeBinding;
import com.npsdk.demo.dialog.ServiceSheet;
import com.npsdk.module.NPayLibrary;
import com.npsdk.module.model.SdkConfig;
import com.npsdk.module.utils.Actions;
import com.npsdk.module.utils.Flavor;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Create flavor by package name test
        String flavorEnv = Flavor.setEnvTest(this);
        String mcCode = "uymvnd";
        String colorCode = "15AE62";
        String secretKey = "vIPldW/y/VJuy8qKEQUoH9ypHTTt9W/8ufvn3BFFTBU="; // vIPldW/y/VJuy8qKEQUoH9ypHTTt9W/8ufvn3BFFTBU=
        SdkConfig sdkConfig = new SdkConfig.Builder(this).merchantCode(mcCode).secretKey(secretKey).uid(null).env(flavorEnv).brandColor(colorCode).build();
        initSdk(sdkConfig);


        binding.btnNinepay.setOnClickListener(v -> {
            //show các dịch vụ của ví
            ServiceSheet serviceSheet = new ServiceSheet();
            serviceSheet.show(getSupportFragmentManager(), "ServiceSheet");

        });

        binding.btnLogin.setOnClickListener(v -> {
            NPayLibrary.getInstance().openSDKWithAction(Actions.LOGIN);
        });

    }

    private void initSdk(SdkConfig sdkConfig) {
        NPayLibrary.getInstance().init(HomeActivity.this, sdkConfig, new LibListener() {

            @Override
            public void getInfoSuccess(String jsonData) {
                System.out.println(jsonData);
            }

            @Override
            public void onError(int errorCode, String message) {
                Toast.makeText(HomeActivity.this, message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLogoutSuccessful() {
                Toast.makeText(HomeActivity.this, "Logout success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCloseSDK() {
                Toast.makeText(HomeActivity.this, "onCloseSDK", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void sdkDidComplete(String name, Object status, @Nullable Object params) {
                Toast.makeText(HomeActivity.this, name + " " + status, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void backToAppFrom(String screen) {
                System.out.println(screen);
            }
        });

        boolean test = NPayLibrary.getInstance().isLogin();


        Log.d("isLogin", "isLogin: " + test);

    }
}
