package cz.misak69.sonar.plugin;

import cz.misak69.sonar.plugin.rules.GetterIsRule;
import cz.misak69.sonar.plugin.rules.PublicAccessImportRule;
import cz.misak69.sonar.plugin.rules.TestRule;
import cz.misak69.sonar.plugin.rules.UnitTestCoverageRule;
import org.slf4j.LoggerFactory;
import org.sonar.api.CoreProperties;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rule.Severity;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;

/**
 * Custom rules definition 1(LOAD TIME - Sonar UI appearance).
 *
 * Since Sonar 4.3 use new API org.sonar.api.server.rule.RulesDefinition to declare rules.
 * It includes metadata related to technical debt model.
 * It replaces the deprecated org.sonar.api.rules.RuleRepository.
 *
 * Create a class implementing RulesDefinition ->
 * it is a ServerExtension whose sole purpose is to make your custom rules appear in SonarQube's UI
 * if you've explicitly provided a definition (programatically, or in a XML file, or through annotations).
 * This extension is loaded at server startup.

 *
 */
public class CustomRulesDefinition implements RulesDefinition {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CustomQualityProfile.class);

    public static final String REPOSITORY_KEY = "custom-rules";
    public static final String REPOSITORY_NAME = "Misak69 rule repository";

    private final RulesDefinitionXmlLoader xmlLoader;

    public CustomRulesDefinition(RulesDefinitionXmlLoader xmlLoader) {
        this.xmlLoader = xmlLoader;
    }
    @Override
    public void define(Context context) {
        logger.debug("initialized...");
        NewRepository  repository = context.createRepository(REPOSITORY_KEY, CoreProperties.CATEGORY_JAVA).setName(REPOSITORY_NAME);
        NewRule accessRule = repository.createRule(PublicAccessImportRule.KEY)
                                       .setName(PublicAccessImportRule.NAME)
                                       .setHtmlDescription(PublicAccessImportRule.DESCRIPTION)
                                       .setTags(PublicAccessImportRule.TAG)
                                       .setStatus(RuleStatus.READY)
                                       .setSeverity(PublicAccessImportRule.SEVERITY);

        NewRule testRule = repository.createRule(TestRule.KEY)
                .setName(TestRule.NAME)
                .setHtmlDescription(TestRule.DESCRIPTION)
                .setTags(TestRule.TAG)
                .setStatus(RuleStatus.READY)
                .setSeverity(Severity.INFO);

        NewRule getterIsRule = repository.createRule(GetterIsRule.KEY)
                .setName(GetterIsRule.NAME)
                .setHtmlDescription(GetterIsRule.DESCRIPTION)
                .setTags(GetterIsRule.TAG)
                .setStatus(RuleStatus.READY)
                .setSeverity(GetterIsRule.SEVERITY);

        NewRule unitTestCoverageRule = repository.createRule(UnitTestCoverageRule.KEY)
                .setName(UnitTestCoverageRule.NAME)
                .setHtmlDescription(UnitTestCoverageRule.DESCRIPTION)
                .setTags(UnitTestCoverageRule.TAG)
                .setStatus(RuleStatus.READY)
                .setSeverity(UnitTestCoverageRule.SEVERITY);

//        xmlLoader.load(repository, getClass().getResourceAsStream("/rules/rules.xml"), "UTF-8");
        repository.done();
    }
}