package app.efleet.vehicleinfo.Activity;

import android.os.Bundle;

import app.efleet.vehicleinfo.R;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;

import app.efleet.vehicleinfo.HttpCalls.ServiceHandler;
import app.efleet.vehicleinfo.R;
import app.efleet.vehicleinfo.adapters.NewTyrePurAdapter;

public class TyreActivity extends AppCompatActivity {
    String days, tyreNo, tyreBrand, tyreManufacturer, tyreCost, tyreLife, tyreScrapValue, tyreCPKM;
    String vehno,VehNo,registration_no,stypeId,Date,sgtype,smonthId;
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
    public static final String TAG_Tyre_Cost = "Cost";
    public static final String TAG_Tyre_Life = "Life";


    ListView lv;
    NewTyrePurAdapter adapter;

    TextView txtTyreManufactr, txtTyreBrand, txtTyrePer, txtnewPurchase,txtTripNewVehicleNo;
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tyre);
        txtTripNewVehicleNo = (TextView) findViewById(R.id.txtTripNewVehicleNo);
        txtnewPurchase=(TextView) findViewById(R.id.txtnewPurchase);
        lv=(ListView)findViewById(R.id.list);
       /* getTripStatus = new GetTripStatus();*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      /*  VehNo = getIntent().getStringExtra("reg_no");*/
     /*   new GetTripStatus().execute();*/
        budgetinfoList = new ArrayList<HashMap<String, String>>();
       // lv = getListView();




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
                            TyreActivity.this,
                            "Response timed out , please try again ....",
                            Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }

            }

        }, 10000);
        if(Type.equals("Tyre")) {
            if (ItemType.equals("New")) {
                txtnewPurchase.setText("New");
            } else {
                txtnewPurchase.setText("Remould");
            }
        } else {
            setTitle(" Battery Details");
            //  txtnewPurchase.setText("Refurnished Details");
            if (ItemType.equals("New")) {
                txtnewPurchase.setText("New");
            } else {

                txtnewPurchase.setText("Refurbished");

        }

       }
    }

    private class GetTyreStatus extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TyreActivity.this);
            pDialog.setMessage("Loading EXpense Details ..... Please Wait");
            pDialog.setCancelable(false);
            pDialog.show();
		/*Toast.makeText(getBaseContext(),
					"Loading New Tyre EXpense Details ..... Please Wait",Toast.LENGTH_SHORT).show();*/
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

			/*url = "http://proxy2.efleetsystems.in/servicespro/api/GetClientVerify.svc/GetClientVerify/"
					+ imeiNumber + "/" + appname + "/" + clientCode;
			Log.e("DriverUrl",url);
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);*/
            Log.e("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    //String urlData = jsonObj.getString("ClientVerifyResult");
                    //JSONArray url_master = new JSONArray(urlData);
                    //for (int j = 0; j < url_master.length(); j++) {
                    //JSONObject d = url_master.getJSONObject(j);
                    String tUrl = jsonObj.getString("Url");
                    String tToken = jsonObj.getString("Token");
                    registration_no = getIntent().getStringExtra("reg_no");

                    tUrl = tUrl.toString().concat(
                            "api/VehAnalysis/GetVehTyrePartBreakUpDetail/" + registration_no+"/"+stypeId+"/"+sgtype+"-"+smonthId+"-"+Date+"/"+Type+"/"+ItemType);
                    //+ verificationCode + "/" +clientCode+"/"+registration_no);
                    encodedUrl = tUrl.replaceAll(" ", "%20");

                    Log.e("codeUrl1", encodedUrl.toString());

                    String authorizationString1 = "Device " + new String(Base64.encode((imeiNumber + ":" + clientCode
                            + ":" + tToken).getBytes(), Base64.NO_WRAP)); // Base64.NO_WRAP flag
//			String authorizationString = "Device " + Base64.encodeToString((encodedimei + ":" + encodedclclode
//					+ ":" + appName).getBytes(), Base64.NO_WRAP); // Base64.NO_WRAP flag
                    Log.e("finalbase64", authorizationString1);
                    ServiceHandler sh1 = new ServiceHandler();
                    String jsonStr1 = sh1.CaLLService(encodedUrl, authorizationString1);
                    //String jsonStr1 = sh.makeServiceCall(encodedUrl, ServiceHandler.GET);
                    Log.e("Response1: ", "> " + jsonStr1);
                    if (jsonStr1 != null) {
                        try {
                            budgetDetail = new JSONArray(jsonStr1);
                            DecimalFormat formatter = new DecimalFormat("#,##,### ");
                            for (int i = 0; i < budgetDetail.length(); i++) {
                                JSONObject c = budgetDetail.getJSONObject(i);
                                days = c.getString(TAG_Date);
                                tyreNo=c.getString(TAG_Tyre_No);
                                tyreBrand= c.getString (TAG_Tyre_Brand);
                                tyreManufacturer=  c.getString (TAG_Tyre_Mnufctrer);
                                tyreCost=  formatter.format(c.getInt(TAG_Tyre_Cost));
                                tyreLife= c.getString(TAG_Tyre_Life);


                                HashMap<String, String> Budget_details = new HashMap<String, String>();

                                Budget_details.put(TAG_Date, days);
                                Budget_details.put(TAG_Tyre_No, tyreNo);
                                Budget_details.put(TAG_Tyre_Brand, tyreBrand);
                                Budget_details.put(TAG_Tyre_Mnufctrer, tyreManufacturer);
                                Budget_details.put(TAG_Tyre_Cost, tyreCost);
                                Budget_details.put(TAG_Tyre_Life, tyreLife);



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
               /* imageView.setVisibility(View.VISIBLE);
                txtNoResults.setVisibility(View.VISIBLE);*/
                Toast.makeText(TyreActivity.this, "Record is null...", Toast.LENGTH_SHORT).show();
            }else{
                adapter = new NewTyrePurAdapter(TyreActivity.this, budgetinfoList);
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
