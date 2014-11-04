
Open points (TO BE DONE): test rule from this project will be successfully deployed to Sonar, but it is NOT applied by analysis (gradle --stacktrace -PsonarValidation=java sonarRunner)

<b>Use project https://github.com/misak69/misak69-sonar-custom-resource as project to be analysed by sonar(using gradle)</b>

Designed for :<br/>
<ul>
<li>Sonar 4.5</li>
<li>Gradle 2.x</li>
</ul>

================================================
Resources for this new way of rules development:
================================================

Coding plugin related questions should NOT be placed on stack-overflow, use following links instead:

Look for help
http://markmail.org/list/org.codehaus.sonar.dev

Ask for help
http://sonarqube.15.x6.nabble.com/SonarQube-Developers-f4523654.html
http://xircles.codehaus.org/lists/dev@sonar.codehaus.org


DEPRECATED Example of how to implement your plugin:
https://github.com/SonarSource/sonar-examples

NEW WAY OF custom plugin implementation:
https://github.com/SonarSource/sonarqube/blob/branch-4.3/plugins/sonar-xoo-plugin/src/main/java/org/sonar/xoo/rule/XooRulesDefinition.java

https://github.com/SonarSource/sonar-java/blob/master/sonar-java-plugin/src/main/java/org/sonar/plugins/java/JavaSonarWayProfile.java

For inspiration how to write more rules look for:
https://github.com/SonarSource/sonar-java/tree/master/java-checks/src/main/java/org/sonar/java/checks


For maven repository artifacts see: http://maven-repository.com/artifact/org.codehaus.sonar-plugins.java


Usage of Sonar 4.5 with gradle needs to use gradle 2.3 and higher (see https://issues.gradle.org/browse/GRADLE-3062)

http://docs.codehaus.org/display/SONAR/API+Changes