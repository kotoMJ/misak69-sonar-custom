package cz.misak69.sonar.plugin.rules;


import cz.misak69.sonar.plugin.rules.GetterIsRule;
import junit.framework.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.sonar.java.JavaAstScanner;
import org.sonar.java.model.VisitorsBridge;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifierRule;

import java.io.File;

public class GetterIsRuleTest {
    @Rule
    public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();


    @Test
    public void testGetterRule(){

        File src = new File("src/test/resources/cz/misak69/module_beta/api/BModuleGetterApi.java");
        Assert.assertNotNull(src);
        Assert.assertTrue(src.exists());
        VisitorsBridge vb = new VisitorsBridge(new GetterIsRule());
        SourceFile file = JavaAstScanner.scanSingleFile(src, vb);

        checkMessagesVerifier.verify(file.getCheckMessages())
            .next().atLine(35).withMessage(GetterIsRule.VIOLATION_MESSAGE)
            .next().atLine(49).withMessage(GetterIsRule.VIOLATION_MESSAGE)
            .next().atLine(72).withMessage(GetterIsRule.VIOLATION_MESSAGE)
            .noMore();


        File src2 = new File("src/test/resources/cz/misak69/module_beta/impl/Level2.java");
        Assert.assertNotNull(src2);
        Assert.assertTrue(src2.exists());

        VisitorsBridge vb2 = new VisitorsBridge(new GetterIsRule());
        SourceFile file2 = JavaAstScanner.scanSingleFile(src2, vb2);
        checkMessagesVerifier.verify(file2.getCheckMessages())
                .noMore();


    }
}
