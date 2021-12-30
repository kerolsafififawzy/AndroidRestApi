package com.kerolsme.androidrestapi.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.kerolsme.androidrestapi.AppExecutors;
import com.kerolsme.androidrestapi.TheResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class DeleteViewModel extends AndroidViewModel {

    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();


    /*
     *
     *
     * Delete Support Mvvm
     *
     * */

    public DeleteViewModel(@NonNull Application application) {
        super(application);
    }


    public void Delete(URL urlSite)
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
                        setMutableLiveData(readFromStream(response));
                    } else {
                        throw new RuntimeException("Error Code "+HttpURLConnection.HTTP_INTERNAL_ERROR);
                    }

                } catch (RuntimeException | IOException e) {
                    e.printStackTrace();
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

    private void setMutableLiveData(String value) {
        mutableLiveData.postValue(value);
    }

    public MutableLiveData<String> getMutableLiveData() {
        return mutableLiveData;
    }
}
