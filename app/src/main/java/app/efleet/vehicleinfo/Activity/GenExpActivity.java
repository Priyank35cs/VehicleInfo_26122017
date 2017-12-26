package app.efleet.vehicleinfo.Activity;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

import app.efleet.vehicleinfo.HttpCalls.ServiceHandler;
import app.efleet.vehicleinfo.R;
import app.efleet.vehicleinfo.adapters.DuesDriverEmiAdapter;
import app.efleet.vehicleinfo.adapters.GenExpAdapter;

public class GenExpActivity extends AppCompatActivity {
    String  days, expences ,amount;
    String vehno,Type;
    Cursor cursor;
    ProgressDialog pDialog;
    String imeiNumber = "";
    String clientCode = "";
    String verificationCode = "";
    String appname = "VEHICLEINFO";
    String encodedUrl = null;
    JSONArray budgetDetail = null;
    GetGenExpDetail getGenExpDetail;
    String DType,registration_no;
    ArrayList<HashMap<String, String>> budgetinfoList;
    public static final String TAG_Date = "dd/mm";
    public static final String TAG_Expences = "Dues";
    public static final String TAG_Amount = "Amount";

    ListView lv;
    GenExpAdapter adapter;
    String VehNo,Dues;
    TextView txtGenExpVehicleNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_exp);
        setTitle("Gen Exp");
        budgetinfoList = new ArrayList<HashMap<String, String>>();
        lv = (ListView)findViewById(R.id.lvGenExpenses);
       /* lv = getListView();*/


        txtGenExpVehicleNo=(TextView)findViewById(R.id.txtGenExpVehicleNo);
        VehNo = getIntent().getStringExtra("reg_no");
 //       Log.e("VehNo",VehNo);
        txtGenExpVehicleNo.setText(VehNo);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        vehno = extras.getString("VehNo");
        Type=extras.getString("GExp");

        getGenExpDetail = new GetGenExpDetail();
        getGenExpDetail.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getGenExpDetail.getStatus() == AsyncTask.Status.RUNNING) {
                    getGenExpDetail.cancel(true);
                    Toast.makeText(
                            GenExpActivity.this,
                            "Response time out, please try again ....",
                            Toast.LENGTH_SHORT).show();

                }

            }

        }, 20000);


    }
    private class GetGenExpDetail extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GenExpActivity.this);
            pDialog.setMessage("Loading Trip Gen Expense ..... Please Wait");
            pDialog.setCancelable(false);
            pDialog.show();
          /*  Toast.makeText(getBaseContext(),"Loading Trip Gen Expences  Details..... Please Wait",
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
                                expences= String.valueOf(c.getInt(TAG_Expences));
                                amount= c.getString(TAG_Amount);

                                HashMap<String, String> Budget_details = new HashMap<String, String>();

                                Budget_details.put(TAG_Date, days);
                                Budget_details.put(TAG_Expences, expences);
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
                adapter = new GenExpAdapter(GenExpActivity.this, budgetinfoList );
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

