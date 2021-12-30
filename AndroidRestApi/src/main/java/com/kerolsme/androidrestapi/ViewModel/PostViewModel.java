package com.kerolsme.androidrestapi.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.kerolsme.androidrestapi.AppExecutors;
import com.kerolsme.androidrestapi.RequestType;
import com.kerolsme.androidrestapi.TheResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;

public class PostViewModel extends AndroidViewModel {

    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();


    /*
    *
    *
    * Post Support Mvvm
    *
    * */
    public PostViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     *       @param hashMap : key and Value
     *       @param  type  : Request Type
     *       @param  url : url for Site
     **/
   public void Post(@NonNull HashMap<String, String> hashMap, RequestType type, URL url) {
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
                jsonObject.put(key, value);
            }
            // Convert JSONObject to String

            String jsonObjectString = jsonObject != null ? jsonObject.toString() : null;
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", getType(type)); // The format of the content we're sending to the server
            httpURLConnection.setRequestProperty("Accept", getType(type));// The format of response we want to get from the server
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            // Send the JSON we created
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
            outputStreamWriter.write(jsonObjectString);
            outputStreamWriter.flush();

            // Check if the connection is successful
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream response = httpURLConnection.getInputStream();
                String resultText = readFromStream(response);
                setMutableLiveData(resultText);



                // Convert raw JSON to pretty JSON using GSON library


            } else {
                throw new RuntimeException("Error Code "+HttpURLConnection.HTTP_INTERNAL_ERROR);
            }
        }catch (RuntimeException | IOException | JSONException e ){
            e.printStackTrace();

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
    public String getType (RequestType type) {

        if(type == RequestType.JSON) {
            return  "application/json";
        }else {
            return "application/xml";
        }
    }
}
