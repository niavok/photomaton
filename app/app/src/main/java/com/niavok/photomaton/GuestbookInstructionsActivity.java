package com.niavok.photomaton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class GuestbookInstructionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guestbook_instructions);

        Button oneSecondButton = (Button) findViewById(R.id.oneSecondButton);
        oneSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestbookInstructionsActivity.this, CaptureActivity.class);
                //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra(CaptureActivity.EXTRA_DELAY, 1);
                startActivity(intent);
            }
        });

        Button fiveSecondsButton = (Button) findViewById(R.id.fiveSecondsButton);
        fiveSecondsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestbookInstructionsActivity.this, CaptureActivity.class);
                //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra(CaptureActivity.EXTRA_DELAY, 5);
                startActivity(intent);
            }
        });

        Button tenSecondsButton = (Button) findViewById(R.id.tenSecondsButton);
        tenSecondsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestbookInstructionsActivity.this, CaptureActivity.class);
                intent.putExtra(CaptureActivity.EXTRA_DELAY, 10);
                //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        Button abandonButton = (Button) findViewById(R.id.abandonButton);
        abandonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestbookInstructionsActivity.this, MainChooseActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guestbook_instructions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
