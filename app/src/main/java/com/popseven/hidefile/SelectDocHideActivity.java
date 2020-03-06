package com.popseven.hidefile;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.popseven.hidefile.adapter.FileDocAdapter;
import com.popseven.hidefile.model.DocModel;

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

public class SelectDocHideActivity extends AppCompatActivity implements FileDocAdapter.FileDocAdapterListener {

    private ImageButton btnBack;
    private Spinner spinnerFolder;
    private RadioButton radioBtnAll;
    private RelativeLayout rlRadiobtn;
    private RecyclerView recyclerViewDoc;
    private Button btnHide;
    private FileDocAdapter adapter;
    private static ArrayList<DocModel> docList = new ArrayList<>();
    private boolean booleanFolder;
    private String folderName;
    private int n = 0;
    private static ArrayList<Integer> selectedArrayList = new ArrayList<>();
    private int selectFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doc_hide);

        AdView adView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        btnBack = findViewById(R.id.btnBack);
        spinnerFolder = findViewById(R.id.spinnerFolder);
        radioBtnAll = findViewById(R.id.radioBtnAll);
        rlRadiobtn = findViewById(R.id.rlRadiobtn);
        recyclerViewDoc = findViewById(R.id.recyclerViewDoc);
        btnHide = findViewById(R.id.btnHide);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            folderName = bundle.getString("folderName");
        }

        recyclerViewDoc.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDoc.setHasFixedSize(true);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        docList.clear();
        loadDocFiles(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
        //removeEmptyDir();

        ArrayList<String> arrayList = new ArrayList<>();
        for(int i=0;i<docList.size();i++){
            arrayList.add(docList.get(i).getStrFolder());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFolder.setAdapter(arrayAdapter);
        spinnerFolder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                selectFolder = position;
                adapter = new FileDocAdapter(SelectDocHideActivity.this,docList.get(position).getDocpathList(),SelectDocHideActivity.this,false);
                recyclerViewDoc.setAdapter(adapter);
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
                    adapter = new FileDocAdapter(SelectDocHideActivity.this,docList.get(selectFolder).getDocpathList(),SelectDocHideActivity.this,false);
                    recyclerViewDoc.setAdapter(adapter);
                    for(int i = 0; i<docList.get(selectFolder).getDocpathList().size(); i++){
                        if (selectedArrayList.contains(Integer.valueOf(i))) {
                            selectedArrayList.remove(Integer.valueOf(i));
                        }
                    }
                    rlRadiobtn.setVisibility(View.GONE);
                    btnHide.setVisibility(View.GONE);
                }else {
                    radioBtnAll.setChecked(true);
                    n=docList.get(selectFolder).getDocpathList().size();
                    radioBtnAll.setText("All");
                    adapter = new FileDocAdapter(SelectDocHideActivity.this,docList.get(selectFolder).getDocpathList(),SelectDocHideActivity.this,true);
                    recyclerViewDoc.setAdapter(adapter);
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
                    File file = new File(docList.get(selectFolder).getDocpathList().get(selectedArrayList.get(i)));
                    moveFile(docList.get(selectFolder).getDocpathList().get(selectedArrayList.get(i)),file.getName(),getFilesDir().getAbsolutePath()+"/document/"+folderName+"/");
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

    private void loadDocFiles(File dir) {

        File[] listFile = dir.listFiles();
        ArrayList<String> al_path = new ArrayList<>();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    loadDocFiles(listFile[i]);
                } else {
                    String name = listFile[i].getName();
                    if ((name.endsWith(".doc") || name.endsWith(".txt") || name.endsWith(".pdf")) && Pattern.compile("(.*)((\\.(doc||txt||pdf))$)", 2).matcher(name).matches()) {
                        al_path.add(listFile[i].getAbsolutePath());
                    }
                }
            }
        }
        if(al_path.size()>0){
            DocModel obj_model = new DocModel();
            obj_model.setStrFolder(dir.getName());
            obj_model.setDocpathList(al_path);
            docList.add(obj_model);
        }
    }

    @Override
    public void onFileDocSelected(int i, FileDocAdapter.MyViewHolder holder) {
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
