package br.com.stemcell.android.http;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

import br.com.stemcell.android.base.ProgressApp;


/**
 * Created by melti on 05/07/15.
 */
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
