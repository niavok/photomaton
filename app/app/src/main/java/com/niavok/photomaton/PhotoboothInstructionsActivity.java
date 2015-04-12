package com.niavok.photomaton;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class PhotoboothInstructionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photobooth_instructions);

        Typeface fontLemon = Typeface.createFromAsset(getAssets(), "DK Lemon Yellow Sun.otf");
        Typeface fontJanda = Typeface.createFromAsset(getAssets(), "JandaQuirkygirl.ttf");


        Button oneSecondButton = (Button) findViewById(R.id.oneSecondButton);
        oneSecondButton.setTypeface(fontLemon);
        oneSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoboothInstructionsActivity.this, CaptureActivity.class);
                //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra(CaptureActivity.EXTRA_DELAY, 1);
                intent.putExtra(CaptureActivity.EXTRA_ALLOW_PRINT, false);
                startActivity(intent);
            }
        });

        Button fiveSecondsButton = (Button) findViewById(R.id.fiveSecondsButton);
        fiveSecondsButton.setTypeface(fontLemon);
        fiveSecondsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoboothInstructionsActivity.this, CaptureActivity.class);
                //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtra(CaptureActivity.EXTRA_DELAY, 5);
                intent.putExtra(CaptureActivity.EXTRA_ALLOW_PRINT, false);
                startActivity(intent);
            }
        });

        Button tenSecondsButton = (Button) findViewById(R.id.tenSecondsButton);
        tenSecondsButton.setTypeface(fontLemon);
        tenSecondsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoboothInstructionsActivity.this, CaptureActivity.class);
                intent.putExtra(CaptureActivity.EXTRA_DELAY, 10);
                intent.putExtra(CaptureActivity.EXTRA_ALLOW_PRINT, false);
                //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        Button abandonButton = (Button) findViewById(R.id.abandonButton);
        abandonButton.setTypeface(fontLemon);
        abandonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoboothInstructionsActivity.this, MainChooseActivity.class);
                startActivity(intent);
            }
        });


        TextView guestbookInstructionsTextView1 = (TextView) findViewById(R.id.guestbookInstructionsTextView1);
        guestbookInstructionsTextView1.setTypeface(fontJanda);

        TextView guestbookInstructionsTextView2 = (TextView) findViewById(R.id.guestbookInstructionsTextView2);
        guestbookInstructionsTextView2.setTypeface(fontJanda);

        TextView guestbookInstructionsTextView3 = (TextView) findViewById(R.id.guestbookInstructionsTextView3);
        guestbookInstructionsTextView3.setTypeface(fontJanda);


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
