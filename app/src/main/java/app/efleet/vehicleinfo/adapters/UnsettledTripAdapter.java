package app.efleet.vehicleinfo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import app.efleet.vehicleinfo.Activity.UnsettledTrip.UnsettledTripBreakup;
import app.efleet.vehicleinfo.Activity.UnsettledTrip.UnsettledTripStatus;
import app.efleet.vehicleinfo.GetVehicleNo;
import app.efleet.vehicleinfo.R;

/**
 * Created by admin on 19/08/17.
 */

public class UnsettledTripAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
    HashMap<String, String> resultp = new HashMap<String, String>();
    TextView days,SettlementID,Route,Party,qyt,Cash;
    public UnsettledTripAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.unsettled_trip_adapter, parent, false);
        resultp = data.get(position);
        days = (TextView) itemView.findViewById(R.id.unddmm);
        Route = (TextView) itemView.findViewById(R.id.Unroute);
        Party = (TextView) itemView.findViewById(R.id.Unparty);
        qyt = (TextView) itemView.findViewById(R.id.Unqyt);
        Cash = (TextView) itemView.findViewById(R.id.UnCash);


        days.setText(resultp.get(UnsettledTripStatus.TAG_Date));
        Route.setText(resultp.get(UnsettledTripStatus.TAG_Route));
      Party.setText(resultp.get(UnsettledTripStatus.TAG_Party));
        qyt.setText(resultp.get(UnsettledTripStatus.TAG_Qyt));
        Cash.setText(resultp.get(UnsettledTripStatus.TAG_Cash));

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resultp = data.get(position);
                Intent intent = new Intent(context, UnsettledTripBreakup.class);
                intent.putExtra("reg_no", GetVehicleNo.reg_no);
                intent.putExtra("triplogno",resultp.get(UnsettledTripStatus.TAG_TripLog_No));


                context.startActivity(intent);
            }
        });
        return itemView;
    }

}