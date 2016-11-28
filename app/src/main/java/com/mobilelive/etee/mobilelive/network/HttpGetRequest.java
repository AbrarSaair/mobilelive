package com.mobilelive.etee.mobilelive.network;

import com.mobilelive.etee.mobilelive.AppPreference;

import java.io.IOException;
import java.net.ProtocolException;

public class HttpGetRequest extends BaseHttpRequest {

    public HttpGetRequest(String apiURL) {
        super(apiURL);
    }

    @Override
    protected void setRequestHeader() throws ProtocolException {
        connection.setRequestMethod("GET");
        connection.addRequestProperty("Cookie", "sessionId=" + AppPreference.getString(context, "session_id", "6a7a99fe-e257-3aca-6db4-0fcf2d2b509f"));

    }

    @Override
    protected void writePrams() throws IOException {
    }
}