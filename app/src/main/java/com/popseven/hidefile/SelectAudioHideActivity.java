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
import com.popseven.hidefile.adapter.AudioAdapter;
import com.popseven.hidefile.adapter.FileAudioAdapter;
import com.popseven.hidefile.model.AudioModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectAudioHideActivity extends AppCompatActivity implements FileAudioAdapter.FileAudioAdapterListener {

    private ImageButton btnBack;
    private Spinner spinnerFolder;
    private RadioButton radioBtnAll;
    private RelativeLayout rlRadiobtn;
    private RecyclerView recyclerViewAudio;
    private Button btnHide;
    private FileAudioAdapter adapter;
    private static ArrayList<AudioModel> audioList = new ArrayList<>();
    private boolean booleanFolder;
    private String folderName;
    private int n = 0;
    private static ArrayList<Integer> selectedArrayList = new ArrayList<>();
    private int selectFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_audio_hide);

        AdView adView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        btnBack = findViewById(R.id.btnBack);
        spinnerFolder = findViewById(R.id.spinnerFolder);
        radioBtnAll = findViewById(R.id.radioBtnAll);
        rlRadiobtn = findViewById(R.id.rlRadiobtn);
        recyclerViewAudio = findViewById(R.id.recyclerViewAudio);
        btnHide = findViewById(R.id.btnHide);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            folderName = bundle.getString("folderName");
        }

        recyclerViewAudio.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAudio.setHasFixedSize(true);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        audioList.clear();
        loadAudioFiles(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
        //removeEmptyDir();

        ArrayList<String> arrayList = new ArrayList<>();
        for(int i=0;i<audioList.size();i++){
            arrayList.add(audioList.get(i).getStrFolder());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFolder.setAdapter(arrayAdapter);
        spinnerFolder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                selectFolder = position;
                adapter = new FileAudioAdapter(SelectAudioHideActivity.this,audioList.get(position).getAudiopathList(),SelectAudioHideActivity.this,false);
                recyclerViewAudio.setAdapter(adapter);
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
                    adapter = new FileAudioAdapter(SelectAudioHideActivity.this,audioList.get(selectFolder).getAudiopathList(),SelectAudioHideActivity.this,false);
                    recyclerViewAudio.setAdapter(adapter);
                    for(int i = 0; i<audioList.get(selectFolder).getAudiopathList().size(); i++){
                        if (selectedArrayList.contains(Integer.valueOf(i))) {
                            selectedArrayList.remove(Integer.valueOf(i));
                        }
                    }
                    rlRadiobtn.setVisibility(View.GONE);
                    btnHide.setVisibility(View.GONE);
                }else {
                    radioBtnAll.setChecked(true);
                    n=audioList.get(selectFolder).getAudiopathList().size();
                    radioBtnAll.setText("All");
                    adapter = new FileAudioAdapter(SelectAudioHideActivity.this,audioList.get(selectFolder).getAudiopathList(),SelectAudioHideActivity.this,true);
                    recyclerViewAudio.setAdapter(adapter);
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
                    File file = new File(audioList.get(selectFolder).getAudiopathList().get(selectedArrayList.get(i)));
                    moveFile(audioList.get(selectFolder).getAudiopathList().get(selectedArrayList.get(i)),file.getName(),getFilesDir().getAbsolutePath()+"/audio/"+folderName+"/");
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

    private void loadAudioFiles(File dir) {

        File[] listFile = dir.listFiles();
        ArrayList<String> al_path = new ArrayList<>();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    loadAudioFiles(listFile[i]);
                } else {
                    String name = listFile[i].getName();
                    if ((name.endsWith(".mp3") || name.endsWith(".ogg") || name.endsWith(".wav")) && Pattern.compile("(.*)((\\.(mp3||ogg||wav))$)", 2).matcher(name).matches()) {
                        al_path.add(listFile[i].getAbsolutePath());
                    }
                }
            }
        }
        if(al_path.size()>0){
            AudioModel obj_model = new AudioModel();
            obj_model.setStrFolder(dir.getName());
            obj_model.setAudiopathList(al_path);
            audioList.add(obj_model);
        }
    }

    @Override
    public void onFileAudioSelected(int i, FileAudioAdapter.MyViewHolder holder) {
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
