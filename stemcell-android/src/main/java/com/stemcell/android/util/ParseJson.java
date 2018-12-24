package com.stemcell.android.util;

import android.util.Log;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Response;
import org.apache.commons.io.IOUtils;
import org.springframework.util.ReflectionUtils;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import com.stemcell.android.exception.BusinessException;
import com.stemcell.android.serializer.DateDeserializer;
import com.stemcell.android.serializer.EnumDeserializer;


/**
 * Created by melti on 03/07/15.
 */
public class ParseJson<E> {

    private GsonBuilder gsonBuilder;
    private Gson gson;
    private static Set<Class> classEnumAdapterList = new HashSet<Class>();

    private ParseJson() {
        gsonBuilder = new GsonBuilder();
        registerAdapters();
        gson = gsonBuilder.create();
    }

    private void registerAdapters() {
        for (Class cl : classEnumAdapterList) {
            gsonBuilder.registerTypeAdapter(cl, new EnumDeserializer(ReflectionUtils.findMethod(cl, "from", String.class)));
        }
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
    }

    public static void registerEnumClass(Class clasz) {
        classEnumAdapterList.add(clasz);
    }

    public static ParseJson createInstance() {
        ParseJson parseJson = new ParseJson();
        return parseJson;
    }

    public E parse(Class classz, Response response) {
        try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(response.body().byteStream(), writer, "UTF-8");
            String s = writer.toString();
            return  (E) gson.fromJson(s, classz);
        } catch (Exception e) {
            Log.e("ParseJson", e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    public E parse(Class classz, String json) {
        try {
            return  (E) gson.fromJson(json, classz);
        } catch (Exception e) {
            Log.e("ParseJson", e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    public String toJson(E object) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
