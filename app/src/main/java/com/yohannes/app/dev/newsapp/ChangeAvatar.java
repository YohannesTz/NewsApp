package com.yohannes.app.dev.newsapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yohannes.app.dev.newsapp.models.User;
import com.yohannes.app.dev.newsapp.util.CircleAvatar;
import com.yohannes.app.dev.newsapp.util.DbManager;
import com.yohannes.app.dev.newsapp.util.Util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

public class ChangeAvatar extends AppCompatActivity {

    private ImageView avatarFullScreen;
    private Button uploadImagebtn;
    private int PICK_IMAGE_REQUEST = 1;
    private int CAMERA_REQUEST = 0;
    private Uri filepath;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_avatar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        avatarFullScreen = (ImageView) findViewById(R.id.avatarFullScreen);
        uploadImagebtn = (Button) findViewById(R.id.uploadImagebtn);

        DbManager dbManager = new DbManager(this, null, 1);
        final User loggedInuser = dbManager.getLastLoggedinUser();

        Picasso.with(ChangeAvatar.this).load(loggedInuser.getAvatar_link()).into(avatarFullScreen);

        uploadImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filepath != null) {
                    new UploadImage(loggedInuser.getUsername(), getImageName(filepath), getStringImage(bitmap)).execute();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_change_avatar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.fromGallery) {
            showFileManager();
        } else if (item.getItemId() == R.id.takePhoto) {
            showCamera();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFileManager() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void showCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            Log.e("Result Code", String.valueOf(requestCode) + ", " + String.valueOf(resultCode) + ", " + String.valueOf(RESULT_OK));
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        filepath = data.getData();
                        Bundle extras = data.getExtras();
                        bitmap = (Bitmap) extras.get("data");
                        avatarFullScreen.setImageBitmap(bitmap);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        filepath = data.getData();
                        try {
                            Log.e("filePath", filepath.toString());
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                            avatarFullScreen.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public String getImageName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }

            if (result == null) {
                result = uri.getPath();
                int cut = result.lastIndexOf('/');
                if (cut != -1) {
                    result = result.substring(cut + 1);
                }
            }
        }
        return result;
    }

    public class UploadImage extends AsyncTask<Bitmap, Void, Void> {

        private Context context;
        private ProgressDialog progressDialog;
        private String resultTag = "UnSucessful";
        private HttpURLConnection connection;

        private String username, filename, image;

        public UploadImage(String username, String filename, String image) {
            this.username = username;
            this.filename = filename;
            this.image = image;
        }

        private HttpURLConnection getChangeAvatarConn(String username, String filename, String image) {
            //Log.e("imageBase64", image);
            try {
                final URL url = new URL(Util.UPLOADIMAGEADRESS);
                final HttpURLConnection connection = connectToUrl(url, "POST");
                if (connection != null) {
                    final String query = String.format("username=%s&filename=%s&image=%s", URLEncoder.encode(username, Util.ENCODING), URLEncoder.encode(filename, Util.ENCODING), URLEncoder.encode(image, Util.ENCODING));
                    final OutputStream stream = connection.getOutputStream();
                    try {
                        stream.write(query.getBytes(Util.ENCODING));
                    } catch (IOException ex) {
                        Log.e("error_uploadImage", ex.toString());
                    }
                }
                return connection;
            } catch (IOException ex) {
                ex.printStackTrace();
                Log.i("eror", ex.toString());
                return null;
            }
        }

        private HttpURLConnection connectToUrl(final URL url, final String method) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(Util.CONNECTION_TIMEOUT);
                connection.setReadTimeout(Util.READ_TIMEOUT);
                if (method.equals("POST")) {
                    connection.setDoOutput(true);
                }
                connection.setRequestMethod(method);

            } catch (IOException e) {
                Log.i("error", "Unable to establish a stable Connection. please try again.");
            }
            return connection;
        }

        private String getServerResponse(final HttpURLConnection connection) {
            try {
                final InputStream stream = connection.getInputStream();
                final BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);
                final StringBuilder builder = new StringBuilder();
                final byte[] data = new byte[1024];
                while (bufferedInputStream.read(data) > 0) {
                    builder.append(new String(data));
                    Arrays.fill(data, (byte) 0);
                }
                final String result = builder.toString();
                final StringBuilder fResult = new StringBuilder();
                for (final char chr : result.toCharArray()) {
                    if (chr != '\0') {
                        fResult.append(chr);
                    }
                }
                // return new JSONObject(fResult.toString());
                return fResult.toString();
            } catch (Exception x) {
                Log.i("eror", "Unable to establish a stable Connection. please try again.");
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ChangeAvatar.this);
            progressDialog.setMessage("Uploading Image...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Bitmap... params) {
            connection = getChangeAvatarConn(username, filename, image);
            if (connection != null) {
                String response = getServerResponse(connection);
                Log.e("ChangeAvater", response);
                if (!Util.isJsonvalid(response)) {
                    resultTag = String.valueOf(response.replaceAll("\"", ""));
                    Log.e("Server Response", response);
                } else {
                    Log.e("Sever Response", response);
                    updateUser(response);
                }
            } else {
                Log.e("newsApp", "Conneciton is null Please try again!");
                resultTag = "Connection problem! please try again!";
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            showToast(resultTag);
            if (connection != null) {
                connection.disconnect();
            }
            if (resultTag.equals("Sucessfully Uploaded Image!"))
                finish();
        }
    }

    private void updateUser(String response) {
        Util util = new Util();
        User updatedUser = util.loggedInUser(response);
        DbManager dbManager = new DbManager(this, null, 1);
        dbManager.UpdateUser(updatedUser);
    }

}
