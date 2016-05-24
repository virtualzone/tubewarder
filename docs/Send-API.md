# Overview
You can either use the SOAP web service API or the REST API for sending messages through Tubewarder. Both provide exactly the same functionality.


# SOAP API
Endpoint: /ws/send

WSDL: /ws/send?wsdl

Request method: POST

Request format: SOAP-XML (application/soap+xml)

Response format: SOAP-XML (application/soap+xml)


# REST API
Endpoint: /rs/send

Request method: POST

Request format: JSON (application/json)

Response format: JSON (application/json)

## Request Schema
```
Request {
    token (string, required - your client app's token)
    keyword (string, optional - a keyword for controlling purposes in logging)
    details (string, optional - additional details for controlling purposes)
    echo (bool, optional - if true, rendered subject and content will be returned)
    template (string, required - the template's name)
    channel (string, required - the channel's name)
    recipient (Address, required - the recipient's address)
    model (array of Model, optional - a key-value-list of template parameters)
    attachments (array of Attachment, optional - a key-value-list of template parameters)
}
Address {
    name (string, optional - recipient's name)
    address (string, required - recipient's address (email, number, ...))
}
Model {
    key (string, required - template parameter name)
    value (string, optional - template parameter value)
}
Attachment {
    filename (string, required - filename)
    contentType (string, optional - file's mime type')
    payload (string, required - Base64 encoded file content)
}
```

## Response Schema
```
Response {
    error (int - error code, see below)
    subject (string, included if echo=true - the rendered subject)
    content (string, included if echo=true - the rendered content)
    queueId (string - the unique id under which the message has been queued)
}
```

## Error Codes
* 0 = OK (no error)
* 1 = Invalid input parameters
* 2 = Object lookup error (i.e. requested object does not exist in the database)
* 3 = Permission denied
* 4 = Authorization required
* 5 = Template corrupted
* 6 = Missing model parameter(s)

## Example Request
```
{
   "token": "00000000-0000-0000-0000-000000000000",
   "keyword": "DOI",
   "details": "",
   "echo": true,
   "template": "DOI",
   "channel": "sms",
   "recipient": {
      "name": "Unknown",
      "address": "+49000000000000"
   },
   "model": [
      {"key": "firstname", "value": "John"},
      {"key": "lastname", "value": "Doe"},
      {"key": "code", "value": "1234567890"},
      {"key": "includeDisclaimer", "value": false}
   ],
   "attachments": [
      {
        "filename": "Terms.pdf",
        "contentType": "application/pdf",
        "payload": "..."
      }
   ]
}
```

## Example Response
```
{
   "error": 0,
   "subject": "Hi John, thanks for signing up!",
   "content": "Dear John Doe, here's your activation code: 1234567890",
   "queueId": "00000000-0000-0000-0000-000000000000"
}
```
