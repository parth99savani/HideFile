package com.popseven.hidefile;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;

public class ChangePinActivity extends AppCompatActivity {

    private TextInputEditText editPin;
    private TextInputEditText editConfirmPin;
    private Button btnSavePin;
    private SharedPreferences sharedPref;
    private String pin,changePin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AdView adView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        editPin = findViewById(R.id.editPin);
        editConfirmPin = findViewById(R.id.editConfirmPin);
        btnSavePin = findViewById(R.id.btnSavePin);

        sharedPref = getSharedPreferences("com.popseven.hidefile.loginpin",MODE_PRIVATE);

        btnSavePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin = editPin.getText().toString();
                changePin = editConfirmPin.getText().toString();
                if(pin.matches("[0-9]+") && pin.length() > 3 && pin.length() < 9){
                    if(changePin.matches("[0-9]+") && changePin.length() > 3 && changePin.length() < 9){
                        if(changePin.matches(pin)){
                            sharedPref.edit().putInt("loginpin", Integer.valueOf(pin)).commit();
                            Toast.makeText(ChangePinActivity.this, "Pin changed successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            editConfirmPin.setError("Please, Enter same pin number.");
                        }
                    }else {
                        editConfirmPin.setError("Please, Enter valid pin between 4 to 8 numbers.");
                    }
                }else{
                    editPin.setError("Please, Enter valid pin between 4 to 8 numbers.");
                }

            }
        });
    }
}
