package com.stemcell.android.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ErrorMessageService extends IntentService {

    private static final String TAG = "ErrorMessageService";

    public ErrorMessageService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), intent.getStringExtra("ERROR_MESSAGE"), Toast.LENGTH_LONG).show();
            }
        });
    }

}
