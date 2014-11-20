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
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.NewClassTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Check for GET and IS method for one property.
 * Evade such multiplicity.
 *
 * User: ${mjenicek}
 * Date: 2014.11.10
 */

@Rule(key = GetterIsRule.KEY,
      priority = Priority.CRITICAL,
      name = GetterIsRule.NAME,
      description = GetterIsRule.DESCRIPTION,
      tags = {GetterIsRule.TAG})
public class GetterIsRule extends BaseTreeVisitor implements JavaFileScanner {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GetterIsRule.class);

    public static final Priority PRIORITY = Priority.CRITICAL;
    public static final String SEVERITY = Severity.CRITICAL;
    public static final String TAG = TestRule.TAG;
    public static final String KEY = "getter-is-rule";
    private final RuleKey RULE_KEY = RuleKey.of(CustomRulesDefinition.REPOSITORY_KEY, KEY);
    public static final String NAME = "DCS GET&IS rule";
    public static final String DESCRIPTION = "You should not have IS and GET method for the same method declaration.";
    public static final String VIOLATION_MESSAGE = "GetterIsRule raised on method declaration!";

    private JavaFileScannerContext context;

    List<String> classId = new ArrayList<String>();//use list because of nested classes
    List<String> classAnonymousId = new ArrayList<String>();//use list because of nested anonymous classes

    /*Map where key is class and value is list of baseMethod names.*/
    private final Map<String,List<String>> getterRootNameMap;

    private static final List<String> getterRootPrefixList = Arrays.asList("get","is");

    private static final List<String> srcExceptionList = Arrays.asList("Document.java");

    public GetterIsRule() {
        this.getterRootNameMap = new HashMap<String,List<String>>();
    }

    @Override
    public void scanFile(JavaFileScannerContext context) {
        String fileName = context.getFileKey();
        logger.debug("fileName: {}", fileName);

        /* Skip files ....*/
        for(String srcException:srcExceptionList) {
            if (fileName.endsWith(srcException)) {
                logger.warn("Skipping file: {}", fileName);
                return;
            }
        }

        this.context = context;
        this.classAnonymousId.clear();
        this.classId.clear();
        getterRootNameMap.clear();//clean map used for previous class
        scan(context.getTree());
    }

    @Override
    public void visitClass(ClassTree tree) {
        if(tree.simpleName()!=null) {
            this.classId.add(tree.simpleName().name());
        };

        super.visitClass(tree);

        if(tree.simpleName()!=null) {
            this.classId.remove(this.classId.size()-1);
        }
    }

    /**
     * Support for anonymous classes.
     *
     * @param tree
     */
    @Override
    public void visitNewClass(NewClassTree tree) {
        if (tree.classBody()==null) return;//skip anonymous class without overriding.
        this.classAnonymousId.add(TreeUtils.getNewClassTreeId(tree));
        super.visitNewClass(tree);
        this.classAnonymousId.remove(this.classAnonymousId.size()-1);
    }



    /**
     * For every method check if the base name is already registered for given class.
     * Create violation if true, register base name otherwise.
     *
     * @param tree
     */
    @Override
    public void visitMethod(MethodTree tree) {
        super.visitMethod(tree);

        String baseMethodName = getRootName(tree.simpleName().name());

        logger.debug("method:[{}],classId:[{}],classAnonymousId:[{}]",baseMethodName, classId, classAnonymousId);
        if (baseMethodName == null) return;//skip method without predefined prefix - performance
        String key = classAnonymousId.isEmpty()?classId.get(classId.size()-1):classAnonymousId.get(classAnonymousId.size()-1);

        String methodHash = TreeUtils.getMethodHash(tree, baseMethodName);
        if (isMethodRegistered(methodHash, key)){
            logger.debug("violation: {}",methodHash);
            context.addIssue(tree, RULE_KEY, VIOLATION_MESSAGE);
        }else{
            registerMethod(methodHash, key);
        }
        logger.debug("visited...");
    }

    /**
     * For specific prefixes cut this prefix and return base name.
     * Null return otherwise.
     *
     * @param methodName
     * @return
     */
    private String getRootName(String methodName){
        for (String methodRootPrefix: getterRootPrefixList){
            if (methodName.startsWith(methodRootPrefix)){
                return methodName.replaceFirst(methodRootPrefix,"");
            }
        }
        return null;
    }

    /**
     * Check if the base method is registered for current class name.
     * @param baseMethodName
     * @return
     */
    private boolean isMethodRegistered(String baseMethodName, String key){
        if (getterRootNameMap.get(key) == null) return false;
        return getterRootNameMap.get(key).contains(baseMethodName);
    }

    /**
     * Register base method for current class name.
     * @param baseMethodName
     */
    private void registerMethod(String baseMethodName, String key){
        if (getterRootNameMap.get(key) == null) getterRootNameMap.put(key,new ArrayList<String>());
        getterRootNameMap.get(key).add(baseMethodName);
    }


}

