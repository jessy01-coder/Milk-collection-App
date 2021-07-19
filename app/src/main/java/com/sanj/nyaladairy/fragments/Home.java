package com.sanj.nyaladairy.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sanj.nyaladairy.R;
import com.sanj.nyaladairy.activities.AddNewCollection;
import com.sanj.nyaladairy.adapters.DailyCollectionRecyclerViewAdapter;
import com.sanj.nyaladairy.data.URLs;
import com.sanj.nyaladairy.models.DailyCollectionModel;
import com.sanj.nyaladairy.wrapper.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sanj.nyaladairy.wrapper.Helper.agentNationalIdentificationNumber;


public class Home extends Fragment {
    private TextView milkCapacity, numberOfTodayFarmers, todayDate;
    private RecyclerView recyclerView;
    private String todayDay, todayMonth, todayYear;
    private Context mContext;
    private FragmentActivity mActivity;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        milkCapacity = root.findViewById(R.id.milkCapacity);
        numberOfTodayFarmers = root.findViewById(R.id.numberOfTodayFarmers);
        todayDate = root.findViewById(R.id.todayDate);
        recyclerView = root.findViewById(R.id.recyclerView);
        FloatingActionButton btnAdd = root.findViewById(R.id.btnAdd);

        mContext = requireContext();
        mActivity = requireActivity();
        String[] months_of_year = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dece"};
        Calendar calendar = Calendar.getInstance();
        todayYear = String.valueOf(calendar.get(Calendar.YEAR));
        todayMonth = months_of_year[calendar.get(Calendar.MONTH)];
        todayDay = String.valueOf(calendar.get(Calendar.DATE));

        todayDate.setText(todayMonth + " " + todayDay + " " + todayYear);

        //btnAdd.setOnClickListener(v -> startActivity(new Intent(mContext, AddNewCollection.class)));
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {
        Runnable loadDataThread = () -> {
            HashMap<String, String> params = new HashMap<>();
            params.put("day", todayDay);
            params.put("month", todayMonth);
            params.put("year", todayYear);
            params.put("nid", agentNationalIdentificationNumber);
            StringRequest request = new StringRequest(Request.Method.POST, URLs.getDailyCollectionUrl, response -> {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    String responseCode = responseObject.getString("responseCode");
                    if (responseCode.equals("1")) {
                        String responseCount = responseObject.getString("responseCount");
                        String responseTotal = responseObject.getString("responseTotal");
                        mActivity.runOnUiThread(() -> {
                            milkCapacity.setText(responseTotal);
                            numberOfTodayFarmers.setText("From " + responseCount + " collection(s) made");
                        });

                        List<DailyCollectionModel> dailyCollectionModelList = new ArrayList<>();
                        JSONArray responseArray = responseObject.getJSONArray("responseData");
                        for (int i = 0; i < responseArray.length(); i++) {
                            JSONObject responseArrayObject = responseArray.getJSONObject(i);
                            String name, capacity, collectionId, dateTime, farmerNID;
                            name = responseArrayObject.getString("name");
                            capacity = responseArrayObject.getString("capacity");
                            collectionId = responseArrayObject.getString("id");
                            dateTime = responseArrayObject.getString("time");
                            farmerNID = responseArrayObject.getString("farmer_nid");

                            dailyCollectionModelList.add(new DailyCollectionModel(name, capacity, collectionId, dateTime, farmerNID));
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        recyclerView.setAdapter(new DailyCollectionRecyclerViewAdapter(dailyCollectionModelList, mContext));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mActivity.runOnUiThread(() -> new Helper().messageDialog("Sorry an internal error occurred please try again later\n" + e.getMessage() + response, mContext));
                }

            }, error -> mActivity.runOnUiThread(() -> new Helper().messageDialog("Sorry failed to connect to server please check your internet connection and try again later\n" + error.getMessage(), mContext))) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };
            Volley.newRequestQueue(mContext).add(request);
        };
        new Thread(loadDataThread).start();
    }
}