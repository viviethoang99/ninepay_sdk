package com.npsdk.demo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.npsdk.demo.databinding.ActivityHomeBinding;
import com.npsdk.demo.dialog.ServiceSheet;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnNinepay.setOnClickListener(v -> {
            //show các dịch vụ của ví
            ServiceSheet serviceSheet = new ServiceSheet();
            serviceSheet.show(getSupportFragmentManager(), "ServiceSheet");

        });

    }
}
