package com.stemcell.android.http;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

import com.stemcell.android.base.ProgressApp;


public abstract class BaseCallback implements Callback {

    @Override
    public void onFailure(Request request, IOException e) {
        ProgressApp.dismiss();
        //ProgressActivity.actionDismiss();
    }

    @Override
    public void onResponse(Response response) throws IOException {
        ProgressApp.dismiss();
        //ProgressActivity.actionDismiss();
        onResponseOk(response);
    }

    public abstract void onResponseOk(Response response);

}
