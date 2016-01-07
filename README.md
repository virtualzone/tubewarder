# Tubewarder
A centric, template-based solution for outbound messaging (email, sms, etc.). 

## Enjoy a new quality of outbound communication
Tubewarder makes sending messages to your users easier than ever:
* All your outbound communication in one place
* Easy-to-use templating system based on [Apache Freemarker](http://freemarker.incubator.apache.org)
* Connect your applications using SOAP web services or RESTful services
* Get rid of message and templating code in your applications
* Configurable outbound connectors
* Central place for controllind and archiving our outbound communication

Note: Tubewarder is still under active development. The current release is 1.0-Alpha1, but features the core functionality. Use it on your own risk.

## Technology
Tubewarder uses [WildFly Swarm](http://wildfly-swarm.io), a lightweight, modular approach for building and running Java applications.

## Getting started
Make sure you have Maven and Java 1.8 installed.

Use the following commands to get the latest source and run the server:

```
git clone git://github.com/virtualzone/tubewarder.git
cd tubewarder
mvn package
java -jar webapp/target/webapp-1.0-SNAPSHOT-swarm.jar
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
java -jar webapp/target/webapp-1.0-SNAPSHOT-swarm.jar -help
```

Probably most important is the configuration of the database. Tubewarder uses the H2 database engine for persistence. If nothing else is set, it uses an in-memory database. This means, as soon as you stop the application, all modifications are lost.

To specify a local file where the H2 database is to be stored:

```
java -jar webapp/target/webapp-1.0-SNAPSHOT-swarm.jar -db /path/to/tubewarder-db
```

To use H2 in Server Mode:

```
java -jar webapp/target/webapp-1.0-SNAPSHOT-swarm.jar -db tcp://localhost//data/tubewarder -dbUser tubewarder -dbPass tubewarder
```

Check out the [H2 website](http://www.h2database.com/html/cheatSheet.html) for more information.

## Current status
* Core functionality is working as intended
* There is an easy-to-use web frontend for all administrative tasks (e.g. managing app tokens, managing channels, and templates)
* Authentication is implemented on the sending API, but it's not featuring authorization. This means: An application requires an App Token to access the sending API, but any valid App Token can access any template.
* There is no authentication/authorization in the web frontend yet. You should definitely protect access to it using some other measures in the meantime.

## Next steps
* Add Authentication/authorization to the web fronted
* Add authorization to the sending API (restrict App Tokens to specific templates/channels)
* Improve error handling in web fronted 
* Enable additional databases
