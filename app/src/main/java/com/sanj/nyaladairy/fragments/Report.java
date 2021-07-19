package com.sanj.nyaladairy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputEditText;
import com.sanj.nyaladairy.R;
import com.sanj.nyaladairy.data.URLs;
import com.sanj.nyaladairy.models.BarchartModel;
import com.sanj.nyaladairy.wrapper.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Report extends Fragment {

    private com.github.mikephil.charting.charts.BarChart barChart;
    private ArrayList<BarEntry> barEntryArrayList;
    private ArrayList<String> label_names;
    private ArrayList<BarchartModel> barChartModelArrayList;
    private String myCurrentYear;
    private Context mContext;
    private FragmentActivity mActivity;
    private TextInputEditText edYear;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = requireContext();
        mActivity = requireActivity();
        View root = inflater.inflate(R.layout.fragment_report, container, false);
        barChart = root.findViewById(R.id.barchart);
        edYear = root.findViewById(R.id.edYear);
        Button btnGenerate = root.findViewById(R.id.btnGenerate);
        Calendar calendar = Calendar.getInstance();
        myCurrentYear = String.valueOf(calendar.get(Calendar.YEAR));
        edYear.setText(myCurrentYear);
        btnGenerate.setOnClickListener(v -> {
            if (!(TextUtils.isEmpty(Objects.requireNonNull(edYear.getText()).toString().trim()))) {
                myCurrentYear = edYear.getText().toString().trim();
                fetchGraphData();
            } else {
                new Helper().errorToast("Empty fields of year", mContext);
            }
        });
        barEntryArrayList = new ArrayList<>();
        label_names = new ArrayList<>();
        barChartModelArrayList = new ArrayList<>();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchGraphData();
    }

    private void fetchGraphData() {
        android.app.AlertDialog waitingDialog = new Helper().waitingDialog("Fetching graph data", mContext);
        Runnable fetchThread = () -> {
            mActivity.runOnUiThread(waitingDialog::show);
            Map<String, String> params = new HashMap<>();
            params.put("year", myCurrentYear);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.fetchGraphDataUrl, response -> {
                try {
                    mActivity.runOnUiThread(waitingDialog::dismiss);
                    JSONObject responseObject = new JSONObject(response);
                    String code = responseObject.getString("responseCode");

                    if (code.equals("1")) {
                        JSONArray responseArray = responseObject.getJSONArray("responseData");
                        //when the required data is obtained from the database all the previously held data in the app is cleared
                        barChartModelArrayList.clear();
                        barEntryArrayList.clear();
                        label_names.clear();
                        /*The data received from the database*/
                        for (int i = 0; i < responseArray.length(); i++) {
                            JSONObject responseArrayObject = responseArray.getJSONObject(i);
                            barChartModelArrayList.add(new BarchartModel("Jan", Float.parseFloat(responseArrayObject.getString("Jan"))));
                            barChartModelArrayList.add(new BarchartModel("Feb", Float.parseFloat(responseArrayObject.getString("Feb"))));
                            barChartModelArrayList.add(new BarchartModel("Mar", Float.parseFloat(responseArrayObject.getString("Mar"))));
                            barChartModelArrayList.add(new BarchartModel("Apr", Float.parseFloat(responseArrayObject.getString("Apr"))));
                            barChartModelArrayList.add(new BarchartModel("May", Float.parseFloat(responseArrayObject.getString("May"))));
                            barChartModelArrayList.add(new BarchartModel("Jun", Float.parseFloat(responseArrayObject.getString("Jun"))));
                            barChartModelArrayList.add(new BarchartModel("Jul", Float.parseFloat(responseArrayObject.getString("Jul"))));
                            barChartModelArrayList.add(new BarchartModel("Aug", Float.parseFloat(responseArrayObject.getString("Aug"))));
                            barChartModelArrayList.add(new BarchartModel("Sep", Float.parseFloat(responseArrayObject.getString("Sep"))));
                            barChartModelArrayList.add(new BarchartModel("Oct", Float.parseFloat(responseArrayObject.getString("Oct"))));
                            barChartModelArrayList.add(new BarchartModel("Nov", Float.parseFloat(responseArrayObject.getString("Nov"))));
                            barChartModelArrayList.add(new BarchartModel("Dec", Float.parseFloat(responseArrayObject.getString("Dece"))));
                        }
                        plotGraph();
                    } else {
                        String responseMessage = responseObject.getString("responseMessage");
                        mActivity.runOnUiThread(() -> new Helper().messageDialog(responseMessage, mContext));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mActivity.runOnUiThread(waitingDialog::dismiss);
                    mActivity.runOnUiThread(() -> new Helper().messageDialog("Sorry an internal error occurred please try again later\n" + e.getMessage() + response, mContext));
                }

            }, error -> {
                mActivity.runOnUiThread(waitingDialog::dismiss);
                mActivity.runOnUiThread(() -> new Helper().messageDialog("Sorry failed to connect to server please check your internet connection and try again later\n" + error.getMessage(), mContext));
            }) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };
            Volley.newRequestQueue(mContext).add(stringRequest);
        };
        new Thread(fetchThread).start();
    }

    private void plotGraph() {
        //The data is later broken down to months and their corresponding values
        for (int i = 0; i < barChartModelArrayList.size(); i++) {
            String month = barChartModelArrayList.get(i).getMonth();
            float val = barChartModelArrayList.get(i).getCollections();
            barEntryArrayList.add(new BarEntry(i, val));
            label_names.add(month);
        }

        //Here the designing of the graph is done
        BarDataSet barDataSet = new BarDataSet(barEntryArrayList, "Monthly Collections(ltrs)");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description = new Description();
        description.setText("Months");
        barChart.setDescription(description);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(label_names));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(label_names.size());
        xAxis.setLabelRotationAngle(225);
        barChart.animateY(2000);
        barChart.invalidate();
    }
}