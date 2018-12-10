package com.example.rajat.yourself;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.DatePickerDialog.*;
import static com.example.rajat.yourself.wealth.result_credit;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

public class study extends Fragment implements OnItemSelectedListener {

    RecyclerView rv_assignment,rv_quiz,rv_attendance;
    ImageButton date,time,dater,timer;
    EditText edate,edater,etime,etimer,name,taskinput , subjectinput ,subjectinputforam;
    int d,m,y,h,min,s;
    String subjectforam;
    String finaltime , finaldate , finaltimer , finaldater;
    String task,subject;
    private ArrayList<taskdata> taskdata = new ArrayList<>();
    Button add,cancel,add_subject;
    DatabaseReference databaseReference,myref1,myref2,myref3;
    String userid;
    Firebase firebase;
    public static String task_type;
    String hou;
    String mi;
    String dd,mm,yyyy;
    private Switch alarm_switch;
    Button submit_am;
    LinearLayout ll_reminddate;

    String previous_yes,last_total;
    float ptotal,pyes;



    FloatingActionButton add_assignment,add_quiz;
    public LinearLayoutManager linearLayoutManager1,linearLayoutManager2,linearLayoutManager3;
    public static FirebaseRecyclerAdapter<taskdata,ShowDataViewHolder> mFirebaseAdapter1,mFirebaseAdapter2;
    public static FirebaseRecyclerAdapter<taskdata,ShowDataViewHolder2>mFirebaseAdapter3;
    public study() {

    }

    public static study newInstance(){
        study fragemnt = new study();
        return fragemnt;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.study_main,container,false);
        Firebase.setAndroidContext(getContext());


        rv_assignment = view.findViewById(R.id.rv_assignments);
        rv_quiz = view.findViewById(R.id.rv_quiz);
        rv_attendance = view.findViewById(R.id.rv_attendance);
        add_subject = view.findViewById(R.id.add_sub);

        add_assignment = view.findViewById(R.id.add_assignment);
        linearLayoutManager1 = new LinearLayoutManager(getContext());
        linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager3 = new LinearLayoutManager(getContext());
        rv_assignment.setLayoutManager(linearLayoutManager1);
        rv_quiz.setLayoutManager(linearLayoutManager2);
        rv_attendance.setLayoutManager(linearLayoutManager3);





        add_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final taskdata mlog = new taskdata();
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.input_for_subject_am);

                subjectinputforam = dialog.findViewById(R.id.subject);
                submit_am = dialog.findViewById(R.id.submit);

                submit_am.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        subjectforam = subjectinputforam.getText().toString();


                        mlog.setSubjectforam(subjectforam);
                        taskdata.add(mlog);

                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        userid = firebaseAuth.getCurrentUser().getUid();

                        firebase = new Firebase("https://yourself-bro.firebaseio.com/").child("Yourself").child(userid).child("Attendance").child(subjectforam);

                        databaseReference.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Firebase subject_ref = firebase.child("subjectforam");
                                final Firebase yes_ref = firebase.child("yes");
                                final Firebase no_ref = firebase.child("no");
                                final Firebase total = firebase.child("total");
                                subject_ref.setValue(subjectforam);
                                yes_ref.setValue("0");
                                no_ref.setValue("0");
                                total.setValue("0");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        dialog.dismiss();

                    }
                });

                dialog.show();

            }
        });

        add_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final taskdata mlog = new taskdata();
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.quiz_assignment_input);


                time = dialog.findViewById(R.id.time_pick);
                date = dialog.findViewById(R.id.date_pick);
                edate = dialog.findViewById(R.id.date_et);
                etime = dialog.findViewById(R.id.time_et);
                taskinput = dialog.findViewById(R.id.input);
                subjectinput = dialog.findViewById(R.id.subject);
                add = dialog.findViewById(R.id.addtask);
                cancel = dialog.findViewById(R.id.cancel_action);
                alarm_switch = dialog.findViewById(R.id.aswitch);
                ll_reminddate = dialog.findViewById(R.id.ll_reminddater);
                edater = dialog.findViewById(R.id.date_etr);
                etimer = dialog.findViewById(R.id.time_etr);
                timer = dialog.findViewById(R.id.time_pickr);
                dater = dialog.findViewById(R.id.date_pickr);


                //Spinner
                final Spinner spinner = dialog.findViewById(R.id.spinner);
                List<String> entries = new ArrayList<String>();
                entries.add("Assignment");
                entries.add("Quiz");
                ArrayAdapter<String> datadapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,entries);
                datadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(datadapter);
                spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        task_type = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        task_type="Assignment";
                    }
                });


                final Calendar c = Calendar.getInstance();
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d = c.get(Calendar.DATE);
                        m = c.get(Calendar.MONTH);
                        y = c.get(Calendar.YEAR);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int date) {
                                int finalm = month+1;
                                edate.setText(date+"-"+finalm+"-"+year);
                                //dd = String.valueOf(date);

                            }
                        },y,m,d);
                        datePickerDialog.show();
                    }

                });
                time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        h = c.get(Calendar.HOUR_OF_DAY);
                        min = c.get(Calendar.MINUTE);
                        s = c.get(Calendar.SECOND);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                etime.setText(hourOfDay+":"+minute+":"+s);
//                                hou = String.valueOf(hourOfDay);
//                                mi = String.valueOf(minute);

                            }
                        },h,min,false);
                        timePickerDialog.show();
                    }
                });
                dater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d = c.get(Calendar.DATE);
                        m = c.get(Calendar.MONTH);
                        y = c.get(Calendar.YEAR);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int date) {
                                int finalm = month+1;
                                edater.setText(date+"-"+finalm+"-"+year);
                                dd = String.valueOf(date);
                                mm = String.valueOf(month);
                                yyyy =String.valueOf(year);

                            }
                        },y,m,d);
                        datePickerDialog.show();
                    }

                });
                timer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        h = c.get(Calendar.HOUR_OF_DAY);
                        min = c.get(Calendar.MINUTE);
                        s = c.get(Calendar.SECOND);

                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                etimer.setText(hourOfDay+":"+minute+":"+s);
                                hou = String.valueOf(hourOfDay);
                                mi = String.valueOf(minute);

                            }
                        },h,min,false);
                        timePickerDialog.show();
                    }
                });



                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finaltime = etime.getText().toString();
                        finaldate = edate.getText().toString();
                        finaldater = edater.getText().toString();
                        finaltimer = etimer.getText().toString();
                        task = taskinput.getText().toString();
                        subject = subjectinput.getText().toString();

                        mlog.setTaskname(task);
                        mlog.setDate(finaldate);
                        mlog.setTime(finaltime);
                        taskdata.add(mlog);

                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        userid = firebaseAuth.getCurrentUser().getUid();

                        if (task_type.equals("Assignment")) {
                            firebase = new Firebase("https://yourself-bro.firebaseio.com/").child("Yourself").child(userid).child("Assignment").child(task);
                        } else {
                            firebase = new Firebase("https://yourself-bro.firebaseio.com/").child("Yourself").child(userid).child("Quiz").child(task);
                        }

                        databaseReference.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Firebase task_ref = firebase.child("taskname");
                                final Firebase time_ref = firebase.child("time");
                                final Firebase date_Ref = firebase.child("date");
                                final Firebase sub_ref = firebase.child("subject");
                                final Firebase hr_ref = firebase.child("hour");
                                final Firebase min_ref = firebase.child("minute");
                                final Firebase day_ref = firebase.child("day");
                                final Firebase month_ref = firebase.child("month");
                                final Firebase year_ref = firebase.child("year");

                                task_ref.setValue(task);
                                time_ref.setValue(finaltime);
                                date_Ref.setValue(finaldate);
                                sub_ref.setValue(subject);
                                hr_ref.setValue(hou);
                                min_ref.setValue(mi);
                                day_ref.setValue(dd);
                                month_ref.setValue(mm);
                                year_ref.setValue(yyyy);



                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                                if (alarm_switch.isChecked()) {

                                    Toast.makeText(getContext(), "Reminder On", Toast.LENGTH_SHORT).show();
                                    set_alarm_time(dd, mm, yyyy, finaltime, finaldate, hou, mi, subject, task + " Submission");

                               } else {
                                    finaldater = "";
                                    finaltime = "";
                                    Toast.makeText(getContext(), "No Reminder set", Toast.LENGTH_SHORT).show();
                                    Intent n_intent = new Intent(getContext(), AlarmReceiver.class);
                                    PendingIntent pendingIntent1 = PendingIntent.getBroadcast(getContext(), 0, n_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                                    alarmManager.cancel(pendingIntent1);

                            }


                        dialog.dismiss();
                    }});

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            dialog.show();
            }});


        return view;
   }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




    //Retrieving Data

    public FirebaseRecyclerAdapter<taskdata, ShowDataViewHolder> getmFirebaseAdapter1() {
        return mFirebaseAdapter1;
    }

    public void setmFirebaseAdapter1(FirebaseRecyclerAdapter<taskdata, ShowDataViewHolder> mFirebaseAdapter) {
        this.mFirebaseAdapter1 = mFirebaseAdapter;
    }

    public FirebaseRecyclerAdapter<taskdata, ShowDataViewHolder> getmFirebaseAdapter2() {
        return mFirebaseAdapter2;
    }

    public void setmFirebaseAdapter2(FirebaseRecyclerAdapter<taskdata, ShowDataViewHolder> mFirebaseAdapter) {
        this.mFirebaseAdapter2 = mFirebaseAdapter;
    }

    public FirebaseRecyclerAdapter<taskdata, ShowDataViewHolder2> getmFirebaseAdapter3() {
        return mFirebaseAdapter3;
    }

    public void setmFirebaseAdapter3(FirebaseRecyclerAdapter<taskdata, ShowDataViewHolder2> mFirebaseAdapter) {
        this.mFirebaseAdapter3 = mFirebaseAdapter;
    }



    //View Holder For Recycler View
    public static class ShowDataViewHolder2 extends RecyclerView.ViewHolder {
        private final TextView subjectname,percentage;
        private final Button yes,no;
        private final CardView subjectCv;


        public ShowDataViewHolder2(final View itemView)
        {
            super(itemView);
            subjectname = itemView.findViewById(R.id.subject);
            yes = itemView.findViewById(R.id.yesforam);
            no = itemView.findViewById(R.id.noforam);
            percentage = itemView.findViewById(R.id.percentage);
            subjectCv =  itemView.findViewById(R.id.subject_cv);
        }

    }



    //View Holder For Recycler View
    public static class ShowDataViewHolder extends RecyclerView.ViewHolder {
        private final TextView image_title;
        private TextView time_left,subject,fav;
        private ImageView delete;


        public ShowDataViewHolder(final View itemView)
        {
            super(itemView);
            time_left = itemView.findViewById(R.id.remaining_time);
            image_title = itemView.findViewById(R.id.taskname);
            delete = itemView.findViewById(R.id.remove);
            subject = itemView.findViewById(R.id.subject);
            fav = itemView.findViewById(R.id.favourite);

        }

    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseAuth mauth = FirebaseAuth.getInstance();
        String userod = mauth.getCurrentUser().getUid();

        myref1 = FirebaseDatabase.getInstance().getReference("Yourself").child(userod).child("Assignment");
        myref2 = FirebaseDatabase.getInstance().getReference("Yourself").child(userod).child("Quiz");
        myref3 = FirebaseDatabase.getInstance().getReference("Yourself").child(userod).child("Attendance");


        setmFirebaseAdapter1(new FirebaseRecyclerAdapter<taskdata, ShowDataViewHolder>(
                taskdata.class, R.layout.quiz_assignment, ShowDataViewHolder.class, myref1.orderByChild("subject")) {
            public void populateViewHolder(final ShowDataViewHolder viewHolder, final taskdata model, final int position) {

                viewHolder.image_title.setText(model.getTaskname());
                viewHolder.time_left.setText(model.getDate()+" "+model.getTime());
                viewHolder.subject.setText(model.getSubject());

                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("You Sure ?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int selectedItems = position;

                                        if(getmFirebaseAdapter1().getItemCount() >2) {
                                            getmFirebaseAdapter1().getRef(selectedItems).removeValue();
                                            getmFirebaseAdapter1().notifyItemRemoved(selectedItems);
                                            rv_assignment.invalidate();
                                            notifyItemRangeChanged(position, getItemCount());
                                        }
                                        else {
                                            getmFirebaseAdapter1().getRef(selectedItems).removeValue();
                                            getmFirebaseAdapter1().notifyItemRemoved(selectedItems);
                                            rv_assignment.invalidate();
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


                viewHolder.fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        userid = firebaseAuth.getCurrentUser().getUid();

                        firebase = new Firebase("https://yourself-bro.firebaseio.com/").child("Yourself").child(userid).child("fav").child("Assignment");


                        databaseReference.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Firebase task_ref = firebase.child("taskname");
                                final Firebase date_ref = firebase.child("date");
                                final Firebase sub_ref = firebase.child("subject");

                                task_ref.setValue(model.getTaskname());
                                date_ref.setValue(model.getDate()+" "+model.getTime());
                                sub_ref.setValue(model.getSubject());

                                }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });


            }

        });
        setmFirebaseAdapter2(new FirebaseRecyclerAdapter<taskdata, ShowDataViewHolder>(
                taskdata.class, R.layout.quiz_assignment, ShowDataViewHolder.class, myref2.orderByPriority()) {
            public void populateViewHolder(final ShowDataViewHolder viewHolder, final taskdata model, final int position) {

                viewHolder.image_title.setText(model.getTaskname());
                viewHolder.time_left.setText(model.getDate()+" "+model.getTime());
                viewHolder.subject.setText(model.getSubject());

                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("You Sure ?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int selectedItems = position;

                                        if(getmFirebaseAdapter2().getItemCount()> 2) {
                                            getmFirebaseAdapter2().getRef(selectedItems).removeValue();
                                            getmFirebaseAdapter2().notifyItemRemoved(selectedItems);
                                            rv_quiz.invalidate();
                                            notifyItemRangeChanged(position, getItemCount());
                                        }
                                        else {
                                            getmFirebaseAdapter2().getRef(selectedItems).removeValue();
                                            getmFirebaseAdapter2().notifyItemRemoved(selectedItems);
                                            rv_quiz.invalidate();
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


                viewHolder.fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        userid = firebaseAuth.getCurrentUser().getUid();

                        firebase = new Firebase("https://yourself-bro.firebaseio.com/").child("Yourself").child(userid).child("fav").child("Quiz");


                        databaseReference.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Firebase task_ref = firebase.child("taskname");
                                final Firebase date_ref = firebase.child("date");
                                final Firebase sub_ref = firebase.child("subject");

                                task_ref.setValue(model.getTaskname());
                                date_ref.setValue(model.getDate()+" "+model.getTime());
                                sub_ref.setValue(model.getSubject());

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });




            }

        });

        setmFirebaseAdapter3(new FirebaseRecyclerAdapter<taskdata, ShowDataViewHolder2>(
                taskdata.class, R.layout.subject_foram, ShowDataViewHolder2.class, myref3.orderByPriority()) {
            public void populateViewHolder(final ShowDataViewHolder2 viewHolder, final taskdata model, final int position) {
                viewHolder.subjectname.setText(model.getSubjectforam());


                databaseReference = FirebaseDatabase.getInstance().getReference();
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                userid = firebaseAuth.getCurrentUser().getUid();


                DatabaseReference myref2 = FirebaseDatabase.getInstance().getReference("Yourself").child(userid).child("Attendance").child(model.getSubjectforam());
                myref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        previous_yes = dataSnapshot.child("yes").getValue(String.class);
                        last_total = dataSnapshot.child("total").getValue(String.class);

                        if(last_total!=null || previous_yes!=null) {
                            ptotal = Float.parseFloat(last_total);
                            pyes = Float.parseFloat(previous_yes);
                        }
                        if(pyes==0 && ptotal==0){
                            viewHolder.percentage.setText("0%");
                        }else {

                            Float d =  ((pyes * 100) / ptotal);
                            if(d<75.0){
                                viewHolder.subjectCv.setCardBackgroundColor(Color.rgb(255,30,0));
                            }else {
                                viewHolder.subjectCv.setCardBackgroundColor(Color.WHITE);
                            }

                            viewHolder.percentage.setText(String.format("%.2f",d)+"%");
                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                viewHolder.yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        firebase = new Firebase("https://yourself-bro.firebaseio.com/").child("Yourself").child(userid).child("Attendance").child(model.getSubjectforam());
                        databaseReference.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Firebase yes_ref = firebase.child("yes");
                                final Firebase total = firebase.child("total");
                                yes_ref.setValue(String.valueOf(pyes+1));
                                total.setValue(String.valueOf(ptotal+1));
                                rv_attendance.invalidate();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

                viewHolder.no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        firebase = new Firebase("https://yourself-bro.firebaseio.com/").child("Yourself").child(userid).child("Attendance").child(model.getSubjectforam());
                        databaseReference.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Firebase total = firebase.child("total");
                                total.setValue(String.valueOf(ptotal+1));
                                rv_attendance.invalidate();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });


            }

        });


            rv_assignment.setAdapter(getmFirebaseAdapter1());
            rv_quiz.setAdapter(getmFirebaseAdapter2());
            rv_attendance.setAdapter(getmFirebaseAdapter3());



        myref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    Toast.makeText(getContext(),"No Assignment added yet!",Toast.LENGTH_SHORT).show();
                }else {
                    mFirebaseAdapter1.notifyItemChanged(0);
                    mFirebaseAdapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    Toast.makeText(getContext(),"No Quizzes added yet!",Toast.LENGTH_SHORT).show();
                }else {
                    mFirebaseAdapter2.notifyItemChanged(0);
                    mFirebaseAdapter2.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myref3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    Toast.makeText(getContext(),"No Subjects added yet!",Toast.LENGTH_SHORT).show();
                }else {
                    mFirebaseAdapter3.notifyItemChanged(0);
                    mFirebaseAdapter3.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mFirebaseAdapter1.notifyItemInserted(taskdata.size()-1);
        mFirebaseAdapter2.notifyItemInserted(taskdata.size()-1);
        mFirebaseAdapter3.notifyItemInserted(taskdata.size()-1);

    }

    public void set_alarm_time(String dd, String mm, String yyyy, String finaltime, String finaldate, final String hou, final String mi, String subjectt, String type) {

                Calendar calset = Calendar.getInstance();
                Calendar cal = (Calendar)calset.clone();
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hou));
                cal.set(Calendar.MINUTE, Integer.parseInt(mi));
                cal.set(Calendar.DATE, Integer.parseInt(dd));
                cal.set(Calendar.MONTH, Integer.parseInt(mm));
                cal.set(Calendar.YEAR, Integer.parseInt(yyyy));
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.MILLISECOND,0);

                if(cal.compareTo(calset) <=0){
                    cal.add(Calendar.DATE,1);
        }

                setAlarm(cal,subjectt,type,finaldate,finaltime);


    }

    public void setAlarm(Calendar targetCal, String subjectt, String type, String date, String time) {
     Toast.makeText(getContext(), "Alarm is set " + targetCal.getTime(), Toast.LENGTH_SHORT).show();

      AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
      Intent n_intent = new Intent(getContext(),AlarmReceiver.class);
      n_intent.putExtra(AlarmReceiver.NOTIFICATION_ID,1);
      n_intent.putExtra(AlarmReceiver.NOTIFICATION,getNotification(type + " on "+date,subjectt));
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