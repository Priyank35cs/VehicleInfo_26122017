package app.efleet.vehicleinfo.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Handler;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import app.efleet.vehicleinfo.GetVehicleNo;
import app.efleet.vehicleinfo.HttpCalls.ServiceHandler;
import app.efleet.vehicleinfo.R;
import app.efleet.vehicleinfo.Session.SessionManager;

public class PeriodicPerformanceActivity extends AppCompatActivity{
    LinearLayout lirLayout, linDriver, linTyre, linBattery, linRepair,linDues, linGenExp, linEMI, linAccidentalRepair;
    RadioButton Year, Month;
    RadioGroup groupRadioBtn;
    ImageView btnshow;
    Spinner SYear, SMonth;
    TextView txtPTripVehicleNo,txtTotalDriverExp,txtPerKMDriverExp, txtDays, txtpriodicActivityKM, txtTotalFreight, txtPerKMFreight, txtTotalLessTripExp, txtPerKMLessTripExp, txtTotalFuelExp,
            txtPerKMFuelExp, txtTotalETollExp, txtPerKMETollExp, txtTotalOther, txtPerKMOther, txtTotalGMargin, txtPerKMGMargin, txtTotalOE, txtTyre, txtBattery, txtRepair, txtDues, txtDriver, txtGenExp, txtNetMargin, txtLCOF, txtEMI, txtAccidentalRepair, txtNCOF;
    TextView perLess,pertyre,perbattery,perRepair,perdue,perDriver,perGenExp,perNetMar,perLCof,perEmi,perAR,perNcof;
    String VehNo,registration_no, Days, KM, TotalFreight, PerKMFreight, TotalLessTripExp, PerKMLessTripExp, TotalFuelExp, PerKMFuelExp, TotalETollExp, PerKMETollExp,
            TotalOther, PerKMOther, TotalGMargin, PerKMGMargin, TotalOE, Tyre, Battery, Repair, Dues, Driver, GenExp, NetMargin, LCOF, EMI, AccidentalRepair, NCOF;
    String  sperLess,spertyre,sperbattery,sperRepair,sperdue,sperDriver,sperGenExp,sperNetMar,sperLCof,sperEmi,sperAR,sperNcof;
    LinearLayout linLayout1, linearLayout2;
    String imeiNumber = "", vehno;
    String clientCode = "";
    String verificationCode = "";
    GetTripPerformanceStatus getTripPerformanceStatus;
    JSONArray tripDetail = new JSONArray();
    ProgressDialog pDialog;
    String encodedUrl = null;
    ArrayAdapter adapter;
    SessionManager session;
    public static String type;
    String sgtype="2017",sgmtype;
    String smonth,smonthId="01",stypeId ,Date="01";
    String LVTExp,KMDriverExp,TotalDriverExp;
    TextView txtPercLesstripExp,txtPercFuelExp,txtPercDrivExp,txtPercOthrExp,txtPercTollExp,txtPercGM;
    String PercLesstripExp,PercFuelExp,PercTollExp,PercDrivExp,PercOthrExp,PercGM;
   double intPercLesstripExp,intPercFuelExp;
    ArrayList<String> XValuesSts=new ArrayList<String>();
   // ArrayList<Integer>YValuesStsNew;
   // ArrayList<Integer>YValuesStsRemold;
    HorizontalBarChart chart;
    public static  final int[] MY_COLORS = {
            Color.rgb(119,119,255), Color.rgb(255,94,0), Color.rgb(255,217,0),
            Color.rgb(95,204,95), Color.rgb(239,51,64) ,Color.rgb(52, 239, 226)
    };
    TextView txtyrid;

    JSONObject c;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periodic_performance);
        session = new SessionManager(PeriodicPerformanceActivity.this);
        txtPTripVehicleNo = (TextView) findViewById(R.id.txtPTripVehicleNo);
        txtDays = (TextView) findViewById(R.id.txtDays);
        SYear = (Spinner) findViewById(R.id.spinYear);
        SMonth = (Spinner) findViewById(R.id.spinMonth);

        txtpriodicActivityKM = (TextView) findViewById(R.id.txtpriodicActivityKM);
        txtTotalFreight = (TextView) findViewById(R.id.txtTotalFreight);
        txtPerKMFreight = (TextView) findViewById(R.id.txtPerKMFreight);
        txtTotalLessTripExp = (TextView) findViewById(R.id.txtTotalLessTripExp);
        txtPerKMLessTripExp = (TextView) findViewById(R.id.txtPerKMLessTripExp);
        txtTotalFuelExp = (TextView) findViewById(R.id.txtTotalFuelExp);
        txtPerKMFuelExp = (TextView) findViewById(R.id.txtPerKMFuelExp);
        txtTotalETollExp = (TextView) findViewById(R.id.txtTotalETollExp);
      /*  /////*/
        txtPerKMETollExp = (TextView) findViewById(R.id.txtPerKMETollExp);
        txtTotalDriverExp=(TextView) findViewById(R.id.txtTotalDriverExp);
       /* \\\\\*/
        txtPerKMDriverExp=(TextView) findViewById(R.id.txtPerKMDriverExp);
        txtTotalOther = (TextView) findViewById(R.id.txtTotalOther);
        txtPerKMOther = (TextView) findViewById(R.id.txtPerKMOther);
        txtTotalGMargin = (TextView) findViewById(R.id.txtTotalGMargin);
        txtPerKMGMargin = (TextView) findViewById(R.id.txtPerKMGMargin);
        txtTotalOE = (TextView) findViewById(R.id.txtTotalOE);
        txtTyre = (TextView) findViewById(R.id.txtTyre);
        txtBattery = (TextView) findViewById(R.id.txtBattery);
        txtRepair = (TextView) findViewById(R.id.txtRepair);
        txtDues = (TextView) findViewById(R.id.txtDues);
        txtDriver = (TextView) findViewById(R.id.txtDriver);
        txtGenExp = (TextView) findViewById(R.id.txtGenExp);
        txtNetMargin = (TextView) findViewById(R.id.txtNetMargin);
        txtLCOF = (TextView) findViewById(R.id.txtLCOF);
        txtEMI = (TextView) findViewById(R.id.txtEMI);
        txtAccidentalRepair = (TextView) findViewById( R.id.txtAccidentalRepair);
        txtNCOF = (TextView) findViewById(R.id.txtNCF);
        lirLayout = (LinearLayout) findViewById(R.id.linLayout);
        linDriver = (LinearLayout) findViewById(R.id.linDriver);
        linTyre = (LinearLayout) findViewById(R.id.linTyre);
        linBattery = (LinearLayout) findViewById(R.id.linBattery);
        linRepair = (LinearLayout) findViewById(R.id.linRepair);

        linDues = (LinearLayout) findViewById(R.id.linDues);
        linGenExp = (LinearLayout) findViewById(R.id.linGenExp);
        linEMI = (LinearLayout) findViewById(R.id.linEMI);

        linAccidentalRepair = (LinearLayout) findViewById(R.id.linAccidentalRepair);
       //linLayout1 = (LinearLayout) findViewById(R.id.linRadioSelectionYear);
        linearLayout2 = (LinearLayout) findViewById(R.id.linRadioSelectionMonth);

        txtPercLesstripExp=(TextView)findViewById(R.id.txtPercentLessTripExp);
        txtPercFuelExp=(TextView)findViewById(R.id.txtPercentFuelExp);
        txtPercTollExp=(TextView)findViewById(R.id.txtPercentETollExp);
        txtPercDrivExp=(TextView)findViewById(R.id.txtPercentDriverExp);
        txtPercOthrExp=(TextView)findViewById(R.id.txtPercentOther);
        txtPercGM=(TextView)findViewById(R.id.txtPercentGMargin);
        perLess= (TextView) findViewById(R.id.perLess);
        pertyre= (TextView) findViewById(R.id.pertyre);
        perbattery= (TextView) findViewById(R.id.perbattery);
        perRepair= (TextView) findViewById(R.id.perRepair);
        perdue= (TextView) findViewById(R.id.perdue);
        perDriver= (TextView) findViewById(R.id.perDriver);
        perGenExp= (TextView) findViewById(R.id.perGenExp);
        perNetMar= (TextView) findViewById(R.id.perNetMar);
        perLCof= (TextView) findViewById(R.id.perLCof);
        perEmi= (TextView) findViewById(R.id.perEmi);
        perAR= (TextView) findViewById(R.id.perAR);
        perNcof= (TextView) findViewById(R.id.perNcof);

        chart = (HorizontalBarChart) findViewById(R.id.chart);
        Year = (RadioButton) findViewById(R.id.rbYearly);
        Month = (RadioButton) findViewById(R.id.rbMonthly);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


      //  txtyrid=(TextView)findViewById(R.id.txtyrid);
        getTripPerformanceStatus = new GetTripPerformanceStatus();

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        vehno = extras.getString("reg_no");

        txtPTripVehicleNo.setText(vehno);

        new GetTripPerformanceStatus().execute();
       /* String*/ smonth=session.GetMonth();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (getTripPerformanceStatus.getStatus() == AsyncTask.Status.RUNNING) {
                    getTripPerformanceStatus.cancel(true);
                    Toast.makeText(
                            PeriodicPerformanceActivity.this,
                            "Response timed out , please try again ....",
                            Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }

            }

        }, 10000);





        ArrayList<String> month = new ArrayList<String>();
        String[] arr={"Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
        for (int i =0; i<arr.length; i++) {
            month.add(arr[i]);
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, month);
        SMonth.setAdapter(adapter);

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = thisYear; i > 2005; i--) {
            years.add(Integer.toString(i));
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        SYear.setAdapter(adapter);

        if(Year.isChecked())
        {
            stypeId="1";

        }
        else{
            stypeId="2";
        }


        btnshow = (ImageView) findViewById(R.id.btnShow);
        groupRadioBtn = (RadioGroup) findViewById(R.id.radioGroupButton);
        if (Year.isChecked()) {
           /* stypeId="1";*/
            SMonth.setVisibility(View.GONE);
           /* SYear.setOnItemSelectedListener(new   AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {

                    sgtype =  SYear.getSelectedItem().toString();
                    Log.e("Select",sgtype);
                    Toast.makeText(PeriodicPerformanceActivity.this, sgtype,Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }

            });*/
        }
        //group Year.se
        SYear.setOnItemSelectedListener(new   AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {

                sgtype =  SYear.getSelectedItem().toString();
                Log.e("Select",sgtype);
                Toast.makeText(PeriodicPerformanceActivity.this, sgtype,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        SMonth.setOnItemSelectedListener(new   AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                sgmtype =  SMonth.getSelectedItem().toString();



                if(sgmtype.equals("Jan")){
                    smonthId="01";
                }else if(sgmtype.equals("Feb")){
                    smonthId="02";
                }else if(sgmtype.equals("Mar")){
                    smonthId="03";
                }else if(sgmtype.equals("Apr")){
                    smonthId="04";
                }else if(sgmtype.equals("May")){
                    smonthId="05";
                }else if(sgmtype.equals("June")){
                    smonthId="06";
                }else if(sgmtype.equals("July")){
                    smonthId="07";
                }else if(sgmtype.equals("Aug")){
                    smonthId="08";
                }else if(sgmtype.equals("Sep")){
                    smonthId="09";
                }else if(sgmtype.equals("Oct")){
                    smonthId="10";
                }else if(sgmtype.equals("Nov")){
                    smonthId="11";
                }else if(sgmtype.equals("Dec"))  {
                    smonthId="12";
                }
                else{

                    Toast.makeText(PeriodicPerformanceActivity.this, sgmtype,Toast.LENGTH_SHORT).show();
                }
                Log.e("Select",sgmtype);


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });

        groupRadioBtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbYearly) {
                    SMonth.setVisibility(View.GONE);
                    /*SYear.setOnItemSelectedListener(new   AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                   int position, long arg3) {

                            sgtype =  SYear.getSelectedItem().toString();
                            Log.e("Select",sgtype);
                            Toast.makeText(PeriodicPerformanceActivity.this, sgtype,Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }

                    });*/

                } else if (checkedId == R.id.rbMonthly) {
                    session.Savepreferences("Type", String.valueOf(Month));
                    SMonth.setVisibility(View.VISIBLE);
                   /* SYear.setOnItemSelectedListener(new   AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                   int position, long arg3) {

                            sgtype =  SYear.getSelectedItem().toString();
                            Log.e("Select",sgtype);
                            Toast.makeText(PeriodicPerformanceActivity.this, sgtype,Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }

                    });*/

                }
                else{
                    Toast.makeText(PeriodicPerformanceActivity.this,"Select Radio Button",Toast.LENGTH_SHORT).show();
                }


            }

        });


        btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.Savepreferences("VEHICLINO", txtPTripVehicleNo.getText().toString());
               /* session.Savepreferences("GType",sgtype);*/
                //session.Savepreferences("Smonth",String.valueOf(smonthId));
                if(Year.isChecked())
                {
                    stypeId="1";
                    smonthId="01";
                }
                else {
                    stypeId = "2";
                }


                new GetTripPerformanceStatus().execute();

            }
        });


        linTyre.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {


                    if( c.getInt("TyreExp")==0){
                        Toast.makeText(getApplicationContext(),"No Data",Toast.LENGTH_SHORT).show();
                    }else{

                        Intent intent = new Intent(PeriodicPerformanceActivity.this, CountBaseTyreExp.class);
                        intent.putExtra("stypeId",stypeId );
                        intent.putExtra("Date",Date );
                        intent.putExtra("sgtype",sgtype );
                        intent.putExtra("smonthId",smonthId);
                        intent.putExtra("DType", "Tyre");
                        intent.putExtra("reg_no", GetVehicleNo.reg_no);
                        intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);

                        startActivity(intent);
                    }
                    }catch(Exception e){
                        e.printStackTrace();
                    }

            }
        });
        linBattery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {


                    if (c.getInt("BatteryExp") == 0) {
                        Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                    } else {

                        Intent intent = new Intent(PeriodicPerformanceActivity.this, CountBaseTyreExp.class);
                        intent.putExtra("stypeId", stypeId);
                        intent.putExtra("Date", Date);
                        intent.putExtra("sgtype", sgtype);
                        intent.putExtra("smonthId", smonthId);
                        intent.putExtra("DType", "Battery");
                        intent.putExtra("reg_no", GetVehicleNo.reg_no);
                        intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        lirLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PeriodicPerformanceActivity.this, TripPerformanceListActivity.class);
                intent.putExtra("stypeId",stypeId );
                intent.putExtra("Date",Date );
                intent.putExtra("sgtype",sgtype );
                intent.putExtra("smonthId",smonthId);
             /*   intent.putExtra("LVTExp","LVTExp" );*/
                intent.putExtra("reg_no", GetVehicleNo.reg_no);
                intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                startActivity(intent);
                }
                
        });
        linDriver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {


                    if (c.getInt ("DriverExp")== 0) {
                        Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                    } else {
                Intent intent = new Intent(PeriodicPerformanceActivity.this, DriverExpanceActivity.class);
                intent.putExtra("Driver","Driver" );
                intent.putExtra("stypeId",stypeId );
                intent.putExtra("Date",Date );
                intent.putExtra("sgtype",sgtype );
                intent.putExtra("smonthId",smonthId);
                intent.putExtra("reg_no", GetVehicleNo.reg_no);
                intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                startActivity(intent);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        linRepair.setOnClickListener(  new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {


                    if (c.getInt ("RepairExp")== 0) {
                        Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                    } else {
                Intent intent = new Intent(PeriodicPerformanceActivity.this, RepairActivity.class);
                intent.putExtra("stypeId",stypeId );
                intent.putExtra("Date",Date );
                intent.putExtra("sgtype",sgtype );
                intent.putExtra("smonthId",smonthId);
                intent.putExtra("Repair","Repair" );
                intent.putExtra("reg_no", GetVehicleNo.reg_no);
                intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                startActivity(intent);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        linDues.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {


                    if (c.getInt ("DueExp")== 0) {
                        Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                    } else {
                Intent intent = new Intent(PeriodicPerformanceActivity.this, DuesActivity.class);
                intent.putExtra("Key","Dues" );
                intent.putExtra("stypeId",stypeId );
                intent.putExtra("Date",Date );
                intent.putExtra("sgtype",sgtype );
                intent.putExtra("smonthId",smonthId);
                intent.putExtra("reg_no", GetVehicleNo.reg_no);
                intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                startActivity(intent);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        linGenExp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {


                    if (c.getInt ("GeneralExp")== 0) {
                        Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                    } else {
                Intent intent = new Intent(PeriodicPerformanceActivity.this, DuesActivity.class);
                intent.putExtra("Key","GenExp" );
                intent.putExtra("stypeId",stypeId );
                intent.putExtra("Date",Date );
                intent.putExtra("sgtype",sgtype );
                intent.putExtra("smonthId",smonthId);
                intent.putExtra("reg_no", GetVehicleNo.reg_no);
                intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                startActivity(intent);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        linEMI.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (c.getInt ("EMI")== 0) {
                        Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                    } else {
                Intent intent = new Intent(PeriodicPerformanceActivity.this, DuesActivity.class);
                intent.putExtra("Key","EMI" );
                intent.putExtra("stypeId",stypeId );
                intent.putExtra("Date",Date );
                intent.putExtra("sgtype",sgtype );
                intent.putExtra("smonthId",smonthId);
                intent.putExtra("reg_no", GetVehicleNo.reg_no);
                intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                startActivity(intent);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        linAccidentalRepair.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (c.getInt ("RAccicedental")== 0) {
                        Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
                    } else {
                Intent intent = new Intent(PeriodicPerformanceActivity.this, AccidentalRepairActivity.class);
                intent.putExtra("AR","AR" );
                intent.putExtra("stypeId",stypeId );
                intent.putExtra("Date",Date );
                intent.putExtra("sgtype",sgtype );
                intent.putExtra("smonthId",smonthId);
                intent.putExtra("reg_no", GetVehicleNo.reg_no);
                intent.putExtra("vehicle_id", GetVehicleNo.vehicle_id);
                startActivity(intent);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void BarChart() {


        try {

            ArrayList<BarEntry> entries = new ArrayList<>();
            ArrayList<BarEntry> entriesRemold = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<String>();
            ArrayList<IBarDataSet> dataSets = null;

           /*  int i1=Integer.parseInt(spertyre);
            int i2=Integer.parseInt(sperbattery);
            int i3=Integer.parseInt(sperRepair);
            int i4=Integer.parseInt(sperdue);
            int i5=Integer.parseInt(sperDriver);
            int i6=Integer.parseInt(sperGenExp);*/




            /*entries.add(0,new BarEntry(c.getInt ("TyreExp"), 5));
            entries.add(new BarEntry(c.getInt ("BatteryExp"), 4));
            entries.add(2,new BarEntry(c.getInt ("RepairExp"), 3));
            entries.add(3,new BarEntry(c.getInt ("DueExp"), 2));
            entries.add(4,new BarEntry(c.getInt ("DriverExp"), 1));
            entries.add(5,new BarEntry(c.getInt ("GeneralExp"), 0));*/


          /*  if(c.getInt ("TyreExp")!=0) {
                entries.add(new BarEntry(c.getInt ("TyreExp"), 5));
            }
            if(c.getInt ("BatteryExp")!=0) {
                entries.add(new BarEntry(c.getInt ("BatteryExp"), 4));
            }
            if(c.getInt ("RepairExp")!=0) {
                entries.add(new BarEntry(c.getInt ("RepairExp"), 3));
            }

            if(c.getInt ("DueExp")!=0) {
                entries.add(new BarEntry(c.getInt ("DueExp"), 2));
            }

            if(c.getInt ("DriverExp")!=0) {
                entries.add(new BarEntry(c.getInt ("DriverExp"), 1));
            }

            if(c.getInt ("GeneralExp")!=0) {
                entries.add(new BarEntry(c.getInt ("GeneralExp"), 0));
            }*/

          /*  if(Float.parseFloat(spertyre)!=0) {
                entries.add(new BarEntry(Float.parseFloat(spertyre), 5));
            }
            if(Float.parseFloat(sperbattery)!=0) {
                entries.add(new BarEntry(Float.parseFloat(sperbattery), 4));
            }
                if(Float.parseFloat(sperRepair)!=0) {
                    entries.add(new BarEntry(Float.parseFloat(sperRepair), 3));
                }

                if(Float.parseFloat(sperdue)!=0) {
                    entries.add(new BarEntry(Float.parseFloat(sperdue), 2));
                }

                if(Float.parseFloat(sperDriver)!=0) {
                    entries.add(new BarEntry(Float.parseFloat(sperDriver), 1));
                }

                if(Float.parseFloat(sperGenExp)!=0) {
                    entries.add(new BarEntry(Float.parseFloat(sperGenExp), 0));
                }
*/

            entries.add(new BarEntry(Float.parseFloat(spertyre), 5));
            entries.add(new BarEntry(Float.parseFloat(sperbattery), 4));
            entries.add(new BarEntry(Float.parseFloat(sperRepair), 3));
            entries.add(new BarEntry(Float.parseFloat(sperdue), 2));
            entries.add(new BarEntry(Float.parseFloat(sperDriver), 1));
            entries.add(new BarEntry(Float.parseFloat(sperGenExp), 0));

            // entriesRemold.add(new BarEntry(YValuesStsRemold.get(i), i));



       /* for (int i = 0; i < xValues.length; i++)
            xVals.add(xValues[i]);*/


            labels.add("Tyre");
            labels.add("Battery");
            labels.add("Repair");
            labels.add("Dues");
            labels.add("Driver");
            labels.add("GenExp");

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

                //  BarDataSet dataset = new BarDataSet(entries, String.valueOf(labels)String.valueOf(labels));

                BarDataSet dataset = new BarDataSet(entries,"");
                // dataset.setDrawValues(true);
                dataset.setBarSpacePercent(50f);

                ArrayList<Integer> colors = new ArrayList<Integer>();

                // Added My Own colors
                for (int c : MY_COLORS)
                    colors.add(c);


                dataset.setColors(colors);

              /*  BarDataSet barDataSet2 = new BarDataSet(entriesRemold, "Remold");
                barDataSet2.setColor(Color.rgb(0, 162, 255));
                //  barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

                dataSets = new ArrayList<>();
                dataSets.add(dataset);
                dataSets.add(barDataSet2);*/

                // dataset.setColor(Color.rgb(0, 155, 0));

                BarData data = new BarData(labels, dataset);
                //   BarData data = new BarData(labels, barDataSet2);
               /* data.setValueTextSize(20f);
                // data.setValueFormatter(new PeriodicPerformanceActivity().MyValueFormatter());
                chart.setData(data);
                chart.setDescription("");
                chart.getAxisLeft().setDrawLabels(false);
                chart.animateXY(500, 500);
                chart.invalidate();*/
               // data.setValueTextSize(20f);

               /* data.setValueTextColor(R.color.Blue);
                data.setValueTextColor(R.color.Orange);
                Legend l = chart.getLegend();
               // l.setMaxSizePercent(0.9f);
               // l.setForm(Legend.LegendForm.CIRCLE);
                 l.setWordWrapEnabled(true);
                l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
             //   l.setWordWrapEnabled(true);
                l.isWordWrapEnabled();
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


                data.setValueTextSize(10f);
                Legend l = chart.getLegend();
            l.setCustom(colors,labels);
           // l.resetCustom();

          l.setWordWrapEnabled(true);
            l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
            l.setXEntrySpace(7);
            l.setYEntrySpace(5);

             //   data.setValueFormatter(new PeriodicPerformanceActivity.MyValueFormatter());
                chart.setData(data);
                chart.animateXY(1400, 1400);
                chart.setDescription("");

                chart.getAxisLeft().setDrawLabels(false);
                chart.getAxisRight().setDrawLabels(false);
                chart.getXAxis().setDrawLabels(false);

            //l.setTypeface(mTF);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public class MyValueFormatter implements ValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("##,##,##0"); // use one decimal if needed
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + ""; // e.g. append a dollar-sign
        }
    }

    private class GetTripPerformanceStatus extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PeriodicPerformanceActivity.this);
            pDialog.setMessage("Loading Trip Performance ..... Please Wait");
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
            URL = URL.replace(" ", "%20");
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
                            "api/VehAnalysis/GetVehSummary/" +registration_no+"/"+stypeId+"/"+sgtype+"-"+smonthId+"-"+Date);
                    /* String sd=tUrl+type;*/
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
                                PeriodicPerformanceActivity.this.runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(getBaseContext(), "Could not get any data from, please check internet "
                                                + "connection or contact administrator", Toast.LENGTH_LONG).show();

                                    }
                                });
                            } else {
                                /* for (int i = 0; i < tripDetail.length(); i++) {*/
                                       c = tripDetail.getJSONObject(0);
                                     DecimalFormat formatter = new DecimalFormat("#,##,### ");
                                     Days = String.valueOf(c.getInt ("Days"));
                                     KM = String.valueOf(c.getInt ("KmRun"));
                                     TotalFreight =formatter.format(c.getInt("Freight"));
                                     PerKMFreight = c.getString("FrtPerKm");
                                     TotalLessTripExp =formatter.format(c.getInt ("LessTripExp"));
                                     PerKMLessTripExp = c.getString("LessTripExpPerKm");
                                     TotalFuelExp =formatter.format(c.getInt ("FuelExp"));
                                     PerKMFuelExp = c.getString ("FuelExpPerKm");
                                     TotalETollExp =formatter.format(c.getInt ("ETollExp"));
                                     PerKMETollExp =c.getString ("ETollPerKm");

                                     KMDriverExp = c.getString ("DriverPerKm") ;
                                     TotalDriverExp = formatter.format(c.getInt ("DriverTripExp"));


                                     TotalOther = formatter.format(c.getInt ("OtherExp"));
                                     PerKMOther =  c.getString ("OthersperKm");
                                     TotalGMargin =formatter.format(c.getInt ("GrossMargin"));
                                     PerKMGMargin =  c.getString ("GrossMarginPerKm") ;
                                     TotalOE =formatter.format(c.getInt ("TyreExp")+c.getInt ("BatteryExp")+c.getInt ("RepairExp")+c.getInt ("DueExp")+c.getInt ("DriverExp")+c.getInt ("GeneralExp"));
                                     Tyre =formatter.format(c.getInt ("TyreExp"));
                                     Battery =formatter.format(c.getInt ("BatteryExp"));
                                     Repair =formatter.format(c.getInt ("RepairExp"));
                                     Dues = formatter.format(c.getInt ("DueExp"));
                                     Driver =formatter.format(c.getInt ("DriverExp"));
                                     GenExp = formatter.format(c.getInt ("GeneralExp"));
                                     NetMargin =formatter.format( c.getInt ("GrossMargin")-(c.getInt ("TyreExp")+c.getInt ("BatteryExp")+c.getInt ("RepairExp")+c.getInt ("DueExp")+c.getInt ("DriverExp")+c.getInt ("GeneralExp")));
                                     LCOF=formatter.format(c.getInt("RAccicedental")+c.getInt ("EMI"));
                                     EMI = formatter.format(c.getInt ("EMI"));
                                     AccidentalRepair =formatter.format(c.getInt ("RAccicedental"));
                                     NCOF = formatter.format((c.getInt ("GrossMargin")-(c.getInt ("TyreExp")+c.getInt ("BatteryExp")+c.getInt ("RepairExp")+c.getInt ("DueExp")+c.getInt ("DriverExp")+c.getInt ("GeneralExp")))- (c.getInt("RAccicedental")+c.getInt ("EMI")));

                                  //  intPercLesstripExp= Integer.parseInt(PerKMLessTripExp)/Integer.parseInt(PerKMFreight) ;

                                        intPercLesstripExp = c.getDouble("LessTripExp") * 100 / c.getInt("Freight");
                                        intPercFuelExp = c.getDouble("FuelExp") * 100 / c.getInt("Freight");
                                        PercTollExp = String.valueOf(String.format("%.2f", (c.getDouble("ETollExp")) * 100 / c.getInt("Freight")));
                                        PercDrivExp = String.valueOf(String.format("%.2f", c.getDouble("DriverTripExp") * 100 / c.getInt("Freight")));
                                        PercOthrExp = String.valueOf(String.format("%.2f", c.getDouble("OtherExp") * 100 / c.getInt("Freight")));
                                        PercGM = String.valueOf(String.format("%.2f", c.getDouble("GrossMargin") * 100 / c.getInt("Freight")));

                                        sperLess = String.valueOf(String.format("%.2f", (c.getDouble("TyreExp") + c.getInt("BatteryExp") + c.getInt("RepairExp") + c.getInt("DueExp") + c.getInt("DriverExp") + c.getInt("GeneralExp")) * 100 / c.getInt("Freight")));
                                        spertyre = String.valueOf(String.format("%.2f", c.getDouble("TyreExp") * 100 / c.getInt("Freight")));
                                        sperbattery = String.valueOf(String.format("%.2f", c.getDouble("BatteryExp") * 100 / c.getInt("Freight")));
                                        sperRepair = String.valueOf(String.format("%.2f", c.getDouble("RepairExp") * 100 / c.getInt("Freight")));
                                        sperdue = String.valueOf(String.format("%.2f", c.getDouble("DueExp") * 100 / c.getInt("Freight")));
                                        sperDriver = String.valueOf(String.format("%.2f", c.getDouble("DriverExp") * 100 / c.getInt("Freight")));
                                        sperGenExp = String.valueOf(String.format("%.2f", c.getDouble("GeneralExp") * 100 / c.getInt("Freight")));
                                        sperNetMar = String.valueOf(String.format("%.2f", (c.getDouble("GrossMargin") - (c.getInt("TyreExp") + c.getInt("BatteryExp") + c.getInt("RepairExp") + c.getInt("DueExp") + c.getInt("DriverExp") + c.getInt("GeneralExp"))) * 100 / c.getInt("Freight")));
                                        sperLCof = String.valueOf(String.format("%.2f", (c.getDouble("RAccicedental") + c.getInt("EMI")) * 100 / c.getInt("Freight")));
                                        sperEmi = String.valueOf(String.format("%.2f", c.getDouble("EMI") * 100 / c.getInt("Freight")));
                                        sperAR = String.valueOf(String.format("%.2f", c.getDouble("RAccicedental") * 100 / c.getInt("Freight")));
                                        sperNcof = String.valueOf(String.format("%.2f", ((c.getDouble("GrossMargin") - (c.getInt("TyreExp") + c.getInt("BatteryExp") + c.getInt("RepairExp") + c.getInt("DueExp") + c.getInt("DriverExp") + c.getInt("GeneralExp"))) - (c.getInt("RAccicedental") + c.getInt("EMI"))) * 100 / c.getInt("Freight")));

                                double a=5;
                                double s=a/3;
                                Log.e("datsafhg",String.valueOf(String.format("%.2f", s)));
                                PeriodicPerformanceActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        try {
                                            txtDays.setText(Days);
                                            txtpriodicActivityKM.setText(KM);
                                            txtTotalFreight.setText(TotalFreight);
                                            txtPerKMFreight.setText(PerKMFreight);
                                            txtTotalLessTripExp.setText(TotalLessTripExp);
                                            txtPerKMLessTripExp.setText(PerKMLessTripExp);
                                            txtTotalFuelExp.setText(TotalFuelExp);
                                            txtPerKMFuelExp.setText(PerKMFuelExp);
                                            txtTotalETollExp.setText(TotalETollExp);
                                            txtPerKMETollExp.setText(PerKMETollExp);

                                            txtPerKMDriverExp.setText(KMDriverExp);
                                            txtTotalDriverExp.setText(TotalDriverExp);

                                            txtTotalOther.setText(TotalOther);
                                            txtPerKMOther.setText(PerKMOther);
                                            txtTotalGMargin.setText(TotalGMargin);
                                            txtPerKMGMargin.setText(PerKMGMargin);
                                            txtTotalOE.setText(TotalOE);
                                            txtTyre.setText(Tyre);
                                            txtBattery.setText(Battery);
                                            txtRepair.setText(Repair);
                                            txtDues.setText(Dues);
                                            txtDriver.setText(Driver);
                                            txtGenExp.setText(GenExp);
                                            txtNetMargin.setText(NetMargin);
                                            txtLCOF.setText(LCOF);
                                            txtEMI.setText(EMI);
                                            txtAccidentalRepair.setText(AccidentalRepair);
                                            txtNCOF.setText(NCOF);
                                            if (c.getInt("Freight") != 0) {
                                                Log.e("perce", String.valueOf(String.format("%.2f", intPercLesstripExp)));
                                                txtPercLesstripExp.setText(String.valueOf(String.format("%.2f", intPercLesstripExp)));
                                                txtPercFuelExp.setText(String.valueOf(String.format("%.2f", intPercFuelExp)));
                                                Log.e("perce", String.valueOf(String.format("%.2f", intPercFuelExp)));
                                                txtPercTollExp.setText(PercTollExp);
                                                txtPercDrivExp.setText(PercDrivExp);
                                                txtPercOthrExp.setText(PercOthrExp);
                                                txtPercGM.setText(PercGM);

                                                perLess.setText(sperLess);
                                                pertyre.setText(spertyre);
                                                perbattery.setText(sperbattery);
                                                perRepair.setText(sperRepair);
                                                perdue.setText(sperdue);
                                                perDriver.setText(sperDriver);
                                                perGenExp.setText(sperGenExp);
                                                perNetMar.setText(sperNetMar);
                                                perLCof.setText(sperLCof);
                                                perEmi.setText(sperEmi);
                                                perAR.setText(sperAR);
                                                perNcof.setText(sperNcof);
                                            } else {

                                                txtPercLesstripExp.setText("0.00");
                                                txtPercFuelExp.setText("0.00");
                                                txtPercTollExp.setText("0.00");
                                                txtPercDrivExp.setText("0.00");
                                                txtPercOthrExp.setText("0.00");
                                                txtPercGM.setText("0.00");

                                                perLess.setText("0.00");
                                                pertyre.setText("0.00");
                                                perbattery.setText("0.00");
                                                perRepair.setText("0.00");
                                                perdue.setText("0.00");
                                                perDriver.setText("0.00");
                                                perGenExp.setText("0.00");
                                                perNetMar.setText("0.00");
                                                perLCof.setText("0.00");
                                                perEmi.setText("0.00");
                                                perAR.setText("0.00");
                                                perNcof.setText("0.00");
                                                spertyre = "0.00";
                                                sperbattery = "0.00";
                                                sperRepair = "0.00";

                                                sperdue = "0.00";
                                                sperDriver = "0.00";
                                                sperGenExp = "0.00";

                                            }
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }



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
            if (tripDetail.length() == 0) {
                txtDays.setText("0");
                txtpriodicActivityKM.setText("0");
                txtTotalFreight.setText("0");
                txtPerKMFreight.setText("0");
                txtTotalLessTripExp.setText("0");
                txtPerKMLessTripExp.setText("0");
                txtTotalFuelExp.setText("0");
                txtPerKMFuelExp.setText("0");
                txtTotalETollExp.setText("0");
                txtPerKMETollExp.setText("0");

                txtPerKMDriverExp.setText("0");
                txtTotalDriverExp.setText("0");

                txtTotalOther.setText("0");
                txtPerKMOther.setText("0");
                txtTotalGMargin.setText("0");
                txtPerKMGMargin.setText("0");
                txtTotalOE.setText("0");
                txtTyre.setText("0");
                txtBattery.setText("0");
                txtRepair.setText("0");
                txtDues.setText("0");
                txtDriver.setText("0");
                txtGenExp.setText("0");
                txtNetMargin.setText("0");
                txtLCOF.setText("0");
                txtEMI.setText("0");
                txtAccidentalRepair.setText("0");
                txtNCOF.setText("0");
                txtPercLesstripExp.setText("0");
                txtPercFuelExp.setText(String.valueOf("0"));
                txtPercTollExp.setText("0");
                txtPercDrivExp.setText("0");
                txtPercOthrExp.setText("0");
                txtPercGM.setText("0");

                perLess.setText("0");
                pertyre.setText("0");
                perbattery.setText("0");
                perRepair.setText("0");
                perdue.setText("0");
                perDriver.setText("0");
                perGenExp.setText("0");
                perNetMar.setText("0");
                perLCof.setText("0");
                perEmi.setText("0");
                perAR.setText("0");
                perNcof.setText("0");
                spertyre="0";
                sperbattery="0";
                sperRepair="0";
                sperdue="0";
                sperDriver="0";
                 sperGenExp="0";
            }
            BarChart();

        }
    }
}



