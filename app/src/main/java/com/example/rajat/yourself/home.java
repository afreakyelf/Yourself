package com.example.rajat.yourself;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.rajat.yourself.login.googleApiClient;

public class home extends Fragment {

    DatabaseReference databaseReference,myref;
    private Firebase firebase;

    ImageView imageView,delete;
    TextView name,email;
    EditText taskinput ;
    Button addtask,cancel;
    String taskoutput;
    int hour,min,sec,ms,dd,m,y;
    static String a,b,ca,d,e,f,g;


    FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private LinearLayout ll;
    public static  taskadapterhome taskadapterhome;
    private LinearLayoutManager recyclerViewLayoutManager;
    FloatingActionButton addbutton;
    public static ArrayList<taskdata> taskdata2 = new ArrayList<>();
    String userid;
    public static FirebaseRecyclerAdapter<taskdata,ShowDataViewHolder> mFirebaseAdapter;

    public static home newInstance(){
        home fragment = new home();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.home,container,false);
        Firebase.setAndroidContext(getActivity());


        imageView = rootview.findViewById(R.id.profile);
        name = rootview.findViewById(R.id.name);
        email = rootview.findViewById(R.id.email);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        name.setText(user.getDisplayName());

        Glide.with(this).load(user.getPhotoUrl()).into(imageView);
        email.setText(user.getEmail());


/**/

        recyclerView =  rootview.findViewById(R.id.rv);
        recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewLayoutManager.setReverseLayout(true);
        recyclerViewLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        taskadapterhome = new taskadapterhome(taskdata2,getContext());
        recyclerView.setAdapter(taskadapterhome);


        addbutton = rootview.findViewById(R.id.add);

        //time

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final taskdata mlog = new taskdata();

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.input_for_task);

                taskinput= dialog.findViewById(R.id.input);
                taskinput.requestFocus();
                addtask = dialog.findViewById(R.id.addtask);

                cancel = dialog.findViewById(R.id.cancel_action);
                dialog.setTitle("New Task");

                addtask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Calendar c = Calendar.getInstance();
                        hour = c.get(Calendar.HOUR_OF_DAY);
                        min = c.get(Calendar.MINUTE);
                        sec = c.get(Calendar.SECOND);
                        ms = c.get(Calendar.MILLISECOND);
                        dd = c.get(Calendar.DATE);
                        m = c.get(Calendar.MONTH);
                        y = c.get(Calendar.YEAR);

                        a = String.valueOf(hour);
                        b = String.valueOf(min);
                        ca = String.valueOf(sec);
                        d = String.valueOf(ms);
                        e= String.valueOf(dd);
                        f = String.valueOf(m);
                        g = String.valueOf(y);


                        taskoutput = taskinput.getText().toString();
                        mlog.setTaskname(taskoutput);
                        taskdata2.add(mlog);
                        taskadapterhome.notifyDataSetChanged();
                        dialog.dismiss();

                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        userid= firebaseAuth.getCurrentUser().getUid();


                        firebase = new Firebase("https://yourself-bro.firebaseio.com/").child("Yourself").child(userid).child("task").child(e+"-"+f+"-"+g+"_"+a+":"+b+":"+ca+":"+d);

                        databaseReference.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Firebase child_ref = firebase.child("taskname");
                                child_ref.setValue(taskoutput);
                                }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }

        });

        recyclerView.smoothScrollToPosition(0);

        return rootview;
    }



    public FirebaseRecyclerAdapter<taskdata, ShowDataViewHolder> getmFirebaseAdapter() {
        return mFirebaseAdapter;
    }

    public void setmFirebaseAdapter(FirebaseRecyclerAdapter<taskdata, ShowDataViewHolder> mFirebaseAdapter) {
        this.mFirebaseAdapter = mFirebaseAdapter;
    }


    //View Holder For Recycler View
    public static class ShowDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView image_title  ;
        private ImageView delete;


        public ShowDataViewHolder(final View itemView)
        {
            super(itemView);

            image_title = itemView.findViewById(R.id.taskname);
            delete = itemView.findViewById(R.id.remove);


        }

        private void Image_Title(String title)
        {
            image_title.setText(title);
        }


    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        String userod = mauth.getCurrentUser().getUid();
        myref = FirebaseDatabase.getInstance().getReference("Yourself").child(userod).child("task");

        setmFirebaseAdapter(new FirebaseRecyclerAdapter<taskdata, ShowDataViewHolder>(
                taskdata.class, R.layout.taskitem, ShowDataViewHolder.class, myref.orderByValue()) {
            public void populateViewHolder(final ShowDataViewHolder viewHolder, taskdata model, final int position) {

                viewHolder.Image_Title(model.getTaskname());


                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("You Sure ?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int selectedItems = position;

                                        if(getmFirebaseAdapter().getItemCount() >0) {
                                            getmFirebaseAdapter().getRef(selectedItems).removeValue();
                                            getmFirebaseAdapter().notifyItemRemoved(selectedItems);
                                            recyclerView.invalidate();
                                            notifyItemRangeChanged(position, getItemCount());
                                        }
                                        else {
                                            getmFirebaseAdapter().getRef(selectedItems).removeValue();
                                            getmFirebaseAdapter().notifyItemRemoved(selectedItems);
                                            recyclerView.invalidate();
                                            onStart();

                                        }
                                        dialog.dismiss();

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        notifyDataSetChanged();
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });

            }

        });


        recyclerView.setAdapter(getmFirebaseAdapter());


        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    Toast.makeText(getContext(),"No Bookmarks added yet!",Toast.LENGTH_SHORT).show();
                    }else {
                    mFirebaseAdapter.notifyItemChanged(0);
                    mFirebaseAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mFirebaseAdapter.notifyItemInserted(taskdata2.size()-1);
    }


    public static void remove(int index) {
        taskdata2.remove(index);
        taskadapterhome.notifyDataSetChanged();
    }
}

