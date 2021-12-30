package com.kerolsme.restapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kerolsme.androidrestapi.DeleteRestApi;
import com.kerolsme.androidrestapi.GetRestApi;
import com.kerolsme.androidrestapi.PostRestApi;
import com.kerolsme.androidrestapi.PutRestApi;
import com.kerolsme.androidrestapi.RequestType;
import com.kerolsme.androidrestapi.ViewModel.DeleteViewModel;
import com.kerolsme.androidrestapi.ViewModel.GetViewModel;
import com.kerolsme.androidrestapi.TheResult;
import com.kerolsme.androidrestapi.ViewModel.PutViewModel;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





    }
}