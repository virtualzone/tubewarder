#!/bin/sh

curl -X POST -d "@send_sms.json" -H "Content-Type: application/json" http://localhost:8080/rs/send