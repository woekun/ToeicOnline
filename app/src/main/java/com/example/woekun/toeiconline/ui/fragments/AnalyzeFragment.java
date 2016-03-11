package com.example.woekun.toeiconline.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class AnalyzeFragment extends Fragment implements OnChartGestureListener {
    private LineChart mChart;
    private ArrayList<Integer> scores;
    private String email;

    private AppController appController;

    public AnalyzeFragment() {
        // Required empty public constructor
    }

    public static AnalyzeFragment newInstance() {
        return new AnalyzeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appController = AppController.getInstance();

        email = appController.getSharedPreferences().getString(Const.EMAIL,null);
        scores = appController.getDatabaseHelper().getScore(email);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analyze, container, false);
        mChart = (LineChart)view.findViewById(R.id.chart1);
        mChart.setDescription("");
        mChart.setOnChartGestureListener(this);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);


        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(200, 0));
        entries.add(new Entry(800, 1));
        entries.add(new Entry(300, 2));
        entries.add(new Entry(500, 3));
        entries.add(new Entry(900, 4));


        ArrayList<String> labels = new ArrayList<>();
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");

        LineDataSet dataset = new LineDataSet(entries, "# of Score");
        dataset.setDrawFilled(true);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        LineData data = new LineData(labels, dataset);
        mChart.setData(data);
        mChart.animateX(1000);
        mChart.animateY(1000);

        return view;
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START");
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END");
        mChart.highlightValues(null);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

}
