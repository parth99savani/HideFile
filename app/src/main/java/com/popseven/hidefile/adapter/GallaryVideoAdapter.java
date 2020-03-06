package com.popseven.hidefile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.popseven.hidefile.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GallaryVideoAdapter extends RecyclerView.Adapter<GallaryVideoAdapter.MyViewHolder> {

    private Context context;
    private GallaryVideoAdapterListener listener;
    private ArrayList<String> videoList = new ArrayList<>();
    private boolean checked;

    public GallaryVideoAdapter(Context context, ArrayList<String> videoList, GallaryVideoAdapterListener listener, boolean checked) {
        this.context = context;
        this.videoList = videoList;
        this.listener = listener;
        this.checked = checked;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private ImageView imageView;
        public RadioButton radiobtnSelect;
        private RelativeLayout rlPlay;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            radiobtnSelect = itemView.findViewById(R.id.radiobtnSelect);
            rlPlay = itemView.findViewById(R.id.rlPlay);
        }
    }

    @NonNull
    @Override
    public GallaryVideoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video, viewGroup, false);

        return new GallaryVideoAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GallaryVideoAdapter.MyViewHolder holder, final int i) {

        Glide.with(context).load(videoList.get(i))
                .into(holder.imageView);

        holder.rlPlay.setVisibility(View.GONE);

        holder.radiobtnSelect.setChecked(checked);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGallaryVideoSelected(i, holder);
            }
        });


    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public interface GallaryVideoAdapterListener {
        void onGallaryVideoSelected(int i, GallaryVideoAdapter.MyViewHolder holder);
    }

}