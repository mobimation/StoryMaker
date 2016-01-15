package com.mobimation.storymaker;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by gunnar on 16-01-15.
 */
public class S3Example extends AsyncTask<Object,Object,Integer> {
    @Override
    protected Integer doInBackground(Object... params) {

        Activity a=(Activity)params[0]; // Pass activity as first argument
        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                a.getApplicationContext(), // Application Context
                "mobimation", // Identity Pool ID
                Regions.EU_CENTRAL_1 // Region enum
        );

        AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider);
        try {
            // Upload file
            File fileToUpload = new File("kalle");
            PutObjectRequest putRequest = new PutObjectRequest("mobimation", "testfile",
                    fileToUpload);
            PutObjectResult putResponse = s3Client.putObject(putRequest);

            // Get it from bucket
            GetObjectRequest getRequest = new GetObjectRequest("mobimation", "testfile");
            S3Object getResponse = s3Client.getObject(getRequest);
            InputStream myObjectBytes = getResponse.getObjectContent();

            // Do what you want with the object

            myObjectBytes.close();
        } catch (IOException ioe) {

        }
        return null;
    }
    protected void onPostExecute(Integer result) {
        Log.d("S3Example","onPostExecute() result="+result);
    }

}
