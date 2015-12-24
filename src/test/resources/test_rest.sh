#!/bin/sh

curl -X POST -d "@send_mail_text_w_disclaimer.json" -H "Content-Type: application/json" http://localhost:8080/rs/send
# curl -X POST -d "@send_mail_text_wo_disclaimer.json" -H "Content-Type: application/json" http://localhost:8080/rs/send
# curl -X POST -d "@send_mail_html_w_disclaimer.json" -H "Content-Type: application/json" http://localhost:8080/rs/send
# curl -X POST -d "@send_mail_html_wo_disclaimer.json" -H "Content-Type: application/json" http://localhost:8080/rs/send
# curl -X POST -d "@send_sms.json" -H "Content-Type: application/json" http://localhost:8080/rs/send
