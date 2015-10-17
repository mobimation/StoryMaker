package com.mobimation.storymaker.transfer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;

/**
 * Transfer content to server
 */
public class Transmitter {
    private static final String TAG = Transmitter.class.getSimpleName();

    public void uploadFile(int directoryID, String filePath) {
        Bitmap bitmapOrg = BitmapFactory.decodeFile(filePath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        String upload_url = "http://medial.com/photo.jpg";
        bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);

        byte[] data = bao.toByteArray();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(upload_url);


        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        //       MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        try {
/*
            // Set Data and Content-type header for the image
            FileBody fb = new FileBody(new File("/Users/gunnar/file.jpg"));
            StringBody contentString = new StringBody("key", ContentType.TEXT_PLAIN);
            builder.addPart("my_file", fb);
            builder.addPart("directory_id", contentString);
            HttpEntity entity = builder.build();
            postRequest.setEntity(entity);

            HttpResponse response = httpClient.execute(postRequest);
            // Read the response
            String jsonString = EntityUtils.toString(response.getEntity());
            Log.e("response after uploading file ", jsonString);
*/
        } catch (Exception e) {
            Log.e("Error in uploadFile", e.getMessage());
        }
    }

}
