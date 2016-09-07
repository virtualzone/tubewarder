# Requirements
Tubewarder is based on WildFly Swarm, a Java Enterprise Edition (JEE) modular container solution. It does not require a Java application server, but instead brings its own.

* Java 1.8 or later (check with `java -version`)
* Recommended: Static IP address
* Optional: MySQL Database
* Optional: PostgreSQL Database
* Optional: Maven 3 (if you want to build from source)
* Optional: Git (if you want to build from source)

# Using Docker
The easiest way to get Tubewarder up and running is by using the pre-built Docker images. If you have Docker installed on your system, run the following command to start the Tubewarder Docker Container:

```
docker pull weweave/tubewarder
docker run \
    -p 8080:8080 \
    --name tubewarder \
    weweave/tubewarder
```

This will expose Tubewarder's web interface on port 8080. Access it at: http://localhost:8080/ (replace localhost with your docker host's hostname or ip address)

Note: If nothing else has been specified, the Docker container will start with an in-memory H2 database, so there will be no persistence. To enable persistence, you need to change the Tuberwarder configuration.

## Method 1: Deriving from the base image

Create a Dockerfile like this:

```
FROM weweave/tubewarder
ADD my-tubewarder.conf /opt/tubewarder/tubewarder.conf
```

## Method 2: Using environment variables

You can overwrite all configuration parameters using environment variables, i.e.:

```
docker run \
    -p 8080:8080 \
    --name tubewarder \
    --link mysql:mysql \
    -e "TUBEWARDER_DB=mysql" \
    -e "TUBEWARDER_MYSQL_PATH=mysql:3306/tubewarder" \
    weweave/tubewarder
```

The environment variables all start with TUBEWARDER\_ and end with the key of the bootstrap configuration option in uppercase, whereby a dot (.) becomes an underscore (\_). So the bootstrap configuration option mysql.path is TUBEWARDER\_MYSQL\_PATH as an environment variable.

## Example: Using Docker Compose to set up MySQL and Tubewarder

Here is an example docker-compose.yml which sets up a MySQL server and links it to a depending Tubewarder container.

```
version: '2'
services:
  tubewarder:
    image: weweave/tubewarder
    volumes:
      - "/etc/localtime:/etc/localtime:ro"
    ports:
      - "8080:8080"
    links:
      - mysql
    depends_on:
      - mysql
    environment:
      - TUBEWARDER_DB=mysql
      - TUBEWARDER_MYSQL_PATH=mysql:3306/tubewarder
      - TUBEWARDER_MYSQL_USERNAME=tubewarder
      - TUBEWARDER_MYSQL_PASSWORD=Test1234
  mysql:
    image: mysql:5.7
    volumes:
      - "/etc/localtime:/etc/localtime:ro"
      - "/docker/storage/mysql/mysql:/var/lib/mysql"
      - "/docker/storage/mysql/conf.d:/etc/mysql/conf.d"
    environment:
      - MYSQL_ROOT_PASSWORD=Test1234
      - MYSQL_DATABASE=tubewarder
      - MYSQL_USER=tubewarder
      - MYSQL_PASSWORD=Test1234
```

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

# Bootstrap configuration
While most of the configuration is handily done using Tubewarder's Web Interface (or the underlying REST APIs), a minimum set of configuration required at the start of Tubewarder is accomplished using a plain configuration file. Essentially, the database and port configuration is done here. 

The bootstrap configuration is located in the file: tubewarder.conf

The file is well documented, so here's a copy & paste of the default configuration file:

```
# Tubewarder Bootstrap Configuration

# The TCP port for the HTTP server
http.port = 8080

# Database backend to be used.
# Valid settings: h2, mysql, postgresql
db = h2

# Settings for h2 database backend.
# Requires: db = h2
# Examples:
# h2.path = mem
# h2.path = /path/to/tubewarder-db
# h2.path = tcp://localhost//data/tubewarder
h2.path = mem:tubewarder

# Username for h2 if you use it in server mode
h2.username = tubewarder

# Password for h2 if you use it in server mode
h2.password = tubewarder

# Settings for MySQL database backend.
# Requires: db = mysql
mysql.path = localhost:3306/tubewarder

# Username for MySQL
mysql.username = tubewarder

# Password for MySQL
mysql.password = tubewarder

# Settings for PostgreSQL database backend.
# Requires: db = postgresql
postgresql.path = localhost:5432/tubewarder

# Username for PostgreSQL
postgresql.username = tubewarder

# Password for PostgreSQL
postgresql.password = tubewarder
```

If you're using the Docker image and want to modify the bootstrap configuration, it's recommended to create a child image of weweave/tubewarder and add your customized configuration file. Example:

```
FROM weweave/tubewarder
ADD my-custom-config.conf /opt/tubewarder/tubewarder.conf
```

# Initial start
You need to accept the License Agreement after the first start of Tubewarder. If you omit this step, all services will return a "Forbidden" (403) HTTP Status Code.

To accept the terms, just browse to the web interface at port 8080 (http://localhost:8080) and tick the checkbox below the License Agreement.

# Log Files
If you're using the Docker image, you can find the log files in these locations:

* Output log: /var/log/tubewarder.log
* Stderr log: /var/log/tubewarder.err

Please note that these are low-level system log files. You can find archived messages being sent via the Send API in a comfortable way using the web interface.

When running Tubewarder using Java from the command line, the log messages are printed directly to the console.


# Securing your installation
Tubewarder provides authentication and authorization for both the REST services used by the web interface and the Send API. However, as Tubewarder is dealing with potentially sensitive information, it's strongly reommended to further secure your installation.

Especially, you should consider doing the following things:

* Run Tubewarder behind a front-end HTTP server (such as Apache or Nginx) that enforces SSL and proxies requests to Tubewarder.
* Use a valid, officially signed SSL certificate for HTTPS with strong encryption. (2048 bit or more).
* Run Tubewarder in an isolated environment (e.g. inside a Docker container or in a virtual machine) with an exclusively provided database.
* Immediately change the default password ("admin") of the admin user.
* Use strong passwords for all of your users.
* Store and use App Tokens carefully. Optimally, these should be stored encrypted in your applications and never get logged/seen anywhere.
* Carefully decide if the REST and SOAP services (reachable at /rs/ and /ws/, see below) need to be exposed anywhere. When possible, limit access to the URLs to specific IP addresses using your front-end HTTP server.

Interactive HTTP resources are exposed at these URLs:

* /ws/send: Send API via SOAP
* /ws/send?wsdl: Send API's WSDL
* /rs/send: Send API via REST
* /rs/auth: RESTful service for authenticating users (required before using any of the REST services below)
* /rs/*: RESTful services for getting and setting system properties (such as Channels, Templates, etc.)

All other HTTP resources are static files that do not expose information about your infrastructure and should not harm your installation.

# High availibility
You can run multiple instances of Tubewarder behind an HTTP load balancer to establish high availibility. It's critical to use the same database in all of your instances because Tubewarder uses the database to keep track of the status (dead/alive) of all instances running in one cluster. You don't need to explicitly configure the instances in one cluster - as long as all instances connect to the same database, they'll find it each other. If one instance fails, the others will automatically process dangling send queue items.

Tubewarder identifies an instance by its ip address. Make sure that all instances in one cluster have unique ip addresses.