There are two possibilities of running App.

Running through Docker, for it you should just execute next commands
        
1) mvn clean package
2) docker build --tag=github-integration:latest .
3) docker run -p8887:8888 message-server:latest

 In this case we can go to swagger page -> http://localhost:8887/swagger-ui/index.html#/
    or just use implemented endpoint
 http://localhost:8887/github/user/{username}/repos?per_page=3&page=1

Also we can run applicationon our PC if we have Java17 installed just using commands:
1) mvn clean package
2) java -jar target/github-integration-0.0.1-SNAPSHOT.jar

In this case we can go to swagger page -> http://localhost:8080/swagger-ui/index.html#/
or just use implemented endpoint
http://localhost:8080/github/user/{username}/repos?per_page=3&page=1

Endpoint returns all not fork repositories of user with branches and last commit sha
