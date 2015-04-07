package com.niavok.photomaton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainChooseActivity extends Activity {

    public static final String EXTRA_PRINTING = "printing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_choose);

        // TODO printing message

        Button guestbookButton = (Button) findViewById(R.id.guestbookButton);
        guestbookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainChooseActivity.this, GuestbookInstructionsActivity.class);
                startActivity(intent);
            }
        });

    }

}
