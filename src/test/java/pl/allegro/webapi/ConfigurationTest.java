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
        assertEquals(100, configuration.getWeightsTotal());
        assertEquals(30, configuration.getGroupWeight("a"));
        assertEquals(50, configuration.getGroupWeight("c"));
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