package com.example.rajat.yourself;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;


public class wealth extends Fragment {

    Spinner month, year;
    String month_output_from_spinner, year_output_from_spinner;
    String[] month_for_spinner = {"Month", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    String[] year_for_spinner = {"Year", "18", "19"};
    EditText credit_input, ireason, ihoumuch;
    public String fixed_credit, userid;
    String phonenumber = "Not Available";


    String ordate,ormonth,oryear;
    EditText oname;
    EditText ohowmuch;
    int d, m, y,hour,min,sec;
    Button find, add, isubmit, set, idate_pick, rc,itodaypick,add_owe,pickcontact;
    RecyclerView rv_table,owe_rv;
    DatabaseReference databaseReference, myref1, myref2;
    public static DatabaseReference retrieceds,retrieceds1;
    public static FirebaseRecyclerAdapter<taskdata, ShowDataViewHolder> mFirebaseAdapterw;
    public static FirebaseRecyclerAdapter<taskdata, ShowDataViewHolder1> mFirebaseAdapter1;
    private ArrayList<taskdata> taskdata = new ArrayList<>();
    LinearLayoutManager linearLayoutManager,linearLayoutManager1;
    String final_object;
   public static String result_credit;
    TextView tom, total, idate,leftmoneytv;
    String totals,targetforgraph,howmuchwas,targets,lefts,lastinputdate;
    int dateeee,monthee;
    static String orderby;
    ProgressBar progressBarring;
    static final int RESULT_PICK_CONTACT = 1;

    Switch aSwitch;

    public wealth() {

    }

    public static wealth newInstance() {
        wealth fragemnt = new wealth();
        return fragemnt;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wealth_main, container, false);
        Firebase.setAndroidContext(getContext());
        final_object = "January_18";
        orderby= "day";

        credit_input = view.findViewById(R.id.credit_input);
        add = view.findViewById(R.id.add);
        rv_table = view.findViewById(R.id.budget_rv);
        rv_table.getRecycledViewPool().setMaxRecycledViews(20,0);
        linearLayoutManager = new LinearLayoutManager(getContext());
        //linearLayoutManager.setStackFromEnd(true);
        //linearLayoutManager.setReverseLayout(true);
        rv_table.setLayoutManager(linearLayoutManager);
        leftmoneytv = view.findViewById(R.id.leftmoney);

        add_owe = view.findViewById(R.id.add_owe);
        owe_rv = view.findViewById(R.id.owe_rv);
        linearLayoutManager1 = new LinearLayoutManager(getContext());
        owe_rv.setLayoutManager(linearLayoutManager1);


        rv_table.getRecycledViewPool().setMaxRecycledViews(9,0);
        total = view.findViewById(R.id.total);
        find = view.findViewById(R.id.find);
        rc = view.findViewById(R.id.refreshchart);

        tom = view.findViewById(R.id.target_of_month);

        progressBarring = view.findViewById(R.id.progressring);


        month = view.findViewById(R.id.month_spinner);
        ArrayAdapter a = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, month_for_spinner);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month.setAdapter(a);
        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month_output_from_spinner = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        year = view.findViewById(R.id.year_spinner);
        ArrayAdapter b = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, year_for_spinner);
        b.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(b);
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year_output_from_spinner = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                final_object = "January_18";
                list(final_object, result_credit);
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final_object = month_output_from_spinner + "_" + year_output_from_spinner;
                DatabaseReference myref2 = FirebaseDatabase.getInstance().getReference("Yourself").child(userid).child("Wealth").child(final_object);

                myref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        result_credit = dataSnapshot.child("target").getValue(String.class);
                        totals = dataSnapshot.child("total").getValue(String.class);
                        String leftmoney = dataSnapshot.child("left").getValue(String.class);
                        if(leftmoney!=null) {
                            int lm = Integer.parseInt(leftmoney);
                            int rc = Integer.parseInt(result_credit);
                            float progress = 100-(float)lm/rc * 100;
                            leftmoneytv.setText(leftmoney);
                            progressBarring.setProgress((int) progress);
                        }
                        list(final_object, result_credit);
                        tom.setText(result_credit);

                        if (totals != null) {
                            total.setText(totals);
                        } else {
                            total.setText("0");
                        }
                        setgraph(result_credit);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        rc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 setgraph(result_credit);

            }
        });

        set = view.findViewById(R.id.set);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        userid = firebaseAuth.getCurrentUser().getUid();
       // fixed_credit = "0";
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fixed_credit = credit_input.getText().toString();
                if (TextUtils.isEmpty(fixed_credit)) {
                    Toast.makeText(getActivity(), "Please set the Target", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Calendar c = Calendar.getInstance();

                databaseReference = FirebaseDatabase.getInstance().getReference();
                final Firebase firebase = new Firebase("https://yourself-bro.firebaseio.com/").child("Yourself")
                        .child(userid).child("Wealth").child(month_output_from_spinner + "_" + year_output_from_spinner);
                final Firebase c_ref = firebase.child("target");
                final Firebase left_ref = firebase.child("left");
                final Firebase last_ref = firebase.child("lastdate");
                if (fixed_credit != null) {
                    c_ref.setValue(fixed_credit);
                    left_ref.setValue(fixed_credit);
                    last_ref.setValue(String.valueOf(c.get(Calendar.DAY_OF_MONTH))+"/"+String.valueOf(c.get(Calendar.MONTH)));
                } else {
                    c_ref.setValue(null);
                    left_ref.setValue(null);
                }
                list(final_object, fixed_credit);
                setgraph(fixed_credit);
                tom.setText(fixed_credit);
            }
        });




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.input_for_wealth);

                ireason = dialog.findViewById(R.id.ireason);
                idate = dialog.findViewById(R.id.idate);
                idate_pick = dialog.findViewById(R.id.idate_pick);
                ihoumuch = dialog.findViewById(R.id.ihowmuch);
                isubmit = dialog.findViewById(R.id.isubmit);
                itodaypick = dialog.findViewById(R.id.itodaypick);


                final Calendar c = Calendar.getInstance();

                itodaypick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d = c.get(Calendar.DATE);
                        m = c.get(Calendar.MONTH);
                        y = c.get(Calendar.YEAR);
                        hour = c.get(Calendar.HOUR_OF_DAY);
                        min = c.get(Calendar.MINUTE);
                        sec = c.get(Calendar.SECOND);

                        int mon = m+1;
                        idate.setText(d+"-"+mon+"-"+y);
                        dateeee = d;
                        monthee = mon;
                    }
                });

                idate_pick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d = c.get(Calendar.DATE);
                        m = c.get(Calendar.MONTH);
                        y = c.get(Calendar.YEAR);

                        hour = c.get(Calendar.HOUR_OF_DAY);
                        min = c.get(Calendar.MINUTE);
                        sec = c.get(Calendar.SECOND);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int date) {
                                int finalm = month + 1;
                                idate.setText(date + "-" + finalm + "-" + year);
                                dateeee = date;
                                monthee = finalm;
                            }
                        }, y, m, d);
                        datePickerDialog.show();
                    }
                });


                isubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String howmuch, reason, date;
                        howmuch = ihoumuch.getText().toString();
                        reason = ireason.getText().toString();
                        date = idate.getText().toString();

                        if ((TextUtils.isEmpty(howmuch)) || (TextUtils.isEmpty(reason)) || (TextUtils.isEmpty(date))) {
                            Toast.makeText(getActivity(), "Please fill all the details first", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        DatabaseReference myref2 = FirebaseDatabase.getInstance().getReference("Yourself").child(userid).child("Wealth").child(final_object);

                        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                totals = dataSnapshot.child("total").getValue(String.class);
                                targets = dataSnapshot.child("target").getValue(String.class);
                                lefts = dataSnapshot.child("left").getValue(String.class);
                                lastinputdate = dataSnapshot.child("lastdate").getValue(String.class);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        userid = firebaseAuth.getCurrentUser().getUid();
                        final Firebase firebase = new Firebase("https://yourself-bro.firebaseio.com/").child("Yourself").child(userid).child("Wealth").child(month_output_from_spinner + "_" + year_output_from_spinner);
                        databaseReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Firebase h_ref = firebase.child("userinput").child(reason + howmuch).child("howmuch");
                                final Firebase r_ref = firebase.child("userinput").child(reason + howmuch).child("reason");
                                final Firebase d_ref = firebase.child("userinput").child(reason + howmuch).child("date");
                                final Firebase l_ref = firebase.child("userinput").child(reason + howmuch).child("left2");
                                final Firebase rev_ref = firebase.child("userinput").child(reason + howmuch).child("day");
                                final Firebase time_ref = firebase.child("userinput").child(reason + howmuch).child("time");
                                final Firebase total_ref = firebase.child("total");
                                final Firebase left = firebase.child("left");
                                final Firebase lastdate = firebase.child("lastdate");



                                int b;
                                if (totals == null) {
                                    b = 0;
                                } else {
                                    b = Integer.parseInt(totals);
                                }

                                int a = b + Integer.parseInt(howmuch);
                                total_ref.setValue(String.valueOf(a));
                                total.setText(String.valueOf(a));


                                h_ref.setValue(howmuch);
                                r_ref.setValue(reason);
                                d_ref.setValue(date);
                                rev_ref.setValue(String.valueOf(dateeee));
                                int mm = m +1;
                                lastdate.setValue(String.valueOf(dateeee)+"/"+String.valueOf(mm));
                                time_ref.setValue(String.valueOf(hour)+":"+String.valueOf(min)+":"+String.valueOf(sec));

                                if(lefts!=null) {
                                    left.setValue(String.valueOf(Integer.parseInt(lefts) - Integer.parseInt(howmuch)));
                                    l_ref.setValue(String.valueOf(Integer.parseInt(lefts) - Integer.parseInt(howmuch)));
                                }


                                int aa=Integer.parseInt(lefts);
                                int bb = Integer.parseInt(howmuch);
                                int cc =Integer.parseInt(targets);
                                float p = 100-(float)(aa-bb)/cc*100;
                                progressBarring.setProgress((int)p);
                                leftmoneytv.setText(String.valueOf(Integer.parseInt(lefts) - Integer.parseInt(howmuch)));

                                if(lastinputdate.equals(String.valueOf(dateeee) + "/" + String.valueOf(mm))){
                                    orderby = "day";
                                }else {
                                    orderby = "time";
                                }




                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });



                        dialog.dismiss();

                        DatabaseReference myref4 = FirebaseDatabase.getInstance().getReference("Yourself").child(userid).child("Wealth").child(final_object).child("userinput");

                        myref4.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                setgraph(result_credit);
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
                dialog.show();
            }
        });


        add_owe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.owe_input);


                final TextView reminddate;
                Button datepick,submit;



                oname = dialog.findViewById(R.id.iname);
                ohowmuch = dialog.findViewById(R.id.iowehowmuch);
                reminddate = dialog.findViewById(R.id.iremind);
                datepick = dialog.findViewById(R.id.iowedate_pick);
                submit = dialog.findViewById(R.id.iowesubmit);
                pickcontact = dialog.findViewById(R.id.pickcontact);
                aSwitch = dialog.findViewById(R.id.aswitch);

                pickcontact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(intent,RESULT_PICK_CONTACT);
                    }
                });

                final Calendar c = Calendar.getInstance();

                datepick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       int d = c.get(Calendar.DATE);
                       int m = c.get(Calendar.MONTH);
                        final int yy = c.get(Calendar.YEAR);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int date) {
                                int finalm = month + 1;
                                idate.setText(date + "-" + finalm + "-" + year);
                                ordate = String.valueOf(date);
                                ormonth = String.valueOf(finalm);
                                oryear = String.valueOf(year);
                            }
                        }, yy, m, d);
                        datePickerDialog.show();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String sname = oname.getText().toString();
                        final String showmuch = ohowmuch.getText().toString();
                        final String date = reminddate.getText().toString();

                        if ((TextUtils.isEmpty(sname)) || (TextUtils.isEmpty(showmuch))) {
                            Toast.makeText(getActivity(), "Please fill all the details first", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        userid = firebaseAuth.getCurrentUser().getUid();
                        final Firebase firebase = new Firebase("https://yourself-bro.firebaseio.com/").child("Yourself")
                                .child(userid).child("Wealth").child(month_output_from_spinner + "_" + year_output_from_spinner).child("owe");
                        databaseReference.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Firebase name_ref = firebase.child(sname+showmuch).child("oname");
                                final Firebase hm_ref = firebase.child(sname+showmuch).child("ohowmuch");
                                final Firebase date_ref = firebase.child(sname+showmuch).child("odate");
                                final Firebase phonenumber_ref = firebase.child(sname+showmuch).child("ophonenumber");

                                name_ref.setValue(sname);
                                hm_ref.setValue(showmuch);
                                date_ref.setValue(date);
                                phonenumber_ref.setValue(phonenumber);


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                        if (aSwitch.isChecked()) {

                            Toast.makeText(getContext(), "Reminder On", Toast.LENGTH_SHORT).show();
                            set_alarm_time(ordate, ormonth, oryear, sname,showmuch);

                        } else {

                            Toast.makeText(getContext(), "No Reminder set", Toast.LENGTH_SHORT).show();
                            Intent n_intent = new Intent(getContext(), AlarmReceiver.class);

                            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getContext(), 0, n_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                            alarmManager.cancel(pendingIntent1);

                        }

                        dialog.dismiss();
                    }
                });



                dialog.show();
            }
        });


        return view;
    }


    public void setgraph(String fixed_credit) {
        android.support.v4.app.Fragment selectedFragment = null;
        selectedFragment =graph.newInstance();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("finalobject",final_object);
        bundle.putString("target",fixed_credit);
        selectedFragment.setArguments(bundle);
        transaction.replace(R.id.graph_frame,selectedFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }







    public FirebaseRecyclerAdapter<taskdata,ShowDataViewHolder> getmFirebaseAdapter1() {
        return mFirebaseAdapterw;
    }

    public void setmFirebaseAdapter1(FirebaseRecyclerAdapter<taskdata,ShowDataViewHolder> mFirebaseAdapter) {
        this.mFirebaseAdapterw = mFirebaseAdapter;
    }

    public FirebaseRecyclerAdapter<taskdata,ShowDataViewHolder1> getmFirebaseAdapter2() {
        return mFirebaseAdapter1;
    }

    public void setmFirebaseAdapter2(FirebaseRecyclerAdapter<taskdata,ShowDataViewHolder1> mFirebaseAdapter) {
        this.mFirebaseAdapter1 = mFirebaseAdapter;
    }


    //View Holder For Recycler View
    public static class ShowDataViewHolder extends RecyclerView.ViewHolder {

        private TextView reason,howmuch,date,left,sno;
        private ImageView remove;



        public ShowDataViewHolder(final View itemView)
        {
            super(itemView);
            reason = itemView.findViewById(R.id.reason);
            howmuch = itemView.findViewById(R.id.howmuch);
            date = itemView.findViewById(R.id.date);
            left = itemView.findViewById(R.id.left);
            sno = itemView.findViewById(R.id.sno);
            remove = itemView.findViewById(R.id.remove);
        }

    }

    //View Holder For Recycler View
    public static class ShowDataViewHolder1 extends RecyclerView.ViewHolder {

        private TextView oname,ohowmuch,odate,ophonenumber,paynow,paid;

        public ShowDataViewHolder1(final View itemView)
        {
            super(itemView);
            oname = itemView.findViewById(R.id.oname);
            ohowmuch = itemView.findViewById(R.id.howmuchowe);
            odate = itemView.findViewById(R.id.remindyou);
            ophonenumber = itemView.findViewById(R.id.phonenumber);
            paynow = itemView.findViewById(R.id.paynow);
            paid = itemView.findViewById(R.id.paid);

        }

    }



    public void list(final String final_object, final String result_credit) {

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        String userod = mauth.getCurrentUser().getUid();

        Toast.makeText(getContext(), this.final_object, Toast.LENGTH_SHORT).show();
        retrieceds = FirebaseDatabase.getInstance().getReference("Yourself").child(userod).child("Wealth").child(final_object).child("userinput");
        retrieceds1 = FirebaseDatabase.getInstance().getReference("Yourself").child(userod).child("Wealth").child(final_object).child("owe");


        setmFirebaseAdapter1(new FirebaseRecyclerAdapter<taskdata, wealth.ShowDataViewHolder>(
                taskdata.class, R.layout.budget_rv_layout, wealth.ShowDataViewHolder.class,retrieceds.orderByChild(orderby)) {
            public void populateViewHolder(final wealth.ShowDataViewHolder viewHolder, final taskdata model, final int position) {

                viewHolder.reason.setText(model.getReason());
                viewHolder.date.setText(model.getDate());
                viewHolder.howmuch.setText(model.getHowmuch());
                viewHolder.sno.setText(String.valueOf(position+1));
                viewHolder.left.setText(model.getLeft2());




                viewHolder.remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("You Sure ?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final int selectedItems = position;
                                        final String ss = getmFirebaseAdapter1().getRef(selectedItems).getKey();
                                        DatabaseReference myref2 = FirebaseDatabase.getInstance().getReference("Yourself").child(userid).child("Wealth").child(final_object);
                                        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                final Firebase firebase = new Firebase("https://yourself-bro.firebaseio.com/").child("Yourself").child(userid).child("Wealth").child(final_object);
                                                howmuchwas = dataSnapshot.child("userinput").child(ss).child("howmuch").getValue(String.class);
                                                totals = dataSnapshot.child("total").getValue(String.class);
                                                int re = Integer.parseInt(totals)-Integer.parseInt(howmuchwas);
                                                final Firebase total_ref = firebase.child("total");
                                                total.setText(String.valueOf(re));
                                                total_ref.setValue(String.valueOf(re));

                                                    getmFirebaseAdapter1().getRef(selectedItems).removeValue();
                                                    getmFirebaseAdapter1().notifyItemRemoved(selectedItems);
                                                    rv_table.invalidate();
                                                    notifyDataSetChanged();

                                                setgraph(result_credit);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });



                                        dialog.dismiss();
                                        setgraph(result_credit);


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


        setmFirebaseAdapter2(new FirebaseRecyclerAdapter<taskdata,wealth.ShowDataViewHolder1>(
                taskdata.class, R.layout.owe_item, wealth.ShowDataViewHolder1.class,retrieceds1.orderByValue()) {
            public void populateViewHolder(final wealth.ShowDataViewHolder1 viewHolder, final taskdata model, final int position) {

                viewHolder.oname.setText(model.getOname());
                viewHolder.odate.setText(model.getOdate());
                viewHolder.ophonenumber.setText(model.getOphonenumber());
                viewHolder.ohowmuch.setText(model.getOhowmuch());


                viewHolder.paid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("You Sure ?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final int selectedItems = position;

                                        DatabaseReference myref2 = FirebaseDatabase.getInstance().getReference("Yourself").
                                                child(userid).child("Wealth").child(final_object);
                                        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                    getmFirebaseAdapter2().getRef(selectedItems).removeValue();
                                                    getmFirebaseAdapter2().notifyItemRemoved(selectedItems);
                                                    owe_rv.invalidate();
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



        owe_rv.setAdapter(getmFirebaseAdapter2());
        rv_table.setAdapter(getmFirebaseAdapter1());

        retrieceds.addListenerForSingleValueEvent(new ValueEventListener() {
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
        retrieceds1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    Toast.makeText(getContext(),"Nothing added yet!",Toast.LENGTH_SHORT).show();
                }else {
                    mFirebaseAdapter1.notifyItemChanged(0);
                    mFirebaseAdapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mFirebaseAdapterw.notifyItemInserted(taskdata.size()-1);
        mFirebaseAdapterw.notifyDataSetChanged();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RESULT_PICK_CONTACT:
                Uri uri = data.getData();
                String name = null;
                Cursor c = getContext().getContentResolver().query(uri,null,null,null,null);
                c.moveToFirst();
                int numbera = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
                int names = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                name = c.getString(names);
                phonenumber = c.getString(numbera);
                oname.setText(name);



        }

    }

    public void set_alarm_time(String dd, String mm, String yyyy, final String name, final String amount) {

        Calendar calset = Calendar.getInstance();
        Calendar cal = (Calendar)calset.clone();
        cal.set(Calendar.HOUR_OF_DAY,9);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.DATE, Integer.parseInt(dd));
        cal.set(Calendar.MONTH, Integer.parseInt(mm));
        cal.set(Calendar.YEAR, Integer.parseInt(yyyy));
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        if(cal.compareTo(calset) <=0){
            cal.add(Calendar.DATE,1);
        }

        setAlarm(cal,name,amount);


    }

    public void setAlarm(Calendar targetCal, String name, String amount) {
        Toast.makeText(getContext(), "Alarm is set " + targetCal.getTime(), Toast.LENGTH_SHORT).show();

        AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
        Intent n_intent = new Intent(getContext(),AlarmReceiver.class);
        n_intent.putExtra(AlarmReceiver.NOTIFICATION_ID,1);
        n_intent.putExtra(AlarmReceiver.NOTIFICATION,getNotification("You have to pay " +amount+ " to "+name ,"I owe"));
        final int _id = (int)System.currentTimeMillis();
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getContext(),_id,n_intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(),pendingIntent1);




    }

    public Notification getNotification(String content, String subjectt){
        Notification.Builder builder = new Notification.Builder(getContext());
        builder.setContentTitle(subjectt);
        builder.setContentText(content);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        return builder.build();
    }


}