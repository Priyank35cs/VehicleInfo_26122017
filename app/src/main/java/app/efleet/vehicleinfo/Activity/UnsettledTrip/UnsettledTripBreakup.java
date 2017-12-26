package app.efleet.vehicleinfo.Activity.UnsettledTrip;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import app.efleet.vehicleinfo.GetVehicleNo;
import app.efleet.vehicleinfo.HttpCalls.ServiceHandler;
import app.efleet.vehicleinfo.R;

import static java.lang.Math.round;

public class UnsettledTripBreakup extends AppCompatActivity {
    LinearLayout unsettledBreakupAmtQyt;
    TextView tripLogNo, fromDatetoDate, party, km, driver,
            SetFuelBud,SetFuelActual,SetFuelVariance,SetCashBud,SetCashActual,SetCashVariance,txtvehicleNo;
    String untripLogNo, unfromDatetoDate, unparty, unkm, undriver, VehicleNo,unSetFuelBud,unSetFuelActual,unSetFuelVariance,
            unSetCashBud,unSetCashActual,unSetCashVariance,Triplogno;
    //String  VehNo, registration_no, stypeId, Date, sgtype, smonthId;

    String imeiNumber = "";
    String clientCode = "";
    String verificationCode = "", registration_no, VehNo, vehno;

   // GetTripPerformanceStatus getTripPerformanceStatus;
    JSONArray tripDetail = null;
    ProgressDialog pDialog;
    String encodedUrl = null, stypeId, Date, sgtype, smonthId, id;

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    String date,type,fuelQuantity,amount;
    ArrayList<HashMap<String, String>> budgetinfoList;

    public static final String TAG_Date = "Date";
    public static final String TAG_Type = "Type";
    public static final String TAG_Quntity = "FuelQuantity";
    public static final String TAG_Cash = "Amount";

    int Actfuel=0;
    int Actcash=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsettled_trip_breakup);
        setTitle("Unsettled Trip  Details");
        tripLogNo = (TextView) findViewById(R.id.untriplogno);
        fromDatetoDate = (TextView) findViewById(R.id.unsettledFromDate);
        party = (TextView) findViewById(R.id.unParty);
        km = (TextView) findViewById(R.id.unKM);
        driver = (TextView) findViewById(R.id.unDriver);
        txtvehicleNo = (TextView) findViewById(R.id.txtUnTripVehicleNo);
        SetFuelBud=(TextView) findViewById(R.id.unSetFuelBud);
        SetFuelActual=(TextView) findViewById(R.id.unSetFuelActual);
        SetFuelVariance=(TextView) findViewById(R.id.unSetFuelVariance);
        SetCashBud=(TextView) findViewById(R.id.textView21);
        SetCashActual=(TextView) findViewById(R.id.ActualCash);
        SetCashVariance=(TextView) findViewById(R.id.cashVariance);
        budgetinfoList = new ArrayList<HashMap<String, String>>();
        unsettledBreakupAmtQyt = (LinearLayout) findViewById(R.id.unsettledBreakupAmtQyt);

        unsettledBreakupAmtQyt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UnsettledTripBreakup.this,
                        BreakupOfQytAmt.class);
                intent.putExtra("reg_no", GetVehicleNo.reg_no);
                intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                intent.putExtra("TripBreakUp",budgetinfoList);
                startActivity(intent);
            }
        });
        VehicleNo = getIntent().getStringExtra("reg_no");
        txtvehicleNo.setText(VehicleNo);
        Triplogno = getIntent().getStringExtra("triplogno");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getTripPerformanceStatus = new GetTripPerformanceStatus();
        new GetTripPerformanceStatus().execute();
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getTripPerformanceStatus.getStatus() == AsyncTask.Status.RUNNING) {
                    getTripPerformanceStatus.cancel(true);
                    Toast.makeText(
                            UnsettledTripBreakup.this,
                            "Response time out, please try again ....",
                            Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
*//*
            }

        }, 20000);*/
        /*Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        stypeId = extras.getString("stypeId");
        id = extras.getString("SettlementID");
        Date = extras.getString("Date");
        sgtype = extras.getString("sgtype");
        smonthId = extras.getString("smonthId");*/

    }

    private class GetTripPerformanceStatus extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(UnsettledTripBreakup.this);
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
                            "api/VDSInfo_Delay/GetBreakupOfUnsettledTripDetails/" + registration_no + "/" + Triplogno);

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
                                UnsettledTripBreakup.this.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "Could not get any data from, please check internet "
                                                + "connection or contact administrator", Toast.LENGTH_LONG).show();

                                    }
                                });
                            } else {

                               // double i = 1142145189;
                                DecimalFormat formatter = new DecimalFormat("#,##,### ");
                                JSONObject c = tripDetail.getJSONObject(0);
                                untripLogNo = c.getString("TripLogNo");
                                unfromDatetoDate = c.getString("FromDate");
                                //  unparty,unkm,undriver
                                unparty = c.getString("Party");
                                unkm = String.valueOf(c.getInt("RunKm"));
                                undriver = c.getString("Driver");
                                unSetFuelBud =  c.getString("BudgetFuelQty");
                                unSetCashBud = formatter.format(c.getInt("BudgetedTripExp"));

                                for(int i = 0; i < tripDetail.length(); i++) {
                                    JSONObject obj = tripDetail.getJSONObject(i);
                                   date = obj.getString(TAG_Date);
                                    // Collections.sort(days);
                                    type = obj.getString(TAG_Type);
                                   /* fuelQuantity = formatter.format(String.valueOf(obj.getInt(TAG_Quntity)));
                                      amount = formatter.format(String.valueOf( obj.getInt(TAG_Cash))) ;*/
                                    fuelQuantity =String.valueOf(obj.getInt(TAG_Quntity));
                                    amount =String.valueOf( obj.getInt(TAG_Cash)) ;



                                    HashMap<String, String> Budget_details = new HashMap<String, String>();

                                    Budget_details.put(TAG_Date, date);
                                    // Collections.sort(Budget_details, Collections.reverseOrder());
                                    Budget_details.put(TAG_Type, type);
                                    Budget_details.put(TAG_Quntity, fuelQuantity);
                                    Budget_details.put(TAG_Cash, amount);
                                   // DecimalFormat formatterr = new DecimalFormat("#,##,### ");
                                    budgetinfoList.add(Budget_details);
                                    Actfuel  =Actfuel+Integer.parseInt(String.valueOf(round(obj.getInt(TAG_Quntity))));
                                    Actcash =Actcash+Integer.parseInt(String.valueOf(round(obj.getInt(TAG_Cash))));
                                    // Collections.sort(budgetinfoList, Collections.reverseOrder());
                                    Log.e("data", budgetinfoList.toString());
                                }
                                /*unSetFuelVariance = formatter.format(c.getInt("FuelVariance"));
                                unSetCashBud =  formatter.format(c.getString("CashBud"));
                                unSetCashActual =  formatter.format(c.getString("CashActual"));
                                unSetCashVariance =  formatter.format(c.getString("CashVariance"));*/
                                   /*    TollExpense = formatter.format(c.getInt("TollExp"));
                                    DriverAccount = formatter.format(c.getInt("DriverExp"));
                                    OtherExpense = formatter.format(c.getInt("OtherExp"));

                                    Advance = formatter.format(c.getInt("Advance"));
                                    // holdAmount = c.getString("holdAmount");
                                    Expense = formatter.format(c.getInt("Exp"));
                                    NetProfit = formatter.format(c.getInt("Freight") - c.getInt("LessExp"));
                                    holdAmount = formatter.format(c.getInt("Exp") - c.getInt("Advance"));*/
                                UnsettledTripBreakup.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        tripLogNo.setText(untripLogNo);
                                        fromDatetoDate.setText(unfromDatetoDate);
                                        party.setText(unparty);
                                        km.setText(unkm);
                                            driver.setText(undriver);
                                        driver.setText(undriver);
                                        //SetFuelBud,SetFuelActual,SetFuelVariance,SetCashBud,SetCashActual,SetCashVariance
                                        SetFuelBud.setText(unSetFuelBud);
                                        SetFuelActual.setText(String.valueOf(Actfuel));
                                        SetFuelVariance.setText(String.valueOf(Integer.parseInt(unSetFuelBud.trim())-Actfuel));
                                        SetCashBud.setText(unSetCashBud);

                                        SetCashActual.setText(String.valueOf(Actcash));
                                        SetCashVariance.setText(String.valueOf(Integer.parseInt(unSetCashBud.trim())-Actcash));
                                          /*   txtDriverName.setText(DriverName);
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
                                            txtDriver.setText(DriverAccount);*/
                                    }
                                });

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
/*
        try {
            if (holdAmount.contains("-")) {
                txtholdAmount.setTextColor(ContextCompat.getColor(TripPerformanceDetailsActivity.this, R.color.PastalShadedkRed));
            } else {
                txtholdAmount.setTextColor(ContextCompat.getColor(TripPerformanceDetailsActivity.this, R.color.PastalShadedkGreen));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }*/

        }
    }
}
