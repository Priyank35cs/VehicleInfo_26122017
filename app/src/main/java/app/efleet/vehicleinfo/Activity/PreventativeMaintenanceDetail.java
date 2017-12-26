package app.efleet.vehicleinfo.Activity;

import android.app.Activity;
import android.support.annotation.StringDef;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.efleet.vehicleinfo.R;

public class PreventativeMaintenanceDetail extends Activity {
    TextView pmname,pmid,jobgroupcode,jobgroupname,lastJSDate,lastjskm,bdgtdays,bdgtkm,pmalert;
    TextView dueondate1,dueonkmrun,currentkmrun,nature,vehicleno,Kmstatus;
    String spmname,spmid,sjobgroupcode,sjobgroupname,slastJSDate,slastjskm,sbdgtdays,sbdgtkm,spmalert;
    String sdueondate1,sdueonkmrun,scurrentkmrun,snature,svehicleno,sbackground;
    LinearLayout linup,lindown,linbdgt,linbdgtdetail,linlastJD,linlastJDdetail,lincurrent,lincurrentdetail;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preventative_maintenance_detail);
        vehicleno = (TextView) findViewById(R.id.VehNo);
        pmname = (TextView) findViewById(R.id.PmName);
        bdgtkm = (TextView) findViewById(R.id.bdgkm);
        pmalert = (TextView) findViewById(R.id.alertkm);
        bdgtdays = (TextView) findViewById(R.id.bdgdays);
        lastJSDate = (TextView) findViewById(R.id.date);
        lastjskm = (TextView) findViewById(R.id.Lkm);
        dueondate1 = (TextView) findViewById(R.id.dueon);
        currentkmrun=(TextView)findViewById(R.id.ckm);
        Kmstatus = (TextView) findViewById(R.id.Ckmstatus);
        linup=(LinearLayout)findViewById(R.id.Linup);
        lindown=(LinearLayout)findViewById(R.id.Lindown);
        linbdgt=(LinearLayout)findViewById(R.id.Linbdgt);
        linbdgtdetail=(LinearLayout)findViewById(R.id.LinbdgtDetail);
        linlastJD=(LinearLayout)findViewById(R.id.LinLJD);
        linlastJDdetail=(LinearLayout)findViewById(R.id.LinLJDDetail);
        lincurrent=(LinearLayout)findViewById(R.id.LinCurrent);
        lincurrentdetail=(LinearLayout)findViewById(R.id.LinCurrentDetail);
        back=(ImageButton)findViewById(R.id.imgback);


        svehicleno = getIntent().getStringExtra("VehNo");
        spmname = getIntent().getStringExtra("PMName");
        sbdgtkm = getIntent().getStringExtra("BdgtKM");
        spmalert=getIntent().getStringExtra("PMAlert");
        sdueonkmrun = getIntent().getStringExtra("DueOnKmRun");
        sbdgtdays = getIntent().getStringExtra("BdgtDays");
        slastJSDate = getIntent().getStringExtra("LastJSDate");
        slastjskm = getIntent().getStringExtra("LastJSKM");
        sdueondate1 = getIntent().getStringExtra("DueOnDate1");
        scurrentkmrun= getIntent().getStringExtra("CurrentRunKM");
        sbackground = getIntent().getStringExtra("BG");
        vehicleno.setText(svehicleno);
        pmname.setText(spmname);
        bdgtkm.setText(sbdgtkm);
        pmalert.setText(spmalert);
        bdgtdays.setText(sbdgtdays);
        lastJSDate.setText(slastJSDate);
        lastjskm.setText(slastjskm);
        dueondate1.setText(sdueondate1);
        currentkmrun.setText(scurrentkmrun);
        Kmstatus.setText(String.valueOf(Integer.parseInt(scurrentkmrun)-Integer.parseInt(sbdgtkm)));
        if( sbackground.equals("R")){

            linup.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalShadedkRed));
            linbdgt.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalRed));
            linbdgtdetail.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalDeepRed));
            linlastJD.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalRed));
            linlastJDdetail.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalDeepRed));
            lincurrent.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalRed));
            lincurrentdetail.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalDeepRed));

            //lindown.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalRed));
        }else if(sbackground.equals("Y")){
            linup.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalShadedkYellow));
            linbdgt.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalYellow));
            linbdgtdetail.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalDeepYellow));
            linlastJD.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalYellow));
            linlastJDdetail.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalDeepYellow));
            lincurrent.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalYellow));
            lincurrentdetail.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalDeepYellow));



           // lindown.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalYellow));
        }else  {
            linup.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalShadedkGreen));
            linbdgt.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalGreen));
            linbdgtdetail.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalDeepGreen));
            linlastJD.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalGreen));
            linlastJDdetail.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalDeepGreen));
            lincurrent.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalGreen));
            lincurrentdetail.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalDeepGreen));


            //lindown.setBackgroundColor(ContextCompat.getColor(this,R.color.PastalGreen));
        }
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
