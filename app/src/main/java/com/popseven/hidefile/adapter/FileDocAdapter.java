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

public class FileDocAdapter extends RecyclerView.Adapter<FileDocAdapter.MyViewHolder> {

    private Context context;
    private FileDocAdapterListener listener;
    private ArrayList<String> docList = new ArrayList<>();
    private boolean checked;

    public FileDocAdapter(Context context, ArrayList<String> docList, FileDocAdapterListener listener, boolean checked) {
        this.context = context;
        this.docList = docList;
        this.listener = listener;
        this.checked = checked;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDocType;
        private TextView txtDocName;
        public RadioButton radiobtnSelect;
        private RelativeLayout rlRadiobtnSelect;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDocType = itemView.findViewById(R.id.txtDocType);
            txtDocName = itemView.findViewById(R.id.txtDocName);
            radiobtnSelect = itemView.findViewById(R.id.radiobtnSelect);
            rlRadiobtnSelect = itemView.findViewById(R.id.rlRadiobtnSelect);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_doc, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

        String name = new File(docList.get(i)).getName();
        if (!name.equalsIgnoreCase("")) {
            holder.txtDocName.setText(name);
        }
        if (name.endsWith(".doc") && Pattern.compile("(.*)((\\.(doc))$)", 2).matcher(name).matches()) {
            holder.txtDocType.setText("DOC");
            holder.txtDocType.setBackgroundColor(Color.parseColor("#0288D1"));
        } else if (name.endsWith(".txt") && Pattern.compile("(.*)((\\.(txt))$)", 2).matcher(name).matches()) {
            holder.txtDocType.setText("TXT");
            holder.txtDocType.setBackgroundColor(Color.parseColor("#F2AE29"));
        } else if (name.endsWith(".pdf") || !Pattern.compile("(.*)((\\.(pdf))$)", 2).matcher(name).matches()) {
            holder.txtDocType.setText("PDF");
            holder.txtDocType.setBackgroundColor(Color.parseColor("#FF0000"));
        }

        holder.radiobtnSelect.setChecked(checked);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFileDocSelected(i, holder);
            }
        });

    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    public interface FileDocAdapterListener {
        void onFileDocSelected(int i, FileDocAdapter.MyViewHolder holder);
    }

}
