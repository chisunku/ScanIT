package com.example.myapplication;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

//import org.eazegraph.lib.charts.PieChart;
//import org.eazegraph.lib.models.PieModel;

import java.util.HashMap;

//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;

public class Analytics extends Fragment {
//    PieChart graphView = null;
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.analytics_fragment, parent, false);
//        graphView = view.findViewById(R.id.piechart);
        Log.d("TAG", "onCreateView: in analytics class");
        dynamoDB ddb = new dynamoDB(getContext());
        ddb.execute("walmart");
//        HashMap<String, Integer> map = ddb.finalAll();
//        Log.d("analytics class", "onCreateView: "+map);
//        for(String s : map.keySet()){
//            graphView.addPieSlice(
//                    new PieModel(
//                            s,
//                            map.get(s),
//                            Color.parseColor("#FFA726")));
//        }

        return view;
    }
}
