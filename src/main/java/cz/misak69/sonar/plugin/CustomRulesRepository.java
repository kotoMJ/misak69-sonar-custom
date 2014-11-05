package cz.misak69.sonar.plugin;

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

    public CustomRulesRepository() {
        super();

        System.out.println("#misak69 | CustomRulesRepository.constructed");
    }

    @Override
    public Iterable<JavaFileScanner> createJavaFileScanners() {
        return Arrays.<JavaFileScanner>asList(new TestRule());
    }

}
