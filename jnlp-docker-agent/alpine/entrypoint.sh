#!/bin/sh

set -e

AZURL="http://169.254.169.254/latest/meta-data/placement/availability-zone"
export AWS_DEFAULT_REGION=`curl -m 0.5 -s $AZURL | sed 's/[a-z]$//'`

exec /usr/local/bin/jenkins-agent "$@"
