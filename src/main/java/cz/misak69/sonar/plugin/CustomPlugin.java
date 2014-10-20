/*
 * Copyright (C) 2009-2014 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */
package cz.misak69.sonar.plugin;
/*
 * Copyright (C) 2009-2014 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */

import com.google.common.collect.ImmutableList;
import org.sonar.api.SonarPlugin;

import java.util.List;

/**
 * Entry point of the plugin.
 * Declares one single extension point: a custom rules repository.
 */
public class CustomPlugin extends SonarPlugin {

    /**
     * Declares all the extensions implemented in the plugin
     */
    @Override
    public List getExtensions() {
        System.out.println("#misak69 | CustomPlugin.getExtensions");
        return ImmutableList.of(CustomRulesDefinition.class, CustomQualityProfile.class, TestRule.class);
    }


}