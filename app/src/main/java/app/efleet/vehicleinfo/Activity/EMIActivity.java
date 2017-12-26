package app.efleet.vehicleinfo.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import app.efleet.vehicleinfo.R;
import app.efleet.vehicleinfo.R;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.efleet.vehicleinfo.HttpCalls.ServiceHandler;
import app.efleet.vehicleinfo.R;
import app.efleet.vehicleinfo.adapters.DuesDriverEmiAdapter;

import android.os.Handler;
import android.widget.Toast;

public class EMIActivity extends AppCompatActivity {
    TextView txtEMITripVehicleNo;
    String  days, EMI ,amount;
    String vehno,Type;
    Cursor cursor;
    ProgressDialog pDialog;
    String imeiNumber = "";
    String clientCode = "";
    String verificationCode = "";
    String appname = "VEHICLEINFO";
    String encodedUrl = null;
    JSONArray budgetDetail = null;
    GetEMIDetail getEMIDetail;
    String DType,registration_no;
    ArrayList<HashMap<String, String>> budgetinfoList;
    public static final String TAG_Date = "dd/mm";
    public static final String TAG_EMI = "EMI";

    public static final String TAG_Amount = "Amount";

    ListView lv;
    DuesDriverEmiAdapter adapter;
    String  VehNo,Dues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emi);
        setTitle("EMI Exp");
        budgetinfoList = new ArrayList<HashMap<String, String>>();
        lv = (ListView)findViewById(R.id.lvEMIActivity);
       /* lv = getListView();*/

        txtEMITripVehicleNo=(TextView)findViewById(R.id.txtEMITripVehicleNo);
        VehNo = getIntent().getStringExtra("reg_no");
    //    Log.e("VehNo",VehNo);
        txtEMITripVehicleNo.setText(VehNo);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        vehno = extras.getString("VehNo");
        Type=extras.getString("EMI");

        getEMIDetail = new GetEMIDetail();
        getEMIDetail.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getEMIDetail.getStatus() == AsyncTask.Status.RUNNING) {
                    getEMIDetail.cancel(true);
                    Toast.makeText(
                            EMIActivity.this,
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
    private class GetEMIDetail extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EMIActivity.this);
            pDialog.setMessage("Loading  EMI Details ..... Please Wait");
            pDialog.setCancelable(false);
            pDialog.show();
           /* Toast.makeText(getBaseContext(),"Loading  EMI Details..... Please Wait",
                    Toast.LENGTH_SHORT).show();*/
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
                            "api/VehAnalysis/GetVehSummary/" + registration_no+"/"+Type);
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
                                EMI= String.valueOf(c.getInt(TAG_EMI));
                                amount= c.getString(TAG_Amount);

                                HashMap<String, String> Budget_details = new HashMap<String, String>();

                                Budget_details.put(TAG_Date, days);
                                Budget_details.put(TAG_EMI, EMI);
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
                adapter = new DuesDriverEmiAdapter(EMIActivity.this, budgetinfoList );
                lv.setAdapter(adapter);
            }
               /* if(onwardId!=null){
                   new GetOnwardRouteBudgetDetail().execute();
                } else{

                }*/

        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}