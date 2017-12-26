package app.efleet.vehicleinfo.DriverInfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import app.efleet.vehicleinfo.R;

public class DriverProfile extends AppCompatActivity {
TextView DriverName,DriverMOB,DriverAddress,DriverDOJ,CompanyAge,DriverDLNO,DriverGRName,DriverGRMOB,DriverDLExpiryDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        setTitle("Driver Profile");
    }
}
