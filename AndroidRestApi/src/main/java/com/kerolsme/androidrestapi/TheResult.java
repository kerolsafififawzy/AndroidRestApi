package com.kerolsme.androidrestapi;

public interface TheResult {

    void Error(int errorCode , Throwable throwable);
    void Succeed(String jsonResponse);

}
