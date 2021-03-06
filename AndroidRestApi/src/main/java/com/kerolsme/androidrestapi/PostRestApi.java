package com.kerolsme.androidrestapi;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class PostRestApi {

        private final URL url;
        public  PostRestApi (URL url) {
        this.url = url;
    }

    /**
     *       @param hashMap : key and Value
     *       @param  result : request response
    **/

    public void Post(@NonNull HashMap<String, String> hashMap , @NonNull TheResult result) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Iterator myVeryOwnIterator = hashMap.keySet().iterator();
                    JSONObject jsonObject = null;
                    while (myVeryOwnIterator.hasNext()) {
                        String key = (String) myVeryOwnIterator.next();
                        String value = (String) hashMap.get(key);
                        jsonObject = new JSONObject();
                        try {
                            jsonObject.put(key, value);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // Convert JSONObject to String

                    String jsonObjectString = jsonObject != null ? jsonObject.toString() : null;
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Content-Type", ""); // The format of the content we're sending to the server
                    httpURLConnection.setRequestProperty("Accept", "application/json");// The format of response we want to get from the server
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);

                    // Send the JSON we created
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
                    outputStreamWriter.write(jsonObjectString);
                    outputStreamWriter.flush();

                    // Check if the connection is successful
                    int responseCode = httpURLConnection.getResponseCode();
                    if (responseCode == HttpsURLConnection.HTTP_CREATED) {
                        InputStream response = httpURLConnection.getInputStream();
                        String resultText = readFromStream(response);
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                result.Succeed(resultText);
                            }
                        });
                        // Convert raw JSON to pretty JSON using GSON library
                    } else {
                        result.Error(HttpURLConnection.HTTP_INTERNAL_ERROR, new RuntimeException("connection error"));
                    }
                }    catch (RuntimeException | IOException  e ){

                    result.Error(HttpURLConnection.HTTP_INTERNAL_ERROR, e);


                }
            }
        });


    }
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
