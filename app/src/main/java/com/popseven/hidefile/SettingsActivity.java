package com.popseven.hidefile;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private TextView txtChangePin;
    private Switch switchHideIcon;
    private SharedPreferences sharedPref;
    private TextView txtInfo;
    private TextView txtShare;
    private TextView txtPolicy;
    private TextView txtRateUs;
    private TextView txtMoreApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AdView adView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        sharedPref = getSharedPreferences("com.popseven.hidefile.hideicon", MODE_PRIVATE);

        txtChangePin = findViewById(R.id.txtChangePin);
        switchHideIcon = findViewById(R.id.switchHideIcon);
        txtInfo = findViewById(R.id.txtInfo);
        txtShare = findViewById(R.id.txtShare);
        txtPolicy = findViewById(R.id.txtPolicy);
        txtRateUs = findViewById(R.id.txtRateUs);
        txtMoreApps = findViewById(R.id.txtMoreApps);

        txtChangePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, ChangePinActivity.class));
            }
        });

        if (sharedPref.getBoolean("hideicon", false) == false) {
            switchHideIcon.setChecked(false);
            txtInfo.setVisibility(View.GONE);
        } else {
            switchHideIcon.setChecked(true);
            txtInfo.setVisibility(View.VISIBLE);
        }

        switchHideIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PackageManager packageManager = getPackageManager();
                    ComponentName componentName = new ComponentName(SettingsActivity.this, "com.popseven.hidefile.Launcher");
                    packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                    sharedPref.edit().putBoolean("hideicon", true).commit();
                    txtInfo.setVisibility(View.VISIBLE);
                } else {
                    PackageManager packageManager = getPackageManager();
                    ComponentName componentName = new ComponentName(SettingsActivity.this, "com.popseven.hidefile.Launcher");
                    packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                    sharedPref.edit().putBoolean("hideicon", false).commit();
                    txtInfo.setVisibility(View.GONE);
                }
            }
        });

        txtShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://play.google.com/store/apps/details?id="+getPackageName();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HideFile");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        txtPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        txtRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
            }
        });

        txtMoreApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
