


<h1>Custom SonarQube Java plugin API development</h1>

<h2>Designed for API:</h2>
<ul>
<li>Sonar 4.5</li>
<li>Gradle 2.x</li>
</ul>

Usage of Sonar 4.5 with gradle needs to use gradle 2.3 and higher (see https://issues.gradle.org/browse/GRADLE-3062)

You may use testing project <em>https://github.com/misak69/misak69-sonar-custom-resource</em> as project to be analysed by sonar(using gradle).

<h2>Basic knowledge for Java plugin API development/deployment</h2>

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

<h2>API development FUTURE</h2>
<h3>BaseTreeVisitor</h3>
The way to write custom checks for Java as of today is to use the <strong>BaseTreeVisitor</strong>. 
All the other ways are now deprecated and Sonar group is working to be able to remove them 
(but it is not always straightforward as some of them requires a complete semantic analysis to be removed).
<br/><br/>
BaseTreeVisitor does not use SSLR directly, the java plugin is not moving away from SSLR rather from one class, 
specifically ASTNode, in order to work on a SyntaxTree with a class specific for each type of node, 
The drop of Xpath checks occurs in that logic of moving away from a non-typed SyntaxTree



<h2>Important gradle tasks</h2>
<ul>
<li><b>idea</b> or <b>eclipse</b> task to generate IDE project files.</li>
<li><b>deployTosonar</b> (default) task to create jar and deploy it to sonar</li>
</ul>

<div>Create file named <strong>gradle.properties</strong> based on <em>gradle.properties.sample</em> to define where is Sonar located 
and for other project related properties.</div>

<h2>References for Java plugin API development</h2>
Use stack-overflow for configuration questions only!
Coding plugin related questions should NOT be placed on stack-overflow, use following links instead:

<strong>Look for help</strong>
http://sonarqube.15.x6.nabble.com/SonarQube-Developers-f4523654.html
http://xircles.codehaus.org/lists/dev@sonar.codehaus.org


<h3>Other resources</h3>

<strong>Important Sonar API changes:</strong><br/>
http://docs.codehaus.org/display/SONAR/API+Changes

<strong>Wide range of mostly 4.5 examples based on BaseTreeVisitor</strong>
https://github.com/SonarSource/sonar-java/tree/master/java-checks/src/main/java/org/sonar/java/checks

<strong>Maven repository</strong><br/>
For maven repository artifacts see: http://maven-repository.com/artifact/org.codehaus.sonar-plugins.java


