package pl.allegro.webapi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SplitterTest {
    public static final int WEIGHT_A = 3;
    public static final int WEIGHT_B = 2;
    public static final int WEIGHT_C = 5;
    public static final int WEIGHT_TOTAL = WEIGHT_A + WEIGHT_B + WEIGHT_C;
    public static final int STATISTICAL_TRIES = 10000;
    public static final double ACCEPTIBLE_STATISTICAL_DELTA = 0.02;

    @Mock
    Configuration configuration;

    Splitter splitter;
    List<String> allGroups;

    @Before
    public void setup() {
        NavigableMap<String, Integer> groupWeights = new TreeMap<>();
        groupWeights.put("a", WEIGHT_A);
        groupWeights.put("b", WEIGHT_B);
        groupWeights.put("c", WEIGHT_C);
        when(configuration.getGroupWeights()).thenReturn(groupWeights);
        when(configuration.getWeightsTotal()).thenReturn(WEIGHT_TOTAL);
        allGroups = Arrays.asList("a", "b", "c");
        splitter = new Splitter(configuration);
    }

    /**
     * @verifies return same group for the same user
     * @see Splitter#getGroupForUser(String)
     */
    @Test
    public void getGroupForUser_shouldReturnSameGroupForTheSameUser() throws Exception {
        String user = "user123";
        assertEquals(splitter.getGroupForUser(user), splitter.getGroupForUser(user));
    }

    /**
     * @verifies return one of the configured groups
     * @see Splitter#getGroupForUser(String)
     */
    @Test
    public void getGroupForUser_shouldReturnOneOfTheConfiguredGroups() throws Exception {
        String group = splitter.getGroupForUser("user123");
        assertTrue("Incorrect group returned", allGroups.contains(group));
    }

    /**
     * @verifies return statistically correct (in approx.) distribution of groups
     * @see Splitter#getGroupForUser(String)
     */
    @Test
    public void getGroupForUser_shouldReturnStatisticallyCorrectInApproxDistributionOfGroups() throws Exception {
        Map<String, List<String>> groupsToList = IntStream.range(0, STATISTICAL_TRIES).mapToObj((i) -> splitter.getGroupForUser(UUID.randomUUID().toString()))
                .collect(groupingBy(String::toString));
        float distributionOfGroupA = (float) groupsToList.get("a").size() / STATISTICAL_TRIES;
        assertTrue(distributionCorrespondsToWeight(distributionOfGroupA, WEIGHT_A));
        float distributionOfGroupB = (float) groupsToList.get("b").size() / STATISTICAL_TRIES;
        assertTrue(distributionCorrespondsToWeight(distributionOfGroupB, WEIGHT_B));
        float distributionOfGroupC = (float) groupsToList.get("c").size() / STATISTICAL_TRIES;
        assertTrue(distributionCorrespondsToWeight(distributionOfGroupC, WEIGHT_C));
    }

    private boolean distributionCorrespondsToWeight(float distributionOfGroupA, int groupWeight) {
        return (float) groupWeight / WEIGHT_TOTAL - ACCEPTIBLE_STATISTICAL_DELTA <= distributionOfGroupA
                && distributionOfGroupA <= (float) groupWeight + ACCEPTIBLE_STATISTICAL_DELTA;
    }
}
