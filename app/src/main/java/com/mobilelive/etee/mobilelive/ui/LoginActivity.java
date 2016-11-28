package com.mobilelive.etee.mobilelive.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobilelive.etee.mobilelive.R;
import com.mobilelive.etee.mobilelive.network.AppNetworkConstants;
import com.mobilelive.etee.mobilelive.network.BaseHttpRequest;
import com.mobilelive.etee.mobilelive.network.BaseNetworkActivity;
import com.mobilelive.etee.mobilelive.network.INetworkRequest;


public class LoginActivity extends BaseNetworkActivity implements View.OnClickListener {

    EditText username;
    EditText password;
    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        initComponents();
    }

    private void initComponents() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signIn = (Button) findViewById(R.id.sing_in);
        signIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        sendLoginRequest();
    }

    /**
     * this method sends a network POST request for login and returns response in one of the overloaded methods of
     * INetworkResponseListener as callback
     */
    private void sendLoginRequest() {
        BaseHttpRequest request = new BaseHttpRequest(INetworkRequest.API_LOGIN);
        request.setContext(context);
        request.addParameter(AppNetworkConstants.PARAM_USER_NAME, String.valueOf(username.getText()));
        request.addParameter(AppNetworkConstants.PARAM_PASSWORD, String.valueOf(password.getText()));
        executeSimpleRequest(request);
    }

    /**
     * login api callback in case of successful api response
     */
    @Override
    public void onSuccess(Object object) {
        openImageListActivity();
    }

    /**
     * login api callback in case of failed api response
     */
    @Override
    public void onFailure(Object error) {
        super.onFailure(error);
        Toast.makeText(LoginActivity.this, R.string.error, Toast.LENGTH_SHORT);
    }

    private void openImageListActivity() {
        Intent i = new Intent(LoginActivity.this, ImageListActivity.class);
        startActivity(i);
        finish();
    }
}