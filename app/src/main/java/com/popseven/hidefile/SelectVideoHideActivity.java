package com.popseven.hidefile;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.popseven.hidefile.adapter.GallaryVideoAdapter;
import com.popseven.hidefile.model.VideoModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectVideoHideActivity extends AppCompatActivity implements GallaryVideoAdapter.GallaryVideoAdapterListener {

    private ImageButton btnBack;
    private Spinner spinnerFolder;
    private RadioButton radioBtnAll;
    private RelativeLayout rlRadiobtn;
    private RecyclerView recyclerViewVideo;
    private Button btnHide;
    private GallaryVideoAdapter adapter;
    private static ArrayList<VideoModel> videoList = new ArrayList<>();
    private boolean booleanFolder;
    private String folderName;
    private int n = 0;
    private static ArrayList<Integer> selectedArrayList = new ArrayList<>();
    private int selectFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_video_hide);

        AdView adView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        btnBack = findViewById(R.id.btnBack);
        spinnerFolder = findViewById(R.id.spinnerFolder);
        radioBtnAll = findViewById(R.id.radioBtnAll);
        rlRadiobtn = findViewById(R.id.rlRadiobtn);
        recyclerViewVideo = findViewById(R.id.recyclerViewVideo);
        btnHide = findViewById(R.id.btnHide);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            folderName = bundle.getString("folderName");
        }

        recyclerViewVideo.setLayoutManager(new GridLayoutManager(this,2));
        recyclerViewVideo.setHasFixedSize(true);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        videoList.clear();
        getVideoPath();

        ArrayList<String> arrayList = new ArrayList<>();
        for(int i=0;i<videoList.size();i++){
            arrayList.add(videoList.get(i).getStrFolder());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFolder.setAdapter(arrayAdapter);
        spinnerFolder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                selectFolder = position;
                adapter = new GallaryVideoAdapter(SelectVideoHideActivity.this,videoList.get(position).getVideopathList(),SelectVideoHideActivity.this,false);
                recyclerViewVideo.setAdapter(adapter);
                selectedArrayList.clear();
                n=0;
                radioBtnAll.setChecked(false);
                rlRadiobtn.setVisibility(View.GONE);
                btnHide.setVisibility(View.GONE);
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {

            }
        });

        rlRadiobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioBtnAll.isChecked()){
                    radioBtnAll.setChecked(false);
                    n=0;
                    radioBtnAll.setText(String.valueOf(n));
                    adapter = new GallaryVideoAdapter(SelectVideoHideActivity.this,videoList.get(selectFolder).getVideopathList(),SelectVideoHideActivity.this,false);
                    recyclerViewVideo.setAdapter(adapter);
                    for(int i = 0; i<videoList.get(selectFolder).getVideopathList().size(); i++){
                        if (selectedArrayList.contains(Integer.valueOf(i))) {
                            selectedArrayList.remove(Integer.valueOf(i));
                        }
                    }
                    rlRadiobtn.setVisibility(View.GONE);
                    btnHide.setVisibility(View.GONE);
                }else {
                    radioBtnAll.setChecked(true);
                    n=videoList.get(selectFolder).getVideopathList().size();
                    radioBtnAll.setText("All");
                    adapter = new GallaryVideoAdapter(SelectVideoHideActivity.this,videoList.get(selectFolder).getVideopathList(),SelectVideoHideActivity.this,true);
                    recyclerViewVideo.setAdapter(adapter);
                    for(int i = 0; i<n; i++){
                        if (!selectedArrayList.contains(Integer.valueOf(i))) {
                            selectedArrayList.add(Integer.valueOf(i));
                        }
                    }
                }
            }
        });

        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<selectedArrayList.size();i++){
                    File file = new File(videoList.get(selectFolder).getVideopathList().get(selectedArrayList.get(i)));
                    moveFile(videoList.get(selectFolder).getVideopathList().get(selectedArrayList.get(i)),file.getName(),getFilesDir().getAbsolutePath()+"/video/"+folderName+"/");
                }
                finish();
            }
        });

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

                MediaScannerConnection.scanFile(this, new String[]{inputPath}, null, new MediaScannerConnection.OnScanCompletedListener() {

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

    public ArrayList<VideoModel> getVideoPath() {
        videoList.clear();

        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfVideo = null;
        uri =  MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()) {
            absolutePathOfVideo = cursor.getString(column_index_data);
            Log.e("Column", absolutePathOfVideo);
            Log.e("Folder", cursor.getString(column_index_folder_name));

            for (int i = 0; i < videoList.size(); i++) {
                if (videoList.get(i).getStrFolder().equals(cursor.getString(column_index_folder_name))) {
                    booleanFolder = true;
                    int_position = i;
                    break;
                } else {
                    booleanFolder = false;
                }
            }


            if (booleanFolder) {

                ArrayList<String> al_path = new ArrayList<>();
                al_path.addAll(videoList.get(int_position).getVideopathList());
                al_path.add(absolutePathOfVideo);
                videoList.get(int_position).setVideopathList(al_path);

            } else {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.add(absolutePathOfVideo);
                VideoModel obj_model = new VideoModel();
                obj_model.setStrFolder(cursor.getString(column_index_folder_name));
                obj_model.setVideopathList(al_path);

                videoList.add(obj_model);

            }


        }

        for (int i = 0; i < videoList.size(); i++) {
            Log.e("FOLDER", videoList.get(i).getStrFolder());
            for (int j = 0; j < videoList.get(i).getVideopathList().size(); j++) {
                Log.e("FILE", videoList.get(i).getVideopathList().get(j));
            }
        }

        return videoList;
    }

    @Override
    public void onGallaryVideoSelected(int i, GallaryVideoAdapter.MyViewHolder holder) {
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
            btnHide.setVisibility(View.VISIBLE);
        } else {
            rlRadiobtn.setVisibility(View.GONE);
            btnHide.setVisibility(View.GONE);
        }
    }
}
