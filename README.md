# Tubewarder ![Tubewarder Icon](https://raw.githubusercontent.com/weweave/tubewarder/master/icon/Tubewarder64.png)
A centric, template-based solution for outbound messaging (email, sms, etc.). 

## Enjoy a new quality of outbound communication
Tubewarder makes sending messages to your users easier than ever:
* All your outbound communication in one place
* Easy-to-use templating system based on [Apache Freemarker](http://freemarker.incubator.apache.org)
* Connect your applications using SOAP web services or RESTful services
* Get rid of message and templating code in your applications
* Configurable, extendable outbound connectors (API available)
* Central place for controlling and archiving your outbound communication

![Tubewarder Icon](https://raw.githubusercontent.com/weweave/tubewarder/master/icon/screenshot.png)

Note: Tubewarder is still under active development. The current release is 1.0-Alpha5, but features the core functionality. Use it on your own risk.

## Technology
Tubewarder uses [WildFly Swarm](http://wildfly-swarm.io), a lightweight, modular approach for building and running Java applications.

## Getting started
Make sure you have Java 1.8 installed.

You can download a pre-built JAR file from the [Releases Page](https://github.com/weweave/tubewarder/releases). After downloading tubewarder-swarm.jar, run it with this command:

```
java -jar tubewarder-swarm.jar
```

If you want to build from the most recent source code instead, use the following commands:

```
git clone git://github.com/weweave/tubewarder.git
cd tubewarder
mvn package
java -jar target/tubewarder-swarm.jar
```

The server listens on port 8080 by default.

## Connecting to the server
For sending messages, use one of the following URLs:
* SOAP: http://localhost:8080/ws/send (WSDL available at http://localhost:8080/ws/send?wsdl)
* REST: http://localhost:8080/rs/send

## Configuration
Tubewarder can be configured using command line arguments.

The following command prints out the available commands:

```
java -jar target/tubewarder-swarm.jar -help
```

Probably most important is the configuration of the database. You can choose between the H2 database engine or MySQL for persistence. If nothing else is set, it uses an H2 in-memory database. This means, as soon as you stop the application, all modifications are lost.

To specify a local file where the H2 database is to be stored:

```
java -jar target/tubewarder-swarm.jar -db h2 -h2 /path/to/tubewarder-db
```

To use H2 in Server Mode:

```
java -jar target/tubewarder-swarm.jar -db h2 -h2 tcp://localhost//data/tubewarder -dbUser tubewarder -dbPass tubewarder
```

Check out the [H2 website](http://www.h2database.com/html/cheatSheet.html) for more information.

To connect to a MySQL Server:

```
java -jar target/tubewarder-swarm.jar -db mysql -mysql localhost:3306/tubewarder -dbUser tubewarder -dbPass tubewarder
```

## Docker container
There is a pre-built [Docker image](https://hub.docker.com/r/weweave/tubewarder/) for Tubewarder. We update the image regularly so that you can be sure you're always running the latest version.

Pull it from weweave/tubewarder like this:

```
docker pull weweave/tubewarder
docker run \
    -p 8080:8080 \
    --name tubewarder \
    weweave/tubewarder
```

This will expose Tubewarder's web interface on port 8080. Access it at: http://localhost:8080/ (replace localhost with your docker host's hostname or ip address)

## Output Handler API
Output Handlers do the actual work, as they perform the outbound processing. There are built-in output handlers for email and console output. You can easily develop additional output handlers by using the Output Handler API.

If you're using Maven, add the following dependency to your pom.xml:
```
<dependency>
	<groupId>net.weweave.tubewarder</groupId>
	<artifactId>outputhandler-api</artifactId>
	<version>1.0-Alpha5</version>
</dependency>
```
Check the source code of the [email output handler](https://github.com/weweave/tubewarder/blob/master/outputhandlers/email/src/main/java/net/weweave/tubewarder/outputhandler/EmailOutputHandler.java) as a reference.

Additional output handler JARs must be located in the libs/ folder of the current working directory. 

## Logging
You can find the log files in these locations:
* Output log: /var/log/tubewarder.log
* Stderr log: /var/log/tubewarder.err

## License
For non-commercial projects, you may use Tubewarder under the terms of the GPLv3.

## Current status
* Core functionality is working as intended
* There is an easy-to-use web frontend for all administrative tasks (e.g. managing app tokens, managing channels, and templates)
* Authentication is implemented on the sending API, but it's not featuring authorization. This means: An application requires an App Token to access the sending API, but any valid App Token can access any template.
* The management web frontend features authentication/authorization as of 1.0-Alpha2.
* You can test the sending API and build your custom JSON request using the Send-API Tester in the web frontend (available as of 1.0-Alpha3).
* Output handlers are located and loaded dynamically using the Output Handler API introduced with 1.0-Alpha4.

## Next steps
Take a look at the [milestones](https://github.com/weweave/tubewarder/milestones) to find out what is planned for upcoming releases.