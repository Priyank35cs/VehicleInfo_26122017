package app.efleet.vehicleinfo.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import app.efleet.vehicleinfo.GetVehicleNo;
import app.efleet.vehicleinfo.HttpCalls.ServiceHandler;
import app.efleet.vehicleinfo.R;

public class CountBaseTyreExp extends AppCompatActivity {
    LinearLayout linNew, linRemold, linScrap;
    TextView txtTyreBat,txtRemold, txtNewCount, txtNewAmount, txtRemoldCount, txtRemoldAmount, txtScrapCount, txtScrapAmount;
    String Type , vehno,newCount="0", newAmount="0",remouldCount="0",remouldAmount="0",scrapCount="0",scrapAmount="0";
    ImageButton imgback;
    String imeiNumber = "";
    String clientCode = "",VehNo,registration_no;
    String verificationCode = "";
    GetTripStatus getTripStatus;
    GetTyreCountBarChart getTyreCountBarChart;
    JSONArray tripDetail = null;
    ProgressDialog pDialog;
    ProgressDialog pDialog1;
    String encodedUrl = null,stypeId,Date,sgtype,smonthId;
    ArrayList<String> XValuesSts=new ArrayList<String>();
    ArrayList<Integer>YValuesStsNew;
    ArrayList<Integer>YValuesStsRemold;
    BarChart chart;
    public static  final int[] MY_COLORS = {
            Color.rgb(119,119,255), Color.rgb(255,94,0), Color.rgb(255,217,0),
            Color.rgb(95,204,95), Color.rgb(239,51,64)
    };

    String[] month={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_base_tyre_exp);
        setTitle("Tyre Exp");
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
        chart = (BarChart) findViewById(R.id.chart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        vehno = extras.getString("VehNo");
        VehNo = getIntent().getStringExtra("reg_no");
        stypeId=extras.getString("stypeId");
        Date=extras.getString("Date");
        sgtype=extras.getString("sgtype");
        smonthId=extras.getString("smonthId");
        Type = extras.getString("DType");

        getTyreCountBarChart = new GetTyreCountBarChart();
        getTyreCountBarChart.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getTyreCountBarChart.getStatus() == AsyncTask.Status.RUNNING) {
                    getTyreCountBarChart.cancel(true);
                    Toast.makeText(
                            CountBaseTyreExp.this,
                            "Response time out, please try again ....",
                            Toast.LENGTH_SHORT).show();
                    pDialog1.dismiss();

//					Toast.makeText(
//							getApplicationContext(),
//							"Connection timed out , please try again later....",
//							Toast.LENGTH_LONG).show();
                }

            }

        }, 20000);

        if (Type.equals("Tyre")) {
            setTitle("Tyre Details");
        } else {
            setTitle("Battery Details");
            txtTyreBat.setText("Battery");
            txtRemold.setText("Refurbished");

        }
        linNew.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) { if( newCount.equals(0)){
                                          Toast.makeText(getApplicationContext(),"No Data",Toast.LENGTH_SHORT).show();
                                      }else{
                                          Intent intent = new Intent(CountBaseTyreExp.this,
                                                  TyreActivity.class);
                                          intent.putExtra("stypeId",stypeId);
                                          intent.putExtra("Date",Date);
                                          intent.putExtra("sgtype",sgtype);
                                          intent.putExtra("smonthId",smonthId);
                                          intent.putExtra("reg_no", GetVehicleNo.reg_no);
                                          intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                                          intent.putExtra("DType", Type);
                                          intent.putExtra("ItemType", "New");
                                          startActivity(intent);}
                                      }
                                  }
        );
        linScrap.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent1 = new Intent(CountBaseTyreExp.this,
                                                    TyreScrapActivity.class);
                                            intent1.putExtra("stypeId",stypeId);
                                            intent1.putExtra("Date",Date);
                                            intent1.putExtra("sgtype",sgtype);
                                            intent1.putExtra("smonthId",smonthId);
                                            intent1.putExtra("reg_no", GetVehicleNo.reg_no);
                                            intent1.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                                            intent1.putExtra("DType", Type);
                                            intent1.putExtra("ItemType", "Scrap");
                                            startActivity(intent1);
                                        }
                                    }
        );
        linRemold.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Intent intent = new Intent(CountBaseTyreExp.this,
                                                     TyreActivity.class);
                                             intent.putExtra("stypeId",stypeId);
                                             intent.putExtra("Date",Date);
                                             intent.putExtra("sgtype",sgtype);
                                             intent.putExtra("smonthId",smonthId);
                                             intent.putExtra("reg_no", GetVehicleNo.reg_no);
                                             intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                                             intent.putExtra("DType", Type);
                                             intent.putExtra("ItemType", "Remold");
                                             startActivity(intent);
                                         }
                                     }
        );

       /* Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        int monthnum=5;
        cal.set(Calendar.MONTH,monthnum);
        String month_name = month_date.format(cal.getTime());

        Log.e("month",""+month_name);
        Log.e("month1",new SimpleDateFormat("MMMM").format(10/12/2017));

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("My Chart");
        chart.animateXY(2000, 2000);
        chart.invalidate();*/
    }

    public void BarChart(){


        try {

            ArrayList<BarEntry> entriesNew = new ArrayList<>();
            ArrayList<BarEntry> entriesRemold = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<String>();
            ArrayList<IBarDataSet> dataSets = null;

            for (int i = 0; i < YValuesStsNew.size(); i++)
                if(YValuesStsNew.get(i)!=0) {
                    entriesNew.add(new BarEntry(YValuesStsNew.get(i), i));
                }

            for (int i = 0; i < YValuesStsRemold.size(); i++)
                if(YValuesStsRemold.get(i)!=0) {
                    entriesRemold.add(new BarEntry(YValuesStsRemold.get(i), i));
                }
               // entriesRemold.add(new BarEntry(YValuesStsRemold.get(i), i));



       /* for (int i = 0; i < xValues.length; i++)
            xVals.add(xValues[i]);*/

            for (int i = 0; i < XValuesSts.size(); i++)
                labels.add(XValuesSts.get(i));

       /* entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));

        //dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        labels = new ArrayList<String>();
        labels.add("2016");
        labels.add("2015");
        labels.add("2014");
        labels.add("2013");
        labels.add("2012");
        labels.add("2011");*/
            try {

              //  BarDataSet dataset = new BarDataSet(entries, String.valueOf(labels));

                BarDataSet dataset = new BarDataSet(entriesNew, "New");
                // dataset.setDrawValues(true);
               // dataset.setBarSpacePercent(50f);

                ArrayList<Integer> colors = new ArrayList<Integer>();

                // Added My Own colors
                for (int c : MY_COLORS)
                    colors.add(c);


                dataset.setColor(Color.rgb(30, 0, 255));
                BarDataSet barDataSet2;
                if (Type.equals("Tyre")) {
                    barDataSet2 = new BarDataSet(entriesRemold, "Remould");
                }else {
                    barDataSet2 = new BarDataSet(entriesRemold, "Refurbished");
                }
                barDataSet2.setColor(Color.rgb(0, 162, 255));
              //  barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

                dataSets = new ArrayList<>();
                dataSets.add(dataset);
                dataSets.add(barDataSet2);

                // dataset.setColor(Color.rgb(0, 155, 0));

                BarData data = new BarData(labels, dataSets);
             //   BarData data = new BarData(labels, barDataSet2);
                data.setValueTextSize(20f);
                data.setValueFormatter(new MyValueFormatter());
                chart.setData(data);
                chart.setDescription("");
                chart.getAxisLeft().setDrawLabels(false);
                chart.animateXY(2000, 2000);
                chart.invalidate();
              /*
                data.setValueTextSize(20f);
                data.setValueTextColor(R.color.Blue);
                data.setValueTextColor(R.color.Orange);
                Legend l = chart.getLegend();

                l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
                l.setXEntrySpace(7);
                l.setYEntrySpace(5);
                chart.setData(data);
                chart.setDescription("");

                chart.getAxisLeft().setDrawLabels(false);
                chart.getAxisRight().setDrawLabels(false);
                chart.getXAxis().setDrawLabels(false);


                //  hbarchart.animateXY(1000, 3000);
                chart.invalidate();
                chart.animateXY(1400, 1400);*/

           /* l.setCustom(colors,labels);
            l.resetCustom();


            l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
            l.setXEntrySpace(7);
            l.setYEntrySpace(5);
            //l.setTypeface(mTF);*/


            }catch (Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }
    }



    public ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);
        BarEntry v1e7 = new BarEntry(106.000f, 6); // Jun
        valueSet1.add(v1e7);
        BarEntry v1e8 = new BarEntry(90.000f, 7); // Jun
        valueSet1.add(v1e8);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);
        BarEntry v2e7 = new BarEntry(40.000f, 5); // Jun
        valueSet2.add(v2e7);
        BarEntry v2e8 = new BarEntry(90.000f, 5); // Jun
        valueSet2.add(v2e8);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    public ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        xAxis.add("JUL");
        xAxis.add("AUG");
        return xAxis;
    }

    private class GetTyreCountBarChart extends AsyncTask<Void, Void, Void>  {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog1 = new ProgressDialog(CountBaseTyreExp.this);
            pDialog1.setMessage("Loading Details ..... Please Wait");
            pDialog1.setCancelable(false);
            pDialog1.show();

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
                        "api/VehAnalysis/GetVehGetTyrePartDetailCount/"+registration_no+"/"+stypeId+"/"+sgtype+"-"+smonthId+"-"+Date+"/"+Type);

                encodedUrl = tUrl.replaceAll(" ", "%20");
                    /*+"/"+ItemType*/

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
                            CountBaseTyreExp.this.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(), "Could not get any data from, please check internet "
                                            + "connection or contact administrator", Toast.LENGTH_LONG).show();

                                }
                            });
                        } else {
                            DecimalFormat formatter = new DecimalFormat("#,##,### ");
                            YValuesStsNew=new ArrayList<Integer>();
                            YValuesStsRemold=new ArrayList<Integer>();
                            XValuesSts = new ArrayList<String>();
                            for(int j=0;j<month.length;j++){
                                String mon=month[j];
                                String mon1="";
                                 int New=0;
                                int Remold=0;

                            for (int i = 0; i < tripDetail.length(); i++) {
                                JSONObject c = tripDetail.getJSONObject(i);
                                String type=c.getString("Month");

                                if(mon.equals(type))
                                {
                                    mon1=type;
                                    New=c.getInt("New");
                                    Remold=c.getInt("Remold");
                             }

                               /* XValuesSts.add(mon1);
                                YValuesStsNew.add(New);
                                YValuesStsRemold.add(Remold);*/

                            }


                                    XValuesSts.add(mon);
                                    YValuesStsNew.add(New);
                                     YValuesStsRemold.add(Remold);

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
            pDialog1.dismiss();

            BarChart();
            getTripStatus = new GetTripStatus();
            getTripStatus.execute();

        }
    }
    private class GetTripStatus extends AsyncTask<Void, Void, Void>  {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(CountBaseTyreExp.this);
                pDialog.setMessage("Loading Details ..... Please Wait");
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
                            "api/VehAnalysis/GetVehGetTyrePartDetail/"+registration_no+"/"+stypeId+"/"+sgtype+"-"+smonthId+"-"+Date+"/"+Type);

                    encodedUrl = tUrl.replaceAll(" ", "%20");
                    /*+"/"+ItemType*/

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
                                CountBaseTyreExp.this.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "Could not get any data from, please check internet "
                                                + "connection or contact administrator", Toast.LENGTH_LONG).show();

                                    }
                                });
                            } else {
                                DecimalFormat formatter = new DecimalFormat("#,##,### ");
                                for (int i = 0; i < tripDetail.length(); i++) {
                                    JSONObject c = tripDetail.getJSONObject(i);
                                    String type=c.getString("Type");
                                    if(type.equals("New")){
                                        newCount = c.getString("Count");
                                        newAmount = formatter.format(c.getInt("Amount"));
                                    }else if(type.equals("Remold")){
                                        remouldCount = c.getString("Count");
                                        remouldAmount = formatter.format(c.getInt("Amount"));
                                    }else {
                                        scrapCount = c.getString("Count");
                                        scrapAmount = formatter.format(c.getInt("Amount"));
                                    }


                                   /* scrapCount = c.getString("scrapCount");
                                    scrapAmount = c.getString("scrapAmount");*/


                                }
                                CountBaseTyreExp.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        txtNewCount.setText(newCount);
                                        txtNewAmount.setText(newAmount);
                                        txtRemoldCount.setText(remouldCount);
                                        txtRemoldAmount.setText(remouldAmount);
                                            txtScrapCount.setText(scrapCount);
                                            txtScrapAmount.setText(scrapAmount);

                                    }
                                });
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
