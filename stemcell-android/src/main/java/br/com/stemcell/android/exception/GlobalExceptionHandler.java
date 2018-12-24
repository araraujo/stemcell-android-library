package br.com.stemcell.android.exception;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.ExecutionException;
import br.com.stemcell.android.base.ProgressApp;
import br.com.stemcell.android.service.ErrorMessageService;


/**
 * Created by melti on 04/07/15.
 */

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "GlobalExceptionHandler";

    Context context;

    public GlobalExceptionHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ProgressApp.dismiss();
        Log.e(TAG, ex.getMessage(), ex);
        handleUncaughtException(thread, ex);
    }

    private void handleUncaughtException (Thread thread, Throwable e) {
        Throwable cleanException = cleanException((Exception)e);
        Intent intent = new Intent(context, ErrorMessageService.class);
        intent.putExtra("ERROR_MESSAGE", cleanException.getMessage());
        context.startService(intent);

        if (cleanException instanceof GcmRegistrationException) {

        }
    }

    public static Throwable cleanException(Exception t) {
        Throwable ex = t;
        while (ex != null && ex.getCause() != null
                && ((ex instanceof InvocationTargetException)
                || (ex instanceof UndeclaredThrowableException)
                || (ex instanceof ExecutionException)
                || (ex instanceof RuntimeException))) {
            ex = ex.getCause();
        }
        return ex;
    }

}
