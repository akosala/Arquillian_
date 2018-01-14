##Potenctial problems with workstations:
* wildfly-maven-plugin 1.1.0.Alpha11 requires maven 3.1.1
    * donwgrade to 1.0.2.Final cause configuration to be ignored
    * update maven:
        * `curl http://ftp.piotrkosoft.net/pub/mirrors/ftp.apache.org/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz | taz xz`

###Problems to point out:
1. shrinkwrap:
    1. only bean in classes, 
       **other used classes (parameters, return values) are also 
       needed**. But we need "only" classes that we actually use. 
       Parameters of not used methods are not necessary.
    2. of course we need implementations of beans as well as
       interfaces
    3. dependency to mocking library
    4. it tends to become very big (even whole application)
2. don't forget to activate right profiles in maven
3. in fact no such think as simple mock
    * alternative implementation
    * cdi specialization
    * produced mock only in as CDI
4. client tests
    * `@RunAsClient`/`@Deployment(testable=false)`
    * problems with urls when jboss listens to all interfaces
5. scoped beans:
    * > When running your tests the embedded Weld SE container, Arquillian activates scopes as follows:
      > 
      > * Application scope - Active for all methods in a test class
      > * Session scope - Active for all methods in a test class
      > * Request scope - Active for a single test method
    * http://ctpjava.blogspot.com/2010/08/test-drive-with-arquillian-and-cdi-part.html
6. `@RunAsClient`
    * be careful with urls, it's derived from server bind interface, when server is bind to multiple
      interfaces (0.0.0.0) ulrs will be wrong
    * silent error for api dependencies
  
###Inormations:
* normal "JUnit" tests (`@Test`, `@Before`, `@After` works as expected)
* separation of integration and unit tests
    * mixed with units (little mess)
    * separate module (well, separate module)
    * custom directory (horrible settings)
* units - fast, integrations - slow
* failsafe plugin vs. surefire plugin
* test pyramid
* archive types
* show archives saved by arquillian
* tell about database, show database testing

##Live coding
* example of `MaxPulseBeanIT`
    * remote container by default
    * start remote wildfly (faster cycle, managed for CI)
    * write simple test
    * show deployment in server logs
    * show debugging
* Interceptor test on `UsersRepositoryDaoBeanIT`
    * add mocking library (show that this not works otherwise)
    * write simple test
    * replace `@EJB` with `@Inject` to use CDI `@Producer` to provide mocked repo
    * show actual archive
* database test on `UsersRepositoryImplIT`
    * empty persistence with default datasource
    * mysql datasource
    * show dependant tests
    * add DBUnit in default datasource
    * add DBUnit for mysql
* They should test `FirstLetterA` validator on user entity
* Managed container
* show client testing if there is time
    * add packages with filter
    * package private classes

####Cheat sheet
1. configure base arquillian
    1. junit-container
    1. wildfly remote container
    1. wildfly itself (dependency plugin)
    1. arquillian.xml with username and password
    1. failsafe plugin
    1. be careful with `@Inject` package
2. add maven dependency to shrinkwrap
    1. Maven.Resolver.load(pom).resolve(no version).wiTrans
3. w wildfly containter java optsy przez propertiesa `javaVmArguments`
4. contener wybieramy przez systemproperyvariables `<arquillian.launch>wildfly-remote</arquillian.launch>`
5. archive store location in engine: property `deploymentExportPath`