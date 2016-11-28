package com.mobilelive.etee.mobilelive.network;

import android.content.Context;
import android.util.Pair;

import com.mobilelive.etee.mobilelive.AppPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Base http request.
 */
public class BaseHttpRequest implements INetworkRequest {

    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
    protected static final String CONTENT_TYPE = "Content-Type";
    public static final String HASH_TAG = "#";
    public static final String AND = "&";
    public static final String EQUALS = "=";
    Context context;
    CookieManager cookieManager;
    private List<Pair> parameters;
    /**
     * The Api url request.
     */
    protected String apiUrlRequest;
    /**
     * The Send as json.
     */
    protected String resultString;
    /**
     * The Connection.
     */
    protected HttpURLConnection connection;
    /**
     * The Result object.
     */
    protected Object resultObject;
    /**
     * The Status code.
     */
    protected int statusCode;


    private boolean putCookieValue;
    String cookieValue = "6a7a99fe-e257-3aca-6db4-0fcf2d2b509f";

    /**
     * Instantiates a new Base http request.
     *
     * @param apiURL the api url
     */
    public BaseHttpRequest(final String apiURL) {
        this.apiUrlRequest = apiURL;
        initParameters();
    }

    /**
     * Gets connection.
     *
     * @return the connection
     * @throws IOException the io exception
     */
    public HttpURLConnection getConnection() throws IOException {
        URL url = new URL(apiUrlRequest);
        return ((HttpURLConnection) url.openConnection());
    }

    /**
     * Init parameters.
     */
    protected void initParameters() {
        parameters = new ArrayList<>();
    }

    public void addParameter(final String key, final String value) {
        parameters.add(new Pair(key, value));
    }

    protected void setRequestHeader() throws ProtocolException {
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        if (hasContentTypeEnabled()) {
            setHeaderForContentType();
        }
        if (putCookieValue) {
            connection.addRequestProperty("Cookie", "sessionId=" + AppPreference.getString(context, "session_id", "6a7a99fe-e257-3aca-6db4-0fcf2d2b509f"));
        }
    }

    /**
     * Has content type enabled boolean.
     *
     * @return the boolean
     */
    protected boolean hasContentTypeEnabled() {
        return true;
    }

    /**
     * Sets header for content type.
     */
    protected void setHeaderForContentType() {
        connection.setRequestProperty(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED);
    }

    protected void createRequest() throws IOException {
        connection = getConnection();
        //   cookieManager = new CookieManager();
        //   CookieHandler.setDefault(cookieManager);
    }

    @Override
    public void execute() {
        try {
            createRequest();
            setRequestHeader();
            executeRequest();
            writePrams();
            parseResponse();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected String getParamsAsURL() {

        final StringBuilder strBuilder = new StringBuilder();
        for (final Pair value : parameters) {
            strBuilder.append(value.first);
            strBuilder.append(EQUALS);
            strBuilder.append(value.second);
            strBuilder.append(AND);
        }
        String end = AND + HASH_TAG;
        String res = strBuilder.toString() + HASH_TAG;

        return res.replace(end, "");
    }

    protected void writePrams() throws IOException {
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(getParamsAsURL());
        wr.flush();
        wr.close();
    }


    /**
     * Execute request.
     *
     * @throws IOException the io exception
     */
    protected void executeRequest() throws IOException {
        connection.connect();
    }

    /**
     * Parse response.
     *
     * @throws IOException the io exception
     */

    protected void parseResponse() throws IOException {
        boolean isSucess = isSucess();
        if (isSucess) {
            readInputStream(connection.getInputStream());
            readCookie();
        } else {
            parseError();
        }
    }

    private void readCookie() {
        cookieManager = new CookieManager();
        Map<String, List<String>> cookie = connection.getHeaderFields();
        if (cookie != null && cookie.size() > 0 && cookie.containsKey("set-cookie")) {
            List<String> sessionId = cookie.get("set-cookie");
            if (sessionId != null) {
                for (String c : sessionId) {
                    cookieManager.getCookieStore().add(null, HttpCookie.parse(c).get(0));
                    String name = HttpCookie.parse(c).get(0).getName();
                    String value = HttpCookie.parse(c).get(0).getValue();
                    AppPreference.saveString(context, value, "session_id");
                }
            }
        }
    }

    private void readInputStream(InputStream inputStream) {
        resultObject = parseResponseAs(inputStream);
        if (resultObject == null) {
            this.resultObject = new String("");
        }
    }

    private Object parseResponseAs(InputStream inputStream) {
        Object result = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 1024);
            readSteamIntoString(bufferedReader);
            Object resultJson = tryToConvertInJsonObject(resultString);
            return resultJson;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected Object tryToConvertInJsonObject(String response) {
        Object result = null;
        try {
            result = new JSONObject(response);
        } catch (JSONException exception) {
        }
        return result;
    }

    protected void readSteamIntoString(final BufferedReader bufferedReader) throws IOException {
        String line;
        StringBuilder stringbuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringbuilder.append(line);
        }
        resultString = stringbuilder.toString();
    }

    private void parseError() {
        try {
            resultObject = String.valueOf(connection.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected boolean isSucess() throws IOException {

        statusCode = connection.getResponseCode();
        return (statusCode == 200) || (statusCode == 201) || (statusCode == 204);
    }

    public Object getResultObject() {
        return resultObject;
    }

    public void setPutCookieValue(boolean putCookieValue) {
        this.putCookieValue = putCookieValue;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}