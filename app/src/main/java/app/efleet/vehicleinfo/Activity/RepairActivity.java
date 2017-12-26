package app.efleet.vehicleinfo.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import app.efleet.vehicleinfo.GetVehicleNo;
import app.efleet.vehicleinfo.HttpCalls.ServiceHandler;
import app.efleet.vehicleinfo.R;
import app.efleet.vehicleinfo.adapters.RepairActivityAdapter;

import static java.lang.Math.round;

public class RepairActivity extends AppCompatActivity{
    LinearLayout linJobGroup, linListView;
    TextView txtRepairVehNo;
    String  jobGroup, JobGroupPer, qty, amt;
    String vehno,Type,Itemtype;
    Cursor cursor;
    ProgressDialog pDialog;
    String imeiNumber = "";
    String clientCode = "";
    String verificationCode = "";
    String appname = "VEHICLEINFO";
    String encodedUrl = null;
    JSONArray budgetDetail = null;
    GetNRepairDetail getNRepairDetail;
    public static String JobGroupID;
    ArrayList<HashMap<String, String>> repairdetailsinfo;
    public static final String TAG_JobGroup = "JobGroup";
    public static final String TAG_JobGroupPer = "TotalRepairCostPer";
    public static final String TAG_JobAmt = "Amount";
    public static final String TAG_JobGroupID= "JobGroupID";
    public static String ItemType, stypeId,Date,sgtype,smonthId;

    ListView lv,onwardLV;
    RepairActivityAdapter adapter;
    public static   String registration_no,  VehNo;
    TextView txtRNextVehicleNo;

    ArrayList<Integer>YValues,YValuesSts;
    ArrayList<String> XValues,XValuesSts ;
    public static  final int[] MY_COLORS = {
            Color.rgb(119,119,255), Color.rgb(255,94,0), Color.rgb(255,217,0),
            Color.rgb(95,204,95), Color.rgb(239,51,64),Color.rgb(52, 239, 226)
    };
    PieChart mChart;
    TextView total;
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);
        linJobGroup = (LinearLayout) findViewById(R.id.linJobGroup);
        txtRepairVehNo = (TextView) findViewById(R.id.txtRepairVehNo);
        lv=(ListView)findViewById(R.id.jobGroupListView);
        total=(TextView)findViewById(R.id.texttotal);
        mChart = (PieChart) findViewById(R.id.chart1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         setTitle("Repair Exp");
        repairdetailsinfo = new ArrayList<HashMap<String, String>>();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        vehno = extras.getString("reg_no");
        stypeId=extras.getString("stypeId");
        Date=extras.getString("Date");
        sgtype=extras.getString("sgtype");
        smonthId=extras.getString("smonthId");
        // Repair = extras.getString("Repair");
        txtRepairVehNo.setText(vehno);
        getNRepairDetail = new GetNRepairDetail();
        getNRepairDetail.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getNRepairDetail.getStatus() == AsyncTask.Status.RUNNING) {
                    getNRepairDetail.cancel(true);
                    Toast.makeText(
                            RepairActivity.this,
                            "Response time out, please try again ....",
                            Toast.LENGTH_SHORT).show();

                }

            }

        }, 20000);


        mChart.setDescription("");
        mChart.setRotationEnabled(true);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;
                /*   Toast.makeText(PieChartActivity.this,
                        xValues[e.getXIndex()] + " is " + e.getVal() + "", Toast.LENGTH_SHORT).show();*/

                Toast.makeText(RepairActivity.this,
                        XValues.get(e.getXIndex()) + " is " + e.getVal() + "", Toast.LENGTH_SHORT).show();
                HashMap<String, String> resultp = new HashMap<String, String>();
                resultp=repairdetailsinfo.get(e.getXIndex());
                String jobId=resultp.get(TAG_JobGroupID);
                Log.e("JobId",jobId);
                if( !XValues.get(e.getXIndex()).equals("Other")) {
                    Intent intent = new Intent(RepairActivity.this, RepairDetailsActivty.class);
                    intent.putExtra("stypeId", stypeId);
                    intent.putExtra("Date", Date);
                    intent.putExtra("sgtype", sgtype);
                    intent.putExtra("smonthId", smonthId);
                    intent.putExtra("JobGroupID",jobId);
                    intent.putExtra("reg_no", GetVehicleNo.reg_no);
                    startActivity(intent);
                }



            }

            @Override
            public void onNothingSelected() {

            }
        });

    }

    public void setDataForPieChart() {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

       /* for (int i = 0; i < yValues.length; i++)
            yVals1.add(new Entry(yValues[i], i));*/
        try {
            for (int i = 0; i < YValues.size(); i++)
                yVals1.add(new Entry(YValues.get(i), i));



       /* for (int i = 0; i < xValues.length; i++)
            xVals.add(xValues[i]);*/

            for (int i = 0; i < XValues.size(); i++)
                xVals.add(XValues.get(i));
        }catch(Exception e){
            e.printStackTrace();
        }

        // create pieDataSet
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // adding colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        // Added My Own colors
       /* try{
            for (int i = 0; i < XValues.size(); i++) {
                String xvalue=XValues.get(i);
                if(xvalue.contains("Repair")){
                    colors.add(MY_COLORS[0]);
                }else if (xvalue.contains("Unload")){
                    colors.add(MY_COLORS[1]);
                }else if (xvalue.contains("Loading")){
                    colors.add(MY_COLORS[2]);
                }else if (xvalue.contains("Enroute")){
                    colors.add(MY_COLORS[3]);
                }else {
                    colors.add(MY_COLORS[4]);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/
        for (int c : MY_COLORS)
            colors.add(c);


        dataSet.setColors(colors);

        //  create pie data object and set xValues and yValues and set it to the pieChart
        PieData data = new PieData(xVals, dataSet);
        //   data.setValueFormatter(new DefaultValueFormatter());
        //   data.setValueFormatter(new PercentFormatter());

        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.WHITE);

        Legend l = mChart.getLegend();
        l.setMaxSizePercent(0.9f);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setWordWrapEnabled(true);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // refresh/update pie chart
        mChart.invalidate();

        // animate piechart
        mChart.animateXY(1400, 1400);


        // Legends to show on bottom of the graph

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


    private class GetNRepairDetail extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RepairActivity.this);
            pDialog.setMessage("Loading Repair Expense ..... Please Wait");
            pDialog.setCancelable(false);
            pDialog.show();
           /* Toast.makeText(getBaseContext(),"Loading Trip Gen Expences  Details..... Please Wait",
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
                            "api/VehAnalysis/GetRepairCostDetails/" +registration_no+"/"+stypeId+"/"+sgtype+"-"+smonthId+"-"+Date);
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
                            YValues=new ArrayList<Integer>();
                            XValues = new ArrayList<String>();
                            int count=0;
                            budgetDetail = new JSONArray(jsonStr1);
                            for (int i = 0; i < budgetDetail.length(); i++) {
                                JSONObject c = budgetDetail.getJSONObject(i);
                                jobGroup = c.getString(TAG_JobGroup);
                                JobGroupPer= String.valueOf(c.getInt(TAG_JobGroupPer));
                                amt= formatter.format(c.getInt(TAG_JobAmt));
                                JobGroupID=c.getString(TAG_JobGroupID);
                                HashMap<String, String> Budget_details = new HashMap<String, String>();

                                Budget_details.put(TAG_JobGroup, jobGroup);
                                Budget_details.put(TAG_JobGroupPer, JobGroupPer);
                                Budget_details.put(TAG_JobAmt, amt);
                                Budget_details.put(TAG_JobGroupID, JobGroupID);


                                repairdetailsinfo.add(Budget_details);
                                Log.e("data",repairdetailsinfo.toString());
                                if(i<5) {
                                    XValues.add(c.getString(TAG_JobGroup));
                                    YValues.add(Integer.parseInt(String.valueOf(round(c.getDouble(TAG_JobAmt)))));

                                }else {

                                    count =count+Integer.parseInt(String.valueOf(round(c.getDouble(TAG_JobAmt))));
                                }
                            }
                            XValues.add("Other");
                            YValues.add(count);
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
            DecimalFormat formatter = new DecimalFormat("#,##,### ");

            try {
                int Sum = 0;

                for (int i = 0; i < YValues.size(); i++) {
                    Sum += YValues.get(i);
                }
                total.setText(formatter.format(Sum));
            }catch(Exception e){
                e.printStackTrace();
            }

            setDataForPieChart();

            if (repairdetailsinfo.isEmpty()) {
               /* imageView.setVisibility(View.VISIBLE);
                txtNoResults.setVisibility(View.VISIBLE);*/
                Toast.makeText(RepairActivity.this, "Record is null...", Toast.LENGTH_SHORT).show();
            }else{
                adapter = new RepairActivityAdapter(RepairActivity.this, repairdetailsinfo);
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