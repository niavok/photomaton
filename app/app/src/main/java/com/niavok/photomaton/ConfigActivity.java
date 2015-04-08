package com.niavok.photomaton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.niavok.photomaton.R;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class ConfigActivity extends ActionBarActivity {
  public final static int WHAT_CHECK_OK = 0;
    public final static int WHAT_CHECK_KO = 1;
    private Handler mHandler;
    private ProgressBar mCheckProgressBar;

    public static final String PREFS_NAME = "PhotomatonPrefs";
    public static final String CONFIG_IP = "ip";

    public static String getCaptureUrl(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String ip = settings.getString(CONFIG_IP, "127.0.0.1");
        return "http://"+ip+"/capture.php";
    }

    public static String getDisplayUrl(Context context, String pictureId)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String ip = settings.getString(CONFIG_IP, "127.0.0.1");
        return "http://"+ip+"/display.php?picture="+pictureId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mCheckProgressBar = (ProgressBar) findViewById(R.id.checkProgressBar);

        final EditText ipTextEdit = (EditText) findViewById(R.id.ipEditText);
        Button checkButton = (Button) findViewById(R.id.checkButton);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(ipTextEdit.getText().toString());
            }
        });

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case WHAT_CHECK_KO:
                        mCheckProgressBar.setVisibility(View.GONE);
                        Toast.makeText(ConfigActivity.this, "Ip test fail", Toast.LENGTH_SHORT).show();
                        break;
                    case WHAT_CHECK_OK:
                        mCheckProgressBar.setVisibility(View.GONE);
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(CONFIG_IP, (String) msg.obj);
                        editor.commit();
                        Intent intent = new Intent(ConfigActivity.this, MainChooseActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String ip = settings.getString(CONFIG_IP, "127.0.0.1");

        ipTextEdit.setText(ip);
        check(ip);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_config, menu);
        return true;
    }

    private void check(final String ip) {
        mCheckProgressBar.setVisibility(View.VISIBLE);


        final Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");

                try {
                    HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 5000);
                    HttpConnectionParams.setSoTimeout(httpClient.getParams(), 6000);

                    HttpResponse response = httpClient.execute(new HttpHead("http://"+ip+"/list.php"));

                    if(response.getStatusLine().getStatusCode() == 200) {
                        mHandler.obtainMessage(WHAT_CHECK_OK, ip).sendToTarget();
                        return;
                    }
                } catch (Exception e) {
                    Log.e("PLOP", "Fail to capture" + e.getMessage());
                } finally {
                    httpClient.close();
                }
                mHandler.sendEmptyMessage(WHAT_CHECK_KO);
            }
        });
        thread.start();
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
