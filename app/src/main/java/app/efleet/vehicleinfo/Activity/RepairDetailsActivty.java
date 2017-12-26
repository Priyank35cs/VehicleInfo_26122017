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
import android.view.View;
import android.widget.ImageView;
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
import app.efleet.vehicleinfo.adapters.ListViewDriverRDAccRepAdapter;
import app.efleet.vehicleinfo.adapters.RepairDetailsAdapter;

public class RepairDetailsActivty extends AppCompatActivity {
    String  DDMM, ItemName, Qty, Amount;
    String vehno,stypeId,Date,sgtype,smonthId,id;
    Cursor cursor;
    ProgressDialog pDialog;
    String imeiNumber = "";
    String clientCode = "";
    String verificationCode = "";
    String appname = "VEHICLEINFO";
    String encodedUrl = null;
    JSONArray budgetDetail = null;
    GetNRepairDetail getNRepairDetail;
    ArrayList<HashMap<String, String>> repairdetailsinfo;
    public static final String TAG_Date = "DDMM";
    public static final String TAG_Item = "ItemName";
    public static final String TAG_Qty= "Qty";
    public static final String TAG_Amt = "Amount";

    ListView lv,onwardLV;
    RepairDetailsAdapter adapter;
    String VehNo,registration_no;
    TextView txtRNextVehicleNo;
    ImageView back;

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
 //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_repair);
        txtRNextVehicleNo=(TextView)findViewById(R.id.txtRNextVehicleNo);
        lv = (ListView)findViewById(R.id.lvRepairListView);
        back=(ImageView)findViewById(R.id.imgback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        repairdetailsinfo = new ArrayList<HashMap<String, String>>();
       // lv = getListView();

        //Log.e("VehNo",VehNo);
        setTitle("Repair Exp");


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        VehNo = extras.getString("reg_no");
        stypeId=extras.getString("stypeId");
        Date=extras.getString("Date");
        sgtype=extras.getString("sgtype");
        smonthId=extras.getString("smonthId");
        id=extras.getString("JobGroupID");
        txtRNextVehicleNo.setText(VehNo);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new RepairDetailsAdapter(RepairDetailsActivty.this, repairdetailsinfo );
                lv.setAdapter(adapter);
            }
        });

        getNRepairDetail = new GetNRepairDetail();
        getNRepairDetail.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getNRepairDetail.getStatus() == AsyncTask.Status.RUNNING) {
                    getNRepairDetail.cancel(true);
                    Toast.makeText(
                            RepairDetailsActivty.this,
                            "Response time out, please try again ....",
                            Toast.LENGTH_SHORT).show();

                }

            }

        }, 20000);


    }
    private class GetNRepairDetail extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RepairDetailsActivty.this);
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
                  //  [{"DDMM":"06/03","ItemName":"Air Filter Assy TC","ItemCode":"920046","Qty":1,"Amount":1600.00}
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    String tUrl = jsonObj.getString("Url");
                    String tToken = jsonObj.getString("Token");
                    //registration_no = getIntent().getStringExtra("reg_no");
                    tUrl = tUrl.toString().concat(
                            "api/VehAnalysis/GetJobGroupDetails/"+registration_no+"/"+stypeId+"/"+sgtype+"-"+smonthId+"-"+Date+"/"+id);
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
                                DDMM = c.getString(TAG_Date);
                                ItemName=  c.getString(TAG_Item);/*String.valueOf(c.getInt(TAG_Item));*/
                                Qty=  c.getString(TAG_Qty) ;
                                Amount= formatter.format(c.getInt(TAG_Amt));

                                HashMap<String, String> Budget_details = new HashMap<String, String>();

                                Budget_details.put(TAG_Date, DDMM);
                                Budget_details.put(TAG_Item, ItemName);
                                Budget_details.put(TAG_Qty, Qty);
                                Budget_details.put(TAG_Amt, Amount);


                                repairdetailsinfo.add(Budget_details);
                                Log.e("data",repairdetailsinfo.toString());
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

            if (repairdetailsinfo.isEmpty()) {
                    /*imageView.setVisibility(View.VISIBLE);
                    txtNoResults.setVisibility(View.VISIBLE);*/
            } else {
                adapter = new RepairDetailsAdapter(RepairDetailsActivty.this, repairdetailsinfo );
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
