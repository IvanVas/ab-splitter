package pl.allegro.webapi;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Thread-safe implementation of the logic of splitting users to different A/B testing groups
 */
public class Splitter {
    private final int weightsTotal;
    private final NavigableMap<Integer, String> groupsSortedPlane = new TreeMap<>();

    public Splitter(Configuration configuration) {
        weightsTotal = configuration.getWeightsTotal();
        NavigableMap<String, Integer> groupWeights = configuration.getGroupWeights();
        int lastPointOnWeightPlace = 0;
        for(Map.Entry<String, Integer> groupWeight : groupWeights.entrySet()) {
            lastPointOnWeightPlace += groupWeight.getValue();
            groupsSortedPlane.put(lastPointOnWeightPlace, groupWeight.getKey());
        }
    }

    /**
     * Returns the user's testing group. Statistical distribution of returned groups corresponds to weights specified in configuration
     *
     * @should return same group for the same user
     * @should return one of the configured groups
     * @should return statistically correct (in approx.) distribution of groups
     */
    public String getGroupForUser(String user) {
        int groupIntervalPoint = Math.abs(user.hashCode()) % weightsTotal;
        return getGroupFromInterval(groupIntervalPoint);
    }

    private String getGroupFromInterval(int groupIntervalPoint) {
        return groupsSortedPlane.higherEntry(groupIntervalPoint).getValue();
    }
}
