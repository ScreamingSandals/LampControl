package org.screamingsandals.lampcontrol.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * @author ScreamingSandals team
 */
public class JsonUtils {
    public static void serialize(Object instance, Writer writer) {
        getGson().toJson(instance, writer);
    }

    public static <T> T deserialize(Reader reader, Type type) {
        return getGson().fromJson(reader, type);
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }
}
