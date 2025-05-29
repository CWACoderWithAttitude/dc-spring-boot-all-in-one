#!/bin/sh
mail_host=mailcatcher.local
#mail_host=host.docker.internal


curl smtp://${mail_host}:10125 --mail-from fry@planet-express.org --mail-rcpt \
zoidberg@planet-express.org --upload-file email.txt