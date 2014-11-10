package cz.misak69.sonar.plugin.rules;

import cz.misak69.sonar.plugin.CustomRulesDefinition;
import cz.misak69.sonar.plugin.utils.TreeUtils;
import org.slf4j.LoggerFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.CompilationUnitTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.ImportTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
@Rule(key = PublicAccessImportRule.KEY,
        priority = Priority.MINOR,
        name = PublicAccessImportRule.NAME,
        description = PublicAccessImportRule.DESCRIPTION,
        tags = {PublicAccessImportRule.TAG})
public class PublicAccessImportRule extends BaseTreeVisitor implements JavaFileScanner {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PublicAccessImportRule.class);

    public static final String KEY = "public-access-rule";
    public static final String NAME = "Public access rule";
    public static final String DESCRIPTION = "Avoid using non API classes across modules!";
    public static final String TAG = TestRule.TAG;
    public static final String VIOLATION_MESSAGE = "Bad using package implementation!";

    private final RuleKey RULE_KEY = RuleKey.of(CustomRulesDefinition.REPOSITORY_KEY, KEY);

    private JavaFileScannerContext context;

    Map<String, List<String>> FO_TO_MODULE = new HashMap<String, List<String>>();
    Map<String, String> PROJECT_EXTENSION = new HashMap<String,String>();
    List<String> EXCEPTIONS_TO_SKIP = new ArrayList<String>();
    List<String> API_PACKAGE = new ArrayList<String>();
    List<String> MODULE_PACKAGE = new ArrayList<String>();
    List<String> WS_CLIENT_TO_SKIP = new ArrayList<String>();


    private String currentPackage = null;
    private String currentProject = null;
    private String currentFunctionalArea = null;

    public PublicAccessImportRule() {
        super();
        init();
        logger.debug("constructed...");
    }

    private void init(){
        FO_TO_MODULE. put("fo01_payment_orders", Arrays.asList("module_transaction", "module_standing_order"));
        FO_TO_MODULE. put("fo02_direct_debits",Arrays.asList("module_direct_debit"));
        FO_TO_MODULE. put("fo03_batch",Arrays.asList("module_batch"));
        FO_TO_MODULE. put("fo04_ptmo",Arrays.asList("module_ptmo"));
        FO_TO_MODULE. put("fo05_cards",Arrays.asList("module_card"));
        FO_TO_MODULE. put("fo06_claims",Arrays.asList("module_claims"));
        FO_TO_MODULE. put("fo07_loans",Arrays.asList("module_loan"));
        FO_TO_MODULE. put("fo08_statements",Arrays.asList("module_statements"));
        FO_TO_MODULE. put("fo08_transaction_history_list",Arrays.asList("module_transaction"));
        FO_TO_MODULE. put("fo09_notification",Arrays.asList("module_notification"));
        FO_TO_MODULE. put("fo10_messagebox",Arrays.asList("module_campaign"));
        FO_TO_MODULE. put("fo11_daughters",Arrays.asList("module_daughters", "module_mpss"));
        FO_TO_MODULE. put("fo12_iks",Arrays.asList("module_iks"));
        FO_TO_MODULE. put("fo13_fx_etrading",Arrays.asList("module_etrading"));
        FO_TO_MODULE. put("fo14_eforms",Arrays.asList("module_eform"));
        FO_TO_MODULE. put("fo15_bapo",new ArrayList());
        FO_TO_MODULE. put("fo17_favourites",new ArrayList());
        FO_TO_MODULE. put("fo18_administration",new ArrayList());
        FO_TO_MODULE. put("fo19_security",Arrays.asList("module_security", "module_security_core"));
        FO_TO_MODULE. put("fo22_others",new ArrayList());
        FO_TO_MODULE. put("fo23_accounts",Arrays.asList("module_account"));

        EXCEPTIONS_TO_SKIP.addAll(Arrays.asList("module_frontend_base",
                "module_cexi_controller",
                "module_frontend_corp",
                "module_init",
                "module_data_objects",
                "app_init"));

        WS_CLIENT_TO_SKIP.addAll(Arrays.asList(
                "service.client",
                "statementsetup"));

        API_PACKAGE.addAll(Arrays.asList("api", "action"));

        MODULE_PACKAGE.addAll(Arrays.asList("module_", "app_"));

        PROJECT_EXTENSION.put("app_mojeplatba", "app_cexi");
    }

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        currentPackage = "";
        currentProject = "";
        currentFunctionalArea = "";

        logger.debug("fileName: {}",context.getFileName());

        // The call to the scan method on the root of the tree triggers the visit of the AST by this visitor
        scan(context.getTree());
    }

    @Override
    public void visitCompilationUnit(CompilationUnitTree tree) {
        if (tree.packageName() != null) {
            currentPackage = TreeUtils.concatenate(tree.packageName());
            logger.debug("-package.name: "+currentPackage);

            if (currentPackage.startsWith("cz.misak69")){
                currentProject = cutProjectOrModule(currentPackage);
                currentFunctionalArea = cutFunctionalArea(currentPackage);
            }
        }
        super.visitCompilationUnit(tree);
    }

    @Override
    public void visitImport(ImportTree importTree) {

        if (!importTree.isStatic()) {
            String declaredImport = TreeUtils.concatenate((ExpressionTree) importTree.qualifiedIdentifier());


            if ((currentProject.length()>0) && (declaredImport.length()>0)&&(isDCSPackage(declaredImport))){

                if (containsException(declaredImport)){
                    logger.debug("-containsException: {}",declaredImport);
                    return;
                }
                if (isServiceClient(declaredImport)){
                    logger.debug("-isServiceClient: {}",declaredImport);
                    return;
                }

                logger.debug("currentProject: {}",currentProject);
                if (isTheSameOrExtendedModule(declaredImport)){
                    //thats ok, same (or extended) module
                    logger.debug("-sameModule: {}",declaredImport);
                } else if (isAPI(declaredImport)) {
                    //thats ok, using API
                    logger.debug("-isAPI: {}",declaredImport);
                } else if (isFOtoModule(declaredImport, currentFunctionalArea)) {
                    //thats ok, calling fo
                    logger.debug("-isFOO: {}",declaredImport);
                } else {
                    logger.debug("-violation: Bad declared import: {}",declaredImport);
                    //that's bad, using package implementation
                    context.addIssue(importTree,this.RULE_KEY, VIOLATION_MESSAGE);
                }
            }
        }
        super.visitImport(importTree);
    }

    public final String cutProjectOrModule(String packageName){
        String projectName = "";
        if ((packageName!=null)&&(packageName.length()>0)){
            Pattern pattern = Pattern.compile("[_]([a-z0-9_]*)");
            Matcher matcher = pattern.matcher(packageName);
            if (matcher.find()) { projectName = matcher.group(1); }
        }
        return projectName;
    }

    public final String cutFunctionalArea(String packageName){
        String projectName = "";
        if ((packageName!=null)&&(packageName.length()>0)){
            Pattern pattern = Pattern.compile("([f][o][0-9]*[_][a-zA-Z_]*)");
            Matcher matcher = pattern.matcher(packageName);
            if (matcher.find()) { projectName = matcher.group(1); }
        }
        return projectName;
    }

    public boolean containsException(String declaredImport){
        for (String exception:EXCEPTIONS_TO_SKIP){
            if (declaredImport.contains(exception)) return true;
        }
        return false;
    }


    public boolean isServiceClient(String declaredImport){
        for (String serviceFragment:WS_CLIENT_TO_SKIP){
            if (declaredImport.contains(serviceFragment)) return true;
        }
        return false;
    }

    public boolean isAPI(String pckg){
        for (String api:API_PACKAGE){
            if (pckg.contains(api)) return true;
        }
        return false;
    }

    public boolean isFOtoModule(String pckg, String currentFO){
        if (pckg==null) return false;
        if (currentFO==null) return false;

        if (currentFO.length()>0){
            if (FO_TO_MODULE.containsKey(currentFO)){
                for (String foModule: FO_TO_MODULE.get(currentFO)){
                    if (pckg.contains(foModule)) return true;
                }
            }else{
                logger.error("Unknown FO :"+currentFO+"! Update FO_TO_MODULE in PublicAccessImportRule.");
            }
        }
        return false;
    }

    public boolean isTheSameOrExtendedModule(String declaredImport){
        boolean ret = declaredImport.contains(currentProject);

        if (!ret){
            for (String extendedProject: PROJECT_EXTENSION.keySet()){
                ret = declaredImport.contains(PROJECT_EXTENSION.get(extendedProject));
                if (ret) break;
            }
        }

        return ret;
    }

    public boolean isDCSPackage(String givenPackage){
        return givenPackage.startsWith("cz.misak69");
    }
}
