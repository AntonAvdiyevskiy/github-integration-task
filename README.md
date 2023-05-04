There are two possibilities of running App.

**Running through Docker**

for it you should just execute next commands
        
1) mvn clean package
2) docker build --tag=github-integration:latest .
3) docker run -p8887:8888 github-integration:latest

 In this case we can go to swagger page -> http://localhost:8887/swagger-ui/index.html#/
    or just use implemented endpoint
 http://localhost:8887/github/user/{username}/repos?per_page=3&page=1

**Running app with java command**

Also we can run application on our PC if we have Java17 installed just using commands:
1) mvn clean package
2) java -jar target/github-integration-0.0.1-SNAPSHOT.jar

In this case we can go to swagger page -> http://localhost:8080/swagger-ui/index.html#/
or just use implemented endpoint
http://localhost:8080/github/user/{username}/repos?per_page=3&page=1

**Endpoint functional description**

Endpoint returns all repositories of user with branches and last commit sha.
There is a possibility to configure endpoint to return fork repos
(by default it returns not fork repos).If we want to retarn fork repos just change the value
in application.properties -> vcs.repositories.fork=false

**Rate Limitation**

API has rate limit for up to 60 requests per hour. If you exceed the rate limit, 
the response will have a 403 status.
