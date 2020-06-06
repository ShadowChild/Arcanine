package me.shadowchild.arcanine.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonUtil {

    public static JsonElement getElementFromPath(Path path) throws IOException {

        BufferedReader br = Files.newBufferedReader(path);
        JsonReader jr = new JsonReader(br);

        JsonParser jp = new JsonParser();

        return jp.parse(jr);
    }

    public static JsonObject getObjectFromPath(Path path) throws IOException {

        return getElementFromPath(path).getAsJsonObject();
    }

    public static String getString(JsonObject obj, String name) {

        return obj.get(name).getAsString();
    }

    public static JsonObject getObject(JsonObject obj, String name) {

        return obj.getAsJsonObject(name);
    }

    public static byte getByte(JsonObject obj, String name) {

        return obj.getAsJsonPrimitive(name).getAsByte();
    }

    public static int getInt(JsonObject obj, String name) {

        return obj.getAsJsonPrimitive(name).getAsInt();
    }

    public static char getChar(JsonObject obj, String name) {

        return obj.getAsJsonPrimitive(name).getAsCharacter();
    }

    public static double getDouble(JsonObject obj, String name) {

        return obj.getAsJsonPrimitive(name).getAsDouble();
    }

    public static float getFloat(JsonObject obj, String name) {

        return obj.getAsJsonPrimitive(name).getAsFloat();
    }

    public static short getShort(JsonObject obj, String name) {

        return obj.getAsJsonPrimitive(name).getAsShort();
    }

    public static long getLong(JsonObject obj, String name) {

        return obj.getAsJsonPrimitive(name).getAsLong();
    }

    public static boolean getBool(JsonObject obj, String name) {

        return obj.getAsJsonPrimitive(name).getAsBoolean();
    }

    public static BigInteger getBigInt(JsonObject obj, String name) {

        return obj.get(name).getAsBigInteger();
    }

    public static BigDecimal getBigDec(JsonObject obj, String name) {

        return obj.get(name).getAsBigDecimal();
    }

    public static JsonArray getArray(JsonObject obj, String name) {

        return obj.getAsJsonArray(name);
    }
}
