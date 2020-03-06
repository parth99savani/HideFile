package com.popseven.hidefile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nineoldandroids.view.ViewHelper;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class HomeActivity extends AppCompatActivity implements ObservableScrollViewCallbacks, View.OnClickListener {

    private RelativeLayout image;
    private ObservableScrollView scroll;
    private int mParallaxImageHeight;
    private Toolbar toolbar;
    private TextView txtTitle;
    private ImageButton btnSettins;
    private RelativeLayout body;
    private CardView btnImage;
    private CardView btnVideo;
    private CardView btnAudio;
    private CardView btnDoc;
    private static final int REQUEST_PERMISSIONS = 100;
    private ImageButton ibImage;
    private ImageButton ibVideo;
    private ImageButton ibAudio;
    private ImageButton ibDoc;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AdView adView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        File dirImg = new File(getFilesDir()+"/image");
        if(!dirImg.exists()) {
            dirImg.mkdir(); //directory is created;
        }
        File dirVid = new File(getFilesDir()+"/video");
        if(!dirVid.exists()) {
            dirVid.mkdir(); //directory is created;
        }
        File dirAud = new File(getFilesDir()+"/audio");
        if(!dirAud.exists()) {
            dirAud.mkdir(); //directory is created;
        }
        File dirDoc = new File(getFilesDir()+"/document");
        if(!dirDoc.exists()) {
            dirDoc.mkdir(); //directory is created;
        }


        image = findViewById(R.id.image);
        scroll = findViewById(R.id.scroll);
        toolbar = findViewById(R.id.toolbar);
        txtTitle = findViewById(R.id.txtTitle);
        btnSettins = findViewById(R.id.btnSettins);
        body = findViewById(R.id.body);
        btnImage = findViewById(R.id.btnImage);
        btnVideo = findViewById(R.id.btnVideo);
        btnAudio = findViewById(R.id.btnAudio);
        btnDoc = findViewById(R.id.btnDoc);
        ibImage = findViewById(R.id.ibImage);
        ibVideo = findViewById(R.id.ibVideo);
        ibAudio = findViewById(R.id.ibAudio);
        ibDoc = findViewById(R.id.ibDoc);


        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.colorPrimary)));
        scroll.setScrollViewCallbacks(this);

        mParallaxImageHeight = 250;


        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) body.getLayoutParams();
        params.height = height - 300;
        body.setLayoutParams(params);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)+ActivityCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.PROCESS_OUTGOING_CALLS}, REQUEST_PERMISSIONS);
            }
        }

        btnImage.setOnClickListener(this);
        btnVideo.setOnClickListener(this);
        btnAudio.setOnClickListener(this);
        btnDoc.setOnClickListener(this);
        ibImage.setOnClickListener(this);
        ibVideo.setOnClickListener(this);
        ibAudio.setOnClickListener(this);
        ibDoc.setOnClickListener(this);

        btnSettins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,SettingsActivity.class));
            }
        });

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(scroll.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.colorPrimary);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        toolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(image, scrollY / 2);
        if (scrollY >= 100) {
            txtTitle.setVisibility(View.VISIBLE);
        } else if (scrollY < 100) {
            txtTitle.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public void onClick(View v) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)+ActivityCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.PROCESS_OUTGOING_CALLS}, REQUEST_PERMISSIONS);
            }
        }else {
            switch (v.getId()){
                case R.id.btnImage:
                    startActivity(new Intent(HomeActivity.this, ImageFolderListActivity.class));
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    break;
                case R.id.btnVideo:
                    startActivity(new Intent(HomeActivity.this, VideoFolderListActivity.class));
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    break;
                case R.id.btnAudio:
                    startActivity(new Intent(HomeActivity.this, AudioFolderListActivity.class));
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    break;
                case R.id.btnDoc:
                    startActivity(new Intent(HomeActivity.this, DocFolderListActivity.class));
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    break;
                case R.id.ibImage:
                    startActivity(new Intent(HomeActivity.this, ImageFolderListActivity.class));
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    break;
                case R.id.ibVideo:
                    startActivity(new Intent(HomeActivity.this, VideoFolderListActivity.class));
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    break;
                case R.id.ibAudio:
                    startActivity(new Intent(HomeActivity.this, AudioFolderListActivity.class));
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    break;
                case R.id.ibDoc:
                    startActivity(new Intent(HomeActivity.this, DocFolderListActivity.class));
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                    } else {
                        Toast.makeText(HomeActivity.this, "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        super.onResume();
    }
}
