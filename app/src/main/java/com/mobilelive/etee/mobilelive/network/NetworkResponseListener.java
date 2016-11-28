package com.mobilelive.etee.mobilelive.network;

public class NetworkResponseListener implements INetworkResponseListener {

	private INetworkResponseListener listener;

	public NetworkResponseListener(INetworkResponseListener responseListener) {
		this.listener = responseListener;
	}

	@Override
	public void onSuccess(Object object) {
		if (this.listener != null) {
			this.listener.onSuccess(object);
		}
	}

	@Override
	public void onFailure(Object error) {
		if (this.listener != null) {
			this.listener.onFailure(error);
		}
	}
}