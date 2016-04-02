# Using the Web Interface
Tubewarder's web interface is the central place where differnt types of users can configure Tubewarder.

By default, the web interface is exposed on port 8080. This can be changed by the ```http.port``` property in the bootstrap configuration (tubewarder.conf). The default username/password is admin/admin.

The web interface consists of two parts: A set of static files (HTML, CSS, JavaScript) deployed in ```libs/static.jar```, and a set of REST services provided under the path ```/rs/```. You can remove the static.jar file from the libs folder to avoid the web interface from being deployed.

# App Tokens
App Tokens are the authentication keys used by applications addressing the Send API. You should create at one App Token per application which is sending messages. The Tokens themselves are generated automatically. You can assign names so you know which token is used by which application. An App Token can be used to invoke the sending of any Template/Channel, so be careful about who can see/use your Tokens.

# Channels
Channels are routes to send outbound messages by using a specific configuration of an Output Handler. For example, you can have a Channel for sending HTML Mails via your company's SMTP Server reachable at a specific TCP/IP address and port and by establishing a TLS connection, or you can have a Channel for sending text messages (SMS) via some RESTful web service reachable via HTTP POST at a specific URL.

# Templates
Templats are concrete messages you want to send. A Template can contain variables and control structures (e.g. placeholders for salutation, name, and personalized URLs). A Template can be bound to one or more Channels, each with different texts as desired. So, you could have a Template called "New User Welcome Message", with different contents for saying Welcome to your new user via Plain Text Email, HTML Email, or SMS.

Tubewarder uses the Apache Freemarker template engine. Refer to the Freemarker documentation regarding the features and syntax you can use in your templates: http://freemarker.org

# Logs
When a message is sent using the Send API, it is automatically logged/archived for legal purposes. The Send Queue Scheduler which is in charge of the actual processing updates the status of the sending process in the logged items. You can view and filter the logged messages in the web interface.

# API
The web interface features a Send API Tester. It allows you to interactively build your query to the REST Send API. This serves for two purposes: First, you can find out if your Templates and Channels work as desired. Furthermore, the Send API Tester prints the JSON request payload which may serve as a skeleton for your actual application queries to the Send API.

# System
## Users
Here you can manage the users that are allowed to use the web interface. Users can be assigned diversified access rights:

* Account is enabled
* Can manage App Tokens
* Can manage Channels
* Can manage Templates
* Can manage system settings and users
* Can view Logs

The access rights correspond to the navigation items in the web interface, so it should be easy for you to decide which access rights to assign.

## Configuration
The following system settings can be customized:

* Max. concurrent threads: The maximum number of concurrent processings the Send Queue Scheduler is allowed to handle. You should evaluate how many concurrent threads your system can handle. Setting this property too low on a system which is to process loads of outgoing messages may lead to enormous delays in delivery.
* Max. retries: If a temporary failure occurs while sending a message, the Send Queue Scheduler will attempt delivering the message again. This setting controls the maximum number of retries per message.
* Retry wait time (seconds): The minimum wait time in seconds between two delivery attempts of a message.

## Queue
Shows statistics about the Send Queue. This can be used to monitor the vitality of your installation, and also to find a good value for the "Max. concurrent threads" setting.
