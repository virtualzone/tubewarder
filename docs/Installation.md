# Requirements
Tubewarder is based on WildFly Swarm, a Java Enterprise Edition (JEE) modular container solution. It does not require a Java application server, but instead brings its own.

* Java 1.8 or later (check with `java -version`)
* Recommended: Static IP address
* Optional: MySQL Database
* Optional: Maven 3 (if you want to build from source)
* Optional: Git (if you want to build from source)

# Using Docker
The easiest way to get Tubewarder up and running is by using the pre-built Docker images. Our Docker image has a MySQL server pre-installed, so it's fully self-contained. If you have Docker installed on your system, run the following command to start the Tubewarder Docker Container:

```
docker pull weweave/tubewarder
docker run \
    -p 8080:8080 \
    --name tubewarder \
    weweave/tubewarder
```

This will expose Tubewarder's web interface on port 8080. Access it at: http://localhost:8080/ (replace localhost with your docker host's hostname or ip address)

# Using pre-built packages
If you don't want to use the Docker image, you can download a ZIP file containing pre-compiled JAR packages:

You can download a pre-built release from the [GitHub Releases Page](https://github.com/weweave/tubewarder/releases). After downloading the release, unzip it, change to the extracted directory, and run Tubewarder with this command:

```
unzip tubewarder.zip
cd tubewarder
java -jar tubewarder-swarm.jar
```

# Building from source
If you want to use the latest cutting-edge features, download the source code and build it yourself. You'll need Git, Maven and the Java 1.8 JDK installed:

```
git clone git://github.com/weweave/tubewarder.git
cd tubewarder
mvn package
cd target
java -jar tubewarder-swarm.jar
```

# Basic configuration
Todo


# Securing your installation
Todo