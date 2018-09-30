package com.example.rajat.yourself;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class quiz_assignment_adapter extends RecyclerView.Adapter<quiz_assignment_adapter.ViewHolder> {
    private ArrayList<taskdata> taskdataArrayList;
    Context context;

    public quiz_assignment_adapter(ArrayList<taskdata> taskdata, Context context){
        this.taskdataArrayList = taskdata;
        this.context = context;
    }


    class ViewHolder extends RecyclerView.ViewHolder{



        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public quiz_assignment_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull quiz_assignment_adapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
