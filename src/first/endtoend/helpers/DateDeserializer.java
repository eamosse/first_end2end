package first.endtoend.helpers;


import java.lang.reflect.Type;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

@SuppressLint("UseValueOf")
public class DateDeserializer implements JsonDeserializer<Date> {
    @Override
	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String JSONDateToMilliseconds = "\\/(Date\\((.*?)(\\+.*)?\\))\\/";
        Pattern pattern = Pattern.compile(JSONDateToMilliseconds);
        Matcher matcher = pattern.matcher(json.getAsJsonPrimitive().getAsString());
        String result = matcher.replaceAll("$2");
        Log.v("Date", result);
        return new Date(new Long(result));
    }
}
