package app.efleet.vehicleinfo.Activity;

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

import app.efleet.vehicleinfo.HttpCalls.ServiceHandler;
import app.efleet.vehicleinfo.R;
import app.efleet.vehicleinfo.TyreDetails;
import app.efleet.vehicleinfo.adapters.ListViewDueStatusAdapter;
import app.efleet.vehicleinfo.adapters.ListViewTyreAdapter;

public class DueStatus extends ListActivity {
    ListView lv;
    ListViewDueStatusAdapter adapter;
    String reg_no;
    String due,due_code,due_date,dueImagePath,due_days;

    private static String url = "";
    public static final String TAG_Due = "DueName";
    public static final String TAG_DueCode= "DueCode";
    public static final String TAG_Due_Date = "DueDate";
    public static final String TAG_Due_Path = "ImagePath";
    public static final String TAG_Due_Days = "Days";
    public static final String TAG_Due_VehicleId = "VehicleID";

    String imeiNumber = "";
    String clientCode = "";
    String verificationCode = "";
    String appname = "VEHICLEINFO";
    JSONArray dueStatus = null;
    ArrayList<HashMap<String, String>> dueStatusList;
    GetDueStatus getDueStatus;
    String encodedUrl = null;
    ImageView imageView;
    TextView txtNoResults;
    ProgressDialog pDialog;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_due_status);
        imageView = (ImageView) findViewById(R.id.image2);
        txtNoResults = (TextView) findViewById(R.id.textNoResults);
        back=(ImageButton)findViewById(R.id.imgback);
        dueStatusList = new ArrayList<HashMap<String, String>>();
        lv = getListView();
        reg_no = getIntent().getStringExtra("reg_no");
        getDueStatus = new  GetDueStatus();
        getDueStatus.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getDueStatus.getStatus() == AsyncTask.Status.RUNNING) {
                    getDueStatus.cancel(true);
                    Toast.makeText(
                            DueStatus.this,
                            "Response time out, please try again ....",
                            Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();

//					Toast.makeText(
//							getApplicationContext(),
//							"Connection timed out , please try again later....",
//							Toast.LENGTH_LONG).show();
                }

            }

        }, 10000);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
            });

    }

    private class GetDueStatus extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DueStatus.this);
            pDialog.setMessage("Loading Due Details ..... Please Wait");
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
                            "api/VehicleInfo/GetDuesDetails/" + reg_no);
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
                            dueStatus = new JSONArray(jsonStr1);
                            for (int i = 0; i < dueStatus.length(); i++) {
                                JSONObject c = dueStatus.getJSONObject(i);
                                due = c.getString(TAG_Due);
                                due_code = c.getString(TAG_DueCode);
                                due_date = c.getString(TAG_Due_Date);
                                due_days = c.getString(TAG_Due_Days);
                                dueImagePath = c.getString(TAG_Due_Path);

                                HashMap<String, String> due_status = new HashMap<String,String>();

                                due_status.put(TAG_Due, due);
                                due_status.put(TAG_DueCode, due_code);
                                due_status.put(TAG_Due_Date, due_date);
                                due_status.put(TAG_Due_Days, due_days);
                                due_status.put(TAG_Due_Path, dueImagePath);


                                dueStatusList.add(due_status);
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
            if (dueStatusList.isEmpty()) {
                imageView.setVisibility(View.VISIBLE);
                txtNoResults.setVisibility(View.VISIBLE);
            }else{
                adapter = new ListViewDueStatusAdapter(DueStatus.this, dueStatusList);
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
