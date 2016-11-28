package com.mobilelive.etee.mobilelive.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mobilelive.etee.mobilelive.R;

import java.io.IOException;
import java.util.Properties;

public class SplashActivity extends AppCompatActivity {

    TextView devName;
    TextView devNumber;

    Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            showNextScreen();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initComponents();
        setDeveloperInfo();
        uiHandler.sendEmptyMessageDelayed(0, 1500);
    }

    private void initComponents() {
        devName = (TextView) findViewById(R.id.developer_name);
        devNumber = (TextView) findViewById(R.id.developer_contact);
    }

    /**
     * this method is used to read properties file from assets and set the values.
     */
    private void setDeveloperInfo() {
        try {
            Properties properties = new Properties();
            properties.load(getApplicationContext().getAssets().open("config.properties"));

            String name = properties.getProperty("developerName", "");
            String contact = properties.getProperty("developerContact", "");

            devName.setText(name);
            devNumber.setText(contact);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showNextScreen() {
        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}
