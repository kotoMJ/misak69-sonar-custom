package cz.misak69.sonar.plugin;

import com.google.common.collect.ImmutableList;
import cz.misak69.sonar.plugin.rules.PublicAccessImportRule;
import cz.misak69.sonar.plugin.rules.TestRule;
import org.slf4j.LoggerFactory;
import org.sonar.api.SonarPlugin;

import java.util.List;

/**
 * Entry point of the plugin.
 * This class MUST be mentioned in MANIFEST.MF of deployed jar!
 *
 * Create a class extending SonarPlugin which returns Extensions created in CustomRulesDefinition and CustomRulesRepository.

 */
public class CustomPlugin extends SonarPlugin {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CustomPlugin.class);

    /**
     * Declares all the extensions implemented in the plugin
     */
    @Override
    public List getExtensions() {
        logger.debug("visited...");
        return ImmutableList.of(CustomQualityProfile.class,
                                CustomRulesDefinition.class,
                                CustomRulesRepository.class,
                                TestRule.class,
                                PublicAccessImportRule.class);
    }


}