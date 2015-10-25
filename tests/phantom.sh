#!/bin/bash
echo $@
phantomjs --web-security=false --local-to-remote-url-access=true --ignore-ssl-errors=true $@
