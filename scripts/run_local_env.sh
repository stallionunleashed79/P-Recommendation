#!/usr/bin/env bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

source "$DIR/style.sh"

display_header "Running RECO locally"

info_msg "Executing maven clean install..."
mvn clean install || { fail_msg "Maven clean install Unsuccessful!"; exit 1; }

cd $DIR/..
info_msg "Executing docker-compose build and up..."
docker-compose build
docker-compose up -d --force-recreate --renew-anon-volumes
sleep 3

info_msg "Checking if RECO is available..."
if [[ $(docker inspect -f '{{.State.Running}}' main) =  "true" ]] && lsof -i :8080; then
 succ_msg "RECO is ready!";
else
 fail_msg "RECO is not available. Taking docker-compose down."
 docker-compose down
 exit 1
fi

info_msg "Checking if MySQL is available..."
if [[ $(docker inspect -f '{{.State.Running}}' mysql) = "true" ]] && lsof -i :3306; then
 succ_msg "MySQL is ready!";
else
 fail_msg "MySQL is not available. Taking docker-compose down."
 docker-compose down
 exit 1
fi
