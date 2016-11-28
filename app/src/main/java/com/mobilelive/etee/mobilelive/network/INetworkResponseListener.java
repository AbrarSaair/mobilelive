package com.mobilelive.etee.mobilelive.network;

public interface INetworkResponseListener {
    void onFailure(Object error);

    void onSuccess(Object object);
}
