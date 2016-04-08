# Tubewarder ![Tubewarder Icon](https://raw.githubusercontent.com/weweave/tubewarder/master/icon/Tubewarder64.png)
A centric, extensible, template-based solution for outbound messaging (email, sms, etc.). 

## Enjoy a new quality of outbound communication
Tubewarder makes sending messages to your users easier than ever:
* All your outbound communication in one place
* Easy-to-use templating system based on [Apache Freemarker](http://freemarker.incubator.apache.org)
* Connect your applications using SOAP web services or RESTful services
* Built-in outbound connectors: Email, HTTP(S)
* Configurable, extendable outbound connectors (API available)
* Powerful administrative web interface
* Send Queue Scheduler with configurable max. concurrent threads and retry count
* Archiving of outbound messages
* Get rid of message-sending, templating, channel-specific error handling, and logging code in your applications

![Tubewarder Icon](https://raw.githubusercontent.com/weweave/tubewarder/master/icon/screenshot.png)

Note: Tubewarder is still under active development. The current release is 1.0 Beta 2, which is feature-complete and well tested. However, we're still hunting bugs and improving stability. Use it on your own risk.

## Purpose
Traditional infrastructures usually look similar to the following diagram: All of your applications have an n:m relationship with all of your outbound messaging services. Besides the vast connectivities, you have lots of duplicated code for logging, archiving, templating, error handling, etc. 

![Traditional infrastructure without Tubewarder](https://raw.githubusercontent.com/weweave/tubewarder/master/icon/infrastructure_wo_tubewarder.png)

Introducing Tubewarder as the central system for handling your outgoing messages, all your applications talk to Tubewarder (1:n), while Tubewarder keeps in touch with the outbound service (1:n). All the code for logging, monitoring, error handling, archiving, etc. is in one place. An easy-to-use web interface allows for managing your message templates for the various channels in one central place. 

![Modern infrastructure wit Tubewarder](https://raw.githubusercontent.com/weweave/tubewarder/master/icon/infrastructure_w_tubewarder.png)

## Documentation
You can find the documentation at:

http://tubewarder.readthedocs.org/

## Technology
Tubewarder uses [WildFly Swarm](http://wildfly-swarm.io), a lightweight, modular approach for building and running Java applications. You don't need an application server - running Tubewarder out-of-the box is easy with Java installed on your server.

## Getting started
Make sure you have Java 1.8 installed.

You can download a pre-built release from the [Releases Page](https://github.com/weweave/tubewarder/releases). After downloading the release, unzip it, change to the extracted directory, and run Tubewarder with this command:

```
unzip tubewarder.zip
cd tubewarder
java -jar tubewarder-swarm.jar
```

Make sure to change to the directory tubewarder-swarm.jar is located in! Otherwise the output handlers residing in libs/ can't be found.

If you want to build from the most recent source code instead, use the following commands:

```
git clone git://github.com/weweave/tubewarder.git
cd tubewarder
mvn package
cd target
java -jar tubewarder-swarm.jar
```

The server listens on port 8080 by default.

## Connecting to the server
For configuring your instance, open your web browser and go to: http://localhost:8080/

Username/password is admin/admin (you really want to to change this...).

For sending messages, use one of the following URLs:
* SOAP: http://localhost:8080/ws/send (WSDL available at http://localhost:8080/ws/send?wsdl)
* REST: http://localhost:8080/rs/send

## Configuration
Most of the configuration can be done using the Web Interface exposed at port 8080. Some configuration options required at startup need to be done using a plain text configuration file called tubewarder.conf in the application's root directory. For more information, please refer to:

http://tubewarder.readthedocs.org/en/latest/Installation/#bootstrap-configuration

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
	<version>1.0-Beta1</version>
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

## Next steps
Take a look at the [milestones](https://github.com/weweave/tubewarder/milestones) to find out what is planned for upcoming releases.