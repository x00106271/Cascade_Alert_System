package services;

import android.annotation.SuppressLint;
import android.util.Log;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

@SuppressLint("SimpleDateFormat")
public class DateDeserializer implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement element, Type type,
                            JsonDeserializationContext ctx) throws JsonParseException {
        String strVal = element.getAsString();
        try {
                return deserialize(strVal);
        } catch (ParseException e) {
            Log.i("date not being parsed: ",strVal);
            throw new JsonParseException(e);
        }

    }

    public static Date deserialize(String strVal) throws ParseException {
        // Change Z to +00:00 to adapt the string to a format
        // that can be parsed in Java
        String s=strVal;
        s=s.substring(0, 19)+"Z";
        s= s.replace("Z", "+00:00");
        try {
            // Remove the ":" character to adapt the string to a
            // format
            // that can be parsed in Java
            s = s.substring(0, 21) + s.substring(23)+s.substring(24);
        } catch (IndexOutOfBoundsException e) {
            throw new JsonParseException("Invalid length");
        }
        // Parse the well-formatted date string
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ssZ");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date date = dateFormat.parse(s);

        return date;
    }

}
