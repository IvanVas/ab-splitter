package pl.allegro.webapi;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class ConfigurationTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * @verifies parse specified valid config file
     * @see Configuration#Configuration(java.io.File)
     */
    @Test
    public void Configuration_shouldParseSpecifiedValidConfigFile() throws Exception {
        File config = getFileFromResource("configuration_valid.json");
        Configuration configuration = new Configuration(config);
        assertEquals(new Long(100L), configuration.getWeightsTotal());
        assertEquals(new Long(30L), configuration.getGroupWeight("a"));
        assertEquals(new Long(50L), configuration.getGroupWeight("c"));
    }

    /**
     * @verifies throw on invalid config file
     * @see Configuration#Configuration(java.io.File)
     */
    @Test
    public void Configuration_shouldThrowOnInvalidConfigFile() throws Exception {
        File config = getFileFromResource("configuration_invalid.json");
        thrown.expect(Configuration.ConfigurationException.class);
        Configuration configuration = new Configuration(config);
    }

    private File getFileFromResource(String resourceName) {
        return new File(getClass().getClassLoader().getResource(resourceName).getPath());
    }
}
