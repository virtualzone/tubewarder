#!/bin/sh

# curl -X POST -d "@send_mail_text_w_disclaimer.json" -H "Content-Type: application/json" http://localhost:8080/rs/send
# curl -X POST -d "@send_mail_text_wo_disclaimer.json" -H "Content-Type: application/json" http://localhost:8080/rs/send
# curl -X POST -d "@send_mail_html_w_disclaimer.json" -H "Content-Type: application/json" http://localhost:8080/rs/send
# curl -X POST -d "@send_mail_html_wo_disclaimer.json" -H "Content-Type: application/json" http://localhost:8080/rs/send
curl -X POST -d "@send_sms.json" -H "Content-Type: application/json" http://localhost:8080/rs/send
curl -X POST -d "@send_sms.xml" -H "Content-Type: application/soap+xml" http://localhost:8080/ws/send
# curl -X POST -d "@sysoutoutputhandlerconfiguration_create.json" -H "Content-Type: application/json" http://localhost:8080/rs/sysoutoutputhandlerconfiguration/set
# curl -X POST -d "@emailoutputhandlerconfiguration_create.json" -H "Content-Type: application/json" http://localhost:8080/rs/emailoutputhandlerconfiguration/set