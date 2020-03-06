package com.popseven.hidefile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.popseven.hidefile.R;
import com.popseven.hidefile.model.VideoModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoFolderAdapter extends RecyclerView.Adapter<VideoFolderAdapter.MyViewHolder> {

    private Context context;
    private VideoFolderAdapterListener listener;
    private ArrayList<VideoModel> videoList = new ArrayList<>();

    public VideoFolderAdapter(Context context, ArrayList<VideoModel> videoList, VideoFolderAdapterListener listener) {
        this.context = context;
        this.videoList = videoList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewIF1;
        private ImageView imageViewIF2;
        private ImageView imageViewIF3;
        private ImageView imageViewIF4;
        private TextView txtFolderName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewIF1 = itemView.findViewById(R.id.imageViewIF1);
            imageViewIF2 = itemView.findViewById(R.id.imageViewIF2);
            imageViewIF3 = itemView.findViewById(R.id.imageViewIF3);
            imageViewIF4 = itemView.findViewById(R.id.imageViewIF4);
            txtFolderName = itemView.findViewById(R.id.txtFolderName);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_folder_image, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {

        holder.txtFolderName.setText(videoList.get(i).getStrFolder());

        int size = videoList.get(i).getVideopathList().size();
        if (size >= 1) {
            Glide.with(context).load(videoList.get(i).getVideopathList().get(0))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.imageViewIF1);
        }else {
            holder.imageViewIF1.setVisibility(View.GONE);
        }
        if (size >= 2) {
            Glide.with(context).load(videoList.get(i).getVideopathList().get(1))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.imageViewIF2);
        }else {
            holder.imageViewIF2.setVisibility(View.GONE);
        }
        if (size >= 3) {
            Glide.with(context).load(videoList.get(i).getVideopathList().get(2))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.imageViewIF3);
        }else {
            holder.imageViewIF3.setVisibility(View.GONE);
        }
        if (size >= 4) {
            Glide.with(context).load(videoList.get(i).getVideopathList().get(3))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.imageViewIF4);
        }else {
            holder.imageViewIF4.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onVideoFolderSelected(videoList.get(i));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onVideoFolderLongClick(videoList.get(i));
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public interface VideoFolderAdapterListener {
        void onVideoFolderSelected(VideoModel i);
        void onVideoFolderLongClick(VideoModel i);
    }

}
