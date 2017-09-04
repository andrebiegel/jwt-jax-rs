#!/usr/bin/env bash
mvn clean package &&  docker build -t abiegel/jaxrs .