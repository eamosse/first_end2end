package first.endtoend.helpers;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateSerializer implements JsonSerializer<Object>
{
    public JsonElement serialize(Date date, Type typeOfT, JsonSerializationContext context)
    {
        return new JsonPrimitive("/Date(" + date.getTime() + ")/");
    }

    @Override
	public JsonElement serialize(Object arg0, Type arg1, JsonSerializationContext arg2) {

        Date date = (Date) arg0;
        return new JsonPrimitive("/Date(" + date.getTime() + ")/");
    }
}

