package com.mobilelive.etee.mobilelive.parser;

import com.mobilelive.etee.mobilelive.model.ImageObject;
import com.mobilelive.etee.mobilelive.network.AppNetworkConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Iterator;

public class ImageParser implements IParser {

    @Override
    public Object parseData(Object object) {
        if (object != null && object instanceof JSONObject) {
            JSONObject data = (JSONObject) object;
            Iterator<String> iter = data.keys();
            ArrayList<ImageObject> images = new ArrayList<>();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    JSONObject value = (JSONObject) data.get(key);
                    ImageObject obj = new ImageObject();
                    obj.setImageName(value.getString(AppNetworkConstants.PARAM_NAME));
                    obj.setImageId(value.getString(AppNetworkConstants.PARAM_ID));
                    obj.setData(value.getString(AppNetworkConstants.PARAM_DATA));
                    obj.setObject(value.getString(AppNetworkConstants.PARAM_OBJECT));
                    obj.setCreatedAt(value.getString(AppNetworkConstants.PARAM_CREATED_AT));
                    obj.setUpdatedAt(value.getString(AppNetworkConstants.PARAM_UPDATED_AT));

                    images.add(obj);
                } catch (JSONException e) {
                }
            }
            return images;
        }
        return null;
    }

    public Object parseImageObject(Object object) {
        if (object != null && object instanceof JSONObject) {
            JSONObject data = (JSONObject) object;
            try {
                ImageObject obj = new ImageObject();
                obj.setImageName(data.getString(AppNetworkConstants.PARAM_NAME));
                obj.setImageId(data.getString(AppNetworkConstants.PARAM_ID));
                obj.setData(data.getString(AppNetworkConstants.PARAM_DATA));
                obj.setObject(data.getString(AppNetworkConstants.PARAM_OBJECT));
                obj.setCreatedAt(data.getString(AppNetworkConstants.PARAM_CREATED_AT));
                obj.setUpdatedAt(data.getString(AppNetworkConstants.PARAM_UPDATED_AT));
                return obj;
            } catch (JSONException e) {

            }
        }
        return null;
    }

    @Override
    public Object parseInputStream(BufferedReader bufferedReader) {
        return null;
    }
}
