/*
 * Copyright (C) 2009-2014 SonarSource SA
 * All rights reserved
 * mailto:contact AT sonarsource DOT com
 */

package cz.misak69.sonar.plugin;

import com.sonar.sslr.api.AstAndTokenVisitor;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.Trivia;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.java.ast.api.JavaKeyword;
import org.sonar.java.ast.api.JavaPunctuator;
import org.sonar.java.ast.api.JavaTokenType;
import org.sonar.java.ast.parser.JavaGrammar;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Deprecated - use PublicAccessRule instead.
 *
 * The way to write custom checks for Java as of today is to use the BaseTreeVisitor.
 * All the other ways are now deprecated and we are working to be able to remove them.
 *
 * BaseTreeVisitor does not use SSLR directly, the java plugin is not moving away from SSLR rather from one class,
 * specifically ASTNode, in order to work on a SyntaxTree with a class specific for each type of node
 *
 */
@Rule(key = PublicAccessRuleDeprecated.KEY,
      priority = Priority.MINOR,
      name = PublicAccessRuleDeprecated.NAME,
      description = PublicAccessRuleDeprecated.DESCRIPTION,
      tags = {PublicAccessRuleDeprecated.TAG})
@BelongsToProfile(title = CustomQualityProfile.CUSTOM_QA_PROFILE, priority = Priority.MINOR)
@Deprecated
public class PublicAccessRuleDeprecated extends SquidCheck<LexerlessGrammar> implements AstAndTokenVisitor {

    public static final String KEY = "public-access-rule";
    public static final String NAME = "Public access rule";
    public static final String DESCRIPTION = "Avoid using non API classes across modules!";
    public static final String TAG = TestRule.TAG;

    Map<String, List<String>> FO_TO_MODULE = new HashMap<String, List<String>>();
    List<String> EXCEPTIONS_TO_SKIP = new ArrayList<String>();
    List<String> API_PACKAGE = new ArrayList<String>();
    List<String> MODULE_PACKAGE = new ArrayList<String>();

    private String currentPackage;
    private String currentProject = null;
    private String currentFunctionalArea = null;

    public PublicAccessRuleDeprecated() {
        super();
        System.out.println("#misak69 | PublicAccessRule.constructed...");
    }

    @Override
    public void init() {

        System.out.println("#misak69 | PublicAccessRule.init!");

        subscribeTo(JavaGrammar.PACKAGE_DECLARATION);
        subscribeTo(JavaGrammar.IMPORT_DECLARATION);
        subscribeTo(JavaGrammar.CLASS_TYPE);
        subscribeTo(JavaGrammar.CREATED_NAME);
        subscribeTo(JavaGrammar.ANNOTATION);
        subscribeTo(JavaKeyword.THROWS);
        subscribeTo(JavaGrammar.QUALIFIED_IDENTIFIER);

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

        API_PACKAGE.addAll(Arrays.asList("api", "action"));

        MODULE_PACKAGE.addAll(Arrays.asList("module_", "app_"));
    }

    @Override
    public void visitFile(AstNode astNode) {
//        pendingReferences.clear();
//        lineByImportReference.clear();
//        pendingImports.clear();
        System.out.println("#misak69 | PublicAccessRule.visitFile:"+astNode.toString());
        currentPackage = "";
        currentProject = "";
        currentFunctionalArea = "";
    }

    @Override
    public void visitNode(AstNode node) {
        getContext().createLineViolation(this, "PublicAccessRule.node visted!", node);

        System.out.println("#misak69 | PublicAccessRule.node.visited");
        if (node.is(JavaGrammar.PACKAGE_DECLARATION)) {
            currentPackage = mergeIdentifiers(node.getFirstChild(JavaGrammar.QUALIFIED_IDENTIFIER));

            getContext().createLineViolation(this, "PublicAccessRule.package:"+currentPackage, node);

            if (currentPackage.startsWith("cz.misak69")){
                currentProject = cutProjectOrModule(currentPackage);
                currentFunctionalArea = cutFunctionalArea(currentPackage);
                //getContext().createLineViolation(this, "... package resolved", node);
            }
        } else if (node.is(JavaGrammar.IMPORT_DECLARATION)) {

            if (isStaticImport(node)) {
                //skip static imports.
            }else{
                String declaredImport = mergeIdentifiers(node.getFirstChild(JavaGrammar.QUALIFIED_IDENTIFIER));

                getContext().createLineViolation(this, "PublicAccessRule.declaredImport:"+declaredImport, node);

                if ((currentProject.length()>0) && (declaredImport.length()>0) && isLinkToModule(currentPackage)){
                    System.out.println("#misak69 | PublicAccessRule.debug...");
                    if (declaredImport.contains(currentProject)){
                        //thats ok, same module
                    } else if (isAPI(declaredImport)) {
                        //thats ok, using API
                    } else if (isFOtoModule(declaredImport, currentFunctionalArea)) {
                        //thats ok, calling fo
                    } else {
                        System.out.println("#misak69 | PublicAccessRule.violation: Bad using package implementation for package:"+currentPackage);
                        //that's bad, using package implementation
                        getContext().createLineViolation(this, "Bad using package implementation!", node);
                    }
                }
            }
        }
    }


    private static String mergeIdentifiers(AstNode node) {
        StringBuilder sb = new StringBuilder();
        for (AstNode child : node.getChildren(JavaTokenType.IDENTIFIER)) {
            sb.append(child.getTokenOriginalValue());
            sb.append('.');
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    private static boolean isStaticImport(AstNode node) {
        return node.hasDirectChildren(JavaKeyword.STATIC);
    }

    private static boolean isImportOnDemand(AstNode node) {
        return node.hasDirectChildren(JavaPunctuator.STAR);
    }

    @Override
    public void visitToken(Token token) {
        if (token.hasTrivia()) {
            for (Trivia trivia : token.getTrivia()) {
//                updatePendingImportsForComments(trivia.getToken().getOriginalValue());
            }
        }
    }


    private static String extractFirstClassName(String reference) {
        int firstIndexOfDot = reference.indexOf('.');
        return firstIndexOfDot == -1 ? reference : reference.substring(0, firstIndexOfDot);
    }

    private static String extractLastClassName(String reference) {
        int lastIndexOfDot = reference.lastIndexOf('.');
        return lastIndexOfDot == -1 ? reference : reference.substring(lastIndexOfDot + 1);
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

    public boolean isLinkToModule(String pckg){
        for (String exception:EXCEPTIONS_TO_SKIP){
            if (pckg.contains(exception)) return false;
        }
        for (String linkSubstring: MODULE_PACKAGE){
            if (pckg.contains(linkSubstring)) return true;
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
        if ((currentFO!=null)&&(currentFO.length()>0)){
            for (String foModule: FO_TO_MODULE.get(currentFO)){
                if (pckg.contains(foModule)) return true;
            }
        }
        return false;
    }
}