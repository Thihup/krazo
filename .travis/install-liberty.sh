#!/usr/bin/env bash

LIBERTY_URL="https://public.dhe.ibm.com/ibmdl/export/pub/software/websphere/wasdev/downloads/wlp/19.0.0.11/wlp-webProfile8-19.0.0.11.zip"

curl -L -s -o wlp.zip "${LIBERTY_URL}"
unzip wlp.zip
cp .travis/wlp-server-template.xml wlp/templates/servers/defaultServer/server.xml
export  LIBERTY_HOME="$(cd ./wlp/ && pwd)"
