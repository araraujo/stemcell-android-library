package com.stemcell.android.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ProgressActivity extends Activity {

    public static final String EXTRA_MSG = "extra_msg";

    private ProgressDialog mPd;
    private static Context contextFrom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();

        mPd = new ProgressDialog(this);
        mPd.setMessage(i.getStringExtra(EXTRA_MSG));
        mPd.setIndeterminate(true);
        mPd.setCancelable(false);
        mPd.setCanceledOnTouchOutside(false);
        mPd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mPd.show();
    }

    @Override
    public void onNewIntent (Intent i) {
        mPd.dismiss();
        contextFrom = null;
        finish();
    }

    public void onStop() {
        super.onStop();
        if (mPd != null && mPd.isShowing()) {
            mPd.dismiss();
        }
        contextFrom = null;
    }

    public static void actionStart (Context from, String msg) {
        contextFrom = from;
        Intent i = new Intent (from, ProgressActivity.class);
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(EXTRA_MSG, msg);
        from.startActivity(i);
    }

    public static void actionDismiss () {
        if (contextFrom != null) {
            Intent i = new Intent(contextFrom, ProgressActivity.class);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            //contextFrom.startActivity(i);
        }
    }
}
