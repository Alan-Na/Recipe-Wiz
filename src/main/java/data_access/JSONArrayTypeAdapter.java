package data_access;

import java.lang.reflect.Type;

import org.json.JSONArray;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Custom Gson type adapter for serializing and deserializing objects.
 */
public class JSONArrayTypeAdapter implements JsonDeserializer<JSONArray>, JsonSerializer<JSONArray> {

    /**
     * Serializes an org.json.JSONArray into a Gson JsonArray.
     *
     * @param src       the source {@link JSONArray} to serialize
     * @param typeOfSrc the specific genericized type of the source object
     * @param context   the context of the serialization process
     * @return a {@link JsonElement} representing the serialized {@link JSONArray}
     */
    @Override
    public JsonElement serialize(JSONArray src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < src.length(); i++) {
            jsonArray.add(src.get(i).toString());
        }
        return jsonArray;
    }

    /**
     * Deserializes a Gson JsonElement into an org.json.JSONArray.
     *
     * @param json   the JSON data being deserialized
     * @param typeOfT the specific generalized type of the desired object
     * @param context the context of the deserialization process
     * @return a {@link JSONArray} representing the deserialized data
     */
    @Override
    public JSONArray deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        final JSONArray jsonArray = new JSONArray();
        for (JsonElement element : json.getAsJsonArray()) {
            jsonArray.put(element.getAsString());
        }
        return jsonArray;
    }
}
