package com.jiaoerzhang.platform.lib_net.network.http;



import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;

public class HttpRequestPresenter<T> implements HttpRequest {

    private HttpRequest httpRequest;
    private static volatile HttpRequestPresenter instance;

    public HttpRequestPresenter(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public static void init(HttpRequest httpRequest){
        if (instance==null) {
            synchronized (HttpRequestPresenter.class){
                if (instance==null) {
                    instance = new HttpRequestPresenter(httpRequest);
                }
            }
        }
    }

    public static HttpRequestPresenter getInstance(){
        return instance;
    }

    @Override
    public void post(String url, HashMap<String,Object> params, ICallback callback) {
        httpRequest.post(url,params,callback);
    }

    @Override
    public void get(String url, HashMap<String, Object> params, ICallback callback) {
        httpRequest.get(url,params,callback);
    }

    @Override
    public void uploadFile(String fileUrl, HashMap<String, String> content, List<MultipartBody.Part> parts, ICallback callback) {
        httpRequest.uploadFile(fileUrl,content,parts,callback);
    }

    @Override
    public void jsonPostData(String url, HashMap<String,String>params,String body, ICallback callback) {

        httpRequest.jsonPostData(url,params,body,callback);
    }

    @Override
    public void jsonPostData(String url, String body, HashMap<String, String> params, ICallback callback) {

        httpRequest.jsonPostData(url,body,params,callback);
    }



}
