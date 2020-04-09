package com.example.gson;

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);

}