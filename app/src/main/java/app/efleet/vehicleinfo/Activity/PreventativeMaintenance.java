package app.efleet.vehicleinfo.Activity;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.efleet.vehicleinfo.DriverDetails;
import app.efleet.vehicleinfo.HttpCalls.ServiceHandler;
import app.efleet.vehicleinfo.R;
import app.efleet.vehicleinfo.adapters.ListViewPreventativeMaintenanceAdapter;

public class PreventativeMaintenance extends Activity {
    ListView lv;
    ListViewPreventativeMaintenanceAdapter adapter;
    String reg_no;

    String pmname,pmid,jobGCode,jobGName,lastJsDate,lastJsKM,bdgtDays,bdgtKM,PmAlert,
            dueOnDate1,dueOnKmRun,currentRunKM,nature,age;

    private static String url = "";
    public static final String TAG_PmName = "PMName";
    public static final String TAG_PmId= "PMID";
    public static final String TAG_JobGCode = "JobgroupCode";
    public static final String TAG_JobGName = "JobgroupName";
    public static final String TAG_LastJSDate = "LastJSDate";
    public static final String TAG_LastJsKM = "LastJSKM";
    public static final String TAG_BdgtDays = "BdgtDays";
    public static final String TAG_BdgtKM = "BdgtKM";
    public static final String TAG_PmAlert = "PMAlert";
    public static final String TAG_DueOnDate1 = "DueOnDate1";
    public static final String TAG_DueOnKmRun = "DueOnKmRun";
    public static final String TAG_CurrentRunKM = "CurrentRunKM";
    public static final String TAG_Nature = "Nature";
    public static final String TAG_Age = "Days";
    String imeiNumber = "";
    String clientCode = "";
    String verificationCode = "";
    String appname = "VEHICLEINFO";
    JSONArray prvntmaint = null;
    ArrayList<HashMap<String, String>> PMList;
    GetPreventativeMaintenance getPreventativeMaintenance;
    String encodedUrl = null;
    ImageView imageView;
    TextView txtNoResults,txtvehicleno;
    ProgressDialog pDialog;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preventative_maintenance);
        imageView = (ImageView)findViewById(R.id.image2);
        txtNoResults = (TextView) findViewById(R.id.textNoResults);
        txtvehicleno = (TextView) findViewById(R.id.txtVNo);
        lv=(ListView)findViewById(R.id.prevmaintlist);
        back=(ImageButton)findViewById(R.id.imgback);
        PMList = new ArrayList<HashMap<String, String>>();
      //  lv = getListView();
        reg_no = getIntent().getStringExtra("reg_no");
        txtvehicleno.setText(reg_no);
        getPreventativeMaintenance = new GetPreventativeMaintenance();
        getPreventativeMaintenance.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getPreventativeMaintenance.getStatus() == AsyncTask.Status.RUNNING) {
                    getPreventativeMaintenance.cancel(true);
                    Toast.makeText(
                            PreventativeMaintenance.this,
                            "Response time out, please try again ....",
                            Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();

//					Toast.makeText(
//							getApplicationContext(),
//							"Connection timed out , please try again later....",
//							Toast.LENGTH_LONG).show();
                }

            }

        }, 15000);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private class GetPreventativeMaintenance extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PreventativeMaintenance.this);
            pDialog.setMessage("Loading PM Details ..... Please Wait");
            pDialog.setCancelable(false);
            pDialog.show();
//			Toast.makeText(getBaseContext(),
//					"Loading Tyre Details ..... Please Wait",
//					Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // registration_no = getIntent().getStringExtra("reg_no");
            SharedPreferences sharedPref1 = getSharedPreferences("aji",
                    Context.MODE_PRIVATE);
            verificationCode = sharedPref1.getString("VARCODE", "");
            imeiNumber = sharedPref1.getString("IMEI", "");
            clientCode = sharedPref1.getString("CLIENTCODE", "");
            String URL = "http://proxy.efleetsystems.in/api/ProxyCalls/AuthrizeMe?action=VEHICLEINFO";
            URL=URL.replace(" ","");
            Log.e("codeUrl",URL.toString());
            String encodedimei = Base64.encodeToString(imeiNumber.getBytes(), Base64.DEFAULT);
            String encodedclclode = Base64.encodeToString(clientCode.getBytes(), Base64.DEFAULT);
            String appName = "VEHICLEINFO";
            String authorizationString = "Device " + new String(Base64.encode((imeiNumber + ":" + verificationCode
                    + ":" + appName).getBytes(), Base64.NO_WRAP)); // Base64.NO_WRAP flag
//			String authorizationString = "Device " + Base64.encodeToString((encodedimei + ":" + encodedclclode
//					+ ":" + appName).getBytes(), Base64.NO_WRAP); // Base64.NO_WRAP flag
            Log.e("finalbase64",authorizationString);
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.CaLLService(URL, authorizationString);

			/*url = "http://proxy2.efleetsystems.in/servicespro/api/GetClientVerify.svc/GetClientVerify/"
					+ imeiNumber + "/" + appname + "/" + clientCode;
			Log.e("DriverUrl",url);
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);*/
            Log.e("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    //String urlData = jsonObj.getString("ClientVerifyResult");
                    //JSONArray url_master = new JSONArray(urlData);
                    //for (int j = 0; j < url_master.length(); j++) {
                    //JSONObject d = url_master.getJSONObject(j);
                    String tUrl = jsonObj.getString("Url");
                    String tToken = jsonObj.getString("Token");
                    tUrl = tUrl.toString().concat(
                            "api/VehicleInfo/GetPMOverDetails/" + reg_no);
                    //+ verificationCode + "/" +clientCode+"/"+registration_no);
                    encodedUrl = tUrl.replaceAll(" ", "%20");

                    Log.e("codeUrl1", encodedUrl.toString());

                    String authorizationString1 = "Device " + new String(Base64.encode((imeiNumber + ":" + clientCode
                            + ":" + tToken).getBytes(), Base64.NO_WRAP)); // Base64.NO_WRAP flag
//			String authorizationString = "Device " + Base64.encodeToString((encodedimei + ":" + encodedclclode
//					+ ":" + appName).getBytes(), Base64.NO_WRAP); // Base64.NO_WRAP flag
                    Log.e("finalbase64", authorizationString1);
                    ServiceHandler sh1 = new ServiceHandler();
                    String jsonStr1 = sh1.CaLLService(encodedUrl, authorizationString1);
                    //String jsonStr1 = sh.makeServiceCall(encodedUrl, ServiceHandler.GET);
                    Log.e("Response1: ", "> " + jsonStr1);
                    if (jsonStr1 != null) {
                        try {
                            prvntmaint = new JSONArray(jsonStr1);
                            if (prvntmaint.length() == 0) {
                                PreventativeMaintenance.this.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "Could not get any data, please check internet "
                                                + "connection or contact administrator", Toast.LENGTH_LONG).show();

                                    }
                                });
                            } else {
                                for (int i = 0; i < prvntmaint.length(); i++) {
                                    JSONObject c = prvntmaint.getJSONObject(i);
                                    pmname = c.getString(TAG_PmName);
                                    pmid = String.valueOf(c.getInt(TAG_PmId));
                                    jobGCode = c.getString(TAG_JobGCode);
                                    jobGName = c.getString(TAG_JobGName);
                                    lastJsDate = c.getString(TAG_LastJSDate);
                                    lastJsKM = c.getString(TAG_LastJsKM);
                                    bdgtDays = String.valueOf(c.getInt(TAG_BdgtDays));
                                    bdgtKM = String.valueOf(c.getInt(TAG_BdgtKM));
                                    PmAlert = String.valueOf(c.getInt(TAG_PmAlert));
                                    dueOnDate1 = c.getString(TAG_DueOnDate1);
                                    dueOnKmRun = String.valueOf(c.getInt(TAG_DueOnKmRun));
                                    currentRunKM = String.valueOf(c.getInt(TAG_CurrentRunKM));
                                    nature = c.getString(TAG_Nature);
                                    age = String.valueOf(c.getInt(TAG_Age));
                                    //Log.e("Age>",age);


                                    HashMap<String, String> due_status = new HashMap<String, String>();

                                    due_status.put(TAG_PmName, pmname);
                                    due_status.put(TAG_PmId, pmid);
                                    due_status.put(TAG_JobGCode, jobGCode);
                                    due_status.put(TAG_JobGName, jobGName);
                                    due_status.put(TAG_LastJSDate, lastJsDate);
                                    due_status.put(TAG_LastJsKM, lastJsKM);
                                    due_status.put(TAG_BdgtDays, bdgtDays);
                                    due_status.put(TAG_BdgtKM, bdgtKM);
                                    due_status.put(TAG_PmAlert, PmAlert);
                                    due_status.put(TAG_DueOnDate1, dueOnDate1);
                                    due_status.put(TAG_DueOnKmRun, dueOnKmRun);
                                    due_status.put(TAG_CurrentRunKM, currentRunKM);
                                    due_status.put(TAG_Nature, nature);
                                    due_status.put(TAG_Age, age);


                                    PMList.add(due_status);
                                }
                                pDialog.dismiss();
                            }
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
            if (PMList.isEmpty()) {
                imageView.setVisibility(View.VISIBLE);
                txtNoResults.setVisibility(View.VISIBLE);
            }else{
                adapter = new ListViewPreventativeMaintenanceAdapter(PreventativeMaintenance.this, PMList,reg_no);
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
