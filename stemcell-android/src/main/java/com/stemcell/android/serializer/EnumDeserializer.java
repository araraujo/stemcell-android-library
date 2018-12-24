package com.stemcell.android.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by melti on 02/07/15.
 */
public class EnumDeserializer<E> implements JsonDeserializer<E> {

    private final Method method;

    public EnumDeserializer(Method method) {
        this.method = method;
    }

    @Override
    public E deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        JsonObject jObject = (JsonObject) element;
        String enumId = jObject.get("id").getAsString();
        return (E) ReflectionUtils.invokeMethod(method, null, enumId);
    }
}
