package app.efleet.vehicleinfo.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import app.efleet.vehicleinfo.adapters.AccidentalRepairAdapter;
import app.efleet.vehicleinfo.adapters.GenExpAdapter;
/* ListView accidentalRepaireListView;
   *//* TextView txtAccRepairVehicleNo;
    String VehNo,vehno,AcRpair;
    *//**//*txtAccRepairVehicleNo=(TextView)findViewById(R.id.txtAccRepairVehicleNo);
    accidentalRepaireListView=(ListView)findViewById(R.id.accidentalRepaireListView);*/

public class AccidentalRepairActivity extends AppCompatActivity {
    String  days, billNo,party,amount;
    String vehno,Type;
    Cursor cursor;
    ProgressDialog pDialog;
    String imeiNumber = "";
    String clientCode = "";
    String verificationCode = "";
    String appname = "VEHICLEINFO";
    String encodedUrl = null;
    JSONArray budgetDetail = null;
    GetAccidentalRepairDetail getAccidentalRepairDetail;
    String DType,registration_no;
    ArrayList<HashMap<String, String>> accirepairinfolist;
    public static final String TAG_Date = "DDMM";
    public static final String TAG_Bill_No = "Nature";
    public static final String TAG_Party = "Type";
    public static final String TAG_Amount = "Amount";

    ListView lv;
    AccidentalRepairAdapter adapter;
    String VehNo,Dues,stypeId,Date,sgtype,smonthId;
    TextView txtAccRepairVehicleNo;
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accidental_repair);
        setTitle("Accidental Repair exp");
        lv = (ListView)findViewById(R.id.lvRepairListView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       /* lv = getListView();*/
        accirepairinfolist = new ArrayList<HashMap<String, String>>();


        txtAccRepairVehicleNo=(TextView)findViewById(R.id.txtAccRepairVehicleNo);
        VehNo = getIntent().getStringExtra("reg_no");
//        Log.e("VehNo",VehNo);
        txtAccRepairVehicleNo.setText(VehNo);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        vehno = extras.getString("VehNo");
        Type=extras.getString("AR");
        stypeId=extras.getString("stypeId");
        Date=extras.getString("Date");
        sgtype=extras.getString("sgtype");
        smonthId=extras.getString("smonthId");

        getAccidentalRepairDetail = new GetAccidentalRepairDetail();
        getAccidentalRepairDetail.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getAccidentalRepairDetail.getStatus() == AsyncTask.Status.RUNNING) {
                    getAccidentalRepairDetail.cancel(true);
                    Toast.makeText(
                            AccidentalRepairActivity.this,
                            "Response time out, please try again ....",
                            Toast.LENGTH_SHORT).show();

                }

            }

        }, 20000);


    }
    private class GetAccidentalRepairDetail extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AccidentalRepairActivity.this);
            pDialog.setMessage("Loading Trip Gen Expences ..... Please Wait");
            pDialog.setCancelable(false);
            pDialog.show();
            Toast.makeText(getBaseContext(),"Loading Trip Gen Expences  Details..... Please Wait",
                    Toast.LENGTH_SHORT).show();
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
                            DecimalFormat formatter = new DecimalFormat("#,##,### ");
                            budgetDetail = new JSONArray(jsonStr1);
                            for (int i = 0; i < budgetDetail.length(); i++) {
                                JSONObject c = budgetDetail.getJSONObject(i);
                                days = c.getString(TAG_Date);
                                billNo=  c.getString(TAG_Bill_No);
                                party=  c.getString(TAG_Party);
                                amount= formatter.format(c.getInt(TAG_Amount));

                                HashMap<String, String> Budget_details = new HashMap<String, String>();

                                Budget_details.put(TAG_Date, days);
                                Budget_details.put(TAG_Bill_No, billNo);
                                Budget_details.put(TAG_Party, party);
                                Budget_details.put(TAG_Amount, amount);


                                accirepairinfolist.add(Budget_details);
                                Log.e("data",accirepairinfolist.toString());
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

            if (accirepairinfolist.isEmpty()) {
                    /*imageView.setVisibility(View.VISIBLE);
                    txtNoResults.setVisibility(View.VISIBLE);*/
            } else {
                adapter = new AccidentalRepairAdapter(AccidentalRepairActivity.this, accirepairinfolist );
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