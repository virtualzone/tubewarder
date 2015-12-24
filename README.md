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

Note: Tubewarder is still under active development. The current release is pre-alpha, but features the core functionality. Use it on your own risk.

## Technology
Tubewarder uses [WildFly Swarm](http://wildfly-swarm.io), a lightweight, modular approach for building and running Java applications.

## Getting started
Make sure you have Maven and Java 1.8 installed.

Use the following commands to get the source and run the server:

```
git clone git://github.com/virtualzone/tubewarder.git
cd tubewarder
mvn wildfly-swarm:run
```

The server listens on port 8080 by default.

## Connecting to the server
For sending messages, use one of the following URLs:
* SOAP: http://localhost:8080/ws/send (WSDL available at http://localhost:8080/ws/send?wsdl)
* REST: http://localhost:8080/rs/send

## Coming soon
* Edit all configuration options using RESTful services
* Lightweight, easy-to-use web frontend for administrative tasks
* Authentication/authorization
