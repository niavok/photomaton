package com.niavok.photomaton;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.http.AndroidHttpClient;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpHead;


public class CaptureActivity extends Activity {
    public final static String EXTRA_DELAY = "delay";
    public final static String EXTRA_ALLOW_PRINT = "allow_print";

    public final static int WHAT_NEW_SECOND = 0;
    public final static int WHAT_CAPTURE_DONE = 1;

    private int mDelay;
    private Handler mHandler;
    private TextView mCountdownTextView;
    private ProgressBar mCaptureProgressBar;
    private TextView mSmileTextView;
    private boolean mAllowPrint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        Typeface fontLemon = Typeface.createFromAsset(getAssets(), "DK Lemon Yellow Sun.otf");




        mCountdownTextView = (TextView) findViewById(R.id.countdownTextView);
        mSmileTextView = (TextView) findViewById(R.id.smileTextView);

        mCountdownTextView.setTypeface(fontLemon);
        mSmileTextView.setTypeface(fontLemon);


        mCaptureProgressBar = (ProgressBar) findViewById(R.id.captureProgressBar);
        mDelay = getIntent().getIntExtra(EXTRA_DELAY,0);
        mAllowPrint = getIntent().getBooleanExtra(EXTRA_ALLOW_PRINT, false);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WHAT_NEW_SECOND:
                        if(mDelay > 0) {
                            mDelay--;
                            mHandler.sendEmptyMessageDelayed(WHAT_NEW_SECOND, 1000);
                            mCountdownTextView.setText(""+(mDelay+1));
                            mCountdownTextView.setVisibility(View.VISIBLE);
                            mCaptureProgressBar.setVisibility(View.GONE);
                            mSmileTextView.setVisibility(View.GONE);
                        } else {
                            capture();
                        }

                        break;
                    case WHAT_CAPTURE_DONE:
                        String disp = (String) msg.obj;

                        String filename = disp.split("\"")[1];
                        Log.e("PLOP", "disp="+disp);
                        Log.e("PLOP", "filename="+filename);
                        Intent intent = new Intent(CaptureActivity.this, PreviewActivity.class);
                        intent.putExtra(PreviewActivity.EXTRA_PICTURE_ID, filename);
                        intent.putExtra(PreviewActivity.EXTRA_ALLOW_PRINT, mAllowPrint);
                        startActivity(intent);



                }
            }
        };
        mHandler.sendEmptyMessage(WHAT_NEW_SECOND);
    }

    private void capture() {
        mCountdownTextView.setVisibility(View.GONE);
        mCaptureProgressBar.setVisibility(View.VISIBLE);
        mSmileTextView.setVisibility(View.VISIBLE);
        // Capture animation
        ObjectAnimator animation = ObjectAnimator.ofInt(mCaptureProgressBar, "progress", 1000);
        animation.setDuration(3100);
        animation.setInterpolator(new LinearInterpolator());
        animation.start();

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
                    HttpResponse response = httpClient.execute(new HttpHead(ConfigActivity.getCaptureUrl(CaptureActivity.this)));
                    Header contentDisposition = response.getLastHeader("Content-Disposition");
                    httpClient.close();
                    mHandler.obtainMessage(WHAT_CAPTURE_DONE, response.getStatusLine().getStatusCode(), 0, contentDisposition.getValue()).sendToTarget();



                } catch (Exception e) {
                    Log.e("PLOP", "Fail to capture" + e.getMessage());
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
