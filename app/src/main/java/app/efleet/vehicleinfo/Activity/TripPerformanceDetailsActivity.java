package app.efleet.vehicleinfo.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DecimalFormat;
import app.efleet.vehicleinfo.GetVehicleNo;
import app.efleet.vehicleinfo.HttpCalls.ServiceHandler;
import app.efleet.vehicleinfo.R;

public class TripPerformanceDetailsActivity extends AppCompatActivity {
    LinearLayout linDriver,linRoute,linETollExpence,linDriverExpence,linOtherExpence,linFuelExpence,linLessTripExp;
    TextView txtViewVhicleNo,txtTripSheetNo,txtDateToDate,txtRoute,txtKM,txtParty,txtDriverName,txtDriverMobNo,txtFrieght,txtLessTripExp,
    txtFuelExpence,txtTollExpence,txtDriver,txtOtherExpence,txtNetProfit,txtAdvance,txtholdAmount,txtExpense;
    String  VhicleNo,TripSheetNo,DateToDate,Route,KM,Party,DriverName,DriverMobNo,LessTripExp,Frieght, FuelExpense,TollExpense,
             DriverAccount,OtherExpense,NetProfit,Advance,holdAmount,Expense;
    String imeiNumber = "";
    String clientCode = "";
    String verificationCode = "",registration_no,VehNo,vehno,LType,LItemType;
    String ExpType,FNature,FBudget,FActual,FVariance,TNature,TBudget,TActual,TVariance,DNature,DBudget,DActual,DVariance,ONature,OBudget,OActual,OVariance;
    GetTripPerformanceStatus getTripPerformanceStatus;
    JSONArray tripDetail = null;
    ProgressDialog pDialog;
    String encodedUrl = null,stypeId,Date,sgtype,smonthId,id;
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_performance_details);
        setTitle("Trip Performance Details");
        linLessTripExp = (LinearLayout) findViewById(R.id.txtLessTripExpense);
        linETollExpence=(LinearLayout)findViewById(R.id.linETollExpence);
        linDriverExpence=(LinearLayout)findViewById(R.id.linDriverExpence);
        linOtherExpence=(LinearLayout)findViewById(R.id.linOtherExpence);
        linFuelExpence=(LinearLayout)findViewById(R.id.linFuelExpence);
        txtViewVhicleNo=(TextView)findViewById(R.id.txtViewVhicleNo);
        txtTripSheetNo=(TextView) findViewById(R.id.txtTripSheetNo);
        txtDateToDate=(TextView)findViewById(R.id.txtDateToDate);
        txtRoute=(TextView)findViewById(R.id.txtRoute);
        txtKM=(TextView)findViewById(R.id.txtKM);

        //////////
        txtParty=(TextView)findViewById(R.id.txtParty);
        txtDriverName=(TextView)findViewById(R.id.txtDriverName);
        txtDriverMobNo=(TextView)findViewById(R.id.txtDriverMobNo);
        txtFrieght=(TextView)findViewById(R.id.txtFrieght);
        txtLessTripExp=(TextView)findViewById(R.id.txtLessTripExp);
        txtFuelExpence=(TextView)findViewById(R.id.txtFuelExpence);
        txtTollExpence=(TextView)findViewById(R.id.txtTollExpence);
        txtDriver=(TextView)findViewById(R.id.txtDriver);
        txtOtherExpence=(TextView)findViewById(R.id.txtOtherExpence);
        txtNetProfit=(TextView)findViewById(R.id.txtNetProfit);
        txtAdvance=(TextView)findViewById(R.id.txtAdvance);
        txtholdAmount=(TextView)findViewById(R.id.txtholdAmount);
        txtExpense=(TextView)findViewById(R.id.txtExpence);
        VhicleNo = getIntent().getStringExtra("reg_no");
        txtViewVhicleNo.setText(VhicleNo);
        id = getIntent().getStringExtra("SettlementID");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getTripPerformanceStatus = new GetTripPerformanceStatus();
        new GetTripPerformanceStatus().execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getTripPerformanceStatus.getStatus() == AsyncTask.Status.RUNNING) {
                    getTripPerformanceStatus.cancel(true);
                    Toast.makeText(
                            TripPerformanceDetailsActivity.this,
                            "Response time out, please try again ....",
                            Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }

            }

        }, 20000);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        stypeId=extras.getString("stypeId");
        id=extras.getString("SettlementID");
        Date=extras.getString("Date");
        sgtype=extras.getString("sgtype");
        smonthId=extras.getString("smonthId");

        linLessTripExp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TripPerformanceDetailsActivity.this, BreakOfLessTripExp.class);

                intent.putExtra("stypeId",stypeId );
                intent.putExtra("Date",Date );
                intent.putExtra("sgtype",sgtype );
                intent.putExtra("smonthId",smonthId);
                intent.putExtra("SettlementID",id);
                intent.putExtra("reg_no", GetVehicleNo.reg_no);
                intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                intent.putExtra("ExpType",ExpType);
                intent.putExtra("FNature",FNature);
                intent.putExtra("FNature",FNature);
                intent.putExtra("FBudget",FBudget);
                intent.putExtra("FActual",FActual);
                intent.putExtra("FVariance",FVariance);
                startActivity(intent);
            }
        });


        linFuelExpence.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TripPerformanceDetailsActivity.this,AdvanceTripExpenceActivity.class);
                intent.putExtra("stypeId",stypeId );
                intent.putExtra("Date",Date );
                intent.putExtra("sgtype",sgtype );
                intent.putExtra("smonthId",smonthId);
                intent.putExtra("SettlementID",id);
                intent.putExtra("reg_no", GetVehicleNo.reg_no);
                intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                intent.putExtra("Vehno",VhicleNo);
                intent.putExtra("FNature",FNature);
                intent.putExtra("FBudget",FBudget);
                intent.putExtra("FActual",FActual);
                intent.putExtra("FVariance",FVariance);
                intent.putExtra("DType", "F");
                startActivity(intent);
            }
        });
        linETollExpence.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TripPerformanceDetailsActivity.this,AdvanceTripExpenceActivity.class);
                intent.putExtra("stypeId",stypeId );
                intent.putExtra("Date",Date );
                intent.putExtra("sgtype",sgtype );
                intent.putExtra("smonthId",smonthId);
                intent.putExtra("SettlementID",id);
                intent.putExtra("reg_no", GetVehicleNo.reg_no);
                intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                intent.putExtra("Vehno",VhicleNo);
                intent.putExtra("TNature",TNature);
                intent.putExtra("TBudget",TBudget);
                intent.putExtra("TActual",TActual);
                intent.putExtra("TVariance",TVariance);
                intent.putExtra("DType", "T");
                startActivity(intent);
            }
        });
        linDriverExpence.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TripPerformanceDetailsActivity.this,AdvanceTripExpenceActivity.class);
                intent.putExtra("stypeId",stypeId );
                intent.putExtra("Date",Date );
                intent.putExtra("sgtype",sgtype );
                intent.putExtra("smonthId",smonthId);
                intent.putExtra("SettlementID",id);
                intent.putExtra("reg_no", GetVehicleNo.reg_no);
                intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                intent.putExtra("Vehno",VhicleNo);
                intent.putExtra("DNature",DNature);
                intent.putExtra("DBudget",DBudget);
                intent.putExtra("DActual",DActual);
                intent.putExtra("DVariance",DVariance);
                intent.putExtra("DType", "D");
                startActivity(intent);
            }
        });
        linOtherExpence.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TripPerformanceDetailsActivity.this,AdvanceTripExpenceActivity.class);
                intent.putExtra("stypeId",stypeId );
                intent.putExtra("Date",Date );
                intent.putExtra("sgtype",sgtype );
                intent.putExtra("smonthId",smonthId);
                intent.putExtra("SettlementID",id);
                intent.putExtra("reg_no", GetVehicleNo.reg_no);
                intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                intent.putExtra("Vehno",VhicleNo);
                intent.putExtra("ONature",ONature);
                intent.putExtra("OBudget",OBudget);
                intent.putExtra("OActual",OActual);
                intent.putExtra("OVariance",OVariance);
                intent.putExtra("DType", "O");
                startActivity(intent);
            }
        });

    }

    private class GetTripPerformanceStatus extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TripPerformanceDetailsActivity.this);
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
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    String tUrl = jsonObj.getString("Url");
                    String tToken = jsonObj.getString("Token");
                    tUrl = tUrl.toString().concat(
                            "api/VehAnalysis/GetVehTripWise/" + registration_no+"/"+stypeId+"/"+sgtype+"-"+smonthId+"-"+Date+"/"+id);

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
                                TripPerformanceDetailsActivity.this.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "Could not get any data from, please check internet "
                                                + "connection or contact administrator", Toast.LENGTH_LONG).show();

                                    }
                                });
                            } else {

                                double i=1142145189;
                                DecimalFormat formatter = new DecimalFormat("#,##,### ");
                                JSONObject c = tripDetail.getJSONObject(0);
                                TripSheetNo = c.getString("TripsheetNo");
                                DateToDate = c.getString("FromDateToDate");
                                Route = c.getString("Route");
                                KM = String.valueOf(c.getInt ("KM"));
                                Party = c.getString("Party");
                                DriverName = c.getString("DriverName");
                                DriverMobNo = c.getString("DriverMobileNo");
                                Frieght =  formatter.format(c.getInt ("Freight"));
                                LessTripExp=formatter.format(c.getInt ("LessExp"));
                                FuelExpense = formatter.format(c.getInt ("FuelExp"));
                                TollExpense =formatter.format(c.getInt ("TollExp"));
                                DriverAccount = formatter.format(c.getInt ("DriverExp"));
                                OtherExpense =formatter.format(c.getInt ("OtherExp"));

                                Advance = formatter.format(c.getInt ("Advance"));
                               // holdAmount = c.getString("holdAmount");
                                 Expense = formatter.format(c.getInt ("Exp"));
                                NetProfit =formatter.format(c.getInt("Freight")-c.getInt("LessExp"));
                                holdAmount=formatter.format(c.getInt ("Exp")-c.getInt ("Advance"));
                                     TripPerformanceDetailsActivity.this.runOnUiThread(new Runnable() {
                                           public void run() {
                                           txtTripSheetNo.setText(TripSheetNo);
                                           txtDateToDate.setText(DateToDate);
                                           txtRoute.setText(Route);
                                           txtKM.setText(KM);
                                           txtParty.setText(Party);
                                            txtDriverName.setText(DriverName);
                                           txtDriverMobNo.setText(DriverMobNo);
                                           txtFrieght.setText(Frieght);
                                               txtLessTripExp.setText(LessTripExp);

                                           txtFuelExpence.setText(FuelExpense);
                                           txtTollExpence.setText(TollExpense);
                                           txtDriver.setText(DriverAccount);
                                           txtOtherExpence.setText(OtherExpense);
                                           txtNetProfit.setText(NetProfit);
                                           txtAdvance.setText(Advance);
                                           txtholdAmount.setText(holdAmount);
                                           txtExpense.setText(Expense);
                                           txtDriver.setText(DriverAccount);
                                        }
                                  }  );

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getBaseContext(), "Couldn't get any data from the url", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

         @Override
         protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();

             try {
                 if (holdAmount.contains("-")) {
                     txtholdAmount.setTextColor(ContextCompat.getColor(TripPerformanceDetailsActivity.this, R.color.PastalShadedkRed));
                 } else {
                     txtholdAmount.setTextColor(ContextCompat.getColor(TripPerformanceDetailsActivity.this, R.color.PastalShadedkGreen));
                 }
             }
             catch (Exception e){
                 e.printStackTrace();
             }
         }
     }
  }

