package com.kerolsme.androidrestapi;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class DeleteRestApi {



   private final URL urlSite;


   /*
   *
   * Delete By URL
   *
   * */

    public DeleteRestApi (@NonNull URL urlSite) {
        this.urlSite = urlSite;
    }


    public void Delete(@NonNull TheResult result)
    {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection httpsURLConnection = (HttpURLConnection) urlSite.openConnection();
                    httpsURLConnection.setRequestMethod("DELETE");
                    httpsURLConnection.setDoInput(true);
                    httpsURLConnection.setDoOutput(false);

                    // Check if the connection is successful
                    int responseCode = httpsURLConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream response = httpsURLConnection.getInputStream();
                        String text = readFromStream(response);
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                result.Succeed(text);
                            }
                        });

                    } else {
                        result.Error(HttpURLConnection.HTTP_INTERNAL_ERROR, new RuntimeException("connection error or There may be nothing"));
                    }

                } catch (RuntimeException | IOException e) {
                    result.Error(HttpURLConnection.HTTP_INTERNAL_ERROR,e);


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
