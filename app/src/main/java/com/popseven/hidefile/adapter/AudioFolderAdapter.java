package com.popseven.hidefile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.popseven.hidefile.R;
import com.popseven.hidefile.model.AudioModel;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AudioFolderAdapter extends RecyclerView.Adapter<AudioFolderAdapter.MyViewHolder> {

    private Context context;
    private AudioFolderAdapterListener listener;
    private ArrayList<AudioModel> audioList = new ArrayList<>();

    public AudioFolderAdapter(Context context, ArrayList<AudioModel> audioList, AudioFolderAdapterListener listener) {
        this.context = context;
        this.audioList = audioList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtAudioType1;
        private TextView txtAudioName1;
        private TextView txtAudioType2;
        private TextView txtAudioName2;
        private TextView txtAudioType3;
        private TextView txtAudioName3;
        private TextView txtAudioType4;
        private TextView txtAudioName4;
        private TextView txtFolderName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAudioType1 = itemView.findViewById(R.id.txtAudioType1);
            txtAudioName1 = itemView.findViewById(R.id.txtAudioName1);
            txtAudioType2 = itemView.findViewById(R.id.txtAudioType2);
            txtAudioName2 = itemView.findViewById(R.id.txtAudioName2);
            txtAudioType3 = itemView.findViewById(R.id.txtAudioType3);
            txtAudioName3 = itemView.findViewById(R.id.txtAudioName3);
            txtAudioType4 = itemView.findViewById(R.id.txtAudioType4);
            txtAudioName4 = itemView.findViewById(R.id.txtAudioName4);
            txtFolderName = itemView.findViewById(R.id.txtFolderName);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_folder_audio, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {

        holder.txtFolderName.setText(audioList.get(i).getStrFolder());

        int size = audioList.get(i).getAudiopathList().size();
        if (size >= 1) {
            String name = new File(audioList.get(i).getAudiopathList().get(0)).getName();
            holder.txtAudioName1.setText(name);
            if (name.endsWith(".mp3") && Pattern.compile("(.*)((\\.(mp3))$)", 2).matcher(name).matches()) {
                holder.txtAudioType1.setText("MP3");
                holder.txtAudioType1.setBackgroundColor(Color.parseColor("#F03680"));
            } else if (name.endsWith(".ogg") && Pattern.compile("(.*)((\\.(ogg))$)", 2).matcher(name).matches()) {
                holder.txtAudioType1.setText("OOG");
                holder.txtAudioType1.setBackgroundColor(Color.parseColor("#7A35E9"));
            } else if (name.endsWith(".wav") || !Pattern.compile("(.*)((\\.(wav))$)", 2).matcher(name).matches()) {
                holder.txtAudioType1.setText("WAV");
                holder.txtAudioType1.setBackgroundColor(Color.parseColor("#F67B2F"));
            }
        }else {
            holder.txtAudioName1.setVisibility(View.GONE);
            holder.txtAudioType1.setVisibility(View.GONE);
        }
        if (size >= 2) {
            String name2 =  new File(audioList.get(i).getAudiopathList().get(1)).getName();
            holder.txtAudioName2.setText(name2);
            if (name2.endsWith(".mp3") && Pattern.compile("(.*)((\\.(mp3))$)", 2).matcher(name2).matches()) {
                holder.txtAudioType2.setText("MP3");
                holder.txtAudioType2.setBackgroundColor(Color.parseColor("#F03680"));
            } else if (name2.endsWith(".ogg") && Pattern.compile("(.*)((\\.(ogg))$)", 2).matcher(name2).matches()) {
                holder.txtAudioType2.setText("OOG");
                holder.txtAudioType2.setBackgroundColor(Color.parseColor("#7A35E9"));
            } else if (name2.endsWith(".wav") || !Pattern.compile("(.*)((\\.(wav))$)", 2).matcher(name2).matches()) {
                holder.txtAudioType2.setText("WAV");
                holder.txtAudioType2.setBackgroundColor(Color.parseColor("#F67B2F"));
            }
        }else {
            holder.txtAudioName2.setVisibility(View.GONE);
            holder.txtAudioType2.setVisibility(View.GONE);
        }
        if (size >= 3) {
            String name3 =  new File(audioList.get(i).getAudiopathList().get(2)).getName();
            holder.txtAudioName3.setText(name3);
            if (name3.endsWith(".mp3") && Pattern.compile("(.*)((\\.(mp3))$)", 2).matcher(name3).matches()) {
                holder.txtAudioType3.setText("MP3");
                holder.txtAudioType3.setBackgroundColor(Color.parseColor("#F03680"));
            } else if (name3.endsWith(".ogg") && Pattern.compile("(.*)((\\.(ogg))$)", 2).matcher(name3).matches()) {
                holder.txtAudioType3.setText("OOG");
                holder.txtAudioType3.setBackgroundColor(Color.parseColor("#7A35E9"));
            } else if (name3.endsWith(".wav") || !Pattern.compile("(.*)((\\.(wav))$)", 2).matcher(name3).matches()) {
                holder.txtAudioType3.setText("WAV");
                holder.txtAudioType3.setBackgroundColor(Color.parseColor("#F67B2F"));
            }
        }else {
            holder.txtAudioName3.setVisibility(View.GONE);
            holder.txtAudioType3.setVisibility(View.GONE);
        }
        if (size >= 4) {
            String name4 =  new File(audioList.get(i).getAudiopathList().get(3)).getName();
            holder.txtAudioName4.setText(name4);
            if (name4.endsWith(".mp3") && Pattern.compile("(.*)((\\.(mp3))$)", 2).matcher(name4).matches()) {
                holder.txtAudioType4.setText("MP3");
                holder.txtAudioType4.setBackgroundColor(Color.parseColor("#F03680"));
            } else if (name4.endsWith(".ogg") && Pattern.compile("(.*)((\\.(ogg))$)", 2).matcher(name4).matches()) {
                holder.txtAudioType4.setText("OOG");
                holder.txtAudioType4.setBackgroundColor(Color.parseColor("#7A35E9"));
            } else if (name4.endsWith(".wav") || !Pattern.compile("(.*)((\\.(wav))$)", 2).matcher(name4).matches()) {
                holder.txtAudioType4.setText("WAV");
                holder.txtAudioType4.setBackgroundColor(Color.parseColor("#F67B2F"));
            }
        }else {
            holder.txtAudioName4.setVisibility(View.GONE);
            holder.txtAudioType4.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAudioFolderSelected(audioList.get(i));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onAudioFolderLongClick(audioList.get(i));
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public interface AudioFolderAdapterListener {
        void onAudioFolderSelected(AudioModel i);

        void onAudioFolderLongClick(AudioModel i);
    }

}

