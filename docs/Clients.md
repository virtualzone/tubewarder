# Clients for sending to Tubewardee
Instead of directly dealing with the Send API, you can instead use pre-implemented client libraries. These libraries hide the SOAP and REST interfaces from the developer, so you can focus on the actual sending of messages.

The following client libraries are currently available:
* Java

Libs for more languages will follow. In the meantime, you can of course connect directly to the Send API.


# Java
Tubewarder' is Java Client currently supports the REST API. It employs the JAX-RS 2.0 API, so you will need a JEE6 or newer application server to use it.

To get started with the Java Client library, the easiest way is to include the following dependency in your Maven pom.xml:

```
<dependency>
    <groupId>net.weweave.tubewarder</groupId>
    <artifactId>client</artifactId>
    <version>1.0.1</version>
</dependency>
```

Your source code might look like this:

```
TubewarderClient client = new TubewarderRestClient("http://your-tubewarder-server");
SendRequest req = new SendRequest("your-app-token");
req.setChannel("Email");
req.setTemplate("Welcome");
req.addModelParam(new KeyValue("firstname", "..."));
SendResponse resp = client.send(req);
```
