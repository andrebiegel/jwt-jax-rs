#!/usr/bin/env bash
mvn clean package &&  docker build -t abiegel/jaxrs .
docker run --name jaxrs -d -p 8080:8080 abiegel/jaxrs
