package com.example.woekun.toeiconline.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.models.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class AnalyzeFragment extends Fragment implements OnChartGestureListener {
    private BarChart mChart;
    private ArrayList<Integer> scores;
    private User currentUser;

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
        currentUser = appController.getCurrentUser();
        scores = appController.getDatabaseHelper().getScore(currentUser.getEmail());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analyze, container, false);
        mChart = (BarChart) view.findViewById(R.id.chart1);
        mChart.setDescription("");
        mChart.setOnChartGestureListener(this);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("1");
        labels.add("2");
        labels.add("3");
        labels.add("4");
        labels.add("5");
        labels.add("6");
        labels.add("7");

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(190f, 0));
        entries.add(new BarEntry(70f, 1));
        entries.add(new BarEntry(30f, 2));
        entries.add(new BarEntry(200f, 3));
        entries.add(new BarEntry(180f, 4));
        entries.add(new BarEntry(100f, 5));
        entries.add(new BarEntry(300f, 6));

        ArrayList<BarEntry> entries2 = new ArrayList<>();
        entries2.add(new BarEntry(100f, 0));
        entries2.add(new BarEntry(40f, 1));
        entries2.add(new BarEntry(60f, 2));
        entries2.add(new BarEntry(270f, 3));
        entries2.add(new BarEntry(180f, 4));
        entries2.add(new BarEntry(130f, 5));
        entries2.add(new BarEntry(200f, 6));

        ArrayList<BarEntry> entries3 = new ArrayList<>();
        entries3.add(new BarEntry(140f, 0));
        entries3.add(new BarEntry(190f, 1));
        entries3.add(new BarEntry(60f, 2));
        entries3.add(new BarEntry(100f, 3));
        entries3.add(new BarEntry(80f, 4));
        entries3.add(new BarEntry(170f, 5));
        entries3.add(new BarEntry(400f, 6));

        BarDataSet dataset1 = new BarDataSet(entries, "# of Score 1");
        dataset1.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet dataset2 = new BarDataSet(entries2, "# of Score 2");
        dataset2.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet dataset3 = new BarDataSet(entries3, "# of Score 3");
        dataset3.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataset1);
        dataSets.add(dataset2);
        dataSets.add(dataset3);

        BarData data = new BarData(labels, dataSets);
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
