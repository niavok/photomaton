package com.niavok.photomaton;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URL;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.

 */
public class PreviewActivity extends Activity {

    public static final String EXTRA_PICTURE_ID = "picture_id";

    private Handler mLoadHandler;
    private ImageView mImageView;
    private Bitmap mBitmap;
    private String mPictureId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preview);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        mImageView = (ImageView) findViewById(R.id.imageView);


        Button retryButton = (Button) findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviewActivity.this, GuestbookInstructionsActivity.class);
                startActivity(intent);
            }
        });

        Button printButton = (Button) findViewById(R.id.printButton);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();
                Intent intent = new Intent(PreviewActivity.this, MainChooseActivity.class);
                intent.putExtra(MainChooseActivity.EXTRA_PRINTING, true);
                startActivity(intent);
            }
        });

        mLoadHandler = new Handler() {
            @Override
            public void handleMessage(Message msg){
                mImageView.setImageBitmap(mBitmap);

            }
        };

        mPictureId = getIntent().getStringExtra(EXTRA_PICTURE_ID);
        loadImage();
    }

    private void print() {
        Toast.makeText(this, "TODO: print", Toast.LENGTH_SHORT).show();
    }

    private void loadImage() {
        Toast.makeText(this, "Load Image", Toast.LENGTH_SHORT).show();

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.1.98/display.php?picture="+mPictureId);
                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize=4;
                    mBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, o2);
                    mLoadHandler.sendEmptyMessage(0);


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
