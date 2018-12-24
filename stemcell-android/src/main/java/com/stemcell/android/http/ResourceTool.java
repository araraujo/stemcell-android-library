package com.stemcell.android.http;

import android.app.Activity;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.stemcell.android.base.BaseApp;
import com.stemcell.android.base.ProgressApp;
import com.stemcell.android.beans.HttpMultipartBean;
import com.stemcell.android.interceptor.HttpInterceptor;
import okio.ByteString;

/**
 * Created by spassu on 01/07/15.
 */

public class ResourceTool {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String IMGUR_CLIENT_ID = "...";
    private static OkHttpClient client = createConnection();

    public static Call asyncGet(Activity activity, String message, String path, Callback callback) {

        if (activity != null) {
            ProgressApp.show(activity, message);
        }

        String appurl = BaseApp.getInstance().getAppUrl();

        Request request = new Request.Builder()
                .url(appurl + path)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;

    }

    public static Response get(Activity activity, String message, String path) {
        if (activity != null) {
            ProgressApp.show(activity, message);
        }

        String appurl = BaseApp.getInstance().getAppUrl();

        Request request = new Request.Builder()
                .url(appurl + path)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!response.isSuccessful()) {
            throw new RuntimeException("Unexpected code " + response);
        }

        return response;

    }

    public static Call asyncPost(Activity activity, String message, String path, String json, Callback callback) {

        if (activity != null) {
            ProgressApp.show(activity, message);
        }

        String appurl = BaseApp.getInstance().getAppUrl();

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(appurl + path)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;

    }

    public static Response post(Activity activity, String message, String path, String json) {

        if (activity != null) {
            ProgressApp.show(activity, message);
        }

        String appurl = BaseApp.getInstance().getAppUrl();

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(appurl + path)
                .post(body)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!response.isSuccessful()) {
            throw new RuntimeException("Unexpected code " + response);
        }

        return response;

    }

    public static Call asyncPostMultipart(Activity activity, String message, String path, List<HttpMultipartBean> parts, Callback callback) {

        if (activity != null) {
            ProgressApp.show(activity, message);
        }

        String appurl = BaseApp.getInstance().getAppUrl();

        MultipartBuilder builder = new MultipartBuilder();
        builder.type(MultipartBuilder.FORM);
        for (HttpMultipartBean part : parts) {
            MediaType mediaType = null;
            if (part.getMediaType() != null) {
                mediaType = MediaType.parse(part.getMediaType());
            }
            RequestBody requestBody;
            if (part.getContent() instanceof String) {
                requestBody = RequestBody.create(mediaType, (String) part.getContent());
            } else if (part.getContent() instanceof ByteString) {
                requestBody = RequestBody.create(mediaType, (ByteString) part.getContent());
            } else if (part.getContent() instanceof byte[]) {
                requestBody = RequestBody.create(mediaType, (byte[]) part.getContent());
            } else if (part.getContent() instanceof File) {
                requestBody = RequestBody.create(mediaType, (File) part.getContent());
            } else {
                throw new RuntimeException("Illegal http multipart form");
            }

            builder.addPart(
                    Headers.of("Content-Disposition", "form-data; name=\"" + part.getName() + "\""), requestBody);
        }

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url(appurl + path)
                .post(builder.build())
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static Response put(Activity activity, String message, String path, String json) {

        if (activity != null) {
            ProgressApp.show(activity, message);
        }

        String appurl = BaseApp.getInstance().getAppUrl();

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(appurl + path)
                .put(body)
                .build();

        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!response.isSuccessful()) {
            throw new RuntimeException("Unexpected code " + response);
        }

        return response;

    }

    public static Response delete(Activity activity, String message, String path, String json) {

        if (activity != null) {
            ProgressApp.show(activity, message);
        }

        String appurl = BaseApp.getInstance().getAppUrl();


        Request.Builder builder = new Request.Builder()
                .url(appurl + path);

        if (json != null && !json.isEmpty()) {
            RequestBody body = RequestBody.create(JSON, json);
            builder .delete(body);
        } else {
            builder.delete();
        }

        Request request = builder.build();

        Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!response.isSuccessful()) {
            throw new RuntimeException("Unexpected code " + response);
        }

        return response;

    }



    private static OkHttpClient createConnection() {

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(20, TimeUnit.SECONDS);
        client.interceptors().add(new HttpInterceptor());
        client.setCookieHandler(cookieManager);
        return client;
    }

}
