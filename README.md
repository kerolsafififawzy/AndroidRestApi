# Android Rest Api
the Android library for Api

## Features

- Support Methods { Get , Put , Delete , Post }
- Support Mvvm

## How To Use

- Add in build.gradle:
```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


```
- Add gradle dependency:
```gradle
dependencies {

	        implementation 'com.github.kerolsafififawzy:AndroidRestApi:1.0.1'
	
      }
```
- Add permission
```gradle
  <uses-permission android:name="android.permission.INTERNET"/>
```
- class not support Mvvm
```java
GetRestApi.class
PutRestApi.class
DeleteRestApi.class
PostRestApi.class
```
- class support Mvvm
```java
GetViewModel.class
PostViewModel.class
DeleteViewModel.class
PutViewModel.class
```

- ### Example For Post 

- PostRestApi
```java
 PostRestApi postRestApi = new PostRestApi(new URL("https://RestApi"));
        HashMap hashMap = new HashMap();
        hashMap.put("name","Kerols");
        postRestApi.Post(hashMap, RequestType.JSON, new TheResult() {
            @Override
            public void Error(int errorCode, Throwable throwable) {
                
            }

            @Override
            public void Succeed(String jsonResponse) {

            }
        });
```
- PostViewModel
```java
 PostViewModel postViewModel  = new ViewModelProvider(MainActivity.this).get(PostViewModel.class);
        HashMap hashMap = new HashMap();
        hashMap.put("name","Kerols");
        postViewModel.Post(hashMap,RequestType.JSON,new URL("https://RestApi"));
        postViewModel.getMutableLiveData().observe(MainActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                
            }
        });
```
- ### Example For Get 

- GetRestApi
```java
        GetRestApi getRestApi = new GetRestApi(new URL("https://RestApi"));
        getRestApi.Get(new TheResult() {
            @Override
            public void Error(int errorCode, Throwable throwable) {

            }

            @Override
            public void Succeed(String jsonResponse) {

            }
        });
```
- GetVeiwModel
```java
        GetViewModel getViewModel = new ViewModelProvider(MainActivity.this).get(GetViewModel.class);
        getViewModel.Get(new URL("https://RestApi"));
        getViewModel.getMutableLiveData().observe(MainActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });
```

