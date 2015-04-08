package com.niavok.photomaton;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainChooseActivity extends Activity {

    public static final String EXTRA_PRINTING = "printing";
    private View mPlugTabletLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_choose);

        mPlugTabletLayout = findViewById(R.id.plugTabletLayout);

        Button guestbookButton = (Button) findViewById(R.id.guestbookButton);
        guestbookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainChooseActivity.this, GuestbookInstructionsActivity.class);
                startActivity(intent);
            }
        });

        if(getIntent().getBooleanExtra(EXTRA_PRINTING, false)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Impression lancée\nEn cas de problème n'hésitez pas à aller chercher Frédéric Bertolus")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                onBatteryChanged(intent);
            }
        }, ifilter);
        if(batteryStatus != null) {
            onBatteryChanged(batteryStatus);
        }

    }


    private void onBatteryChanged(Intent batteryStatus)
    {
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
        status == BatteryManager.BATTERY_STATUS_FULL;
        if(isCharging) {
            mPlugTabletLayout.setVisibility(View.GONE);
        } else {
            mPlugTabletLayout.setVisibility(View.VISIBLE);
        }
    }


}
