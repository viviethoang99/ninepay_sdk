package com.npsdk.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SetupActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        EditText colorCode = findViewById(R.id.colorCode);
        EditText merchantCode = findViewById(R.id.merchantCode);
        Button btn_open_sdk = findViewById(R.id.btn_open_sdk);

        btn_open_sdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra("color_code", colorCode.getText().toString());
                intent.putExtra("merchant_code", merchantCode.getText().toString());
                v.getContext().startActivity(intent);
            }
        });
    }
}