package app.efleet.vehicleinfo.Activity;

import android.app.ListActivity;
import android.os.Bundle;

import app.efleet.vehicleinfo.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import app.efleet.vehicleinfo.adapters.TyreScrapadapter;

public class TyreScrapActivity extends AppCompatActivity {
    String days, tyreNo, tyreBrand, tyreManufacturer, tyreMonth, tyreMilage, tyreScrapValue, tyreCPKM;
    String vehno,  registration_no;
    String stypeId,Date,sgtype,smonthId;
    String ItemType;
    Cursor cursor;
    ProgressDialog pDialog;
    String imeiNumber = "";
    String clientCode = "";
    String verificationCode = "";
    String appname = "VEHICLEINFO";
    String encodedUrl = null;
    JSONArray budgetDetail = null;
    GetTyreStatus getTyreStatus;
   public static String Type;
    ArrayList<HashMap<String, String>> budgetinfoList;
    public static final String TAG_Date = "Date";
    public static final String TAG_Tyre_No = "TyreNo";
    public static final String TAG_Tyre_Brand = "TyreBrand";
    public static final String TAG_Tyre_Mnufctrer = "TyreManufacturer";
    public static final String TAG_Tyre_Month = "Month";
    public static final String TAG_Tyre_Milage = "Milage";
    public static final String TAG_Tyre_Scrap_Value = "ScrapValue";
    public static final String TAG_Tyre_CPKM = "CostKM";


    ListView lv;
    TyreScrapadapter adapter;

    TextView txtTyreManufactr, txtTyreBrand, txtTyrePer, txtnewPurchase,txtTripNewVehicleNo;
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trye_scrap);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtTripNewVehicleNo = (TextView) findViewById(R.id.txtTripScrapVehicleNo);
        lv=(ListView)findViewById(R.id.list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        budgetinfoList = new ArrayList<HashMap<String, String>>();




        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        vehno = extras.getString("reg_no");
        txtTripNewVehicleNo.setText(vehno);
        stypeId=extras.getString("stypeId");
        Date=extras.getString("Date");
        sgtype=extras.getString("sgtype");
        smonthId=extras.getString("smonthId");
        Type = extras.getString("DType");
        ItemType = extras.getString("ItemType");

        getTyreStatus = new GetTyreStatus();
        getTyreStatus.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getTyreStatus.getStatus() == AsyncTask.Status.RUNNING) {
                    getTyreStatus.cancel(true);
                    Toast.makeText(
                            TyreScrapActivity.this,
                            "Response timed out , please try again ....",
                            Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }

            }

        }, 10000);
       /* if (Type.equals("T")) {
            setTitle(" Tyre Details");
            if (ItemType.equals("R")) {
                txtnewPurchase.setText("Remold");
            } else {
            }*/

      /*  } else {
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
        //}
    }

    private class GetTyreStatus extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TyreScrapActivity.this);
            pDialog.setMessage("Loading Tyre Scrap Expense Details ..... Please Wait");
            pDialog.setCancelable(false);
            pDialog.show();
           /* Toast.makeText(getBaseContext(),
                    "Loading Tyre Scrap Expense Details ..... Please Wait",Toast.LENGTH_SHORT).show();*/
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
                            "api/VehAnalysis/GetVehTyrePartBreakUpScrapDetail/" + registration_no+"/"+stypeId+"/"+sgtype+"-"+smonthId+"-"+Date+"/"+Type+"/"+ItemType);
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
                            DecimalFormat formatter = new DecimalFormat("#,##,### ");
                            budgetDetail = new JSONArray(jsonStr1);
                            for (int i = 0; i < budgetDetail.length(); i++) {
                                JSONObject c = budgetDetail.getJSONObject(i);
                                days = c.getString(TAG_Date);
                                tyreNo=   c.getString(TAG_Tyre_No);
                                tyreBrand=  c.getString (TAG_Tyre_Brand);
                                tyreManufacturer=  c.getString(TAG_Tyre_Mnufctrer);
                                tyreMonth=  c.getString(TAG_Tyre_Month);
                                tyreMilage=  c.getString(TAG_Tyre_Milage) ;
                                tyreScrapValue=  formatter.format(c.getInt(TAG_Tyre_Scrap_Value));
                                tyreCPKM= c.getString(TAG_Tyre_CPKM);

                                HashMap<String, String> Budget_details = new HashMap<String, String>();

                                Budget_details.put(TAG_Date, days);
                                Budget_details.put(TAG_Tyre_No, tyreNo);
                                Budget_details.put(TAG_Tyre_Brand, tyreBrand);
                                Budget_details.put(TAG_Tyre_Mnufctrer, tyreManufacturer);
                                Budget_details.put(TAG_Tyre_Month, tyreMonth);
                                Budget_details.put(TAG_Tyre_Milage, tyreMilage);
                                Budget_details.put(TAG_Tyre_Scrap_Value, tyreScrapValue);
                                Budget_details.put(TAG_Tyre_CPKM, tyreCPKM);




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
                Toast.makeText(TyreScrapActivity.this, "Record is null...", Toast.LENGTH_SHORT).show();
            }else{
                adapter = new TyreScrapadapter(TyreScrapActivity.this, budgetinfoList);
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
