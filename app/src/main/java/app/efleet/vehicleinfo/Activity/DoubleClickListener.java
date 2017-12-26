package app.efleet.vehicleinfo.Activity;

import android.os.SystemClock;
import android.view.View;

/**
 * Created by admin on 10-07-2017.
 */

public abstract class DoubleClickListener implements View.OnClickListener {

    // The time in which the second tap should be done in order to qualify as
    // a double click
    private static final long DEFAULT_QUALIFICATION_SPAN = 200;
    private long doubleClickQualificationSpanInMillis;
    private long timestampLastClick;

    public
    DoubleClickListener() {
        doubleClickQualificationSpanInMillis = DEFAULT_QUALIFICATION_SPAN;
        timestampLastClick = 0;
    }

    public DoubleClickListener(long doubleClickQualificationSpanInMillis) {
        this.doubleClickQualificationSpanInMillis = doubleClickQualificationSpanInMillis;
        timestampLastClick = 0;
    }

    @Override
    public void onClick(View v) {
        if((SystemClock.elapsedRealtime() - timestampLastClick) < doubleClickQualificationSpanInMillis) {
            onDoubleClick();
        }
        timestampLastClick = SystemClock.elapsedRealtime();
    }

    public abstract void onDoubleClick();

}