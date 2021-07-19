package com.sanj.nyaladairy.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sanj.nyaladairy.R;
import com.sanj.nyaladairy.activities.AddNewFarmer;
import com.sanj.nyaladairy.adapters.FarmerRecyclerViewAdapter;
import com.sanj.nyaladairy.data.URLs;
import com.sanj.nyaladairy.models.FarmerModel;
import com.sanj.nyaladairy.wrapper.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.sanj.nyaladairy.wrapper.Helper.agentNationalIdentificationNumber;


public class Farmers extends Fragment {
    private Context mContext;
    private FragmentActivity mActivity;
    private RecyclerView recyclerView;
    private Spinner searchSpinner;
    private EditText edSearchValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = requireContext();
        mActivity = requireActivity();
        View root = inflater.inflate(R.layout.fragment_farmers, container, false);
        FloatingActionButton btnAdd = root.findViewById(R.id.btnAdd);
        recyclerView = root.findViewById(R.id.recyclerView);
        btnAdd.setOnClickListener(v -> startActivity(new Intent(mContext, AddNewFarmer.class)));
        searchSpinner = root.findViewById(R.id.searchSpinner);
        edSearchValue = root.findViewById(R.id.edSearchValue);
        ImageView btnSearch = root.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> searchFarmer());
        return root;
    }

    private void searchFarmer() {
        String[] searchFields = getResources().getStringArray(R.array.search_farmer_by);
        String searchColumn = getSearchColumn(searchFields[(int) searchSpinner.getSelectedItemId()]);
        String searchValue = edSearchValue.getText().toString();
        AlertDialog waitingDialog = new Helper().waitingDialog("Searching farmer(s)", mContext);
        Runnable searchCollectionThread = () -> {
            mActivity.runOnUiThread(waitingDialog::show);
            HashMap<String, String> params = new HashMap<>();
            params.put("search_col", searchColumn);
            params.put("value", searchValue);
            StringRequest request = new StringRequest(Request.Method.POST, URLs.searchFarmersUrl, response -> {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    mActivity.runOnUiThread(waitingDialog::dismiss);
                    String responseCode = responseObject.getString("responseCode");
                    if (responseCode.equals("1")) {
                        List<FarmerModel> farmerModelList = new ArrayList<>();
                        JSONArray responseArray = responseObject.getJSONArray("responseData");
                        for (int i = 0; i < responseArray.length(); i++) {
                            JSONObject responseArrayObject = responseArray.getJSONObject(i);
                            String name, nid, phone, randomPic;
                            name = responseArrayObject.getString("name");
                            nid = responseArrayObject.getString("nid");
                            phone = responseArrayObject.getString("phone");
                            randomPic = String.valueOf(new Random().nextInt(4));

                            farmerModelList.add(new FarmerModel(name, nid, phone, randomPic));
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        recyclerView.setAdapter(new FarmerRecyclerViewAdapter(farmerModelList, mContext));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mActivity.runOnUiThread(waitingDialog::dismiss);
                    mActivity.runOnUiThread(() -> new Helper().messageDialog("Sorry an internal error occurred please try again later\n" + e.getMessage() + response, mContext));
                }

            }, error -> mActivity.runOnUiThread(() -> {
                waitingDialog.dismiss();
                new Helper().messageDialog("Sorry failed to connect to server please check your internet connection and try again later\n" + error.getMessage(), mContext);
            })) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };
            Volley.newRequestQueue(mContext).add(request);
        };
        if (!TextUtils.isEmpty(searchValue) && !searchColumn.equals("Search by")) {
            new Thread(searchCollectionThread).start();
        } else {
            new Helper().errorToast("Incomplete search fields", mContext);
        }

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
            params.put("agent_nid", agentNationalIdentificationNumber);

            StringRequest request = new StringRequest(Request.Method.POST, URLs.fetchAllFarmersUrl, response -> {
                try {
                    JSONObject responseObject = new JSONObject(response);

                    String responseCode = responseObject.getString("responseCode");
                    if (responseCode.equals("1")) {
                        List<FarmerModel> farmerModelList = new ArrayList<>();
                        JSONArray responseArray = responseObject.getJSONArray("responseData");
                        for (int i = 0; i < responseArray.length(); i++) {
                            JSONObject responseArrayObject = responseArray.getJSONObject(i);
                            String name, nid, phone, randomPic;
                            name = responseArrayObject.getString("name");
                            nid = responseArrayObject.getString("nid");
                            phone = responseArrayObject.getString("phone");
                            randomPic = String.valueOf(new Random().nextInt(4));

                            farmerModelList.add(new FarmerModel(name, nid, phone, randomPic));
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        recyclerView.setAdapter(new FarmerRecyclerViewAdapter(farmerModelList, mContext));
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

    private String getSearchColumn(String searchField) {
        switch (searchField) {
            case "Name":
                return "name";
            case "Phone":
                return "phone";
            case "Farmer National ID":
                return "nid";
            default:
                return "";
        }
    }
}