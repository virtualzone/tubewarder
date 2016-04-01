# Overview
Traditional infrastructures usually look similar to the following diagram: All of your applications have an n:m relationship with all of your outbound messaging services. Besides the vast connectivities, you have lots of duplicated code for logging, archiving, templating, error handling, etc. 

![Traditional infrastructure without Tubewarder](img/infrastructure_wo_tubewarder.png)

Introducing Tubewarder as the central system for handling your outgoing messages, all your applications talk to Tubewarder (1:n), while Tubewarder keeps in touch with the outbound service (1:n). All the code for logging, monitoring, error handling, archiving, etc. is in one place. An easy-to-use web interface allows for managing your message templates for the various channels in one central place. 

![Modern infrastructure wit Tubewarder](img/infrastructure_w_tubewarder.png)

Tubewarder is easy to deploy, has low overhead, and is a good idea if you have as few as two applications doing outbound messaging.


# Features
* Connect applications to Tubewarder using SOAP web services or RESTful services
* Easy-to-use templating system based on [Apache Freemarker](http://freemarker.incubator.apache.org)
* Built-in outbound connectors: Email, HTTP(S)
* Configurable, extendable outbound connectors (API available)
* Error handling for outgoing messages
* Asynchronous processing
* Send Queue Scheduler with configurable max. concurrent threads and retry count
* Archiving of outbound messages for legal purposes
* Powerful administrative web interface
* Integrated user and access management for the web interface 


# Architecture
Todo