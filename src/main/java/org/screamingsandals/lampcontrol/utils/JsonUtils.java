package org.screamingsandals.lampcontrol.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * @author ScreamingSandals team
 */
public class JsonUtils {
    public static String serialize(Object instance) {
        return getGson().toJson(instance);
    }

    public static String serialize(Object instance, boolean expose) {
        if (expose) {
            return getExposeGson().toJson(instance);
        } else {
            return serialize(instance);
        }
    }

    public static <T> T deserialize(String jsonString, Class<?> customClass) {
        return getGson().fromJson(jsonString, (Type) customClass);
    }

    public static <T> T deserialize(String jsonString, Type type) {
        return getGson().fromJson(jsonString, type);
    }

    public static <T> T deserialize(String jsonString, Class<?> customClass, boolean expose) {
        if (expose) {
            return getExposeGson().fromJson(jsonString, (Type) customClass);
        } else {
            return deserialize(jsonString, customClass);
        }
    }

    private static Gson getExposeGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }


    public static Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }
}
