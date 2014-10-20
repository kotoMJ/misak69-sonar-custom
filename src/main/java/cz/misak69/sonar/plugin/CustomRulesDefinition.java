package cz.misak69.sonar.plugin;

import org.sonar.api.CoreProperties;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;

/**
 * Defines a custom rules repository
 *
 * With Sonar 4.3 New API org.sonar.api.server.rule.RulesDefinition to declare rules.
 * It includes metadata related to technical debt model.
 * It replaces the deprecated org.sonar.api.rules.RuleRepository.
 *
 *
 *
 */
public class CustomRulesDefinition implements RulesDefinition {

    public static final String REPOSITORY_KEY = "custom-rules";
    public static final String REPOSITORY_NAME = "CustomRuleRepository";

    private final RulesDefinitionXmlLoader xmlLoader;

    public CustomRulesDefinition(RulesDefinitionXmlLoader xmlLoader) {
        this.xmlLoader = xmlLoader;
    }
    @Override
    public void define(Context context) {
        System.out.println("#misak69 | CustomRulesDefinition.define");
        NewRepository  repository = context.createRepository(REPOSITORY_KEY, CoreProperties.CATEGORY_JAVA).setName("Custom rule repository");
//        NewRule accessRule = repository.createRule(PublicAccessRule.KEY)
//                                       .setName(PublicAccessRule.NAME)
//                                       .setHtmlDescription(PublicAccessRule.DESCRIPTION)
//                                       .setTags(PublicAccessRule.TAG)
//                                       .setStatus(RuleStatus.READY)
//                                       .setSeverity(Severity.CRITICAL);
//
        NewRule testRule = repository.createRule(TestRule.KEY)
                .setName(TestRule.NAME)
                .setHtmlDescription(TestRule.DESCRIPTION)
                .setTags(TestRule.TAG)
                .setStatus(RuleStatus.READY)
                .setSeverity(Severity.INFO);

//        xmlLoader.load(repository, getClass().getResourceAsStream("/rules/rules.xml"), "UTF-8");
        repository.done();
    }
}