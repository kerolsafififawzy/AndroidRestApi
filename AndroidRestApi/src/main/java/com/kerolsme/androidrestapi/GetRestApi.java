package com.kerolsme.androidrestapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class GetRestApi {


   private final URL urlSite;
    public GetRestApi (URL urlSite) {

        this.urlSite = urlSite;
    }

    public void Get(TheResult result){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                // If the URL is null, then return early.
                if (urlSite == null) {

                    result.Error(HttpURLConnection.HTTP_INTERNAL_ERROR,new RuntimeException("connection error"));

                } else {
                    HttpURLConnection urlConnection = null;
                    InputStream inputStream = null;
                    try {
                        urlConnection = (HttpURLConnection) urlSite.openConnection();
                        urlConnection.setReadTimeout(10000 /* milliseconds */);
                        urlConnection.setConnectTimeout(15000 /* milliseconds */);
                        urlConnection.setRequestMethod("GET");
                        urlConnection.connect();

                        // If the request was successful (response code 200),
                        // then read the input stream and parse the response.
                        if (urlConnection.getResponseCode() == 200) {
                            inputStream = urlConnection.getInputStream();
                           String jsonResponse = readFromStream(inputStream);
                            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    result.Succeed(jsonResponse);

                                }
                            });

                        } else {
                            result.Error(HttpURLConnection.HTTP_INTERNAL_ERROR, new RuntimeException("connection error or There may be nothing"));

                        }
                    } catch (IOException e) {

                        result.Error(HttpURLConnection.HTTP_INTERNAL_ERROR,e);

                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                        if (inputStream != null) {
                            // Closing the input stream could throw an IOException, which is why
                            // the makeHttpRequest(URL url) method signature specifies than an IOException
                            // could be thrown.
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                result.Error(HttpURLConnection.HTTP_INTERNAL_ERROR,e);
                            }
                        }
                    }
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
