package com.example.unitconverter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    private TextView tvDetailInfo;
    private TextView tvCalculatedResult;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        tvDetailInfo = findViewById(R.id.tv_detail_info);
        tvCalculatedResult = findViewById(R.id.tv_calculated_result);
        btnBack = findViewById(R.id.btn_back);


        Intent intent = getIntent();
        if (intent != null) {
            double inputValue = intent.getDoubleExtra("KEY_INPUT_VALUE", 0.0);
            String sourceUnit = intent.getStringExtra("KEY_SOURCE_UNIT");
            String targetUnit = intent.getStringExtra("KEY_TARGET_UNIT");
            double result = intent.getDoubleExtra("KEY_RESULT", 0.0);


            String detailText = String.format(Locale.getDefault(), "%.4f %s değeri, %s birimine dönüştürüldü.",
                    inputValue, sourceUnit, targetUnit);
            tvDetailInfo.setText(detailText);


            String resultText = String.format(Locale.getDefault(), "%.4f", result);


            if (sourceUnit.contains("Santigrat") || sourceUnit.contains("Fahrenhayt")) {
                resultText = String.format(Locale.getDefault(), "%.2f", result);
            }


            String finalResult = resultText + getUnitAbbreviation(targetUnit);
            tvCalculatedResult.setText(finalResult);
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String getUnitAbbreviation(String unit) {
        if (unit.contains("Metre")) return " m";
        if (unit.contains("Kilometre")) return " km";
        if (unit.contains("Mil")) return " mil";
        if (unit.contains("Santigrat")) return " °C";
        if (unit.contains("Fahrenhayt")) return " °F";
        if (unit.contains("Kilogram")) return " kg";
        if (unit.contains("Gram")) return " g";
        if (unit.contains("Pound")) return " lb";
        return "";
    }
}