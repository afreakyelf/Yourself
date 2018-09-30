package com.example.rajat.yourself;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.rajat.yourself.wealth.orderby;
import static com.example.rajat.yourself.wealth.retrieceds;

public class graph extends Fragment {


    LineChart lineChart;
    List<String> howmuchs,ondate;
    String target;

    public graph() {
    }

    public static graph newInstance() {
        graph fragment = new graph();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph,container,false);

        lineChart = view.findViewById(R.id.linechart);
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);

        Legend l = lineChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);


        assert getArguments() != null;
        String finalobject = getArguments().getString("finalobject");

        target = getArguments().getString("target");


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userid = firebaseAuth.getCurrentUser().getUid();

       DatabaseReference myref2 = FirebaseDatabase.getInstance().getReference("Yourself").child(userid).child("Wealth").child(finalobject).child("userinput");


       myref2.orderByChild(orderby).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){

             howmuchs  = new ArrayList<>();
             ondate= new ArrayList<>();
             for(DataSnapshot dsp : dataSnapshot.getChildren()){
             howmuchs.add(String.valueOf(dsp.child("howmuch").getValue()));
             ondate.add(String.valueOf(dsp.child("day").getValue()));
             }

            if(howmuchs.size()>1){
                setData(howmuchs,ondate);

            }else {
                Toast.makeText(getContext(), "Graph will show upon atleast 2 Values ", Toast.LENGTH_SHORT).show();
            }

            }
                else{
                    lineChart.invalidate();
                    Toast.makeText(getContext(), "Add atleast 2 items for Graph", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return  view;

    }


    //FOR GRAPH
    private void setData(List<String> howmuchs, List<String> ondate) {


        Float ul,max,fuck;
        if(target!=null){
            ul = Float.parseFloat(target);
        }else {
            ul = 5000f;
        }
        LimitLine upperlimit = new LimitLine(ul,"Target");
        upperlimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upperlimit.setLineWidth(2f);
        upperlimit.setTextSize(12f);
        upperlimit.setTextColor(Color.RED);

        upperlimit.enableDashedLine(10f,5f,0f);


        max = Float.valueOf(howmuchs.get(0));
        for(int i = 1 ; i<howmuchs.size();i++){
            if(Float.parseFloat(howmuchs.get(i))>max){
                max = Float.parseFloat(howmuchs.get(i));
            }
        }
        if(max>ul){
            fuck = max;
        }else{
            fuck = ul;
        }


        Log.d("largest", String.valueOf(max));


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(upperlimit);
        leftAxis.setAxisMaximum((float) (fuck+(fuck/10.0)));
        leftAxis.setDrawLimitLinesBehindData(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateXY(2000,2000);






        ArrayList<Entry> yvalues = new ArrayList<>();
        for(int i = 0; i<howmuchs.size(); i++){
            yvalues.add(new Entry(i,Float.valueOf(howmuchs.get(i))));
        }

        LineDataSet set = new LineDataSet(yvalues,"Expenses");
        set.setDrawFilled(true);
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setFillAlpha(110);
        set.setLineWidth(4f);
        set.notifyDataSetChanged();

        ArrayList<ILineDataSet> set1 = new ArrayList<>();
        set1.add(set);

        LineData data = new LineData(set1);
        lineChart.setData(data);
        data.setDrawValues(true);


        ArrayList<String> values = new ArrayList<>();
        for(int i=0;i<howmuchs.size();i++){
            values.add(ondate.get(i));
        }


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new MyXaxis(values));
        xAxis.setLabelCount(howmuchs.size(),true);
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


        lineChart.notifyDataSetChanged();
        lineChart.invalidate();

    }

    public class MyXaxis implements IAxisValueFormatter {
        private ArrayList<String> mvalues;

        public MyXaxis(ArrayList<String> values){
            this.mvalues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mvalues.get((int) value);
        }
    }


}
