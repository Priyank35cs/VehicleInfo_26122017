package app.efleet.vehicleinfo.Activity.UnsettledTrip;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import app.efleet.vehicleinfo.GetVehicleNo;
import app.efleet.vehicleinfo.HttpCalls.ServiceHandler;
import app.efleet.vehicleinfo.R;
import app.efleet.vehicleinfo.adapters.UnsettledTripAdapter;

import static app.efleet.vehicleinfo.R.id.imageView;

public class UnsettledTripStatus extends AppCompatActivity {
    String days, Route, Party,Qyt,Cash,triplogno,txtVehicle;
    public static String SettlementID;
    String vehno, Type;
    Cursor cursor;
    ProgressDialog pDialog;
    String imeiNumber = "";
    String clientCode = "";
    String verificationCode = "";
    String appname = "VEHICLEINFO";
    String encodedUrl = null;
    JSONArray budgetDetail = null;
   GetTripPerformanceDetail getTripPerformanceDetail;
    ArrayList<HashMap<String, String>> budgetinfoList;
    public static final String TAG_Date = "Date";
    public static final String TAG_Route = "Route";
    public static final String TAG_Party = "Party";
    public static final String TAG_Qyt = "FuelQuantity";
    public static final String TAG_Cash = "Amount";
    public static final String TAG_TripLog_No = "TripLogNo";
    ListView lv;
    UnsettledTripAdapter adapter;
    TextView txtTripVehicleNo, tpLictView;
    LinearLayout lin;
    public static String LVTExp, VehNo, registration_no, stypeId, Date, sgtype, smonthId;



    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsettled_trip_status);
        lin = (LinearLayout) findViewById(R.id.linTripPerformanceActivity);
        txtTripVehicleNo = (TextView) findViewById(R.id.txtunVehicleNo);
       LinearLayout unsettledTrip=(LinearLayout) findViewById(R.id.unsettledTrip);
        lv = (ListView) findViewById(R.id.unTListView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Unsettled Trip Status");
        /*lv = getListView();*/
        VehNo = getIntent().getStringExtra("reg_no");
        txtTripVehicleNo.setText(VehNo);
        budgetinfoList = new ArrayList<HashMap<String, String>>();
        unsettledTrip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UnsettledTripStatus.this,
                        UnsettledTripBreakup.class);
                startActivity(intent);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(UnsettledTripStatus.this, UnsettledTripBreakup.class);
                intent.putExtra("stypeId",stypeId );
                intent.putExtra("SettlementID",SettlementID);
                intent.putExtra("Date",Date );
                intent.putExtra("sgtype",sgtype );
                intent.putExtra("smonthId",smonthId);
                intent.putExtra("reg_no", GetVehicleNo.reg_no);
                intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                startActivity(intent);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        vehno = extras.getString("VehNo");
        stypeId = extras.getString("stypeId");
        Date = extras.getString("Date");
        sgtype = extras.getString("sgtype");
        smonthId = extras.getString("smonthId");
        getTripPerformanceDetail = new GetTripPerformanceDetail();
        getTripPerformanceDetail.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getTripPerformanceDetail.getStatus() == AsyncTask.Status.RUNNING) {
                    getTripPerformanceDetail.cancel(true);
                    Toast.makeText(
                            UnsettledTripStatus.this,
                            "Response time out, please try again ....",
                            Toast.LENGTH_SHORT).show();
                }

            }

        }, 20000);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }



    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    private class GetTripPerformanceDetail extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UnsettledTripStatus.this);
            pDialog.setMessage("Loading Trip Performance Details ..... Please Wait");
            pDialog.setCancelable(false);
            pDialog.show();
        Toast.makeText(getBaseContext(),
					"Loading at Trip Performance Details ..... Please Wait", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // registration_no = getIntent().getStringExtra("reg_no");
            SharedPreferences sharedPref1 = getSharedPreferences("aji",
                    Context.MODE_PRIVATE);
            verificationCode = sharedPref1.getString("VARCODE", "");
            imeiNumber = sharedPref1.getString("IMEI", "");
            clientCode = sharedPref1.getString("CLIENTCODE", "");
            registration_no = getIntent().getStringExtra("reg_no");
            // LVTExp= "LVTExp";
            String URL = "http://proxy.efleetsystems.in/api/ProxyCalls/AuthrizeMe?action=VEHICLEINFO";
            URL = URL.replace(" ", "%20");
            Log.e("codeUrl", URL.toString());
            String encodedimei = Base64.encodeToString(imeiNumber.getBytes(), Base64.DEFAULT);
            String encodedclclode = Base64.encodeToString(clientCode.getBytes(), Base64.DEFAULT);
            String appName = "VEHICLEINFO";
            String authorizationString = "Device " + new String(Base64.encode((imeiNumber + ":" + verificationCode
                    + ":" + appName).getBytes(), Base64.NO_WRAP)); // Base64.NO_WRAP flag
            Log.e("finalbase64", authorizationString);
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.CaLLService(URL, authorizationString);
            Log.e("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String tUrl = jsonObj.getString("Url");
                    String tToken = jsonObj.getString("Token");
                    tUrl = tUrl.toString().concat(
                            "api/VDSInfo_Delay/GetUnsettledTripStatus/" + registration_no);
                    encodedUrl = tUrl.replaceAll(" ", "%20");
                    Log.e("codeUrl1", encodedUrl.toString());

                    String authorizationString1 = "Device " + new String(Base64.encode((imeiNumber + ":" + clientCode
                            + ":" + tToken).getBytes(), Base64.NO_WRAP));
                    Log.e("finalbase64", authorizationString1);
                    ServiceHandler sh1 = new ServiceHandler();
                    String jsonStr1 = sh1.CaLLService(encodedUrl, authorizationString1);
                    Log.e("Response1: ", "> " + jsonStr1);
                    if (jsonStr1 != null) {
                        try {
                            DecimalFormat formatter = new DecimalFormat("#,##,### ");
                            budgetDetail = new JSONArray(jsonStr1);
                            for (int i = 0; i < budgetDetail.length(); i++) {
                                JSONObject c = budgetDetail.getJSONObject(i);
                                days = c.getString(TAG_Date);
                                // Collections.sort(days);
                                Route = c.getString(TAG_Route);

                                Party = c.getString(TAG_Party);
                                Qyt = formatter.format(c.getInt(TAG_Qyt));
                                Cash = formatter.format(c.getInt(TAG_Cash) / 1000);
                                triplogno=c.getString(TAG_TripLog_No);


                                HashMap<String, String> Budget_details = new HashMap<String, String>();

                                Budget_details.put(TAG_Date, days);
                                // Collections.sort(Budget_details, Collections.reverseOrder());
                                Budget_details.put(TAG_Route, Route);
                                Budget_details.put(TAG_Party, Party);
                                Budget_details.put(TAG_Qyt, Qyt);
                                Budget_details.put(TAG_Cash, Cash);
                                Budget_details.put(TAG_TripLog_No, triplogno);


                                budgetinfoList.add(Budget_details);
                                // Collections.sort(budgetinfoList, Collections.reverseOrder());
                                Log.e("data", budgetinfoList.toString());
                            }
                            Collections.sort(budgetinfoList, Collections.reverseOrder());
                            pDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();

            if (budgetinfoList.isEmpty()) {
                   /* imageView.setVisibility(View.VISIBLE);
                    txtNoResults.setVisibility(View.VISIBLE);*/
            } else {
                adapter = new UnsettledTripAdapter(UnsettledTripStatus.this, budgetinfoList);
                lv.setAdapter(adapter);
            }


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}


