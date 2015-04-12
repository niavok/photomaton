package com.niavok.photomaton;


import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.w3c.dom.Text;

import java.net.URL;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.

 */
public class PreviewActivity extends Activity {

    public static final String EXTRA_PICTURE_ID = "picture_id";
    public final static String EXTRA_ALLOW_PRINT = "allow_print";

    public final static int WHAT_LOAD_DONE = 0;
    public final static int WHAT_PRINT_DONE = 1;
    public final static int WHAT_DELETE_DONE = 2;

    private Handler mLoadHandler;
    private ImageView mImageView;
    private Bitmap mBitmap;
    private String mPictureId;
    private View mControls;
    private View mLoadingView;
    private View mTopView;
    private ProgressBar mCaptureProgressBar;
    private boolean mAllowPrint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mControls = findViewById(R.id.fullscreen_content_controls);
        mLoadingView = findViewById(R.id.loadingView);
        mTopView = findViewById(R.id.topView);
        mCaptureProgressBar = (ProgressBar) findViewById(R.id.captureProgressBar);

        // Capture animation
        ObjectAnimator animation = ObjectAnimator.ofInt(mCaptureProgressBar, "progress", 1000);
        animation.setDuration(5000);
        animation.setInterpolator(new LinearInterpolator());
        animation.start();


        Typeface fontLemon = Typeface.createFromAsset(getAssets(), "DK Lemon Yellow Sun.otf");


        TextView loadingTextView = (TextView) findViewById(R.id.loadingTextView);
        loadingTextView.setTypeface(fontLemon);

        final Button retryButton = (Button) findViewById(R.id.retryButton);
        retryButton.setTypeface(fontLemon);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
                retryButton.setEnabled(false);

            }
        });

        final Button printButton = (Button) findViewById(R.id.printButton);
        printButton.setTypeface(fontLemon);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();
                printButton.setEnabled(false);
            }
        });

        final Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setTypeface(fontLemon);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviewActivity.this, MainChooseActivity.class);
                startActivity(intent);
            }
        });


        mAllowPrint = getIntent().getBooleanExtra(EXTRA_ALLOW_PRINT, false);

        if(mAllowPrint) {
            saveButton.setVisibility(View.GONE);
            printButton.setVisibility(View.VISIBLE);
        } else {
            saveButton.setVisibility(View.VISIBLE);
            printButton.setVisibility(View.GONE);
        }

        mLoadHandler = new Handler() {
            @Override
            public void handleMessage(Message msg){
                switch (msg.what) {
                    case WHAT_LOAD_DONE:
                        mImageView.setImageBitmap(mBitmap);
                        mControls.setVisibility(View.VISIBLE);
                        mLoadingView.setVisibility(View.GONE);
                        //mTopView.setBackgroundColor(Color.BLACK);
                        break;
                    case WHAT_PRINT_DONE: {
                        Intent intent = new Intent(PreviewActivity.this, MainChooseActivity.class);
                        intent.putExtra(MainChooseActivity.EXTRA_PRINTING, true);
                        startActivity(intent);
                    }
                    break;
                    case WHAT_DELETE_DONE: {
                        if(mAllowPrint) {
                            Intent intent = new Intent(PreviewActivity.this, GuestbookInstructionsActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(PreviewActivity.this, PhotoboothInstructionsActivity.class);
                            startActivity(intent);
                        }
                    }
                    break;
                }

            }
        };

        mPictureId = getIntent().getStringExtra(EXTRA_PICTURE_ID);
        loadImage();
    }

    private void print() {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
                    String printUrl = ConfigActivity.getPrintUrl(PreviewActivity.this, mPictureId);
                    Log.d("PLOP", "Print URL=" + printUrl);
                    HttpResponse response = httpClient.execute(new HttpHead(printUrl));
                    Log.d("PLOP", "Print response=" + response.getStatusLine().getStatusCode());
                    httpClient.close();
                    mLoadHandler.sendEmptyMessage(WHAT_PRINT_DONE);
                } catch (Exception e) {
                    Log.e("PLOP", "Fail to print: " + e);
                }
            }
        });
        thread.start();
    }

    private void delete() {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
                    String printUrl = ConfigActivity.getDeleteUrl(PreviewActivity.this, mPictureId);
                    Log.d("PLOP", "Delete URL=" + printUrl);
                    HttpResponse response = httpClient.execute(new HttpHead(printUrl));
                    Log.d("PLOP", "Delete response=" + response.getStatusLine().getStatusCode());
                    httpClient.close();
                    mLoadHandler.sendEmptyMessage(WHAT_DELETE_DONE);
                } catch (Exception e) {
                    Log.e("PLOP", "Fail to print: " + e);
                }
            }
        });
        thread.start();
    }

    private void loadImage() {
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL(ConfigActivity.getDisplayUrl(PreviewActivity.this, mPictureId));
                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize=4;
                    mBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, o2);
                    mLoadHandler.sendEmptyMessage(WHAT_LOAD_DONE);


                } catch (Exception e) {
                    Log.e("PLOP", "Fail to capture" + e.getMessage());
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }



}
