package com.popseven.hidefile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.popseven.hidefile.R;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FileAudioAdapter extends RecyclerView.Adapter<FileAudioAdapter.MyViewHolder> {

    private Context context;
    private FileAudioAdapterListener listener;
    private ArrayList<String> audioList = new ArrayList<>();
    private boolean checked;

    public FileAudioAdapter(Context context, ArrayList<String> audioList, FileAudioAdapterListener listener, boolean checked) {
        this.context = context;
        this.audioList = audioList;
        this.listener = listener;
        this.checked = checked;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtAudioType;
        private TextView txtAudioName;
        public RadioButton radiobtnSelect;
        private RelativeLayout rlRadiobtnSelect;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtAudioType = itemView.findViewById(R.id.txtAudioType);
            txtAudioName = itemView.findViewById(R.id.txtAudioName);
            radiobtnSelect = itemView.findViewById(R.id.radiobtnSelect);
            rlRadiobtnSelect = itemView.findViewById(R.id.rlRadiobtnSelect);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_audio, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

        String name = new File(audioList.get(i)).getName();
        holder.txtAudioName.setText(name);
        if (name.endsWith(".mp3") && Pattern.compile("(.*)((\\.(mp3))$)", 2).matcher(name).matches()) {
            holder.txtAudioType.setText("MP3");
            holder.txtAudioType.setBackgroundColor(Color.parseColor("#F03680"));
        } else if (name.endsWith(".ogg") && Pattern.compile("(.*)((\\.(ogg))$)", 2).matcher(name).matches()) {
            holder.txtAudioType.setText("OOG");
            holder.txtAudioType.setBackgroundColor(Color.parseColor("#7A35E9"));
        } else if (!name.endsWith(".wav") || !Pattern.compile("(.*)((\\.(wav))$)", 2).matcher(name).matches()) {
            holder.txtAudioType.setText("WAV");
            holder.txtAudioType.setBackgroundColor(Color.parseColor("#F67B2F"));
        }

        holder.radiobtnSelect.setChecked(checked);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFileAudioSelected(i, holder);
            }
        });

    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public interface FileAudioAdapterListener {
        void onFileAudioSelected(int i, FileAudioAdapter.MyViewHolder holder);
    }

}

