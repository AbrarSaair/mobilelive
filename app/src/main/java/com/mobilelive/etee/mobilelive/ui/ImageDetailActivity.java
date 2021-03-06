package com.mobilelive.etee.mobilelive.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilelive.etee.mobilelive.R;
import com.mobilelive.etee.mobilelive.model.ImageObject;
import com.mobilelive.etee.mobilelive.network.AppNetworkConstants;
import com.mobilelive.etee.mobilelive.network.BaseHttpRequest;
import com.mobilelive.etee.mobilelive.network.BaseNetworkActivity;
import com.mobilelive.etee.mobilelive.network.HttpGetRequest;
import com.mobilelive.etee.mobilelive.network.INetworkRequest;
import com.mobilelive.etee.mobilelive.parser.ImageParser;

public class ImageDetailActivity extends BaseNetworkActivity {

    String imageID;
    TextView imageId;
    TextView imageName;
    ImageView imageData;
    TextView imageType;
    TextView imageCreated;
    TextView imageUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        initComponents();
        getImageId(getIntent().getExtras());
        loadImageDetail();
    }

    /**
     * this method sends a GET network call to load image detail.
     */
    private void loadImageDetail() {
        BaseHttpRequest request = new HttpGetRequest(INetworkRequest.API_IMAGE_UPLOAD + "/" + imageID);
        request.setContext(context);
        request.setPutCookieValue(true);
        executeSimpleRequest(request);
    }

    private void getImageId(Bundle extras) {
        if (extras.containsKey(AppNetworkConstants.PARAM_IMAGE_ID)) {
            imageID = extras.getString(AppNetworkConstants.PARAM_IMAGE_ID);
        }
    }

    private void initComponents() {
        imageId = (TextView) findViewById(R.id.image_id);
        imageName = (TextView) findViewById(R.id.image_name);
        imageData = (ImageView) findViewById(R.id.image_data);
        imageType = (TextView) findViewById(R.id.image_type);
        imageCreated = (TextView) findViewById(R.id.image_created);
        imageUpdated = (TextView) findViewById(R.id.image_updated);
    }

    @Override
    public void onSuccess(Object object) {
        super.onSuccess(object);
        ImageObject imageObject = (ImageObject) new ImageParser().parseImageObject(object);
        setImageData(imageObject);
    }

    @Override
    public void onFailure(Object error) {
        super.onFailure(error);
        Toast.makeText(ImageDetailActivity.this, R.string.error, Toast.LENGTH_SHORT);
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private void setImageData(ImageObject imageObject) {
        if (imageObject != null) {
            imageId.setText(imageObject.getImageId());
            imageName.setText(imageObject.getImageName());
            imageType.setText(imageObject.getObject());
            imageData.setImageBitmap(StringToBitMap(imageObject.getData()));
            imageCreated.setText(imageObject.getCreatedAt());
            imageUpdated.setText(imageObject.getUpdatedAt());
        }
    }
}
