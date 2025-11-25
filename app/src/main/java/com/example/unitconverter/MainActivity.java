package com.example.unitconverter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etInputValue;
    private Spinner spinnerSourceUnit;
    private Spinner spinnerTargetUnit;
    private Button btnConvert;


    private final String[] units = {"Metre (m)", "Kilometre (km)", "Mil (mil)",
            "Santigrat (°C)", "Fahrenhayt (°F)",
            "Kilogram (kg)", "Gram (g)", "Pound (lb)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etInputValue = findViewById(R.id.et_input_value);
        spinnerSourceUnit = findViewById(R.id.spinner_source_unit);
        spinnerTargetUnit = findViewById(R.id.spinner_target_unit);
        btnConvert = findViewById(R.id.btn_convert);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerSourceUnit.setAdapter(adapter);
        spinnerTargetUnit.setAdapter(adapter);


        spinnerSourceUnit.setSelection(1); // km
        spinnerTargetUnit.setSelection(2); // mil

        // Buton Dinleyicisini Lambda İfadesiyle Ayarlama (Uyarı Düzeltmesi)
        btnConvert.setOnClickListener(v -> performConversion());
    }

    private void performConversion() {

        String inputStr = etInputValue.getText().toString();

        if (inputStr.isEmpty()) {
            Toast.makeText(this, "Lütfen dönüştürülecek bir değer girin.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double inputValue = Double.parseDouble(inputStr);
            String sourceUnit = spinnerSourceUnit.getSelectedItem().toString();
            String targetUnit = spinnerTargetUnit.getSelectedItem().toString();


            if (sourceUnit.equals(targetUnit)) {
                Toast.makeText(this, "Aynı birimler seçilemez.", Toast.LENGTH_LONG).show();
                return;
            }


            double result = convertValue(inputValue, sourceUnit, targetUnit);


            if (result == 0.0 && isDifferentType(sourceUnit, targetUnit)) {
                return;
            }


            Intent intent = new Intent(MainActivity.this, ResultActivity.class);

            intent.putExtra("KEY_INPUT_VALUE", inputValue);
            intent.putExtra("KEY_SOURCE_UNIT", sourceUnit);
            intent.putExtra("KEY_TARGET_UNIT", targetUnit);
            intent.putExtra("KEY_RESULT", result);

            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Geçersiz sayısal giriş.", Toast.LENGTH_SHORT).show();
        }
    }

    // Basit dönüşüm mantığı
    private double convertValue(double value, String source, String target) {
        try {

            if (isDifferentType(source, target)) {
                throw new IllegalArgumentException("Hata: Farklı kategorideki birimler dönüştürülemez.");
            }


            double baseValue = toBaseUnit(value, source);


            return fromBaseUnit(baseValue, target);

        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return 0.0;
        }
    }

    // ********** Yardımcı Fonksiyonlar **********

    private String getUnitType(String unit) {
        if (unit.contains("Metre") || unit.contains("Kilometre") || unit.contains("Mil")) {
            return "MESAFE";
        } else if (unit.contains("Santigrat") || unit.contains("Fahrenhayt")) {
            return "SICAKLIK";
        } else if (unit.contains("Kilogram") || unit.contains("Gram") || unit.contains("Pound")) {
            return "AĞIRLIK";
        }
        return "BILINMEYEN";
    }


    private boolean isDifferentType(String source, String target) {
        return !getUnitType(source).equals(getUnitType(target));
    }


    private double toBaseUnit(double value, String unit) {

        if (unit.contains("Metre")) return value;
        if (unit.contains("Kilometre")) return value * 1000.0;
        if (unit.contains("Mil")) return value * 1609.34;


        if (unit.contains("Santigrat")) return value;
        if (unit.contains("Fahrenhayt")) return (value - 32) * 5 / 9;


        if (unit.contains("Kilogram")) return value;
        if (unit.contains("Gram")) return value / 1000.0;
        if (unit.contains("Pound")) return value / 2.20462;

        throw new IllegalArgumentException("Hata: Bilinmeyen kaynak birimi.");
    }


    private double fromBaseUnit(double baseValue, String target) {

        if (target.contains("Metre")) return baseValue;
        if (target.contains("Kilometre")) return baseValue / 1000.0;
        if (target.contains("Mil")) return baseValue / 1609.34;


        if (target.contains("Santigrat")) return baseValue;
        if (target.contains("Fahrenhayt")) return (baseValue * 9 / 5) + 32;


        if (target.contains("Kilogram")) return baseValue;
        if (target.contains("Gram")) return baseValue * 1000.0;
        if (target.contains("Pound")) return baseValue * 2.20462;

        throw new IllegalArgumentException("Hata: Bilinmeyen hedef birimi.");
    }
}