package com.mobilelive.etee.mobilelive.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.mobilelive.etee.mobilelive.R;


public class AsyncTaskNetworkRequest extends AsyncTask<Object, Void, Void> {

    protected Context mContext;
    private ProgressDialog progressDialog;
    private String title;
    private String message;
    private INetworkResponseListener requestListener;
    private BaseHttpRequest networkRequest;
    private boolean isSuccessfulResponse;
    private Object resultObject;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            progressDialog = ProgressDialog.show(mContext, getTittle(), getMessage());

        } catch (Exception exception) {

        }
    }

    private String getTittle() {
        if (title == null) {
            title = mContext.getResources().getString(R.string.wait);
        }
        return title;
    }

    private String getMessage() {
        if (message == null) {
            message = mContext.getResources().getString(R.string.processing);
        }
        return message;
    }

    @Override
    protected Void doInBackground(final Object... params) {

        if (networkRequest != null) {
            networkRequest.execute();
            Object jsonResult = networkRequest.getResultObject();
            isSuccessfulResponse = jsonResult != null;
            resultObject = jsonResult;
        }
        return null;
    }

    @Override
    protected void onPostExecute(final Void result) {
        super.onPostExecute(result);
        if (isSuccessfulResponse) {
            requestListener.onSuccess(resultObject);
        } else {
            requestListener.onFailure(resultObject);
        }
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception exception) {
        }
    }

    public BaseHttpRequest getNetworkRequest() {
        return networkRequest;
    }

    public void setNetworkRequest(BaseHttpRequest networkRequest) {
        this.networkRequest = networkRequest;
    }

    public INetworkResponseListener getRequestListener() {
        return requestListener;
    }

    public void setRequestListener(INetworkResponseListener requestListener) {
        this.requestListener = requestListener;
    }
}