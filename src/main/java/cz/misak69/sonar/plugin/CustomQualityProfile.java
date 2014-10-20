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
 * User: misak
 * Date: 2014.10.06
 */
public class CustomQualityProfile extends ProfileDefinition {

//    private final AnnotationProfileParser annotationProfileParser;

    public static final String DCS_QA_PROFILE = "DCS Test profile";

//    public DCSQualityProfile(AnnotationProfileParser annotationProfileParser) {
//        this.annotationProfileParser = annotationProfileParser;
//        System.out.println("#dcs | DCSQualityProfile.constructor");
//    }

    private final RuleFinder ruleFinder;

    public CustomQualityProfile(RuleFinder ruleFinder) {
        this.ruleFinder = ruleFinder;
        System.out.println("#dcs | DCSQualityProfile.constructor");
    }

//    private final XMLProfileParser parser;
//
//    public DCSQualityProfile(XMLProfileParser parser) {
//        this.parser = parser;
//    }

    @Override
    public RulesProfile createProfile(ValidationMessages validationMessages) {
        System.out.println("#dcs | DCSQualityProfile.createProfile");
        Collection<Class> annotatedCollection = new ArrayList<Class>();
        annotatedCollection.add(TestRule.class);
        //annotatedCollection.add(PublicAccessRule.class);
        //return annotationProfileParser.parse(DCSRulesDefinition.REPOSITORY_KEY, DCS_QA_PROFILE, Java.KEY, annotatedCollection, validationMessages);

//        InputStream input = getClass().getResourceAsStream("/rules/rules.xml");
//        InputStreamReader reader = new InputStreamReader(input);
//        try {
//            return parser.parse(reader, validationMessages);
//        } finally {
//            IOUtils.closeQuietly(reader);
//        }

        RulesProfile profile = RulesProfile.create(DCS_QA_PROFILE, Java.KEY);
        for (Class ruleClass : annotatedCollection) {
            String ruleKey = RuleAnnotationUtils.getRuleKey(ruleClass);
            System.out.println("#dcs | ruleKey:"+ruleKey+" for class:"+ruleClass);
            Rule rule = ruleFinder.findByKey(CustomRulesDefinition.REPOSITORY_KEY, ruleKey);
            System.out.println("#dcs | rule:"+rule);
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