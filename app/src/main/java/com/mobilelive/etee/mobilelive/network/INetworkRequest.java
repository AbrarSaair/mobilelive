package com.mobilelive.etee.mobilelive.network;


/**
 * The interface Network request.
 */
public interface INetworkRequest {

    String API_LOGIN = "http://mobilelive.getsandbox.com/users/login";
    String API_IMAGE_LIST = "http://mobilelive.getsandbox.com/v2/photos";
    String API_IMAGE_UPLOAD = "http://mobilelive.getsandbox.com/v2/photos";

    void execute();
}
