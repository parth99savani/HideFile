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

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private Context context;
    private ImageAdapterListener listener;
    private ArrayList<String> imageList = new ArrayList<>();
    private boolean checked;

    public ImageAdapter(Context context, ArrayList<String> imageList, ImageAdapterListener listener, boolean checked) {
        this.context = context;
        this.imageList = imageList;
        this.listener = listener;
        this.checked = checked;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        private ImageView imageView;
        public RadioButton radiobtnSelect;
        private RelativeLayout rlRadiobtnSelect;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            radiobtnSelect = itemView.findViewById(R.id.radiobtnSelect);
            rlRadiobtnSelect = itemView.findViewById(R.id.rlRadiobtnSelect);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

        Glide.with(context).load(imageList.get(i))
                .into(holder.imageView);

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
                listener.onImageSelected(imageList.get(i), holder);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public interface ImageAdapterListener {
        void onRadioBtnSelected(int i, ImageAdapter.MyViewHolder holder);
        void onImageSelected(String image, ImageAdapter.MyViewHolder holder);
    }

}