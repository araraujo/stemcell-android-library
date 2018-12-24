package com.stemcell.android.interceptor;

import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import com.stemcell.android.exception.BusinessException;

/**
 * Created by melti on 05/07/15.
 */

public class HttpInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response;

        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            throw new BusinessException("A comunicação com o servidor falhou. Verifique sua conexão com a internet.");
        }

        if (!response.isSuccessful()) {
            final InputStream is = response.body().byteStream();
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, "UTF-8");
            String theString = writer.toString();
            ErrorMessage message = new Gson().fromJson(theString, ErrorMessage.class);
            Log.e("HttpInterceptor", message.getSt());
            throw new BusinessException(message.getMessage());
        }

        return response;


    }

    private class ErrorMessage {
        private String st;
        private String message;

        public String getSt() {
            return st;
        }

        public void setSt(String st) {
            this.st = st;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
