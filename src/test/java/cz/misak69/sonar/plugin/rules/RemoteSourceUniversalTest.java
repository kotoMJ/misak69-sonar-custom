package cz.misak69.sonar.plugin.rules;

import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.sonar.java.JavaAstScanner;
import org.sonar.java.model.VisitorsBridge;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifierRule;

import java.io.File;

/**
 * User: ${mjenicek}
 * Date: 2014.11.13
 */
@Ignore
public class RemoteSourceUniversalTest {

    //final String REMOTE_FILE = "/home/misak/depot/svn/2015_1/app_cexi/src/main/java/cz/kb/dcs/app_cexi/CampaignResourceLoaderServlet.java";
    final String REMOTE_FILE = "src/test/resources/cz/misak69/module_beta/api/BModuleGetterApi.java";
    JavaFileScanner RULE = new UnitTestCoverageRule();


    @Rule
    public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();


    @Test
    public void testAnyRemoteFile() {

        File src = new File(REMOTE_FILE);
        Assert.assertNotNull(src);
        Assert.assertTrue(src.exists());
        VisitorsBridge vb = new VisitorsBridge(RULE);
        SourceFile file = JavaAstScanner.scanSingleFile(src, vb);

        checkMessagesVerifier.verify(file.getCheckMessages())
                .noMore();
    }
}
