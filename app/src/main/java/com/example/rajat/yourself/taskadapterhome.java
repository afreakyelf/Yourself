package com.example.rajat.yourself;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

public class taskadapterhome extends RecyclerView.Adapter<taskadapterhome.ViewHolder> {

    private ArrayList<taskdata> tasklist;
    Context context;
    DatabaseReference myRef;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    public taskadapterhome(ArrayList<taskdata> td, Context context){
        this.context = context;
        this.tasklist = td;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView taskname;
        public ImageView delete;
        public ViewHolder(View itemView) {
            super(itemView);
            taskname = itemView.findViewById(R.id.taskname);
            delete = itemView.findViewById(R.id.remove);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    home.remove(getAdapterPosition());
                }
            });

            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            String userid = firebaseAuth.getCurrentUser().getUid();

            myRef = FirebaseDatabase.getInstance().getReference("News_User_Details").child(userid);

        }
    }

    @NonNull
    @Override
    public taskadapterhome.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.taskitem,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull taskadapterhome.ViewHolder holder, int position) {
        holder.taskname.setText(tasklist.get(position).getTaskname().toString());



    }


    @Override
    public int getItemCount() {
        return (null != tasklist?tasklist.size():0);
    }





}
