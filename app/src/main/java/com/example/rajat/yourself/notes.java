package com.example.rajat.yourself;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class notes extends Fragment {

    RecyclerView rv_notes;
    Button add_notes,save,pickcolor;
    EditText etitle,edes;
    String title_input,des_input,userid;
    DatabaseReference databaseReference,myref;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    public static FirebaseRecyclerAdapter<taskdata,ShowDataViewHolder> mFirebaseAdapterw;
    int dd,mm,yyyy,h,m;

    int pcolor;
        String ptitle,pdes;
     Dialog dialog;
     ImageView fillimage;
     LinearLayout ll;

     int  color;


    public notes() {

    }

    public static notes newInstance() {
        notes fragemnt = new notes();
        return fragemnt;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_main, container, false);
        Firebase.setAndroidContext(getContext());

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        userid = firebaseAuth.getCurrentUser().getUid();

        rv_notes = view.findViewById(R.id.rv_notes);
        add_notes = view.findViewById(R.id.addnotes);
        staggeredGridLayoutManager  = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rv_notes.setLayoutManager(staggeredGridLayoutManager);



        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.notes_input);


        edes = dialog.findViewById(R.id.edes);
        etitle = dialog.findViewById(R.id.etitle);
        save = dialog.findViewById(R.id.save);
        pickcolor = dialog.findViewById(R.id.pickcolor);
       // fillimage = dialog.findViewById(R.id.fillimage);
        ll = dialog.findViewById(R.id.ll);

        add_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edes.setText("");
                etitle.setText("");


                pickcolor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ColorPickerDialogBuilder
                                .with(getContext())
                                .setTitle("Choose color")
                                .initialColor(0xacacac)
                                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                                .density(12)
                                .setOnColorSelectedListener(new OnColorSelectedListener() {
                                    @Override
                                    public void onColorSelected(int i) {

                                    }
                                })
                                .setPositiveButton("Ok", new ColorPickerClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i, Integer[] integers) {
                                        color = i;
                                       // fillimage.setBackgroundColor(i);
                                        ll.setBackgroundColor(i);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .build()
                                .show();
                    }
                });


                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        title_input = etitle.getText().toString();
                        des_input = edes.getText().toString();
                        if(TextUtils.isEmpty(title_input) || TextUtils.isEmpty(des_input)){
                            Toast.makeText(getContext(), "Please fill all the entries first", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        long ct = System.currentTimeMillis();

                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        final Firebase firebase = new Firebase("https://yourself-bro.firebaseio.com/").child("Yourself")
                                .child(userid).child("Notes").child(title_input);
                        Firebase title_ref = firebase.child("title");
                        Firebase des_ref = firebase.child("description");
                        Firebase time_ref = firebase.child("ntime");
                        Firebase negtime_ref = firebase.child("negtime");
                        Firebase colors = firebase.child("color");


                        colors.setValue(color);
                        title_ref.setValue(title_input);
                        des_ref.setValue(des_input);
                        time_ref.setValue(String.valueOf(ct));
                        double neg = (double) 1/ct;
                        negtime_ref.setValue(neg);

                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });
     list();
        return view;
    }


    public static FirebaseRecyclerAdapter<taskdata,ShowDataViewHolder> getmFirebaseAdapter1() {
        return mFirebaseAdapterw;
    }

    public void setmFirebaseAdapter1(FirebaseRecyclerAdapter<taskdata, ShowDataViewHolder> mFirebaseAdapter) {
        this.mFirebaseAdapterw = mFirebaseAdapter;
    }

    public static class ShowDataViewHolder extends RecyclerView.ViewHolder {

        private TextView title,des,time;
        private ImageView remove;
        CardView cardView;



        public ShowDataViewHolder(final View itemView)
        {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            des = itemView.findViewById(R.id.description);
            time = itemView.findViewById(R.id.time);
            remove = itemView.findViewById(R.id.remove);
            cardView = itemView.findViewById(R.id.cv);



        }

    }

   public void list() {


        FirebaseAuth mauth = FirebaseAuth.getInstance();
        String userod = mauth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Yourself").child(userod).child("Notes");


        setmFirebaseAdapter1(new FirebaseRecyclerAdapter<taskdata, ShowDataViewHolder>(
                taskdata.class, R.layout.notes_item,ShowDataViewHolder.class,databaseReference.orderByChild("negtime")) {
            public void populateViewHolder(final ShowDataViewHolder viewHolder, final taskdata model, final int position) {
                viewHolder.title.setText(model.getTitle());
                viewHolder.des.setText(model.getDescription());
                viewHolder.time.setText(gettimeago(model.getNtime()));

                int c = model.getColor();
                if(String.valueOf(c)!=null){
                    viewHolder.cardView.setCardBackgroundColor(c);
                }else {

                }

                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d("position", String.valueOf(position));


                        final int selectedItems = position;
                        final String ss = getmFirebaseAdapter1().getRef(selectedItems).getKey();
                        DatabaseReference myref2 = FirebaseDatabase.getInstance().getReference("Yourself").child(userid).child("Notes");
                        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Firebase firebase = new Firebase("https://yourself-bro.firebaseio.com/").child("Yourself").child(userid).child("Notes");
                                ptitle = dataSnapshot.child(ss).child("title").getValue(String.class);
                                pdes = dataSnapshot.child(ss).child("description").getValue(String.class);
                                pcolor = dataSnapshot.child(ss).child("color").getValue(int.class);
                                etitle.setText(ptitle);
                                edes.setText(pdes);
                                ll.setBackgroundColor(pcolor);
                                updatedata();
                                dialog.show();


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });


                    }
                });

                viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("You Sure ?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final int selectedItems = position;

                                        DatabaseReference myref2 = FirebaseDatabase.getInstance().getReference("Yourself").child(userid).child("Wealth").child("Notes");
                                        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                getmFirebaseAdapter1().getRef(selectedItems).removeValue();
                                                getmFirebaseAdapter1().notifyItemRemoved(selectedItems);
                                                rv_notes.invalidate();
                                                notifyDataSetChanged();

                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
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


        rv_notes.setAdapter(getmFirebaseAdapter1());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    Toast.makeText(getContext(),"Nothing added yet!",Toast.LENGTH_SHORT).show();
                }else {
                    mFirebaseAdapterw.notifyItemChanged(0);
                    mFirebaseAdapterw.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mFirebaseAdapterw.notifyDataSetChanged();
    }



    public String gettimeago(String ntime) {


        final int SECOND_MILLIS = 1000;
        final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        final int DAY_MILLIS = 24 * HOUR_MILLIS;

        if (ntime != null) {
            long earlier = Long.parseLong(ntime);

            long now = System.currentTimeMillis();
            if (earlier < 1000000000000L) {
                earlier *= 1000;
            }

            if (earlier > now || earlier <= 0) {
                return null;
            }

            final long diff = now - earlier;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return (diff / MINUTE_MILLIS + " minutes ago");
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "a hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return (diff / HOUR_MILLIS + " hours ago");
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " days ago";
            }


        }

        return "";

    }

    private void updatedata() {

        etitle.setEnabled(false);

        pickcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(getContext())
                        .setTitle("Choose color")
                        .initialColor(0xacacac)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int i) {

                            }
                        })
                        .setPositiveButton("Ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, Integer[] integers) {
                                color = i;
                                // fillimage.setBackgroundColor(i);
                                ll.setBackgroundColor(i);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .build()
                        .show();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title_input = etitle.getText().toString();
                des_input = edes.getText().toString();
                if(TextUtils.isEmpty(title_input) || TextUtils.isEmpty(des_input)){
                    Toast.makeText(getContext(), "Please fill all the entries first", Toast.LENGTH_SHORT).show();
                    return;
                }
                long ct = System.currentTimeMillis();

                databaseReference = FirebaseDatabase.getInstance().getReference();
                final Firebase firebase = new Firebase("https://yourself-bro.firebaseio.com/").child("Yourself")
                        .child(userid).child("Notes").child(title_input);
                Firebase title_ref = firebase.child("title");
                Firebase des_ref = firebase.child("description");
                Firebase time_ref = firebase.child("ntime");
                Firebase colors = firebase.child("color");


                colors.setValue(color);
                title_ref.setValue(title_input);
                des_ref.setValue(des_input);
                time_ref.setValue(String.valueOf(ct));


                dialog.dismiss();

                rv_notes.invalidate();
            }
        });

    }



}
