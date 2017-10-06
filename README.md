# JWT secured jaxrs implemented with an container request filter 

my primary interest in implementing this, was to investigate the fundamental handling with JWT.
furthermore i wanted to examine options to embedd docker into my daily work.  
## Demo 
the demo contains a jaxrs backend, bundled into a docker container. The backend has s hello Resoruce and container request filter, which authenticates and authorizes a user,when the provided password is 42. 
The integration test therefore tests the following communication sequence. 

1. GET: to an unsecured resource app/hello?message=dude
2. POST: Login via form post app/login
3. Extracting the bearer token
4. GET: requesting a secured resource app/hello/secured?message=dude with auth token
 

##Requirements

* Java 8 
* Docker
* Maven

## Sources
the following sources have been used to realize this demo. 

* https://github.com/spotify/docker-maven-plugin
* https://github.com/fabric8io/docker-maven-plugin
* https://antoniogoncalves.org/2016/10/03/securing-jax-rs-endpoints-with-jwt/
* https://jwt.io/