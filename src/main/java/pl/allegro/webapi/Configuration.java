package pl.allegro.webapi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Configuration {
    public static final String CONF_GROUP_WEIGHTS = "groupWeights";

    private final NavigableMap<String, Integer> groupWeights = new TreeMap<>();
    private int weightsTotal = 0;

    /**
     * Parses the config file and constructs configuration
     * @should parse specified valid config file
     * @should throw on invalid config file
     */
    public Configuration(File configFile) throws ConfigurationException {
        try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(br);
            for (Map.Entry<String, JsonElement> groupWeight : ((JsonObject) jsonElement).get(CONF_GROUP_WEIGHTS).getAsJsonObject().entrySet()) {
                int weight = groupWeight.getValue().getAsInt();
                groupWeights.put(groupWeight.getKey(), weight);
                weightsTotal += weight;
            }
        } catch (Exception ex) {
            throw new ConfigurationException("Configuration can't be read", ex);
        }

    }

    public NavigableMap<String, Integer> getGroupWeights() {
        return groupWeights;
    }

    public int getGroupWeight(String group) {
        return groupWeights.get(group);
    }

    public int getWeightsTotal() {
        return weightsTotal;
    }

    public class ConfigurationException extends Exception {
        public ConfigurationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
