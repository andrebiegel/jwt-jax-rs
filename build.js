#!/usr/bin/jjs -fv
var cmd = "mvn clean install && docker build -t abiegel/jaxrs ."
var System = Java.type("java.lang.System");
$EXEC(cmd, System.in, System.out, System.err);