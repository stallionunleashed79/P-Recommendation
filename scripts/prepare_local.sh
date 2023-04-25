#!/usr/bin/env bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

source "$DIR/style.sh"

display_header "Preparing Grief environment locally"

cd $DIR/..
info_msg "Running docker images"
docker run -d --rm --name postgres -p 5432:5432 postgres:10.7-alpine
docker run  -d --rm --name s3 -p 9090:9090  adobe/s3mock:2.1.17
docker run -d --rm --name mock -p 1080:1080 mockserver/mockserver -serverPort 1080 -logLevel INFO

sleep 3

info_msg "Checking if Postgres DB is available..."
if [[ $(docker inspect -f '{{.State.Running}}' postgres) =  "true" ]] && lsof -i :5432; then
 succ_msg "Postgres DB is ready!";
else
 fail_msg "Postgres DB is not available."
 exit 1
fi

info_msg "Checking if S3 is available..."
if [[ $(docker inspect -f '{{.State.Running}}' s3) =  "true" ]] && lsof -i :9090; then
 succ_msg "S3 is ready!";
else
 fail_msg "s3 is not available."
 exit 1
fi

info_msg "Checking if Mockserver is available..."
if [[ $(docker inspect -f '{{.State.Running}}' mock) =  "true" ]] && lsof -i :1080; then
 succ_msg "Mockserver is ready!";
else
 fail_msg "Mockserver is not available."
 exit 1
fi

