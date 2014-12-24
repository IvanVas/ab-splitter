package pl.allegro.webapi;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Thread-safe implementation of the logic of splitting users to different A/B testing groups
 */
public class Splitter {
    private final int weightsTotal;
    private final NavigableMap<Integer, String> groupsOnSortedRange = new TreeMap<>(); // represent the following segmented line |--A|---B|-----C|

    public Splitter(Configuration configuration) {
        weightsTotal = configuration.getWeightsTotal();
        NavigableMap<String, Integer> groupWeights = configuration.getGroupWeights();
        int lastPointOnRange = 0;
        for(Map.Entry<String, Integer> groupWeight : groupWeights.entrySet()) {
            lastPointOnRange += groupWeight.getValue();
            groupsOnSortedRange.put(lastPointOnRange, groupWeight.getKey());
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
        //TODO String::hashCode could be inappropriate hash function depending on the inputs. Consider FNV or similar. Won't improve performance much as proved using a profiler.
        int groupPointOnSortedRange = Math.abs(user.hashCode()) % weightsTotal;
        return getGroupFromInterval(groupPointOnSortedRange);
    }

    private String getGroupFromInterval(int groupPointOnSortedRange) {
        return groupsOnSortedRange.higherEntry(groupPointOnSortedRange).getValue(); // choose a segment |--A|---B|-----C| according to point's coordinate
    }
}
