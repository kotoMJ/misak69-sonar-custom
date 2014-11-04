


<h1>Custom SonarQube Java plugin API development</h1>

<h2>Designed for API:</h2>
<ul>
<li>Sonar 4.5</li>
<li>Gradle 2.x</li>
</ul>

Usage of Sonar 4.5 with gradle needs to use gradle 2.3 and higher (see https://issues.gradle.org/browse/GRADLE-3062)
http://docs.codehaus.org/display/SONAR/API+Changes

<b>Use project https://github.com/misak69/misak69-sonar-custom-resource as project to be analysed by sonar(using gradle)</b>

<h2>Basics for Java plugin API development</h2>

<ul>
<li>Create a class implementing RulesDefinition -> it is a ServerExtension whose sole purpose is to make your custom 
rules appear in SonarQube's UI if you've explicitly provided a definition (programatically, or in a XML file, or through annotations).
This extension is loaded at server startup.</li>
<li>Create a class implementing BatchExtension and JavaFileScannersFactory -> its purpose is to make all your custom 
java rules available during batch analysis by returning instances of your rules. This extension is loaded during analysis.</li>
<li>Create a class extending SonarPlugin which returns Extensions created in points 1 and 2 above.</li>
<div>Your custom rules will then be both available in UI and during analysis. 
If you don't do 1. you won't be able to activate / configure them. 
If you forget 2, they will be activable / configurable, but will never be executed (and no error will be raised neither)
</div>



<h2>References for Java plugin API development</h2>
Use stack-overflow for configuration questions only!
Coding plugin related questions should NOT be placed on stack-overflow, use following links instead:

<b>Look for help</B>
http://sonarqube.15.x6.nabble.com/SonarQube-Developers-f4523654.html
http://xircles.codehaus.org/lists/dev@sonar.codehaus.org


<h3>Other resources</h3>
<b>DEPRECATED Example of how to implement your plugin</b>:
https://github.com/SonarSource/sonar-examples

<b>NEW WAY OF custom plugin implementation</b>:
https://github.com/SonarSource/sonarqube/blob/branch-4.3/plugins/sonar-xoo-plugin/src/main/java/org/sonar/xoo/rule/XooRulesDefinition.java
https://github.com/SonarSource/sonar-java/blob/master/sonar-java-plugin/src/main/java/org/sonar/plugins/java/JavaSonarWayProfile.java

<b>For inspiration how to write more rules look for</b>:
https://github.com/SonarSource/sonar-java/tree/master/java-checks/src/main/java/org/sonar/java/checks

For maven repository artifacts see: http://maven-repository.com/artifact/org.codehaus.sonar-plugins.java
