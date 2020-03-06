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

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.MyViewHolder> {

    private Context context;
    private AudioAdapterListener listener;
    private ArrayList<String> audioList = new ArrayList<>();
    private boolean checked;

    public AudioAdapter(Context context, ArrayList<String> audioList, AudioAdapterListener listener, boolean checked) {
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

        holder.rlRadiobtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRadioBtnSelected(i, holder);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAudioSelected(audioList.get(i), holder);
            }
        });

    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public interface AudioAdapterListener {
        void onRadioBtnSelected(int i, AudioAdapter.MyViewHolder holder);

        void onAudioSelected(String audio, AudioAdapter.MyViewHolder holder);
    }

}
