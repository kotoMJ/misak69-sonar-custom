/*
 * Copyright (C) 2009-2014 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */
package cz.misak69.sonar.plugin;

import com.google.common.collect.ImmutableList;
import org.sonar.api.SonarPlugin;

import java.util.List;

/**
 * Entry point of the plugin.
 * This class MUST be mentioned in MANIFEST.MF of deployed jar!
 *
 * Create a class extending SonarPlugin which returns Extensions created in CustomRulesDefinition and CustomRulesRepository.

 */
public class CustomPlugin extends SonarPlugin {

    /**
     * Declares all the extensions implemented in the plugin
     */
    @Override
    public List getExtensions() {
        System.out.println("#misak69 | CustomPlugin.getExtensions");
        return ImmutableList.of(CustomRulesDefinition.class, CustomRulesRepository.class, TestRule.class);
    }


}