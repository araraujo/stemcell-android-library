package br.com.stemcell.android.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

public class ProgressApp3 {

    private static ProgressApp3 instance;

    private ProgressDialog progressBar = null;
    private Activity activity;


    private ProgressApp3() {}

    public static synchronized ProgressApp3 getInstance(){
        if(instance == null){
            instance = new ProgressApp3();
        }
        return instance;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressBar.dismiss();
            progressBar = null;
        }
    };

    public static void show(final Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                ProgressApp3 pa = getInstance();

                if (pa.progressBar != null) {
                    dismiss();
                }

                pa.activity = activity;

                pa.progressBar = new ProgressDialog(activity);
                pa.progressBar.setCancelable(false);
                pa.progressBar.setCanceledOnTouchOutside(false);
                pa.progressBar.setIndeterminate(true);
                pa.progressBar.setMessage(message);
                pa.progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pa.progressBar.show();
            }
        });
    }

    public static void dismiss() {
        ProgressApp3 pa = getInstance();
        if (pa.progressBar != null) {
            pa.handler.sendEmptyMessage(0);
        }
    }

}
