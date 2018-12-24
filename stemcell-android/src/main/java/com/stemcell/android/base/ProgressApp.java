package com.stemcell.android.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;


public class ProgressApp {

    private static final int PROGRESS_START = 0;
    private static final int PROGRESS_FINISH = 1;

    private static ProgressApp instance;

    private ProgressDialog progressBar = null;
    private static Activity activity = null;
    private String message = null;


    private ProgressApp() {}

    public static synchronized ProgressApp getInstance(){
        if(instance == null){
            instance = new ProgressApp();
        }
        return instance;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESS_START:
                    if (progressBar != null && progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                    progressBar = new ProgressDialog(activity);
                    progressBar.setCancelable(false);
                    progressBar.setCanceledOnTouchOutside(false);
                    progressBar.setIndeterminate(true);
                    progressBar.setMessage(message);
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.show();
                    break;
                case PROGRESS_FINISH:
                    if(progressBar != null && progressBar.isShowing()){
                        progressBar.dismiss();
                    }
                    break;
            }
        }
    };

    public static void show(final Activity activity, final String message) {

        ProgressApp.activity = activity;
        activity.runOnUiThread(new Runnable() {
            public void run() {

                ProgressApp pa = getInstance();
                pa.activity = activity;
                pa.message = message;

                Message m = new Message();
                m.what = PROGRESS_START;
                pa.handler.sendMessage(m);

            }
        });
    }

    public static void dismiss() {
        if (ProgressApp.activity != null) {
            ProgressApp.activity.runOnUiThread(new Runnable() {
                public void run() {
                    ProgressApp pa = getInstance();
                    Message m = new Message();
                    m.what = PROGRESS_FINISH;
                    pa.handler.sendMessage(m);

                }
            });
        }
    }

}