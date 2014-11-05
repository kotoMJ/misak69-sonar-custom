package cz.misak69.sonar.plugin;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleAnnotationUtils;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.plugins.java.Java;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Quality profile pre-definition.
 *
 * CURRENTLY not working for this plugin.
 *
 */
public class CustomQualityProfile extends ProfileDefinition {

    public static final String CUSTOM_QA_PROFILE = "Custom Test profile";


    private final RuleFinder ruleFinder;

    public CustomQualityProfile(RuleFinder ruleFinder) {
        this.ruleFinder = ruleFinder;
        System.out.println("#misak69 | CustomQualityProfile.constructor");
    }


    @Override
    public RulesProfile createProfile(ValidationMessages validationMessages) {
        System.out.println("#misak69 | CustomQualityProfile.createProfile");
        Collection<Class> annotatedCollection = new ArrayList<Class>();
        annotatedCollection.add(TestRule.class);

        RulesProfile profile = RulesProfile.create(CUSTOM_QA_PROFILE, Java.KEY);
        for (Class ruleClass : annotatedCollection) {
            String ruleKey = RuleAnnotationUtils.getRuleKey(ruleClass);
            System.out.println("#misak69 | CustomQualityProfile-ruleKey:"+ruleKey+" for class:"+ruleClass);
            Rule rule = ruleFinder.findByKey(CustomRulesDefinition.REPOSITORY_KEY, ruleKey);
            System.out.println("#misak69 | CustomQualityProfile-rule:"+rule);
            profile.activateRule(rule, null);
        }
        return profile;
    }



    /**
     * sources:
     *  https://github.com/SonarSource/sonar-java/blob/master/sonar-java-plugin/src/main/java/org/sonar/plugins/java/JavaSonarWayProfile.java
     *  http://stackoverflow.com/questions/24204641/how-to-create-rule-repository-and-load-it-with-some-checklist-rule-in-sonarqube
     */


}