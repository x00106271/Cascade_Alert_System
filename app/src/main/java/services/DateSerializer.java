package services;

import android.annotation.SuppressLint;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@SuppressLint("SimpleDateFormat")
public class DateSerializer implements JsonSerializer<Date> {

    @Override
    public JsonElement serialize(Date date, Type type,
                                 JsonSerializationContext ctx) {
        JsonElement element = new JsonPrimitive(serialize(date));
        return element;
    }

    public static String serialize(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        String formatted = dateFormat.format(date);

        return formatted;
    }
}