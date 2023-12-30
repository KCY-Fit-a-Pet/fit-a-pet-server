#!/bin/sh

docker-compose down

docker image rm -f $(docker image ls -f reference='jaeseo/*' -q)
docker container rm fitapet-api

docker-compose up -d
