package cz.misak69.sonar.plugin;

import cz.misak69.sonar.plugin.rules.GetterIsRule;
import cz.misak69.sonar.plugin.rules.PublicAccessImportRule;
import cz.misak69.sonar.plugin.rules.TestRule;
import cz.misak69.sonar.plugin.rules.UnitTestCoverageRule;
import org.slf4j.LoggerFactory;
import org.sonar.api.BatchExtension;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannersFactory;

import java.util.Arrays;

/**
 * Custom rules definition 2(RUN TIME - Sonar analyzer appearance).
 *
 * Create a class implementing BatchExtension and JavaFileScannersFactory
 * -> its purpose is to make all your custom java rules available during batch analysis
 * by returning instances of your rules. This extension is loaded during analysis.
 *
 * User: misak69
 * Date: 2014.11.04
 */
public class CustomRulesRepository implements JavaFileScannersFactory, BatchExtension {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CustomQualityProfile.class);

    public CustomRulesRepository() {
        super();
        logger.debug("constructor...");
    }

    @Override
    public Iterable<JavaFileScanner> createJavaFileScanners() {
        return Arrays.<JavaFileScanner>asList(new TestRule(), new PublicAccessImportRule(), new GetterIsRule(), new UnitTestCoverageRule());
    }

}
