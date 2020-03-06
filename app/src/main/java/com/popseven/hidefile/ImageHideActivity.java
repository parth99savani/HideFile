package com.popseven.hidefile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.popseven.hidefile.adapter.ImageAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImageHideActivity extends AppCompatActivity implements ImageAdapter.ImageAdapterListener {

    private ImageButton btnBack;
    private TextView txtFolderName;
    private RadioButton radioBtnAll;
    private RelativeLayout rlRadiobtn;
    private ImageButton btnDelete;
    private RecyclerView recyclerViewImage;
    private FloatingActionButton fabAdd;
    private Button btnUnhide;
    private ArrayList<String> imageList = new ArrayList<>();
    private String folderName;
    private ImageAdapter adapter;
    private int n = 0;
    private static ArrayList<Integer> selectedArrayList = new ArrayList<>();
    private File dir;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_hide);

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
        recyclerViewImage = findViewById(R.id.recyclerViewImage);
        fabAdd = findViewById(R.id.fab);
        btnUnhide = findViewById(R.id.btnUnhide);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            folderName = bundle.getString("folderName");
            txtFolderName.setText(folderName);
        }

        recyclerViewImage.setLayoutManager(new GridLayoutManager(this,3));
        recyclerViewImage.setHasFixedSize(true);

        File dirUnhide = new File(Environment.getExternalStorageDirectory()+"/HideFile");
        if(!dirUnhide.exists()) {
            dirUnhide.mkdir(); //directory is created;
        }
        dir = new File(Environment.getExternalStorageDirectory()+"/HideFile/Image");
        if(!dir.exists()) {
            dir.mkdir(); //directory is created;
        }

        //moveFile(Environment.getExternalStorageDirectory().getAbsolutePath(),"/imageBook.jpg",getFilesDir().getAbsolutePath()+"/image/New Folder");

        //getImagePath();

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
                Intent intent = new Intent(ImageHideActivity.this,SelectImageHideActivity.class);
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
                    adapter = new ImageAdapter(ImageHideActivity.this,imageList,ImageHideActivity.this,false);
                    recyclerViewImage.setAdapter(adapter);
                    for(int i = 0; i<imageList.size(); i++){
                        if (selectedArrayList.contains(Integer.valueOf(i))) {
                            selectedArrayList.remove(Integer.valueOf(i));
                        }
                    }
                    rlRadiobtn.setVisibility(View.GONE);
                    btnUnhide.setVisibility(View.GONE);
                    btnDelete.setVisibility(View.GONE);
                }else {
                    radioBtnAll.setChecked(true);
                    n=imageList.size();
                    radioBtnAll.setText("All");
                    adapter = new ImageAdapter(ImageHideActivity.this,imageList,ImageHideActivity.this,true);
                    recyclerViewImage.setAdapter(adapter);
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
                    File file = new File(imageList.get(selectedArrayList.get(i)));
                    moveFile(imageList.get(selectedArrayList.get(i)),file.getName(),dir.getAbsolutePath()+"/");
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
                final AlertDialog.Builder alertDelete = new AlertDialog.Builder(ImageHideActivity.this);
                alertDelete.setMessage("Are you want to delete this image?");

                alertDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        for(int i=0;i<selectedArrayList.size();i++){
                            File file = new File(imageList.get(selectedArrayList.get(i)));
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
        imageList.clear();
        loadImageFiles(new File(getFilesDir().getAbsolutePath()+"/image/"+folderName));

        adapter = new ImageAdapter(this,imageList,this,false);
        recyclerViewImage.setAdapter(adapter);
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
                    if ((name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")) && Pattern.compile("(.*)((\\.(jpg||jpeg||png))$)", 2).matcher(name).matches()) {
                        al_path.add(listFile[i].getAbsolutePath());
                    }
                }
            }
        }
        imageList.addAll(al_path);
    }

    @Override
    protected void onResume() {
        reloadRecyclerView();
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        super.onResume();
    }

    @Override
    public void onRadioBtnSelected(int i, ImageAdapter.MyViewHolder holder) {
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
    public void onImageSelected(String image, ImageAdapter.MyViewHolder holder) {
        Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", new File(image));
        Intent openImageIntent = new Intent();
        openImageIntent.setAction(Intent.ACTION_VIEW)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .setDataAndType(
                        fileUri,
                        getContentResolver().getType(fileUri));
        startActivity(openImageIntent);
    }

    /*private void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath + inputFile);
            out = openFileOutput("imageBook.jpg", Context.MODE_PRIVATE);
            //out = new FileOutputStream(outputPath + inputFile);

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
            new File(inputPath + inputFile).delete();


        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }*/

    /*public ArrayList<String> getImagePath() {
        imageList.clear();

        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri =  MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        String condition = MediaStore.Images.Media.DATA + " like '%/image/%'";

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getApplicationContext().getContentResolver().query(uri, projection, condition, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.e("Column", absolutePathOfImage);
            Log.e("Folder", cursor.getString(column_index_folder_name));

            imageList.add(absolutePathOfImage);

        }

        adapter = new ImageAdapter(this,imageList,this);
        recyclerViewImage.setAdapter(adapter);
        return imageList;
    }*/

}
