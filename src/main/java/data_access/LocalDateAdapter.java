package data_access;

import java.lang.reflect.Type;
import java.time.LocalDate;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * A custom Gson adapter for serializing and deserializing objects.
 */
public class LocalDateAdapter implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {

    /**
     * Serializes LocalDate object into a JSON string.
     *
     * @param date       the {@link LocalDate} to serialize
     * @param typeOfSrc  the type of the source object
     * @param context    the context of the serialization process
     * @return a {@link JsonElement} representing the serialized {@link LocalDate}
     */
    @Override
    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.toString());
    }

    /**
     * Deserializes a JSON string into a LocalDate object.
     *
     * @param json       the JSON element to deserialize
     * @param typeOfT    the type of the target object
     * @param context    the context of the deserialization process
     * @return a {@link LocalDate} parsed from the JSON string
     */
    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return LocalDate.parse(json.getAsString());
    }
}
