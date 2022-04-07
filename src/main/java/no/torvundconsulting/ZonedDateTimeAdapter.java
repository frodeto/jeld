package no.torvundconsulting;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeAdapter implements JsonSerializer<ZonedDateTime> {
    public JsonElement serialize(ZonedDateTime dateTime, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

}
