package astra.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import lombok.Getter;

import java.util.function.Consumer;

public final class JsonHandler {

    @Getter
    private Gson cleanGson;
    @Getter private Gson gson;
    @Getter private JsonParser jsonParser;
    private GsonBuilder tempBuilder;

    public JsonHandler() {
        cleanGson = new Gson();
        jsonParser = new JsonParser();
        tempBuilder = new GsonBuilder();
    }

    public void finalTouchAndCreate(Consumer<GsonBuilder> consumer) {
        if (consumer != null)
            consumer.accept(tempBuilder);
        gson = tempBuilder.create();
        tempBuilder = null;
    }
}
