Tubewarder NodeJS Client
========================

NodeJS Client for the Tubewarder Central Messaging Gateway. For more information, see: http://weweave.net/products/tubewarder/

## Installation

  npm install tubewarder-client --save

## Usage

  var TubewarderClient = require('tubewarder-client');
  var client = new TubewarderClient('https://localhost:8080');
  var sr = client.createSendRequest('your-access-token');

## Tests

  npm test
