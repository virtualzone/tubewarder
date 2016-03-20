var bodyParser = require('body-parser');
var express = require("express");
var basicAuth = require('basic-auth-connect');

var app = express();

app.use(bodyParser.urlencoded({ extended: true }));
app.use(basicAuth("testUser", "testPass"));

/**
 * Example query strings:
 * http://localhost:8089/get?toAddr=${recipientAddress}&toName=${recipientName}&subject=${subject}&content=${content}
 */

app.get("/get", function(req, res) {
    console.log("Received GET request at /get");
    console.log("GET Parameters:");
    for (var key in req.query) {
        console.log(key + " = " + req.query[key]);
    }
    res.send("OK");
});

app.post("/post", function(req, res) {
    console.log("Received POST request at /post");
    console.log("Content type: " + req.get('Content-Type'));
    console.log("GET Parameters:");
    for (var key in req.query) {
        console.log(key + " = " + req.query[key]);
    }
    console.log("POST Parameters:");
    for (var key in req.body) {
        console.log(key + " = " + req.body[key]);
    }
    res.send("OK");
});

app.listen(8089, function() {
    console.log("Listening on port 8089");
});