package com.popseven.hidefile;

import android.content.Context;
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
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.popseven.hidefile.adapter.GallaryImageAdapter;
import com.popseven.hidefile.model.ImageModel;

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

public class SelectImageHideActivity extends AppCompatActivity implements GallaryImageAdapter.GallaryImageAdapterListener {

    private ImageButton btnBack;
    private Spinner spinnerFolder;
    private RadioButton radioBtnAll;
    private RelativeLayout rlRadiobtn;
    private RecyclerView recyclerViewImage;
    private Button btnHide;
    private GallaryImageAdapter adapter;
    private static ArrayList<ImageModel> imageList = new ArrayList<>();
    private boolean booleanFolder;
    private String folderName;
    private int n = 0;
    private static ArrayList<Integer> selectedArrayList = new ArrayList<>();
    private int selectFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image_hide);

        AdView adView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        btnBack = findViewById(R.id.btnBack);
        spinnerFolder = findViewById(R.id.spinnerFolder);
        radioBtnAll = findViewById(R.id.radioBtnAll);
        rlRadiobtn = findViewById(R.id.rlRadiobtn);
        recyclerViewImage = findViewById(R.id.recyclerViewImage);
        btnHide = findViewById(R.id.btnHide);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            folderName = bundle.getString("folderName");
        }

        recyclerViewImage.setLayoutManager(new GridLayoutManager(this,3));
        recyclerViewImage.setHasFixedSize(true);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageList.clear();
        getImagePath();

        ArrayList<String> arrayList = new ArrayList<>();
        for(int i=0;i<imageList.size();i++){
            arrayList.add(imageList.get(i).getStrFolder());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFolder.setAdapter(arrayAdapter);
        spinnerFolder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                selectFolder = position;
                adapter = new GallaryImageAdapter(SelectImageHideActivity.this,imageList.get(position).getImagepathList(),SelectImageHideActivity.this,false);
                recyclerViewImage.setAdapter(adapter);
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
                    adapter = new GallaryImageAdapter(SelectImageHideActivity.this,imageList.get(selectFolder).getImagepathList(),SelectImageHideActivity.this,false);
                    recyclerViewImage.setAdapter(adapter);
                    for(int i = 0; i<imageList.get(selectFolder).getImagepathList().size(); i++){
                        if (selectedArrayList.contains(Integer.valueOf(i))) {
                            selectedArrayList.remove(Integer.valueOf(i));
                        }
                    }
                    rlRadiobtn.setVisibility(View.GONE);
                    btnHide.setVisibility(View.GONE);
                }else {
                    radioBtnAll.setChecked(true);
                    n=imageList.get(selectFolder).getImagepathList().size();
                    radioBtnAll.setText("All");
                    adapter = new GallaryImageAdapter(SelectImageHideActivity.this,imageList.get(selectFolder).getImagepathList(),SelectImageHideActivity.this,true);
                    recyclerViewImage.setAdapter(adapter);
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
                    File file = new File(imageList.get(selectFolder).getImagepathList().get(selectedArrayList.get(i)));
                    moveFile(imageList.get(selectFolder).getImagepathList().get(selectedArrayList.get(i)),file.getName(),getFilesDir().getAbsolutePath()+"/image/"+folderName+"/");
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

    public ArrayList<ImageModel> getImagePath() {
        imageList.clear();

        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri =  MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.e("Column", absolutePathOfImage);
            Log.e("Folder", cursor.getString(column_index_folder_name));

            for (int i = 0; i < imageList.size(); i++) {
                if (imageList.get(i).getStrFolder().equals(cursor.getString(column_index_folder_name))) {
                    booleanFolder = true;
                    int_position = i;
                    break;
                } else {
                    booleanFolder = false;
                }
            }


            if (booleanFolder) {

                ArrayList<String> al_path = new ArrayList<>();
                al_path.addAll(imageList.get(int_position).getImagepathList());
                al_path.add(absolutePathOfImage);
                imageList.get(int_position).setImagepathList(al_path);

            } else {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.add(absolutePathOfImage);
                ImageModel obj_model = new ImageModel();
                obj_model.setStrFolder(cursor.getString(column_index_folder_name));
                obj_model.setImagepathList(al_path);

                imageList.add(obj_model);

            }


        }

        for (int i = 0; i < imageList.size(); i++) {
            Log.e("FOLDER", imageList.get(i).getStrFolder());
            for (int j = 0; j < imageList.get(i).getImagepathList().size(); j++) {
                Log.e("FILE", imageList.get(i).getImagepathList().get(j));
            }
        }

        return imageList;
    }

    @Override
    public void onGallaryImageSelected(int i, GallaryImageAdapter.MyViewHolder holder) {
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
