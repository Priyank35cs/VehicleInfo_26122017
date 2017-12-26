package app.efleet.vehicleinfo.DriverInfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.efleet.vehicleinfo.R;

public class DriverEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_event);
        setTitle("Driver Event");
    }
}
