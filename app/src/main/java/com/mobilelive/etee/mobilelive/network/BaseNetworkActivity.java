package com.mobilelive.etee.mobilelive.network;


import android.support.v7.app.AppCompatActivity;

/**
 * The type Base network activity.
 */
public abstract class BaseNetworkActivity extends AppCompatActivity implements IAsyncTaskSimple,
        INetworkResponseListener {
    protected AsyncTaskNetworkRequest networkRequestTask;

    protected NetworkResponseListener networkResponseListener;

    protected boolean isRequestInProgress;

    public void onPreExecute() {
        isRequestInProgress = true;
    }

    public void onPostExecute() {
        isRequestInProgress = false;
    }

    /**
     * Execute simple request.
     *
     * @param request the request
     */
    protected void executeSimpleRequest(BaseHttpRequest request) {
        if (networkResponseListener == null) {
            networkResponseListener = new NetworkResponseListener(this);
        }

        networkRequestTask = new AsyncTaskNetworkRequest();
        networkRequestTask.setNetworkRequest(request);
        networkRequestTask.setRequestListener(networkResponseListener);
        TaskHandler.executeAsync(networkRequestTask);
    }

    protected void executeSimpleRequest(BaseHttpRequest request, INetworkResponseListener listener) {
        if (networkResponseListener == null) {
            networkResponseListener = new NetworkResponseListener(this);
        }

        networkRequestTask = new AsyncTaskNetworkRequest();
        networkRequestTask.setNetworkRequest(request);
        if(listener!= null){
            networkRequestTask.setRequestListener(listener);
        }else
        {
            networkRequestTask.setRequestListener(networkResponseListener);
        }
        TaskHandler.executeAsync(networkRequestTask);
    }

    @Override
    public void onSuccess(Object object) {
    }

    @Override
    public void onFailure(Object error) {

    }


}
