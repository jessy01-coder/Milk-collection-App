package com.sanj.nyaladairy.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sanj.nyaladairy.R;
import com.sanj.nyaladairy.adapters.DailyCollectionRecyclerViewAdapter;
import com.sanj.nyaladairy.data.URLs;
import com.sanj.nyaladairy.models.DailyCollectionModel;
import com.sanj.nyaladairy.wrapper.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sanj.nyaladairy.wrapper.Helper.agentNationalIdentificationNumber;

public class MyCollections extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Context mContext;
    private Spinner searchSpinner;
    private EditText edSearchValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collections);
        mContext = this;
        recyclerView = findViewById(R.id.recyclerView);
        searchSpinner = findViewById(R.id.searchSpinner);
        edSearchValue = findViewById(R.id.edSearchValue);
        ImageView btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> searchCollection());
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void searchCollection() {
        String[] searchFields = getResources().getStringArray(R.array.search_collection_by);
        String searchColumn = getSearchColumn(searchFields[(int) searchSpinner.getSelectedItemId()]);
        String searchValue = edSearchValue.getText().toString();
        AlertDialog waitingDialog = new Helper().waitingDialog("Searching collection(s)", mContext);
        Runnable searchCollectionThread = () -> {
            runOnUiThread(waitingDialog::show);
            HashMap<String, String> params = new HashMap<>();
            params.put("search_col", searchColumn);
            params.put("value", searchValue);
            StringRequest request = new StringRequest(Request.Method.POST, URLs.searchCollectionUrl, response -> {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    runOnUiThread(waitingDialog::dismiss);
                    String responseCode = responseObject.getString("responseCode");
                    if (responseCode.equals("1")) {
                        List<DailyCollectionModel> dailyCollectionModelList = new ArrayList<>();
                        JSONArray responseArray = responseObject.getJSONArray("responseData");
                        for (int i = 0; i < responseArray.length(); i++) {
                            JSONObject responseArrayObject = responseArray.getJSONObject(i);
                            String name, capacity, collectionId, dateTime, farmerNID;
                            name = responseArrayObject.getString("name");
                            capacity = responseArrayObject.getString("capacity");
                            collectionId = responseArrayObject.getString("id");
                            dateTime = "On " + responseArrayObject.getString("day") + " " + responseArrayObject.getString("month")
                                    + " " + responseArrayObject.getString("year") + " at " + responseArrayObject.getString("time");
                            farmerNID = responseArrayObject.getString("farmer_nid");

                            dailyCollectionModelList.add(new DailyCollectionModel(name, capacity, collectionId, dateTime, farmerNID));
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        recyclerView.setAdapter(new DailyCollectionRecyclerViewAdapter(dailyCollectionModelList, mContext));
                    } else {
                        String responseMessage = responseObject.getString("responseMessage");
                        runOnUiThread(() -> new Helper().messageDialog(responseMessage, mContext));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(waitingDialog::dismiss);
                    runOnUiThread(() -> new Helper().messageDialog("Sorry an internal error occurred please try again later\n" + e.getMessage() + response, mContext));
                }

            }, error -> runOnUiThread(() -> {
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
        AlertDialog waitingDialog = new Helper().waitingDialog("Retrieving Your Collections", mContext);
        Runnable loadDataThread = () -> {
            runOnUiThread(waitingDialog::show);
            HashMap<String, String> params = new HashMap<>();
            params.put("nid", agentNationalIdentificationNumber);
            StringRequest request = new StringRequest(Request.Method.POST, URLs.getMyCollectionUrl, response -> {
                try {
                    JSONObject responseObject = new JSONObject(response);
                    runOnUiThread(waitingDialog::dismiss);
                    String responseCode = responseObject.getString("responseCode");
                    if (responseCode.equals("1")) {
                        List<DailyCollectionModel> dailyCollectionModelList = new ArrayList<>();
                        JSONArray responseArray = responseObject.getJSONArray("responseData");
                        for (int i = 0; i < responseArray.length(); i++) {
                            JSONObject responseArrayObject = responseArray.getJSONObject(i);
                            String name, capacity, collectionId, dateTime, farmerNID;
                            name = responseArrayObject.getString("name");
                            capacity = responseArrayObject.getString("capacity");
                            collectionId = responseArrayObject.getString("id");
                            dateTime = "On " + responseArrayObject.getString("day") + " " + responseArrayObject.getString("month")
                                    + " " + responseArrayObject.getString("year") + " at " + responseArrayObject.getString("time");
                            farmerNID = responseArrayObject.getString("farmer_nid");

                            dailyCollectionModelList.add(new DailyCollectionModel(name, capacity, collectionId, dateTime, farmerNID));
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                        recyclerView.setAdapter(new DailyCollectionRecyclerViewAdapter(dailyCollectionModelList, mContext));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(waitingDialog::dismiss);
                    runOnUiThread(() -> new Helper().errorToast("Sorry an internal error occurred please try again later\n" + e.getMessage() + response, mContext));
                finish();
                }

            }, error -> runOnUiThread(() -> {
                waitingDialog.dismiss();
                finish();
                new Helper().errorToast("Sorry failed to connect to server please check your internet connection and try again later\n" + error.getMessage(), mContext);
            })) {
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
            case "Can Number":
                return "can_number";
            case "Delivery ID":
                return "id";
            case "Farmer National ID":
                return "farmer_nid";
            default:
                return "";
        }
    }
}