#!/bin/sh
# file: sendMail.sh
#
# Test mailcatcher by sending a test mail.
#
# Works best from inside the devcontainer aka started docker compose stack.

# https://stackoverflow.com/questions/14722556/using-curl-to-send-email
curl  \
  --url 'smtp://mailcatcher.local:1025' \
  --user 'mail-user@stafleet.net:Warp.DriveRulez!' \
  --mail-from 'cpt.archer@nx-01.starfleet.net' \
  --mail-rcpt 'lt.reed@nx-01.starfleet.net' \
  --upload-file mail.txt
