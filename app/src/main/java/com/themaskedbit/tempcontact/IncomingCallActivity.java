package com.themaskedbit.tempcontact;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

public class IncomingCallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            Log.d("Tag", "flag2");



            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

            Log.d("Tag", "flagy");

            setContentView(R.layout.activity_incoming_call);

            Log.d("Tag", "flagz");

            String number = getIntent().getStringExtra(
                    TelephonyManager.EXTRA_INCOMING_NUMBER);
            TextView text = (TextView) findViewById(R.id.text);
            text.setText("Incoming call from " + number);
        }
        catch (Exception e) {
            Log.d("Exception", e.toString());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
