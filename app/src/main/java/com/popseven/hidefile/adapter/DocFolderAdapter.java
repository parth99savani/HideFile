package com.popseven.hidefile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.popseven.hidefile.R;
import com.popseven.hidefile.model.DocModel;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DocFolderAdapter extends RecyclerView.Adapter<DocFolderAdapter.MyViewHolder> {

    private Context context;
    private DocFolderAdapterListener listener;
    private ArrayList<DocModel> docList = new ArrayList<>();

    public DocFolderAdapter(Context context, ArrayList<DocModel> docList, DocFolderAdapterListener listener) {
        this.context = context;
        this.docList = docList;
        this.listener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDocType1;
        private TextView txtDocName1;
        private TextView txtDocType2;
        private TextView txtDocName2;
        private TextView txtDocType3;
        private TextView txtDocName3;
        private TextView txtDocType4;
        private TextView txtDocName4;
        private TextView txtFolderName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDocType1 = itemView.findViewById(R.id.txtDocType1);
            txtDocName1 = itemView.findViewById(R.id.txtDocName1);
            txtDocType2 = itemView.findViewById(R.id.txtDocType2);
            txtDocName2 = itemView.findViewById(R.id.txtDocName2);
            txtDocType3 = itemView.findViewById(R.id.txtDocType3);
            txtDocName3 = itemView.findViewById(R.id.txtDocName3);
            txtDocType4 = itemView.findViewById(R.id.txtDocType4);
            txtDocName4 = itemView.findViewById(R.id.txtDocName4);
            txtFolderName = itemView.findViewById(R.id.txtFolderName);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_folder_doc, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {

        holder.txtFolderName.setText(docList.get(i).getStrFolder());

        int size = docList.get(i).getDocpathList().size();
        if (size >= 1) {
            String name = new File(docList.get(i).getDocpathList().get(0)).getName();
            if (!name.equalsIgnoreCase("")) {
                holder.txtDocName1.setText(name);
            }
            if (name.endsWith(".doc") && Pattern.compile("(.*)((\\.(doc))$)", 2).matcher(name).matches()) {
                holder.txtDocType1.setText("DOC");
                holder.txtDocType1.setBackgroundColor(Color.parseColor("#0288D1"));
            } else if (name.endsWith(".txt") && Pattern.compile("(.*)((\\.(txt))$)", 2).matcher(name).matches()) {
                holder.txtDocType1.setText("TXT");
                holder.txtDocType1.setBackgroundColor(Color.parseColor("#F2AE29"));
            } else if (name.endsWith(".pdf") || !Pattern.compile("(.*)((\\.(pdf))$)", 2).matcher(name).matches()) {
                holder.txtDocType1.setText("PDF");
                holder.txtDocType1.setBackgroundColor(Color.parseColor("#FF0000"));
            }
        }else {
            holder.txtDocName1.setVisibility(View.GONE);
            holder.txtDocType1.setVisibility(View.GONE);
        }
        if (size >= 2) {
            String name2 = new File(docList.get(i).getDocpathList().get(1)).getName();
            if (!name2.equalsIgnoreCase("")) {
                holder.txtDocName2.setText(name2);
            }
            if (name2.endsWith(".doc") && Pattern.compile("(.*)((\\.(doc))$)", 2).matcher(name2).matches()) {
                holder.txtDocType2.setText("DOC");
                holder.txtDocType2.setBackgroundColor(Color.parseColor("#0288D1"));
            } else if (name2.endsWith(".txt") && Pattern.compile("(.*)((\\.(txt))$)", 2).matcher(name2).matches()) {
                holder.txtDocType2.setText("TXT");
                holder.txtDocType2.setBackgroundColor(Color.parseColor("#F2AE29"));
            } else if (name2.endsWith(".pdf") || !Pattern.compile("(.*)((\\.(pdf))$)", 2).matcher(name2).matches()) {
                holder.txtDocType2.setText("PDF");
                holder.txtDocType2.setBackgroundColor(Color.parseColor("#FF0000"));
            }
        }else {
            holder.txtDocName2.setVisibility(View.GONE);
            holder.txtDocType2.setVisibility(View.GONE);
        }
        if (size >= 3) {
            String name3 = new File(docList.get(i).getDocpathList().get(2)).getName();
            if (!name3.equalsIgnoreCase("")) {
                holder.txtDocName3.setText(name3);
            }
            if (name3.endsWith(".doc") && Pattern.compile("(.*)((\\.(doc))$)", 2).matcher(name3).matches()) {
                holder.txtDocType3.setText("DOC");
                holder.txtDocType3.setBackgroundColor(Color.parseColor("#0288D1"));
            } else if (name3.endsWith(".txt") && Pattern.compile("(.*)((\\.(txt))$)", 2).matcher(name3).matches()) {
                holder.txtDocType3.setText("TXT");
                holder.txtDocType3.setBackgroundColor(Color.parseColor("#F2AE29"));
            } else if (name3.endsWith(".pdf") || !Pattern.compile("(.*)((\\.(pdf))$)", 2).matcher(name3).matches()) {
                holder.txtDocType3.setText("PDF");
                holder.txtDocType3.setBackgroundColor(Color.parseColor("#FF0000"));
            }
        }else {
            holder.txtDocName3.setVisibility(View.GONE);
            holder.txtDocType3.setVisibility(View.GONE);
        }
        if (size >= 4) {
            String name4 = new File(docList.get(i).getDocpathList().get(3)).getName();
            if (!name4.equalsIgnoreCase("")) {
                holder.txtDocName4.setText(name4);
            }
            if (name4.endsWith(".doc") && Pattern.compile("(.*)((\\.(doc))$)", 2).matcher(name4).matches()) {
                holder.txtDocType4.setText("DOC");
                holder.txtDocType4.setBackgroundColor(Color.parseColor("#0288D1"));
            } else if (name4.endsWith(".txt") && Pattern.compile("(.*)((\\.(txt))$)", 2).matcher(name4).matches()) {
                holder.txtDocType4.setText("TXT");
                holder.txtDocType4.setBackgroundColor(Color.parseColor("#F2AE29"));
            } else if (name4.endsWith(".pdf") || !Pattern.compile("(.*)((\\.(pdf))$)", 2).matcher(name4).matches()) {
                holder.txtDocType4.setText("PDF");
                holder.txtDocType4.setBackgroundColor(Color.parseColor("#FF0000"));
            }
        }else {
            holder.txtDocName4.setVisibility(View.GONE);
            holder.txtDocType4.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDocFolderSelected(docList.get(i));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onDocFolderLongClick(docList.get(i));
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return docList.size();
    }

    public interface DocFolderAdapterListener {
        void onDocFolderSelected(DocModel docModel);

        void onDocFolderLongClick(DocModel docModel);
    }

}
