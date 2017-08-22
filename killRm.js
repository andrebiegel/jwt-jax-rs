#!/usr/bin/jjs -fv
var cmd = "docker kill jaxrs && docker rm jaxrs"
var System = Java.type("java.lang.System");
$EXEC(cmd, System.in, System.out, System.err);