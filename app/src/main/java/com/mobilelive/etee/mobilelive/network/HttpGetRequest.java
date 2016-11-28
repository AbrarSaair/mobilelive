package com.mobilelive.etee.mobilelive.network;

import java.io.IOException;
import java.net.ProtocolException;

public class HttpGetRequest extends BaseHttpRequest {

    public HttpGetRequest(String apiURL) {
        super(apiURL);
    }

    @Override
    protected void setRequestHeader() throws ProtocolException {
        connection.setRequestMethod("GET");
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Linux; U; Android 2.2.1; en-us; MB525 Build/3.4.2-107_JDN-9) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");

        connection.addRequestProperty("Cookie", "sessionid=" + cookieValue);

    }

    @Override
    protected void writePrams() throws IOException {
    }
}