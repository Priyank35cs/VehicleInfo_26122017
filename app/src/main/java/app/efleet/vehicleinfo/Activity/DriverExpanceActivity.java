package app.efleet.vehicleinfo.Activity;

import android.app.Activity;
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
import org.w3c.dom.Text;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.HashMap;

import app.efleet.vehicleinfo.HttpCalls.ServiceHandler;
import app.efleet.vehicleinfo.R;
import app.efleet.vehicleinfo.adapters.ListViewDriverRDAccRepAdapter;

public class DriverExpanceActivity extends AppCompatActivity{
    String days, nature, type, amount;
    String vehno;
    Cursor cursor;
    ProgressDialog pDialog;
    String imeiNumber = "";
    String clientCode = "";
    String verificationCode = "";
    String appname = "VEHICLEINFO";
    String encodedUrl = null;
    JSONArray budgetDetail = null;
    GetBudgetDetail getBudgetDetail;
    String DType;
    ArrayList<HashMap<String, String>> budgetinfoList;
    public static final String TAG_Date = "DDMM";
    public static final String TAG_Nature = "Nature";
    public static final String TAG_Type = "Type";
    public static final String TAG_Amount = "Amount";

    ListView lv, onwardLV;
    ListViewDriverRDAccRepAdapter adapter;
    String bdgtType,VehNo,registration_no;
    TextView txtbdgttype,txtDriverExpVNo;
    String Type,stypeId,Date,sgtype,smonthId;
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_expance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Driver Exp");
        txtDriverExpVNo=(TextView)findViewById(R.id.txtDriverExpVNo);
        budgetinfoList = new ArrayList<HashMap<String, String>>();
        VehNo = getIntent().getStringExtra("reg_no");
        Log.e("VehNo",VehNo);
        txtDriverExpVNo.setText(VehNo);


        lv = (ListView)findViewById(R.id.driverExpenceActivity);
       /* lv = getListView();*/

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        vehno = extras.getString("VehNo");
        Type=extras.getString("Driver");
        stypeId=extras.getString("stypeId");
        Date=extras.getString("Date");
        sgtype=extras.getString("sgtype");
        smonthId=extras.getString("smonthId");


        getBudgetDetail = new GetBudgetDetail();
        getBudgetDetail.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getBudgetDetail.getStatus() == AsyncTask.Status.RUNNING) {
                    getBudgetDetail.cancel(true);
                    Toast.makeText(
                            DriverExpanceActivity.this,
                            "Response time out, please try again ....",
                            Toast.LENGTH_SHORT).show();
//					Toast.makeText(
//							getApplicationContext(),
//							"Connection timed out , please try again later....",
//							Toast.LENGTH_LONG).show();
                }

            }

        }, 20000);

        // txtvehNo.setText(vehno);}
    }
    private class GetBudgetDetail extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DriverExpanceActivity.this);
            pDialog.setMessage("Loading Driver Expense Details ..... Please Wait");
            pDialog.setCancelable(false);
            pDialog.show();
//			Toast.makeText(getBaseContext(),
//					"Loading Tyre Details ..... Please Wait",
//					Toast.LENGTH_SHORT).show();
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
                    //registration_no = getIntent().getStringExtra("reg_no");
                    tUrl = tUrl.toString().concat(
                            "api/VehAnalysis/GetDriver_AccidentalDetails/" + registration_no+"/"+stypeId+"/"+sgtype+"-"+smonthId+"-"+Date+"/"+Type);
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
                                nature=  c.getString(TAG_Nature);
                                type =  c.getString(TAG_Type);
                                amount= c.getString(TAG_Amount);


                                HashMap<String, String> Budget_details = new HashMap<String, String>();
                                // tyre_details.put(TAG_OFFICE_NAME, officeName);
                               
                                Budget_details.put(TAG_Date, days);
                                Budget_details.put(TAG_Nature, nature);
                                Budget_details.put(TAG_Type, type);
                                Budget_details.put(TAG_Amount, amount);


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
                    /*imageView.setVisibility(View.VISIBLE);
                    txtNoResults.setVisibility(View.VISIBLE);*/
            } else {
                adapter = new ListViewDriverRDAccRepAdapter(DriverExpanceActivity.this, budgetinfoList );
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