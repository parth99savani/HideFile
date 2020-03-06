package com.popseven.hidefile;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.popseven.hidefile.adapter.ImageFolderAdapter;
import com.popseven.hidefile.model.ImageModel;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ImageFolderListActivity extends AppCompatActivity implements ImageFolderAdapter.ImageFolderAdapterListener{

    private ImageButton btnBack;
    private ImageButton btnAddFolder;
    private RecyclerView recyclerViewFolder;
    private static ArrayList<ImageModel> imageList = new ArrayList<>();
    private boolean booleanFolder;
    private ImageFolderAdapter adapter;
    private File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_folder_list);

        AdView adView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        btnBack = findViewById(R.id.btnBack);
        btnAddFolder = findViewById(R.id.btnAddFolder);
        recyclerViewFolder = findViewById(R.id.recyclerViewFolder);
        recyclerViewFolder.setLayoutManager(new GridLayoutManager(this, 2));

        File dirImg = new File(getFilesDir()+"/image/New Folder");
        if(!dirImg.exists()) {
            dirImg.mkdir(); //directory is created;
        }

        File dirUnhide = new File(Environment.getExternalStorageDirectory()+"/HideFile");
        if(!dirUnhide.exists()) {
            dirUnhide.mkdir(); //directory is created;
        }
        dir = new File(Environment.getExternalStorageDirectory()+"/HideFile/Image");
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

        btnAddFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(ImageFolderListActivity.this).inflate(R.layout.dialog_new_folder, null);
                final AlertDialog.Builder alertNewFol = new AlertDialog.Builder(ImageFolderListActivity.this);
                final EditText edittext = view.findViewById(R.id.editText);
                alertNewFol.setTitle("Create New Folder");

                alertNewFol.setView(view);

                alertNewFol.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String folderName = edittext.getText().toString();
                        File dirNew = new File(getFilesDir()+"/image/"+folderName);
                        if(!dirNew.exists()) {
                            dirNew.mkdir(); //directory is created;
                            dialog.dismiss();
                            reloadRecyclerView();
                        }else {
                            Toast.makeText(ImageFolderListActivity.this, folderName+" is already created.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            reloadRecyclerView();
                        }
                    }
                });

                alertNewFol.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                       dialog.dismiss();
                    }
                });

                alertNewFol.show();
            }
        });

    }

    private void reloadRecyclerView(){
        imageList.clear();
        loadImageFiles(new File(getFilesDir().getAbsolutePath()+"/image/"));
        imageList.remove(imageList.size()-1);
        adapter = new ImageFolderAdapter(this,imageList,this);
        recyclerViewFolder.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        reloadRecyclerView();
        super.onResume();
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
        ImageModel obj_model = new ImageModel();
        obj_model.setStrFolder(dir.getName());
        obj_model.setImagepathList(al_path);
        imageList.add(obj_model);
    }

    /*public ArrayList<ImageModel> getImagePath() {
        imageList.clear();

        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri =  MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
        String condition = MediaStore.Images.Media.DATA + " like '%/image/%'";

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        if(cursor!=null){
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        }else {
            File dirImg = new File(getFilesDir()+"/image/New Folder");
            if(!dirImg.exists()) {
                dirImg.mkdir(); //directory is created;
            }
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        }

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

        boolean newFol=false;

        File f = new File(getFilesDir()+"/image");
        File[] files = f.listFiles();
        for (File inFile : files) {
            if (inFile.isDirectory()) {
                // is directory
                for (int i = 0; i < imageList.size(); i++) {
                    if(imageList.get(i).getStrFolder()==inFile.getName()){
                        newFol=true;
                    }
                }
                if(newFol==false){
                    ArrayList<String> al_path = new ArrayList<>();
                    ImageModel obj_model = new ImageModel();
                    obj_model.setStrFolder(inFile.getName());
                    obj_model.setImagepathList(al_path);
                    imageList.add(obj_model);
                }
            }
        }

        adapter = new ImageFolderAdapter(this,imageList,this);
        recyclerViewFolder.setAdapter(adapter);
        return imageList;
    }*/

    @Override
    public void onImageFolderSelected(ImageModel imageModel) {
        Intent intent = new Intent(ImageFolderListActivity.this,ImageHideActivity.class);
        intent.putExtra("folderName",imageModel.getStrFolder());
        startActivity(intent);
    }

    @Override
    public void onImageFolderLongClick(final ImageModel imageModel) {
        final AlertDialog.Builder alertOptions = new AlertDialog.Builder(ImageFolderListActivity.this);

        String[] list = {"Rename", "Unhide", "Delete"};
        alertOptions.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    case 0:
                        View view = LayoutInflater.from(ImageFolderListActivity.this).inflate(R.layout.dialog_new_folder, null);
                        final AlertDialog.Builder alertRename = new AlertDialog.Builder(ImageFolderListActivity.this);
                        final EditText edittext = view.findViewById(R.id.editText);
                        alertRename.setTitle("Rename Folder");
                        edittext.setText(imageModel.getStrFolder());
                        alertRename.setView(view);

                        alertRename.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String folderName = edittext.getText().toString();
                                File oldFolder = new File(getFilesDir()+"/image/"+imageModel.getStrFolder());
                                File newFolder = new File(getFilesDir()+"/image/"+folderName);
                                boolean success = oldFolder.renameTo(newFolder);
                                if(success){
                                    Toast.makeText(ImageFolderListActivity.this, "Folder rename succesfully.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    reloadRecyclerView();
                                }else {
                                    Toast.makeText(ImageFolderListActivity.this, "Please, try again.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    reloadRecyclerView();
                                }
                            }
                        });

                        alertRename.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });

                        alertRename.show();
                        dialog.dismiss();
                        break;

                    case 1:
                        final AlertDialog.Builder alertUnhide = new AlertDialog.Builder(ImageFolderListActivity.this);
                        alertUnhide.setMessage("Are you want to Unhide this folder?");

                        alertUnhide.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                File fileDir = new File(dir+"/"+imageModel.getStrFolder());
                                if(!fileDir.exists()) {
                                    fileDir.mkdir(); //directory is created;
                                }
                                for (int k=0;k<imageModel.getImagepathList().size();k++){
                                    File file = new File(imageModel.getImagepathList().get(k));
                                    moveFile(file.getAbsolutePath(),file.getName(),fileDir.getAbsolutePath()+"/");
                                }
                                File file = new File(getFilesDir()+"/image/"+imageModel.getStrFolder());
                                deleteDir(file);
                                reloadRecyclerView();
                            }
                        });

                        alertUnhide.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });

                        alertUnhide.show();
                        dialog.dismiss();
                        break;

                    case 2:
                        final AlertDialog.Builder alertDelete = new AlertDialog.Builder(ImageFolderListActivity.this);
                        alertDelete.setMessage("Are you want to delete this folder?");

                        alertDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                File file = new File(getFilesDir()+"/image/"+imageModel.getStrFolder());
                                deleteDir(file);
                                if(file.exists()){
                                    Toast.makeText(ImageFolderListActivity.this, "Please, try again.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    reloadRecyclerView();
                                }else {
                                    Toast.makeText(ImageFolderListActivity.this, "Folder deleted succesfully.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    reloadRecyclerView();
                                }
                            }
                        });

                        alertDelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });

                        alertDelete.show();
                        dialog.dismiss();
                        break;
                }
            }
        });

        alertOptions.show();
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

    private void deleteDir(File file) {
        if (file.isDirectory())
            for (String child : file.list())
                deleteDir(new File(file, child));
        file.delete();  // delete child file or empty directory
    }

}
