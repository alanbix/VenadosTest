package com.alanbarrera.venadostest.network;

import com.alanbarrera.venadostest.models.Game;
import com.alanbarrera.venadostest.models.Player;
import com.alanbarrera.venadostest.models.Statistic;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

/**
 * This is the TypeAdapater used by Retrofit.
 */
public class DataDeserializer implements JsonDeserializer<List>
{
    @Override
    public List deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        // Get the data json object
        JsonObject jObject = json.getAsJsonObject();
        JsonObject jDataObject = jObject.get("data").getAsJsonObject();

        // Get the first key
        Iterator<String> keys = jDataObject.keySet().iterator();
        String key = keys.next();

        // Get the list
        JsonElement list = jDataObject.get(key);

        // Create a null list type.
        Type listType = null;

        // Set the listType depending on the key.
        switch (key)
        {
            case "games":
                listType = new TypeToken<List<Game>>(){}.getType();
                break;
            case "statistics":
                listType = new TypeToken<List<Statistic>>(){}.getType();
                break;
            case "team":
                list = getJPlayers(list.getAsJsonObject());
                listType = new TypeToken<List<Player>>(){}.getType();
                break;
            default:
                break;
        }

        return new Gson().fromJson(list, listType);
    }

    /**
     * De-group the players and return a single list
     * @param jTeam The object with the grouped players
     * @return A Json Array with the players NOT grouped
     */
    private JsonElement getJPlayers(JsonObject jTeam)
    {
        // Get a key iterator.
        Iterator<String> keys = jTeam.keySet().iterator();

        // Create new Json Array
        JsonArray jArrayPlayers = new JsonArray();

        // Iterate over the grouped players
        while (keys.hasNext())
        {
            // Extract the players of the group and add them in a single list
            jArrayPlayers.addAll(jTeam.get(keys.next()).getAsJsonArray());
        }

        // Return the single players list.
        return jArrayPlayers;
    }
}
