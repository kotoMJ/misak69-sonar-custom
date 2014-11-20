package cz.misak69.sonar.plugin.rules;

import cz.misak69.sonar.plugin.CustomRulesDefinition;
import cz.misak69.sonar.plugin.utils.TreeUtils;
import org.slf4j.LoggerFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.Severity;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.CompilationUnitTree;


/**
 *  Rule to check whether important classes/methods are coverage by unit test.
 *
 *  This rule should traverse all files and compute result at the end.
 *
 */
@Rule(key = UnitTestCoverageRule.KEY,
      priority = Priority.CRITICAL,
      name = UnitTestCoverageRule.NAME,
      description = UnitTestCoverageRule.DESCRIPTION,
      tags = {UnitTestCoverageRule.TAG})
public class UnitTestCoverageRule extends BaseTreeVisitor implements JavaFileScanner {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UnitTestCoverageRule.class);

    public static final String TAG = TestRule.TAG;
    public static final String SEVERITY = Severity.CRITICAL;
    public static final String KEY = "unit-test-coverage-rule";
    private final RuleKey RULE_KEY = RuleKey.of(CustomRulesDefinition.REPOSITORY_KEY, KEY);
    public static final String NAME = "Unit test coverage rule";
    public static final String DESCRIPTION = "Check for important methods to have unit test.";

    private String currentPackage;
    private StringBuilder classStorage;

    public UnitTestCoverageRule() {
        super();
        classStorage = new StringBuilder().append("[69]");
        System.out.println("[69] UnitTestCoverageRule constructed...");
    }

    /**
     * Private field to store the context: this is the object used to create issues.
     */
    private JavaFileScannerContext context;

    /**
     * Implementation of the method of the JavaFileScanner interface.
     * @param context Object used to attach issues to source file.
     */
    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public void visitCompilationUnit(CompilationUnitTree tree) {
        currentPackage = TreeUtils.concatenate(tree.packageName());
        super.visitCompilationUnit(tree);
    }

    @Override
    public void visitClass(ClassTree tree) {
        classStorage.append(currentPackage);
        classStorage.append(tree.simpleName().name()).append("\n");
        System.out.println(classStorage.toString());
        super.visitClass(tree);
    }

}