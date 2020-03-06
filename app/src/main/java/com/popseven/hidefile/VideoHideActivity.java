package com.popseven.hidefile;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.popseven.hidefile.adapter.VideoAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VideoHideActivity extends AppCompatActivity implements VideoAdapter.VideoAdapterListener {

    private ImageButton btnBack;
    private TextView txtFolderName;
    private RadioButton radioBtnAll;
    private RelativeLayout rlRadiobtn;
    private ImageButton btnDelete;
    private RecyclerView recyclerViewVideo;
    private FloatingActionButton fabAdd;
    private Button btnUnhide;
    private ArrayList<String> videoList = new ArrayList<>();
    private String folderName;
    private VideoAdapter adapter;
    private int n = 0;
    private static ArrayList<Integer> selectedArrayList = new ArrayList<>();
    private File dir;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_hide);

        AdView adView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        btnBack = findViewById(R.id.btnBack);
        txtFolderName = findViewById(R.id.txtFolderName);
        radioBtnAll = findViewById(R.id.radioBtnAll);
        rlRadiobtn = findViewById(R.id.rlRadiobtn);
        btnDelete = findViewById(R.id.btnDelete);
        recyclerViewVideo = findViewById(R.id.recyclerViewVideo);
        fabAdd = findViewById(R.id.fabAdd);
        btnUnhide = findViewById(R.id.btnUnhide);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            folderName = bundle.getString("folderName");
            txtFolderName.setText(folderName);
        }

        recyclerViewVideo.setLayoutManager(new GridLayoutManager(this,2));
        recyclerViewVideo.setHasFixedSize(true);

        File dirUnhide = new File(Environment.getExternalStorageDirectory()+"/HideFile");
        if(!dirUnhide.exists()) {
            dirUnhide.mkdir(); //directory is created;
        }
        dir = new File(Environment.getExternalStorageDirectory()+"/HideFile/Video");
        if(!dir.exists()) {
            dir.mkdir(); //directory is created;
        }

        reloadRecyclerView();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoHideActivity.this,SelectVideoHideActivity.class);
                intent.putExtra("folderName",folderName);
                startActivity(intent);
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });

        rlRadiobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioBtnAll.isChecked()){
                    radioBtnAll.setChecked(false);
                    n=0;
                    radioBtnAll.setText(String.valueOf(n));
                    adapter = new VideoAdapter(VideoHideActivity.this,videoList,VideoHideActivity.this,false);
                    recyclerViewVideo.setAdapter(adapter);
                    for(int i = 0; i<videoList.size(); i++){
                        if (selectedArrayList.contains(Integer.valueOf(i))) {
                            selectedArrayList.remove(Integer.valueOf(i));
                        }
                    }
                    rlRadiobtn.setVisibility(View.GONE);
                    btnUnhide.setVisibility(View.GONE);
                    btnDelete.setVisibility(View.GONE);
                }else {
                    radioBtnAll.setChecked(true);
                    n=videoList.size();
                    radioBtnAll.setText("All");
                    adapter = new VideoAdapter(VideoHideActivity.this,videoList,VideoHideActivity.this,true);
                    recyclerViewVideo.setAdapter(adapter);
                    for(int i = 0; i<n; i++){
                        if (!selectedArrayList.contains(Integer.valueOf(i))) {
                            selectedArrayList.add(Integer.valueOf(i));
                        }
                    }
                }
            }
        });

        btnUnhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<selectedArrayList.size();i++){
                    File file = new File(videoList.get(selectedArrayList.get(i)));
                    moveFile(videoList.get(selectedArrayList.get(i)),file.getName(),dir.getAbsolutePath()+"/");
                }
                selectedArrayList.clear();
                n=0;
                radioBtnAll.setChecked(false);
                rlRadiobtn.setVisibility(View.GONE);
                btnUnhide.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                reloadRecyclerView();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDelete = new AlertDialog.Builder(VideoHideActivity.this);
                alertDelete.setMessage("Are you want to delete this video?");

                alertDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        for(int i=0;i<selectedArrayList.size();i++){
                            File file = new File(videoList.get(selectedArrayList.get(i)));
                            deleteDir(file);
                        }
                        selectedArrayList.clear();
                        n=0;
                        radioBtnAll.setChecked(false);
                        rlRadiobtn.setVisibility(View.GONE);
                        btnUnhide.setVisibility(View.GONE);
                        btnDelete.setVisibility(View.GONE);
                        reloadRecyclerView();
                    }
                });

                alertDelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });

                alertDelete.show();
            }
        });


    }

    private void deleteDir(File file) {
        if (file.isDirectory())
            for (String child : file.list())
                deleteDir(new File(file, child));
        file.delete();// delete child file or empty directory
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        } else{

            MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {

                public void onScanCompleted(String path, Uri uri)
                {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });

        }
    }

    private void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            // delete the original file
            new File(inputPath).delete();

            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            } else{

                MediaScannerConnection.scanFile(this, new String[]{inputPath,outputPath+inputFile}, null, new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri)
                    {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });

            }

        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    private void reloadRecyclerView(){
        videoList.clear();
        loadImageFiles(new File(getFilesDir().getAbsolutePath()+"/video/"+folderName));

        adapter = new VideoAdapter(this,videoList,this,false);
        recyclerViewVideo.setAdapter(adapter);
    }

    private void loadImageFiles(File dir) {

        File[] listFile = dir.listFiles();
        ArrayList<String> al_path = new ArrayList<>();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    loadImageFiles(listFile[i]);
                } else {
                    String name = listFile[i].getName();
                    if ((name.endsWith(".mp4") || name.endsWith(".3gp") || name.endsWith(".avi")) && Pattern.compile("(.*)((\\.(mp4||3gp||avi))$)", 2).matcher(name).matches()) {
                        al_path.add(listFile[i].getAbsolutePath());
                    }
                }
            }
        }
        videoList.addAll(al_path);
    }

    @Override
    protected void onResume() {
        reloadRecyclerView();
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        super.onResume();
    }

    @Override
    public void onRadioBtnSelected(int i, VideoAdapter.MyViewHolder holder) {
        if (holder.radiobtnSelect.isChecked()) {
            holder.radiobtnSelect.setChecked(false);
            n--;
            radioBtnAll.setText(String.valueOf(n));
            radioBtnAll.setChecked(false);
            if (selectedArrayList.contains(Integer.valueOf(i))) {
                selectedArrayList.remove(Integer.valueOf(i));
            }
        } else {
            holder.radiobtnSelect.setChecked(true);
            n++;
            radioBtnAll.setText(String.valueOf(n));
            if (!selectedArrayList.contains(Integer.valueOf(i))) {
                selectedArrayList.add(Integer.valueOf(i));
            }
        }

        if (n > 0) {
            rlRadiobtn.setVisibility(View.VISIBLE);
            btnUnhide.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            rlRadiobtn.setVisibility(View.GONE);
            btnUnhide.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public void onVideoSelected(String video, VideoAdapter.MyViewHolder holder) {
        Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", new File(video));

        Intent openVideoIntent = new Intent();
        openVideoIntent.setAction(Intent.ACTION_VIEW)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .setDataAndType(
                        fileUri,
                        getContentResolver().getType(fileUri));
        startActivity(openVideoIntent);
    }
}
