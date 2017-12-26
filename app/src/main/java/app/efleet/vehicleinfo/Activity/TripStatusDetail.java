package app.efleet.vehicleinfo.Activity;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.efleet.vehicleinfo.R;

public class TripStatusDetail extends Activity {
    TextView txtvehNo,txtcrntsts,txtclient,txtRoute,txtbdgH,txtbdgkm,txtlgu,txtlgl;
    TextView txtgpskm,txtdelay;
    TextView txtStsDate,txtstsloc,txtstsage,txtstsremark,txtdnmae,txtdmob,txtsfldt,txtrfldt,txtlcompdt,txtrptfunlgngdt,txtunldngcompdt;
    TextView txtsflhr,txtrflhr,txtlcomphr,txtrfunldnghr,txtunldngcomphr;
    String vehno,crntsts,rules,client,route,bdghours,bdgkm,lastgpsupdt,lastgpsloc;
    String gpskm,gpslat,gpslong,delay,stsdate,stsloc,stsage,stsrmrk,dname,dmob;
    String sntforloadingdt,reportforlodngdt,ldngcomptdt,rptunldngdt,unldngcomptdt;
    String sntforloadinghr,reportforlodnghr,ldngcompthr,rptunldnghr,unldngcompthr;
    ImageButton imgback;
    LinearLayout LinUp,Lintrp,lintripdetail,lintimesheet,lintimesheetdetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_status_detail);
        imgback = (ImageButton)findViewById(R.id.imgback);
        txtvehNo = (TextView) findViewById(R.id.VehNo);
        txtcrntsts = (TextView) findViewById(R.id.CStatus);
        txtStsDate=(TextView) findViewById(R.id.txtSDate);
        txtstsloc=(TextView) findViewById(R.id.StsLoc);
        txtstsage=(TextView) findViewById(R.id.StsAge);
        txtstsremark=(TextView) findViewById(R.id.StsRmrk);
        txtclient = (TextView) findViewById(R.id.client);
        txtRoute = (TextView) findViewById(R.id.route);
        txtbdgH = (TextView) findViewById(R.id.bdghours);
        txtbdgkm = (TextView) findViewById(R.id.bdgkm);
        txtdnmae = (TextView) findViewById(R.id.diver);
        txtdmob = (TextView) findViewById(R.id.drivermobno);
        txtsfldt = (TextView) findViewById(R.id.txtSntforDT);
        txtrfldt = (TextView) findViewById(R.id.txtRptforLdngDT);
        txtlcompdt = (TextView) findViewById(R.id.LCDT);
        txtrptfunlgngdt = (TextView) findViewById(R.id.txtRptUpldDT);
        txtunldngcompdt = (TextView) findViewById(R.id.txtUpldCompDT);
        txtsflhr = (TextView) findViewById(R.id.txtSntforldngH);
        txtrflhr = (TextView) findViewById(R.id.txtRptforLdngH);
        txtlcomphr = (TextView) findViewById(R.id.LCH);
        txtrfunldnghr = (TextView) findViewById(R.id.txtRptUpldH);
        txtunldngcomphr = (TextView) findViewById(R.id.txtUpldCompH);
        LinUp=(LinearLayout)findViewById(R.id.Linup);
        Lintrp=(LinearLayout)findViewById(R.id.LinTrip);
        lintripdetail=(LinearLayout)findViewById(R.id.LinTripDetail);
        lintimesheet=(LinearLayout)findViewById(R.id.LinTimesheet);
        lintimesheetdetail=(LinearLayout)findViewById(R.id.LinTimeSheeetDetail);

        imgback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        vehno=extras.getString("Vehno");
        crntsts=extras.getString("CSts");
        stsdate=extras.getString("StsDate");
        stsloc=extras.getString("StsLoc");
        stsage=extras.getString("StsAge");
        stsrmrk=extras.getString("StsRemark");
        client=extras.getString("Client");
        route=extras.getString("Rout");
        bdghours=extras.getString("BdgHr");
        bdgkm=extras.getString("BdgKm");
        dname=extras.getString("DName");
        dmob=extras.getString("DNumb");
        sntforloadingdt=extras.getString("SFL");
        reportforlodngdt=extras.getString("RFL");
        ldngcomptdt=extras.getString("LC");
        rptunldngdt=extras.getString("RU");
        unldngcomptdt=extras.getString("UC");
        rules=extras.getString("Rule");
        try {
            String sntfldngtime="";
            String repttime="";
            String lcomptime="";
            String rptunldngtime="";
            String unldngcomptime="";
            if(!sntforloadingdt.isEmpty()&&!reportforlodngdt.isEmpty()) {
                /*String[] sendforloading = sntforloadingdt.split(" ");
                  sntfldngtime = sendforloading[1];
                String[] reporttime = reportforlodngdt.split(" ");
                  repttime = reporttime[1];*/
               // DateFormat df = DateFormat.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
                Date date1 = df.parse(reportforlodngdt);
                Date date2 = df.parse(sntforloadingdt);
                long difference = date1.getTime() - date2.getTime();
                if(difference<0) {
                    int days = (int) (difference / (1000 * 60 * 60 * 24));
                    int hours = (int) ((difference) / (1000 * 60 * 60));
                    int min = (int) (difference - (1000 * 60 * 60 * hours)) / (1000 * 60);
                    int sec=(int) (difference - (1000 * 60 * 60 * hours)-(1000*60*min)) / (1000);
                    txtrflhr.setText("-"+new DecimalFormat("00").format(-hours) + ":" + new DecimalFormat("00").format(-min)+":" + new DecimalFormat("00").format(-sec));
                }else{
                    int days = (int) (difference / (1000 * 60 * 60 * 24));
                    int hours = (int) ((difference) / (1000 * 60 * 60));
                    int min = (int) (difference - (1000 * 60 * 60 * hours)) / (1000 * 60);
                    int sec=(int) (difference - (1000 * 60 * 60 * hours)-(1000*60*min)) / (1000);
                    txtrflhr.setText(new DecimalFormat("00").format(hours) + ":" + new DecimalFormat("00").format(min)+":" + new DecimalFormat("00").format(sec));
                }

            }
            if(!reportforlodngdt.isEmpty()&&!ldngcomptdt.isEmpty()) {
               /* String[] lcomplete = ldngcomptdt.split(" ");
                  lcomptime = lcomplete[1];*/
                //DateFormat df = DateFormat.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
                Date date1 = df.parse(ldngcomptdt);
                Date date2 = df.parse(reportforlodngdt);
                long difference = date1.getTime() - date2.getTime();
                if(difference<0) {
                    int days = (int) (difference / (1000 * 60 * 60 * 24));
                    int hours = (int) ((difference) / (1000 * 60 * 60));
                    int min = (int) (difference - (1000 * 60 * 60 * hours)) / (1000 * 60);
                    int sec=(int) (difference - (1000 * 60 * 60 * hours)-(1000*60*min)) / (1000);
                    txtlcomphr.setText("-"+new DecimalFormat("00").format(-hours) + ":" + new DecimalFormat("00").format(-min)+":" + new DecimalFormat("00").format(-sec));
                }else{
                    int days = (int) (difference / (1000 * 60 * 60 * 24));
                    int hours = (int) ((difference) / (1000 * 60 * 60));
                    int min = (int) (difference - (1000 * 60 * 60 * hours)) / (1000 * 60);
                    int sec=(int) (difference - (1000 * 60 * 60 * hours)-(1000*60*min)) / (1000);
                    txtlcomphr.setText(new DecimalFormat("00").format(hours) + ":" + new DecimalFormat("00").format(min)+":" + new DecimalFormat("00").format(sec));
                }
            }
            if(!rptunldngdt.isEmpty()&&!ldngcomptdt.isEmpty()) {
              /*  String[] rptunldng = rptunldngdt.split(" ");
                  rptunldngtime = rptunldng[1];*/
               // DateFormat df = DateFormat.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
                Date date1 = df.parse(rptunldngdt);
                Date date2 = df.parse(ldngcomptdt);
                long difference = date1.getTime() - date2.getTime();
                if(difference<0) {
                    int days = (int) (difference / (1000 * 60 * 60 * 24));
                    int hours = (int) (difference / (1000 * 60 * 60));
                    int min = (int) (difference - (1000 * 60 * 60 * hours)) / (1000 * 60);
                    int sec=(int) (difference - (1000 * 60 * 60 * hours)-(1000*60*min)) / (1000);
                    txtrfunldnghr.setText("-"+new DecimalFormat("00").format(-hours) + ":" + new DecimalFormat("00").format(-min)+":" + new DecimalFormat("00").format(-sec));
                }else {
                    int days = (int) (difference / (1000 * 60 * 60 * 24));
                    int hours = (int) (difference / (1000 * 60 * 60));
                    int min = (int) (difference - (1000 * 60 * 60 * hours)) / (1000 * 60);
                    int sec=(int) (difference - (1000 * 60 * 60 * hours)-(1000*60*min)) / (1000);
                    txtrfunldnghr.setText(new DecimalFormat("00").format(hours) + ":" + new DecimalFormat("00").format(min)+":" + new DecimalFormat("00").format(sec));
                }
            }
            if(!unldngcomptdt.isEmpty()&&!rptunldngdt.isEmpty()) {
               /* String[] unldngcomp = unldngcomptdt.split(" ");
                  unldngcomptime = unldngcomp[1];*/
                //DateFormat df = DateFormat.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
                Date date1 = df.parse(unldngcomptdt);
                Date date2 = df.parse(rptunldngdt);
                long difference = date1.getTime() - date2.getTime();
                if(difference<0) {
                    int days = (int) (difference / (1000 * 60 * 60 * 24));
                    int hours = (int) (difference) / (1000 * 60 * 60);
                    int min = (int) (difference - (1000 * 60 * 60 * hours)) / (1000 * 60);
                    int sec=(int) (difference - (1000 * 60 * 60 * hours)-(1000*60*min)) / (1000);
                    txtunldngcomphr.setText("-"+new DecimalFormat("00").format(-hours) + ":" + new DecimalFormat("00").format(-min)+":" + new DecimalFormat("00").format(-sec));
                }else {
                    int days = (int) (difference / (1000 * 60 * 60 * 24));
                    int hours = (int) (difference) / (1000 * 60 * 60);
                    int min = (int) (difference - (1000 * 60 * 60 * hours)) / (1000 * 60);
                    int sec=(int) (difference - (1000 * 60 * 60 * hours)-(1000*60*min)) / (1000);
                    txtunldngcomphr.setText(new DecimalFormat("00").format(hours) + ":" + new DecimalFormat("00").format(min)+":" + new DecimalFormat("00").format(sec));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        txtvehNo.setText(vehno);
        txtcrntsts.setText(crntsts);
        txtStsDate.setText(stsdate);
        txtstsloc.setText(stsloc);
        txtstsage.setText(stsage);
        txtstsremark.setText(stsrmrk);
        txtclient.setText(client);
        txtRoute.setText(route);
        txtbdgH.setText(bdghours);
        txtbdgkm.setText(bdgkm);
        txtdnmae.setText(dname);
        txtdmob.setText(dmob);
        txtsfldt.setText(sntforloadingdt);
        txtrfldt.setText(reportforlodngdt);
        txtlcompdt.setText(ldngcomptdt);
        txtrptfunlgngdt.setText(rptunldngdt);
        txtunldngcompdt.setText(unldngcomptdt);
        /*if(crntsts.contains("Unload")){

        }*/

      try {
          if (rules.equals("S - Standing")) {
              LinUp.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalShadedkRed));
              Lintrp.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalRed));
              lintripdetail.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalDeepRed));
              lintimesheet.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalRed));
              lintimesheetdetail.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalDeepRed));
          } else if (rules.equals("E - Enroute")) {
              LinUp.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalShadedkGreen));
              Lintrp.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalGreen));
              lintripdetail.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalDeepGreen));
              lintimesheet.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalGreen));
              lintimesheetdetail.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalDeepGreen));
          }else if (rules.equals("U - Unloading")) {
              LinUp.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalShadedOrange));
              Lintrp.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalOrange));
              lintripdetail.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalDeepOrange));
              lintimesheet.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalOrange));
              lintimesheetdetail.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalDeepOrange));
          }else if (rules.equals("R - Repairing")){
              LinUp.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalShadedBlue));
              Lintrp.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalBlue));
              lintripdetail.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalDeepBlue));
              lintimesheet.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalBlue));
              lintimesheetdetail.setBackgroundColor(ContextCompat.getColor(TripStatusDetail.this, R.color.PastalDeepBlue));
          }
      }catch (Exception e){
          e.printStackTrace();
      }


    }
}
