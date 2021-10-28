package com.example.hostelkhoj;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class Adapter extends FirestoreRecyclerAdapter<HostelModel,Adapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private OnItemClickListener listener;
    private List<String> hnames;
    private List<String> onames;
    private List<String> types;


    public Adapter(@NonNull FirestoreRecyclerOptions<HostelModel> options) {
        super(options);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout,parent,false);
        return new ViewHolder(view);
    }
/*
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String hname=hnames.get(position);
        holder.hostelname.setText(hname);

        String oname=onames.get(position);
        holder.ownername.setText(oname);

        String type=types.get(position);
        holder.hosteltype.setText(type);


    }

 */

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull HostelModel model) {


        holder.hostelname.setText(model.getHostelname());
        holder.hosteltype.setText(model.getType());
        holder.ownername.setText(model.getOwnername());
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView hostelname,ownername,hosteltype;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hostelname=itemView.findViewById(R.id.hostelnametxt);
            ownername=itemView.findViewById(R.id.ownernametxt);
            hosteltype=itemView.findViewById(R.id.hosteltypetxt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }


    }

    public interface OnItemClickListener{
        public void onItemClick(DocumentSnapshot snapshot,int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener=listener;
    }

}
