package cz.misak69.sonar.plugin;

import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;

/**
 *
 */
public class PublicAccessRule extends BaseTreeVisitor implements JavaFileScanner {

    @Override
    public void scanFile(JavaFileScannerContext context) {

    }


}
