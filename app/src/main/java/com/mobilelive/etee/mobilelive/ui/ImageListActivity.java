package com.mobilelive.etee.mobilelive.ui;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mobilelive.etee.mobilelive.PermissionUtils;
import com.mobilelive.etee.mobilelive.R;
import com.mobilelive.etee.mobilelive.adapters.ImageAdapter;
import com.mobilelive.etee.mobilelive.model.ImageObject;
import com.mobilelive.etee.mobilelive.network.AppNetworkConstants;
import com.mobilelive.etee.mobilelive.network.BaseHttpRequest;
import com.mobilelive.etee.mobilelive.network.BaseNetworkActivity;
import com.mobilelive.etee.mobilelive.network.HttpGetRequest;
import com.mobilelive.etee.mobilelive.network.INetworkRequest;
import com.mobilelive.etee.mobilelive.network.INetworkResponseListener;
import com.mobilelive.etee.mobilelive.parser.ImageParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.bitmap;

public class ImageListActivity extends BaseNetworkActivity {

    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    RecyclerView recyclerView;
    ImageAdapter adapter;
    ArrayList<ImageObject> images = new ArrayList<>();

    public interface OnItemClickListener {
        public void onItemClick(View view, int position, int id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        loadImageList();
        initAdapter();
    }

    /**
     * this method sends a GET network call to load list of images.
     */
    private void loadImageList() {
        BaseHttpRequest request = new HttpGetRequest(INetworkRequest.API_IMAGE_LIST);
        request.setPutCookieValue(true);
        executeSimpleRequest(request);
    }

    private void initComponents() {
        recyclerView = (RecyclerView) findViewById(R.id.images_recycler_view);

    }

    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_image, menu);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_select_image) {
            startGalleryChooser();
            return true;
        } else if (itemId == R.id.action_select_camera) {
            startCamera();
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    /**
     * Starts and intent to select image form gallery
     */
    public void startGalleryChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                GALLERY_IMAGE_REQUEST);
    }

    /**
     * Starts and intent to capture image form camera
     */
    public void startCamera() {
        if (PermissionUtils.requestPermission(this, CAMERA_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
            startCamera();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_IMAGE_REQUEST) {
            getImageDataFromCameraIntent(data);
        } else if (requestCode == GALLERY_IMAGE_REQUEST) {
            getImageDataFromGalleryIntent(data);
        }
    }

    /**
     * this method extract the bitmap form gallery intent and converts it to BASE-64 encoded string
     *
     * @param data
     */
    private void getImageDataFromGalleryIntent(Intent data) {
        try {
            Uri selectedImage = data.getData();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 200, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();

            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            uploadImageToServer("test", encodedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method extract the bitmap form camera intent and converts it to BASE-64 encoded string
     *
     * @param data
     */
    private void getImageDataFromCameraIntent(Intent data) {
        Bitmap bm = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 200, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        uploadImageToServer("camera", encodedImage);
    }

    /**
     * this method sends a POST network call to upload image. api response is received in the call back
     * passed as delegate.
     *
     * @param imageName image name
     * @param imageData image content as string
     */
    private void uploadImageToServer(String imageName, String imageData) {
        BaseHttpRequest request = new BaseHttpRequest(INetworkRequest.API_IMAGE_UPLOAD);
        request.addParameter(AppNetworkConstants.PARAM_NAME, String.valueOf(imageName));
        request.addParameter(AppNetworkConstants.PARAM_DATA, String.valueOf(imageData));
        request.setPutCookieValue(true);
        executeSimpleRequest(request, new INetworkResponseListener() {
            @Override
            public void onFailure(Object error) {
                //Failure
            }

            @Override
            public void onSuccess(Object object) {
                // upload success
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_image) {
            askForCameraOrGallery();
            return true;
        }
        return false;
    }

    private void askForCameraOrGallery() {
        registerForContextMenu(recyclerView);
        openContextMenu(recyclerView);
    }

    /**
     * load images api callback in case of failed api response
     */
    @Override
    public void onFailure(Object error) {
        Toast.makeText(ImageListActivity.this, R.string.error, Toast.LENGTH_SHORT);
    }

    /**
     * load images api callback in case of successful api response
     */
    @Override
    public void onSuccess(Object object) {
        images = (ArrayList<ImageObject>) new ImageParser().parseData(object);
        updateAdapter();
    }

    private void updateAdapter() {
        adapter.setData(images);
        adapter.notifyDataSetChanged();
    }

    protected void initAdapter() {
        if (adapter == null) {
            adapter = new ImageAdapter(images);
            adapter.setListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, int id) {
                    openDetailActivity(position);
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setData(images);
            adapter.notifyDataSetChanged();
        }
    }

    private void openDetailActivity(int position) {
        Intent i = new Intent(ImageListActivity.this, ImageDetailActivity.class);
        i.putExtra(AppNetworkConstants.PARAM_IMAGE_ID, images.get(position).getImageId());
        startActivity(i);
    }
}
