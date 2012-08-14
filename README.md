Elija
=====
This is a small JEE application which uses the New Family Search  API.
It walks the user through the OAuth steps, and then makes a few simple requests from New Family Search.\r\n

It's built in the NetBeans IDE with Maven. 
It uses some Spring MVC Framework stuff for controllers and setup, and the JAXB New Family Search API.\r\n

To Build:\r\n
1. Install NetBeans IDE with Glassfish (I suppose you could use Eclipse IDE and/or Tomcat, or some other Web Container, but I haven't tested them)\r\n
2. Install Maven. \r\n
3. Checkout this code from github, remember it's location.\r\n
4. Open this project in NetBeans\r\n
5. Download the New Family Search Java Client Library for the Family Tree module (http://www.dev.usys.org/familytree/downloads.html#Java Client Library)
and add it to your maven repository by running this command on it:\r\n
mvn install:install-file
  -Dfile=<path-to-familytree-jar-file>
  -DgroupId=org.familysearch.new.reference
  -DartifactId=familytree
  -Dversion=2.0.0
  -Dpackaging=jar
  -DgeneratePom=true
6. in src/main/webapp/WEB-INF/web.xml, change the context parameter "NFSdeveloperKey" to be the developer key you received from Family Search\r\n
7. You should now be able to build it in NetBeans, and run it. Direct your browser to http://localhost/GenealogyGuide/nfs/authenticate.htm to get started!
