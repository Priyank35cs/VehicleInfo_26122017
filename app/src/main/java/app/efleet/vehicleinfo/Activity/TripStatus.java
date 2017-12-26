package app.efleet.vehicleinfo.Activity;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import app.efleet.vehicleinfo.DriverDetails;
import app.efleet.vehicleinfo.HttpCalls.ServiceHandler;
import app.efleet.vehicleinfo.Map.MapActivity1;
import app.efleet.vehicleinfo.R;
import app.efleet.vehicleinfo.adapters.ListViewAdapter;

public class TripStatus extends Activity {
    TextView txtvehNo,txtcrntsts,txtclient,txtRoute,txtbdgH,txtbdgkm,txtlgu,txtlgl;
    TextView txtgpskm,txtdelay;
    TextView txtStsDate,txtstsloc,txtstsage,txtstsremark,txtdnmae,txtdmob;
    TextView txtTripFuel,txtTripCash,txtTripDiesel,txtTripSETA,txtGpsRETA;
    String vehno,crntsts,rules,client,route,bdghours,bdgkm,lastgpsupdt,lastgpsloc,
            FuelAdvAmt,CashAdvAmt,Diesel,ScheduledETA="",RevisedEta;
    String gpskm,gpslat="",gpslong,delay,stsdate,stsloc,stsage,stsrmrk,dname,dmob;
    String sntforloadingdt,reportforlodngdt,ldngcomptdt,rptunldngdt,unldngcomptdt;
    String sntforloadinghr,reportforlodnghr,ldngcompthr,rptunldnghr,unldngcompthr;
    ImageButton imgback;
    String imeiNumber = "";
    String clientCode = "";
    String verificationCode = "";
    GetTripStatus getTripStatus;
    JSONArray tripDetail = null;
    ProgressDialog pDialog;
    String encodedUrl = null;
    LinearLayout LinUp,Lintrp,lintripdetail,lingps,lingpsdetail,linstatus,linstatusdetail,linearCashAdvance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_status);
        imgback = (ImageButton)findViewById(R.id.imgback);
        txtvehNo = (TextView) findViewById(R.id.VehNo);
        txtcrntsts = (TextView) findViewById(R.id.CStatus);
        txtclient = (TextView) findViewById(R.id.client);
        txtRoute = (TextView) findViewById(R.id.route);
        txtbdgH = (TextView) findViewById(R.id.bdghours);
        txtbdgkm = (TextView) findViewById(R.id.bdgkm);
        txtlgu = (TextView) findViewById(R.id.LGU);
        txtlgl = (TextView) findViewById(R.id.LGL);
        txtgpskm = (TextView) findViewById(R.id.LGpsKm);
        txtdelay = (TextView) findViewById(R.id.delay);
        txtStsDate=(TextView) findViewById(R.id.txtSDate);
        txtstsloc=(TextView) findViewById(R.id.StsLoc);
        txtstsage=(TextView) findViewById(R.id.StsAge);
        txtstsremark=(TextView) findViewById(R.id.StsRmrk);
        txtdnmae = (TextView) findViewById(R.id.diver);
        txtdmob = (TextView) findViewById(R.id.drivermobno);
        LinUp=(LinearLayout)findViewById(R.id.Linup);
        txtTripCash=(TextView) findViewById(R.id.cashAdvance);
        txtTripFuel=(TextView) findViewById(R.id.fueladvance);
        txtTripDiesel=(TextView) findViewById(R.id.dieselLtr);
        txtTripSETA=(TextView)findViewById(R.id.scheduleDeta);
        // txtGpsRETA=(TextView)findViewById(R.id.revisedEta);
        linstatus=(LinearLayout)findViewById(R.id.LinStatus);
        linstatusdetail=(LinearLayout)findViewById(R.id.LinStatusDetail);
        Lintrp=(LinearLayout)findViewById(R.id.LinTrip);
        lintripdetail=(LinearLayout)findViewById(R.id.LinTripDetail);
        lingps=(LinearLayout)findViewById(R.id.LinGps);
        lingpsdetail=(LinearLayout)findViewById(R.id.LinGpsDetail);
        linearCashAdvance=(LinearLayout)findViewById(R.id.linearCashAdvance);

        vehno = getIntent().getStringExtra("reg_no");
        txtvehNo.setText(vehno);
        imgback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getTripStatus = new GetTripStatus();
        getTripStatus.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getTripStatus.getStatus() == AsyncTask.Status.RUNNING) {
                    getTripStatus.cancel(true);
                    Toast.makeText(
                            TripStatus.this,
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
        lintripdetail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i=new Intent(TripStatus.this,TripStatusDetail.class);
                i.putExtra("Vehno",vehno);
                i.putExtra("CSts",crntsts);
                i.putExtra("StsDate",stsdate);
                i.putExtra("StsLoc",stsloc);
                i.putExtra("StsAge",stsage);
                i.putExtra("StsRemark",stsrmrk);
                i.putExtra("Client",client);
                i.putExtra("Rout",route);
                i.putExtra("BdgHr",bdghours);
                i.putExtra("BdgKm",bdgkm);
                i.putExtra("DName",dname);
                i.putExtra("DNumb",dmob);
                i.putExtra("SFL",sntforloadingdt);
                i.putExtra("RFL",reportforlodngdt);
                i.putExtra("LC",ldngcomptdt);
                i.putExtra("RU",rptunldngdt);
                i.putExtra("UC",unldngcomptdt);
                i.putExtra("Rule",rules);
                startActivity(i);
            }
        });
        txtlgl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!gpslat.isEmpty()){
                    String[] LatLong=gpslat.split(":");
                    String lat=LatLong[0];
                    String Long=LatLong[1];
                    Intent i=new Intent(TripStatus.this, MapActivity1.class);
                    i.putExtra("lat",lat);
                    i.putExtra("long",Long);
                    i.putExtra("Cloc",lastgpsloc);
                    i.putExtra("vehicleno",vehno);
                    startActivity(i);
                }


            }
        });


    }
    private class GetTripStatus extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TripStatus.this);
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


            String URL = "http://proxy.efleetsystems.in/api/ProxyCalls/AuthrizeMe?action=VEHICLEINFO";
            URL=URL.replace(" ","");
            Log.e("codeUrl",URL.toString());

            String appName = "VEHICLEINFO";
            String authorizationString = "Device " + new String(Base64.encode((imeiNumber + ":" + verificationCode
                    + ":" + appName).getBytes(), Base64.NO_WRAP)); // Base64.NO_WRAP flag

            Log.e("finalbase64",authorizationString);
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.CaLLService(URL, authorizationString);


            Log.e("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    String tUrl = jsonObj.getString("Url");
                    String tToken = jsonObj.getString("Token");
                    tUrl = tUrl.toString().concat(
                            "api/VehicleInfo/GetTripStatusDetails/"+vehno);

                    encodedUrl = tUrl.replaceAll(" ", "%20");

                    Log.e("codeUrl1",encodedUrl.toString());

                    String authorizationString1 = "Device " + new String(Base64.encode((imeiNumber + ":" + clientCode
                            + ":" + tToken).getBytes(), Base64.NO_WRAP)); // Base64.NO_WRAP flag

                    Log.e("finalbase64",authorizationString1);
                    ServiceHandler sh1 = new ServiceHandler();
                    String jsonStr1 = sh1.CaLLService(encodedUrl, authorizationString1);

                    Log.e("Response1: ", "> " + jsonStr1);
                    if (jsonStr1 != null) {
                        try {

                            tripDetail = new JSONArray(jsonStr1);
                            if (tripDetail.length() == 0) {
                                TripStatus.this.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "Could not get any data from, please check internet "
                                                + "connection or contact administrator", Toast.LENGTH_LONG).show();

                                    }
                                });
                            } else {
                                //for (int i = 0; i < tripDetail.length(); i++) {
                                JSONObject c = tripDetail.getJSONObject(0);
                                crntsts=c.getString("CurrentStatus");
                                client=c.getString("Client");
                                route=c.getString("Route");
                                bdghours=c.getString("BudgetHours");
                                bdgkm=c.getString("BudgetKm");

                              //  c.getJSONObject("ScheduledETA");



/*
                                if(c.getString("ScheduledETA")==null){

                                }else{
                                    ScheduledETA  =c.getString("ScheduledETA");
                                }*/
                                 ScheduledETA  =c.getString("ScheduledETA");
                                lastgpsupdt=c.getString("LastGpsUpdate");
                                lastgpsloc=c.getString("LastGpsLocation");
                                gpskm=c.getString("GpsKm");
                                gpslat=c.getString("Gpslatlong");
                                //gpslong=c.getString("");
                                delay=c.getString("Delay");
                                stsdate=c.getString("StatusDate");
                                stsloc=c.getString("StatusLocation");
                                stsage=c.getString("StatusAge");
                                stsrmrk=c.getString("StatusRemark");
                                dname=c.getString("DriverName");
                                dmob=c.getString("DriverNumber");
                                CashAdvAmt=String.valueOf(c.getDouble("CashAdvAmt"));
                                FuelAdvAmt=String.valueOf(c.getDouble("FuelAdvAmt"));
                                Diesel=String.valueOf(c.getDouble("FuelQty"));
                                sntforloadingdt=c.getString("SentforLoading");
                                reportforlodngdt=c.getString("ReportforLoading");
                                ldngcomptdt=c.getString("LoadingComplete");
                                rptunldngdt=c.getString("ReportUnloading");
                                unldngcomptdt=c.getString("UnloadingComplete");
                                rules=c.getString("RULES");
                                /* sntforloadinghr=c.getString("");
                                    reportforlodnghr=c.getString("");
                                    ldngcompthr=c.getString("");
                                    rptunldnghr=c.getString("");
                                    unldngcompthr=c.getString("");*/
                                //Update Uri fgor dataset
                                TripStatus.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        txtcrntsts.setText(crntsts);
                                        txtclient.setText(client);
                                        txtRoute.setText(route);
                                        txtbdgH.setText(bdghours);
                                        txtbdgkm.setText(bdgkm);
                                        txtTripCash.setText(CashAdvAmt);
                                        txtTripFuel.setText(FuelAdvAmt);
                                        txtTripDiesel.setText(Diesel);
                                        txtlgu.setText(lastgpsupdt);
                                        txtlgl.setText(lastgpsloc);
                                        txtgpskm.setText(gpskm);
                                        txtdelay.setText(delay);
                                        txtStsDate.setText(stsdate);
                                        txtstsloc.setText(stsloc);
                                        txtstsage.setText(stsage);
                                        txtstsremark.setText(stsrmrk);
                                        txtdnmae.setText(dname);
                                         txtTripSETA.setText(ScheduledETA);
                                        txtdmob.setText(dmob);

                                        //txtTripFuel.setText(FuelAdvance);

                                    }
                                });





                                   /* driver_name = c.getString(TAG_DRIVER_NAME);
                                    driver_code = c.getString(TAG_DRIVER_CODE);
                                    driver_photo = c.getString(TAG_DRIVER_PHOTO);
                                    licenseNo = c.getString(TAG_LICENSE_NO);
                                    expiryDate = c.getString(TAG_EXPIRY_DATE);
                                    issuedAt = c.getString(TAG_CITY_NAME);
                                    qualification = c.getString(TAG_QUALIFICATION);
                                    statusDate = c.getString(TAG_STATUS_DATE);
                                    phone_no = c.getString(TAG_PHONE_NO);
                                    granter=c.getString(TAG_DRIVER_Granter);
                                    HashMap<String, String> driver_details = new HashMap<String, String>();
                                    driver_details.put(TAG_DRIVER_NAME, driver_name);
                                    driver_details.put(TAG_DRIVER_CODE, driver_code);
                                    driver_details.put(TAG_DRIVER_PHOTO, driver_photo);
                                    driver_details.put(TAG_LICENSE_NO, licenseNo);
                                    driver_details.put(TAG_EXPIRY_DATE, expiryDate);
                                    driver_details.put(TAG_CITY_NAME, issuedAt);
                                    driver_details
                                            .put(TAG_QUALIFICATION, qualification);
                                    driver_details.put(TAG_STATUS_DATE, statusDate);
                                    driver_details.put(TAG_PHONE_NO, phone_no);
                                    driver_details.put(TAG_DRIVER_Granter, granter);
                                    driverInfoList.add(driver_details);*/

                                // }
                                //pDialog.dismiss();
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
                Toast.makeText(getBaseContext(),
                        "Couldn't get any data from the url",
                        Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            try {
                if (rules.equals("S - Standing")) {
                    LinUp.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalShadedkRed));
                    linstatus.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalRed));
                    linstatusdetail.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalDeepRed));
                    Lintrp.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalRed));
                    lintripdetail.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalDeepRed));
                    lingps.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalRed));
                    lingpsdetail.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalDeepRed));
                } else if (rules.equals("E - Enroute")) {
                    LinUp.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalShadedkGreen));
                    linstatus.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalGreen));
                    linstatusdetail.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalDeepGreen));
                    Lintrp.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalGreen));
                    lintripdetail.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalDeepGreen));
                    lingps.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalGreen));
                    lingpsdetail.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalDeepGreen));
                }else if (rules.equals("U - Unloading")) {
                    LinUp.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalShadedOrange));
                    linstatus.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalOrange));
                    linstatusdetail.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalDeepOrange));
                    Lintrp.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalOrange));
                    lintripdetail.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalDeepOrange));
                    lingps.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalOrange));
                    lingpsdetail.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalDeepOrange));
                }else if (rules.equals("R - Repairing")) {
                    LinUp.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalShadedBlue));
                    linstatus.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalBlue));
                    linstatusdetail.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalDeepBlue));
                    Lintrp.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalBlue));
                    lintripdetail.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalDeepBlue));
                    lingps.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalBlue));
                    lingpsdetail.setBackgroundColor(ContextCompat.getColor(TripStatus.this, R.color.PastalDeepBlue));
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

}