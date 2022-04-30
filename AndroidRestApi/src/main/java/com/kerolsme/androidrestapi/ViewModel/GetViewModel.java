package com.kerolsme.androidrestapi.ViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.kerolsme.androidrestapi.AppExecutors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;


public class GetViewModel extends  AndroidViewModel {

        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

        public GetViewModel(@NonNull Application application) {
            super(application);
        }


    public void Get(@NonNull URL urlSite) {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {


                String jsonResponse = "";

                // If the URL is null, then return early.
                if (urlSite == null) {

                    new RuntimeException("connection error");

                }else {

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
                            jsonResponse = readFromStream(inputStream);
                            setMutableLiveData(jsonResponse);

                        } else {
                            throw new RuntimeException("Error Code "+HttpURLConnection.HTTP_INTERNAL_ERROR);
                        }
                    } catch (RuntimeException | ProtocolException e) {

                        e.printStackTrace();

                    } catch (IOException e) {
                        e.printStackTrace();
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
                            } catch (RuntimeException | IOException e) {
                              e.printStackTrace();

                            }
                        }
                    }
                }
            }
        });
    }


        private void setMutableLiveData(String value) {
            mutableLiveData.postValue(value);
        }

        public MutableLiveData<String> getMutableLiveData() {
            return mutableLiveData;
        }

        private  String readFromStream(InputStream inputStream) throws IOException {
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