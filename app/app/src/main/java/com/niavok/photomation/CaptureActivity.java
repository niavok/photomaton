package com.niavok.photomation;

import android.net.http.AndroidHttpClient;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpHead;

import java.io.IOException;


public class CaptureActivity extends ActionBarActivity {
    public final static String EXTRA_DELAY = "delay";

    public final static int WHAT_NEW_SECOND = 0;
    public final static int WHAT_CAPTURE_DONE = 1;

    private int mDelay;
    private Handler mHandler;
    private TextView mCountdownTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        mCountdownTextView = (TextView) findViewById(R.id.countdownTextView);
        mDelay = getIntent().getIntExtra(EXTRA_DELAY,0);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WHAT_NEW_SECOND:
                        if(mDelay > 0) {
                            mDelay--;
                            mHandler.sendEmptyMessageDelayed(WHAT_NEW_SECOND, 1000);
                        } else {
                            capture();
                        }
                        mCountdownTextView.setText(""+mDelay);
                        break;
                    case WHAT_CAPTURE_DONE:
                        String disp = (String) msg.obj;
                        Toast.makeText(CaptureActivity.this, "Code: "+msg.arg1+" disp: "+disp, Toast.LENGTH_LONG).show();
                }
            }
        };
        mHandler.sendEmptyMessage(WHAT_NEW_SECOND);
    }

    private void capture() {
        Toast.makeText(this, "Capture", Toast.LENGTH_SHORT).show();

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    HttpResponse response = AndroidHttpClient.newInstance("Android").execute(new HttpHead("http://192.168.1.98/capture.php"));
                    Header contentDisposition = response.getLastHeader("Content-Disposition");
                    mHandler.obtainMessage(WHAT_CAPTURE_DONE, response.getStatusLine().getStatusCode(), 0, contentDisposition.getValue()).sendToTarget();


                } catch (Exception e) {
                    Log.e("PLOP", e.getMessage());
                }
            }
        });
        thread.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_capture, menu);
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
