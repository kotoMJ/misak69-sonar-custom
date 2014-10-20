package cz.misak69.sonar.plugin;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: misak
 * Date: 2014.10.06
 */
public class CustomPluginTest {

    @Test
    public void provide_extensions() {
        Assert.assertEquals(4,new CustomPlugin().getExtensions().size());
    }
}
