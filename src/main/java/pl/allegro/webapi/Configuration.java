package pl.allegro.webapi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Configuration {
    public static final String CONF_GROUP_WEIGHTS = "groupWeights";

    private final Map<String, Long> groupWeights = new HashMap<>();
    private Long weightsTotal = 0L;

    /**
     * Parses the config file and constructs configuration
     * @should parse specified valid config file
     * @should throw on invalid config file
     *
     */
    public Configuration(File configFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(br);
            for (Map.Entry<String, JsonElement> groupWeight : ((JsonObject) jsonElement).get(CONF_GROUP_WEIGHTS).getAsJsonObject().entrySet()) {
                long weight = groupWeight.getValue().getAsLong();
                groupWeights.put(groupWeight.getKey(), weight);
                weightsTotal += weight;
            }
        } catch (Exception ex) {
            throw new ConfigurationException("Configuration can't be read", ex);
        }

    }

    public Long getGroupWeight(String group) {
        return groupWeights.get(group);
    }

    public Long getWeightsTotal() {
        return weightsTotal;
    }

    public class ConfigurationException extends RuntimeException {
        public ConfigurationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
