# Using the Web Interface
Tubewarder's web interface is the central place where differnt types of users can configure Tubewarder.

By default, the web interface is exposed on port 8080. This can be changed by the ```http.port``` property in the bootstrap configuration (tubewarder.conf). The default username/password is admin/admin.

The web interface consists of two parts: A set of static files (HTML, CSS, JavaScript) deployed in ```libs/static.jar```, and a set of REST services provided under the path ```/rs/```. You can remove the static.jar file from the libs folder to avoid the web interface from being deployed.

# App Tokens
App Tokens are the authentication keys used by applications addressing the Send API. You should create at one App Token per application which is sending messages. The Tokens themselves are generated automatically. You can assign names so you know which token is used by which application. An App Token can be used to invoke the sending of any Template/Channel, so be careful about who can see/use your Tokens.

# Channels
Channels are routes to send outbound messages by using a specific configuration of an Output Handler. For example, you can have a Channel for sending HTML Mails via your company's SMTP Server reachable at a specific TCP/IP address and port and by establishing a TLS connection, or you can have a Channel for sending text messages (SMS) via some RESTful web service reachable via HTTP POST at a specific URL.

## Generic configuration options
* Unique name: A unique name for the channel. This will be used by applications using the Send API, so you should keep it expressive and short.
* Rewrite rules for recipient name, recipient address, subject, and content: There are situations in which you want to change the recipient name, address, subject or content without bothering the template authors or application developers with it. Imagine an email-to-sms gateway where the recipient's mobile phone number and the actual text (content) goes into the subject of the email (something like "SMS:+49123456790[This is the text.]"). However, your template authors should not need to care about such technical details. So you can set the "Rewrite Subject" to "SMS:${recipientAddress}[${content}]", the "Rewrite Content" to an empty string ("") and the "Recipient Address" to your mail-to-sms gateway's email address.

## Email configuration options
* SMTP Server: Your SMTP server's IP address or hostname.
* Port: Defaults to 25. Could be 465 or 587 if you use SSL/TLS.
* Authentication required: Check this if your SMTP server requires you to sign in before being able to send messages.
* Username: The username for authentication (ignored if "Authentication required" is not checked).
* Password: The password for authentication (ignored if "Authentication required" is not checked).
* Security: None, TLS, or SSL
* Content Type: The MIME Type used when sending messages over this channel.

## Webservice configuration options
* URL (encoded): The encoded URL (including http:// or https:// at the beginning).
* Authentication: None or Basic.
* Username: The username for authentication (ignored if "Authentication" is "None").
* Password: The password for authentication (ignored if "Authentication" is "None").
* Method: The HTTP request method (GET or POST).
* Content Type (POST only): Value of HTTP request header "Content-Type".
* Payload (POST only): Optional HTTP POST request body payload. 

## Facebook configuration options
* Access Token: A Page Access Token for your Facebook app.

Get the token from [Facebook's Developer website](https://developers.facebook.com/apps/). Follow [Facebook's quick start guide](https://developers.facebook.com/docs/messenger-platform/guides/quick-start) for setting up a new for the messenger platform. 

Note: Tubewarder uses [Facebook's Messenger Platform](https://developers.facebook.com/docs/messenger-platform) for delivering the messages. Due to restrictions of this API, the recipient must have sent you a message first before you're able to send messages to him.

## Twitter configuration options
* Consumer Key: Your Twitter app's Consumer Key (API Key).
* Consumer Secret: Your Twitter app's Consumer Secret (API Secret).
* Access Token: Your personal Twitter Access Token to make API requests on your own account's behalf.
* Access Token Secret: Your personal Twitter Access Token Secret to make API requests on your own account's behalf.

Get the required keys and secrets from [Twitter's Application Management website](https://apps.twitter.com/).

Note: Due to restrictions of Twitter's API, you can only send messages on your own account's behalf. In most cases, the recipient must follow you on Twitter in order to be able to send direct messages to him. Alternatively, the recipient must have his/her "allow_dms_from" account setting set to "all". Read more on the [API documentation page for "direct_messages/new"](https://dev.twitter.com/rest/reference/post/direct_messages/new) that's being used for delivering the Twitter messages.

## Console configuration options
* Prefix: Text printed on the console before the actual content.
* Suffix: Text printed on the console after the actual content.

# Templates
Templats are concrete messages you want to send. A Template can contain variables and control structures (e.g. placeholders for salutation, name, and personalized URLs). A Template can be bound to one or more Channels, each with different texts as desired. So, you could have a Template called "New User Welcome Message", with different contents for saying Welcome to your new user via Plain Text Email, HTML Email, or SMS.

Tubewarder uses the Handlebars template system syntax. Refer to the Handlebars documentation regarding the features and syntax you can use in your templates: http://handlebarsjs.com/

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
