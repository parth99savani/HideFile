package com.popseven.hidefile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.popseven.hidefile.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GallaryImageAdapter extends RecyclerView.Adapter<GallaryImageAdapter.MyViewHolder> {

    private Context context;
    private GallaryImageAdapterListener listener;
    private ArrayList<String> imageList = new ArrayList<>();
    private boolean checked;

    public GallaryImageAdapter(Context context, ArrayList<String> imageList, GallaryImageAdapterListener listener, boolean checked) {
        this.context = context;
        this.imageList = imageList;
        this.listener = listener;
        this.checked = checked;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private ImageView imageView;
        public RadioButton radiobtnSelect;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            radiobtnSelect = itemView.findViewById(R.id.radiobtnSelect);
        }
    }

    @NonNull
    @Override
    public GallaryImageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image, viewGroup, false);

        return new GallaryImageAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GallaryImageAdapter.MyViewHolder holder, final int i) {

        Glide.with(context).load(imageList.get(i))
                .into(holder.imageView);

        holder.radiobtnSelect.setChecked(checked);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGallaryImageSelected(i,holder);
            }
        });


    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public interface GallaryImageAdapterListener {
        void onGallaryImageSelected(int i,GallaryImageAdapter.MyViewHolder holder);
    }

}
