package app.efleet.vehicleinfo.DriverInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import app.efleet.vehicleinfo.R;

public class DriverInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     /*   LinearLayout liDP=(LinearLayout)findViewById(R.id.liDriverProfile);
        LinearLayout liDS=(LinearLayout)findViewById(R.id.linDriverStatus);
        LinearLayout liDA=(LinearLayout)findViewById(R.id.linDriverAccount);
        LinearLayout liDSecurity=(LinearLayout)findViewById(R.id.linDriverSecurity);
        LinearLayout liDE=(LinearLayout)findViewById(R.id.linDriverEvent);
        LinearLayout liDKPI=(LinearLayout)findViewById(R.id.linDriverKPI);

        liDP.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverInformation.this, DriverProfile.class);
                startActivity(intent);
            }
        });
        liDS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverInformation.this, DriverStatus.class);
                startActivity(intent);
            }
        });
        liDA.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverInformation.this, DriverAccount.class);
                startActivity(intent);
            }
        });
        liDSecurity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverInformation.this, DriverSecurity.class);
                startActivity(intent);
            }
        });
        liDE.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverInformation.this, DriverEvent.class);
                startActivity(intent);
            }
        });
        liDKPI.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverInformation.this, DriverKPI.class);
                startActivity(intent);
            }
        });*/
    }

}
