package app.efleet.vehicleinfo.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.efleet.vehicleinfo.GetVehicleNo;
import app.efleet.vehicleinfo.HttpCalls.ServiceHandler;
import app.efleet.vehicleinfo.R;

public class CountBaseBatteryExp extends AppCompatActivity {
    LinearLayout linNew, linRemold, linScrap;
    TextView txtTyreBat,txtRemold, txtNewCount, txtNewAmount, txtRemoldCount, txtRemoldAmount, txtScrapCount, txtScrapAmount;
    String Type , vehno,newCount, newAmount,remoldCount,remoldAmount,scrapCount,scrapAmount;
    ImageButton imgback;
    String imeiNumber = "";
    String clientCode = "",VehNo,registration_no;
    String verificationCode = "";
    GetTripStatus getTripStatus;
    JSONArray tripDetail = null;
    ProgressDialog pDialog;
    String encodedUrl = null,stypeId,sgtype,smonthId,Date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_base_battery_exp);
        linNew = (LinearLayout) findViewById(R.id.linNewActivity);
        linRemold = (LinearLayout) findViewById(R.id.linRemoldActivity);
        linScrap = (LinearLayout) findViewById(R.id.linScrapActivity);
        txtRemold = (TextView) findViewById(R.id.txtRemold);
        txtNewCount = (TextView) findViewById(R.id.txtNewCount);
        txtNewAmount = (TextView) findViewById(R.id.txtNewAmount);
        txtRemoldCount = (TextView) findViewById(R.id.txtRemoldCount);
        txtRemoldAmount = (TextView) findViewById(R.id.txtRemoldAmount);
        txtScrapCount = (TextView) findViewById(R.id.txtScrapCount);
        txtScrapAmount = (TextView) findViewById(R.id.txtScrapAmount);
        txtTyreBat = (TextView) findViewById(R.id.textView17);
        getTripStatus = new GetTripStatus();
        VehNo = getIntent().getStringExtra("reg_no");
        getTripStatus.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getTripStatus.getStatus() == AsyncTask.Status.RUNNING) {
                    getTripStatus.cancel(true);
                    Toast.makeText(
                            CountBaseBatteryExp.this,
                            "Response time out, please try again ....",
                            Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();

//					Toast.makeText(
//							getApplicationContext(),
//							"Connection timed out , please try again later....",
//							Toast.LENGTH_LONG).show();
                }

            }

        }, 20000);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        vehno = extras.getString("VehNo");
        stypeId=extras.getString("stypeId");
        Date=extras.getString("Date");
        sgtype=extras.getString("sgtype");
        smonthId=extras.getString("smonthId");
        Type = extras.getString("DType");

       /* if (Type.equals("T")) {
            setTitle("Tyre Details");
        } else {
            setTitle("Battery Details");
            txtTyreBat.setText("Battery");
            txtRemold.setText("Refurnished");

        }*/
        linNew.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(CountBaseBatteryExp.this,
                                                  BatteryActivity.class);
                                          intent.putExtra("reg_no", GetVehicleNo.reg_no);
                                          intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                                          intent.putExtra("DType", Type);
                                          intent.putExtra("ItemType", "N");
                                          startActivity(intent);
                                      }
                                  }
        );
        linScrap.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent1 = new Intent(CountBaseBatteryExp.this,
                                                    BatteryScrapActivity.class);
                                            intent1.putExtra("reg_no", GetVehicleNo.reg_no);
                                            intent1.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                                            intent1.putExtra("DType", Type);
                                            intent1.putExtra("ItemType","S");
                                            startActivity(intent1);
                                        }
                                    }
        );
        linRemold.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Intent intent = new Intent(CountBaseBatteryExp.this,
                                                     BatteryActivity.class);
                                             intent.putExtra("reg_no", GetVehicleNo.reg_no);
                                             intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                                             intent.putExtra("DType", Type);
                                             intent.putExtra("ItemType", "R");
                                             startActivity(intent);
                                         }
                                     }
        );


    }
    private class GetTripStatus extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(CountBaseBatteryExp.this);
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
                    + ":" + appName).getBytes(), Base64.NO_WRAP)); // Base64.NO_WRAP flag

            Log.e("finalbase64", authorizationString);
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.CaLLService(URL, authorizationString);


            Log.e("Response: ", "> " + jsonStr);
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                String tUrl = jsonObj.getString("Url");
                String tToken = jsonObj.getString("Token");
                tUrl = tUrl.toString().concat(
                        "api/VehAnalysis/GetVehSummary/" + registration_no+"/"+stypeId+"/"+sgtype+"-"+smonthId+"-"+Date+"/"+Type);

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
                            CountBaseBatteryExp.this.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(), "Could not get any data from, please check internet "
                                            + "connection or contact administrator", Toast.LENGTH_LONG).show();

                                }
                            });
                        } else {
                            for (int i = 0; i < tripDetail.length(); i++) {
                                JSONObject c = tripDetail.getJSONObject(0);
                                newCount = c.getString("newCount");
                                newAmount = c.getString("newAmount");
                                remoldCount = c.getString("remoldCount");
                                remoldAmount = c.getString("remoldAmount");
                                scrapCount = c.getString("scrapCount");
                                scrapCount = c.getString("scrapCount");
                                scrapAmount = c.getString("scrapAmount");
                                CountBaseBatteryExp.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        txtNewCount.setText(newCount);
                                        txtNewAmount.setText(newAmount);
                                        txtRemoldCount.setText(remoldCount);
                                        txtRemoldAmount.setText(remoldAmount);
                                        txtScrapCount.setText(scrapCount);
                                        txtScrapAmount.setText(scrapAmount);

                                    }
                                });

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //}

                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    Toast.makeText(getBaseContext(),
                            "Couldn't get any data from the url",
                            Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } return null;
        }
        @Override
        protected void onPostExecute (Void result){
            super.onPostExecute(result);
            pDialog.dismiss();

        }
    }
}