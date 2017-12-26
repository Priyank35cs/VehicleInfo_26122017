package app.efleet.vehicleinfo.Activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.efleet.vehicleinfo.HttpCalls.ServiceHandler;

import app.efleet.vehicleinfo.R;
import app.efleet.vehicleinfo.adapters.NewBatteryPerAdapter;

public class BatteryActivity extends ListActivity {
    String days, BatteryNo, BatteryBrand, BatteryManufacturer, BatteryCost, BatteryLife, BatteryScrapValue, BatteryCPKM;
    String vehno,VehNo,registration_no;
    Cursor cursor;
    ProgressDialog pDialog;
    String imeiNumber = "";
    String clientCode = "";
    String verificationCode = "";
    String appname = "VEHICLEINFO";
    String encodedUrl = null;
    JSONArray budgetDetail = null;
    GetBatteryStatus getBatteryStatus;
    String Type;
    String ItemType,stypeId,Date,sgtype,smonthId;
    ArrayList<HashMap<String, String>> budgetinfoList;
    public static final String TAG_Date = "dd/mm";
    public static final String TAG_Battery_No = "BatteryNo";
    public static final String TAG_Battery_Brand = "BatteryBrand";
    public static final String TAG_Battery_Mnufctrer = "BatteryManufacturer";
    public static final String TAG_Battery_Cost = "BatteryMonth";
    public static final String TAG_Battery_Life = "BatteryMilage";
    ListView lv;
    NewBatteryPerAdapter adapter;
    TextView txtTyreManufactr, txtTyreBrand, txtTyrePer, txtnewPurchase,txtTripNewVehicleNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        txtTripNewVehicleNo = (TextView) findViewById(R.id.txtTripNewVehicleNo);
        txtnewPurchase  = (TextView) findViewById(R.id.txtnewPurchase);
        budgetinfoList = new ArrayList<HashMap<String, String>>();
        lv = getListView();
        VehNo = getIntent().getStringExtra("reg_no");
        txtTripNewVehicleNo.setText(VehNo);
        getBatteryStatus = new GetBatteryStatus();
          getBatteryStatus.execute();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        vehno = extras.getString("VehNo");
        Type = extras.getString("DType");
        ItemType = extras.getString("ItemType");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getBatteryStatus.getStatus() == AsyncTask.Status.RUNNING) {
                    getBatteryStatus.cancel(true);
                    Toast.makeText(
                            BatteryActivity.this,
                            "Response timed out , please try again ....",
                            Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }

            }

        }, 10000);
        if (ItemType.equals("N")) {
            txtnewPurchase.setText("New ");}
           else{
                txtnewPurchase.setText("Refurnished ");}
           /* } else {
            }

        } else {
            setTitle(" Battery Details");
            //  txtnewPurchase.setText("Refurnished Details");
            if (ItemType.equals("R")) {
                txtnewPurchase.setText("Refurnished");
            } else {
            }

            txtTyrePer.setText("Battery No");
            txtTyreBrand.setText("Battery Brand");
            txtTyreManufactr.setText("Battery Manufacturer");
        }

*/
    }


    private class GetBatteryStatus extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BatteryActivity.this);
            pDialog.setMessage("Loading  Battery  Details ..... Please Wait");
            pDialog.setCancelable(false);
            pDialog.show();
		Toast.makeText(getBaseContext(), "Loading   Battery Details ..... Please Wait", Toast.LENGTH_SHORT).show();
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
            String URL = "http://proxy.efleetsystems.in/api/ProxyCalls/AuthrizeMe?action=VEHICLEINFO";
            URL=URL.replace(" ","%20");
            Log.e("codeUrl",URL.toString());
            String encodedimei = Base64.encodeToString(imeiNumber.getBytes(), Base64.DEFAULT);
            String encodedclclode = Base64.encodeToString(clientCode.getBytes(), Base64.DEFAULT);
            String appName = "VEHICLEINFO";
            String authorizationString = "Device " + new String(Base64.encode((imeiNumber + ":" + verificationCode
                    + ":" + appName).getBytes(), Base64.NO_WRAP));
            Log.e("finalbase64",authorizationString);
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.CaLLService(URL, authorizationString);
            Log.e("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String tUrl = jsonObj.getString("Url");
                    String tToken = jsonObj.getString("Token");
                    registration_no = getIntent().getStringExtra("reg_no");
                    tUrl = tUrl.toString().concat(
                            "api/VehAnalysis/GetVehSummary/" + registration_no+"/"+stypeId+"/"+sgtype+"-"+smonthId+"-"+Date+"/"+Type/*+"/"+ItemType*/);
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
                            budgetDetail = new JSONArray(jsonStr1);
                            for (int i = 0; i < budgetDetail.length(); i++) {
                                JSONObject c = budgetDetail.getJSONObject(i);
                                days = c.getString(TAG_Date);
                                BatteryNo= String.valueOf(c.getInt(TAG_Battery_No));
                                BatteryBrand= String.valueOf (TAG_Battery_Brand);
                                BatteryManufacturer= String.valueOf (TAG_Battery_Mnufctrer);
                                BatteryCost= String.valueOf(c.getString(TAG_Battery_Cost));
                                BatteryLife= String.valueOf(c.getInt(TAG_Battery_Life));


                                HashMap<String, String> Budget_details = new HashMap<String, String>();

                                Budget_details.put(TAG_Date, days);
                                Budget_details.put(TAG_Battery_No, BatteryNo);
                                Budget_details.put(TAG_Battery_Brand, BatteryBrand);
                                Budget_details.put(TAG_Battery_Mnufctrer, BatteryManufacturer);
                                Budget_details.put(TAG_Battery_Cost, BatteryCost);
                                Budget_details.put(TAG_Battery_Life, BatteryLife);



                                budgetinfoList.add(Budget_details);
                                Log.e("data",budgetinfoList.toString());
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
                Toast.makeText(BatteryActivity.this, "Record is null...", Toast.LENGTH_SHORT).show();
            }else{
                adapter = new NewBatteryPerAdapter(BatteryActivity.this, budgetinfoList);
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
