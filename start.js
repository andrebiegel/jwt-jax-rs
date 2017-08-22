#!/usr/bin/jjs -fv
var cmd = "docker run -d -p 8080:8080 abiegel/jaxrs --name jaxrs"
var System = Java.type("java.lang.System");
$EXEC(cmd, System.in, System.out, System.err);