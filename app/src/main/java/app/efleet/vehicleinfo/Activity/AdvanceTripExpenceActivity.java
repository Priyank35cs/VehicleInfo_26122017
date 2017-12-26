package app.efleet.vehicleinfo.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import app.efleet.vehicleinfo.HttpCalls.ServiceHandler;
import app.efleet.vehicleinfo.R;
import app.efleet.vehicleinfo.adapters.AdvTripExpAdapter;

public class AdvanceTripExpenceActivity extends AppCompatActivity {
    ListView lv;
    TextView txtViewVehicleNo;
    ArrayList<HashMap<String, String>> budgetinfoList;
    String  ATEType;
    String imeiNumber = "", vehno, VehNo;
    String clientCode = "";
    String verificationCode = "";
    GetTripStatus getTripStatus;
    JSONArray tripDetail = null;
    ProgressDialog pDialog;
    String encodedUrl = null,id,Type;
    String Nature, Budget, Actual, Variance, DType, VhicleNo, registration_no,LType,LItemType,stypeId,Date,sgtype,smonthId;
    AdvTripExpAdapter adapter;
    public static final String TAG_Nature = "Nature";
    public static final String TAG_Budget = "Budget";
    public static final String TAG_Actual = "Actual";
    public static final String TAG_Variance = "Varience";
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_trip_expence);
        txtViewVehicleNo = (TextView) findViewById(R.id.txtViewVehicleNo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv=(ListView)findViewById(R.id.adListView);
        budgetinfoList=new ArrayList<HashMap<String, String>>();
        VehNo = getIntent().getStringExtra("reg_no");
        txtViewVehicleNo.setText(VehNo);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        stypeId=extras.getString("stypeId");
        Date=extras.getString("Date");
        sgtype=extras.getString("sgtype");
        smonthId=extras.getString("smonthId");
       // id=extras.getString("SettlementID");
        vehno = extras.getString("VehNo");
        LType = extras.getString("VehNo");
        LItemType= extras.getString("VehNo");
        ATEType = extras.getString("DType");
        if (ATEType.equals("F")) {
            setTitle("Fuel Expense");
            id=extras.getString("SettlementID");
            Type="Fuel";
        } else if (ATEType.equals("T")) {
            setTitle("Toll Expense");
            id=extras.getString("SettlementID");
            Type="Toll";
        } else if (ATEType.equals("D")) {
            setTitle("Driver Expense");
            id=extras.getString("SettlementID");
            Type="Driver";
        } else {
            setTitle("Other Expense");
            id=extras.getString("SettlementID");
            Type="Other";
        }
        getTripStatus = new GetTripStatus();
        new GetTripStatus().execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getTripStatus.getStatus() == AsyncTask.Status.RUNNING) {
                    getTripStatus.cancel(true);
                    Toast.makeText(
                            AdvanceTripExpenceActivity.this,
                            "Response time out, please try again ....",
                            Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();


                }

            }

        }, 20000);


    }

    private class GetTripStatus extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AdvanceTripExpenceActivity.this);
            pDialog.setMessage("Loading Trip Details ..... Please Wait");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {


            SharedPreferences sharedPref1 = getSharedPreferences("aji",
                    Context.MODE_PRIVATE);
            verificationCode = sharedPref1.getString("VARCODE", "");
            imeiNumber = sharedPref1.getString("IMEI", "");
            clientCode = sharedPref1.getString("CLIENTCODE", "");
            registration_no = getIntent().getStringExtra("reg_no");
            String URL = "http://proxy.efleetsystems.in/api/ProxyCalls/AuthrizeMe?action=VEHICLEINFO";
            URL = URL.replace(" ", "");
            Log.e("codeUrl", URL.toString());

            String appName = "VEHICLEINFO";
            String authorizationString = "Device " + new String(Base64.encode((imeiNumber + ":" + verificationCode
                    + ":" + appName).getBytes(), Base64.NO_WRAP));

            Log.e("finalbase64", authorizationString);
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.CaLLService(URL, authorizationString);


            Log.e("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    String tUrl = jsonObj.getString("Url");
                    String tToken = jsonObj.getString("Token");
                  /* registration_no = getIntent().getStringExtra("reg_no");*/
                    tUrl = tUrl.toString().concat(
                            "api/VehAnalysis/GetVehBreakupFuel/" + registration_no+"/"+stypeId+"/"+sgtype+"-"+smonthId+"-"+Date+"/"+id+"/"+Type);

                    encodedUrl = tUrl.replaceAll(" ", "%20");

                    Log.e("codeUrl1", encodedUrl.toString());

                    String authorizationString1 = "Device " + new String(Base64.encode((imeiNumber + ":" + clientCode
                            + ":" + tToken).getBytes(), Base64.NO_WRAP)); // Base64.NO_WRAP flag

                    Log.e("finalbase64", authorizationString1);
                    ServiceHandler sh1 = new ServiceHandler();
                    String jsonStr1 = sh1.CaLLService(encodedUrl, authorizationString1);

                    Log.e("Response1: ", "> " + jsonStr1);
                    if (jsonStr1 != null) {
                        try {

                            tripDetail = new JSONArray(jsonStr1);
                            if (tripDetail.length() == 0) {
                                AdvanceTripExpenceActivity.this.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "Could not get any data from, please check internet "
                                                + "connection or contact administrator", Toast.LENGTH_LONG).show();

                                    }
                                });
                            } else {
                                DecimalFormat formatter = new DecimalFormat("#,##,### ");
                                for (int i = 0; i < tripDetail.length(); i++) {
                                    JSONObject c = tripDetail.getJSONObject(i);
                                    Nature = c.getString(TAG_Nature);
                                    Budget = formatter.format(c.getInt (TAG_Budget));
                                    Actual =formatter.format(c.getInt (TAG_Actual));
                                    Variance = formatter.format(c.getInt (TAG_Variance));

                                    HashMap<String, String> Budget_details = new HashMap<String, String>();

                                    Budget_details.put(TAG_Nature, Nature);
                                    Budget_details.put(TAG_Budget, Budget);
                                    Budget_details.put(TAG_Actual, Actual);
                                    Budget_details.put(TAG_Variance, Variance);
                                    budgetinfoList.add(Budget_details);
                                    Log.e("data", budgetinfoList.toString());

                                }

                            }
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

                Toast.makeText(getApplicationContext(),"No record",Toast.LENGTH_LONG).show();
            } else {
                adapter = new AdvTripExpAdapter(AdvanceTripExpenceActivity.this, budgetinfoList);
                lv.setAdapter(adapter);
            }

        }
    }
}