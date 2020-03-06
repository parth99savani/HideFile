package com.popseven.hidefile;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.popseven.hidefile.adapter.AudioFolderAdapter;
import com.popseven.hidefile.model.AudioModel;

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

public class AudioFolderListActivity extends AppCompatActivity implements AudioFolderAdapter.AudioFolderAdapterListener {

    private ImageButton btnBack;
    private ImageButton btnAddFolder;
    private RecyclerView recyclerViewFolder;
    private static ArrayList<AudioModel> audioList = new ArrayList<>();
    private AudioFolderAdapter adapter;
    private File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_folder_list);

        AdView adView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        btnBack = findViewById(R.id.btnBack);
        btnAddFolder = findViewById(R.id.btnAddFolder);
        recyclerViewFolder = findViewById(R.id.recyclerViewFolder);
        recyclerViewFolder.setLayoutManager(new GridLayoutManager(this, 2));

        File dirAud = new File(getFilesDir()+"/audio/New Folder");
        if(!dirAud.exists()) {
            dirAud.mkdir(); //directory is created;
        }

        File dirUnhide = new File(Environment.getExternalStorageDirectory()+"/HideFile");
        if(!dirUnhide.exists()) {
            dirUnhide.mkdir(); //directory is created;
        }
        dir = new File(Environment.getExternalStorageDirectory()+"/HideFile/Audio");
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
                View view = LayoutInflater.from(AudioFolderListActivity.this).inflate(R.layout.dialog_new_folder, null);
                final AlertDialog.Builder alertNewFol = new AlertDialog.Builder(AudioFolderListActivity.this);
                final EditText edittext = view.findViewById(R.id.editText);
                alertNewFol.setTitle("Create New Folder");

                alertNewFol.setView(view);

                alertNewFol.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String folderName = edittext.getText().toString();
                        File dirNew = new File(getFilesDir()+"/audio/"+folderName);
                        if(!dirNew.exists()) {
                            dirNew.mkdir(); //directory is created;
                            dialog.dismiss();
                            reloadRecyclerView();
                        }else {
                            Toast.makeText(AudioFolderListActivity.this, folderName+" is already created.", Toast.LENGTH_SHORT).show();
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
        audioList.clear();
        loadAudioFiles(new File(getFilesDir().getAbsolutePath()+"/audio/"));
        audioList.remove(audioList.size()-1);
        adapter = new AudioFolderAdapter(this,audioList,this);
        recyclerViewFolder.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        reloadRecyclerView();
        super.onResume();
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
        AudioModel obj_model = new AudioModel();
        obj_model.setStrFolder(dir.getName());
        obj_model.setAudiopathList(al_path);
        audioList.add(obj_model);
    }

    @Override
    public void onAudioFolderSelected(AudioModel audioModel) {
        Intent intent = new Intent(AudioFolderListActivity.this,AudioHideActivity.class);
        intent.putExtra("folderName",audioModel.getStrFolder());
        startActivity(intent);
    }

    @Override
    public void onAudioFolderLongClick(final AudioModel audioModel) {
        final AlertDialog.Builder alertOptions = new AlertDialog.Builder(AudioFolderListActivity.this);

        String[] list = {"Rename", "Unhide", "Delete"};
        alertOptions.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    case 0:
                        View view = LayoutInflater.from(AudioFolderListActivity.this).inflate(R.layout.dialog_new_folder, null);
                        final AlertDialog.Builder alertRename = new AlertDialog.Builder(AudioFolderListActivity.this);
                        final EditText edittext = view.findViewById(R.id.editText);
                        alertRename.setTitle("Rename Folder");
                        edittext.setText(audioModel.getStrFolder());
                        alertRename.setView(view);

                        alertRename.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String folderName = edittext.getText().toString();
                                File oldFolder = new File(getFilesDir()+"/audio/"+audioModel.getStrFolder());
                                File newFolder = new File(getFilesDir()+"/audio/"+folderName);
                                boolean success = oldFolder.renameTo(newFolder);
                                if(success){
                                    Toast.makeText(AudioFolderListActivity.this, "Folder rename succesfully.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    reloadRecyclerView();
                                }else {
                                    Toast.makeText(AudioFolderListActivity.this, "Please, try again.", Toast.LENGTH_SHORT).show();
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
                        final AlertDialog.Builder alertUnhide = new AlertDialog.Builder(AudioFolderListActivity.this);
                        alertUnhide.setMessage("Are you want to Unhide this folder?");

                        alertUnhide.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                File fileDir = new File(dir+"/"+audioModel.getStrFolder());
                                if(!fileDir.exists()) {
                                    fileDir.mkdir(); //directory is created;
                                }
                                for (int k=0;k<audioModel.getAudiopathList().size();k++){
                                    File file = new File(audioModel.getAudiopathList().get(k));
                                    moveFile(file.getAbsolutePath(),file.getName(),fileDir.getAbsolutePath()+"/");
                                }
                                File file = new File(getFilesDir()+"/audio/"+audioModel.getStrFolder());
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
                        final AlertDialog.Builder alertDelete = new AlertDialog.Builder(AudioFolderListActivity.this);
                        alertDelete.setMessage("Are you want to delete this folder?");

                        alertDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                File file = new File(getFilesDir()+"/audio/"+audioModel.getStrFolder());
                                deleteDir(file);
                                if(file.exists()){
                                    Toast.makeText(AudioFolderListActivity.this, "Please, try again.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    reloadRecyclerView();
                                }else {
                                    Toast.makeText(AudioFolderListActivity.this, "Folder deleted succesfully.", Toast.LENGTH_SHORT).show();
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
