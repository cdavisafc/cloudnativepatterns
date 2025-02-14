# Chapter 5: App redundancy: Scale-out and Statelessness

This chapter introduces the idea that any microservice can (will) have multiple instances deployed, delivering both scale and resilience. With this comes the challenge of managing state across those multiple instances. Hence the chapter title of "scale-out and statelessness."

This particular version of the chapter 5 code is an experiment to apply newer technology - namely Temporal - to the implementation of the post aggregation service. While the book initially did not add retry logic into the solution until chapter 9, because retries are a key capability of Temporal, I've included it in this version. The current implementation does not yet address the state-management challenge (coming soon!).

Two (or three) blog posts cover the implementation in more detail:
- [Applying Temporal to Request-Response Microserves](https://medium.com/@cdavisafc/applying-temporal-to-request-response-microservices-4312ad59b165)
- [Spring Boot and Temporal](https://medium.com/@cdavisafc/spring-boot-and-temporal-3840114fc341)
  - and a short [addendum](https://medium.com/@cdavisafc/temporal-activities-as-spring-beans-an-addendum-f9f72418cd79) that does a bit more Spring Boot magic for Temporal activities

# The app

The demo application consists of three separate microservices:
- A *connections* service that maintains a list of system users as well as a list of connections between them; one user may follow another user.
- A *posts* service that manages a list of posts written by the users of the system
- A *connection's posts* service which aggregates posts for all of the users that a particular individual follows.

Each of these microservices is built into a separate jar file and each jar will expose a web server - when running locally you must have them run on different ports.

This version of the implementation experiments with using a Temporal workflow to implement the *connection's posts* (also refered to as the *posts aggregator*) service. Once again, I refer you to the above blog posts for the details of that experiment.

I am planning several enhancements including:
- shifting from redis to Temporal-managed state
- Kubernetes deployment manifests
- ... (if you have something, file and issue or send me a note)

## Setup

See the README for this repo for base setup instructions.

Additionally, you will need to do three things:

1. Run a local Temporal dev service. You will need to install the Temporal cli and then run the following command:

```
temporal server start-dev
```

2. Run a local mysql instance and create the `cookbook` database. Running mysql locally is most easily done using docker.

```
docker run --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -d mysql
```

And then using a mysql client some some sort (I did `brew install mysql-client`), providing the password `password` when prompted:

```
mysql -h 127.0.0.1 -P 3306 -u root -p -e "create database cookbook";
``` 

3. Run a local redis instance. This is easiest done using docker.

```
docker run --name redis -p 6379:6379 -d redis
```

## Running the sample

From within this directory:

```
mvn clean install
```

This will build four jar files, one for each of the connections and posts RESTful services, and two more for the the post aggregation service - one that runns the API endpoints and another for the Temporal worker that will run the workflow and activities. You can then run these jar files in four different terminal windows with the following commands:

```
java -Dserver.port=8081 -Dspring.datasource.url=jdbc:mysql://localhost:3306/cookbook -jar cloudnative-posts/target/posts-2.0-SNAPSHOT.jar
```
```
java -Dserver.port=8082 -Dspring.datasource.url=jdbc:mysql://localhost:3306/cookbook -jar cloudnative-connections/target/connections-2.0-SNAPSHOT.jar
```
```
java -Dserver.port=8080 -jar cloudnative-connectionposts/controller/target/connectionposts-controller-2.0-SNAPSHOT.jar
```
```
java -jar cloudnative-connectionposts/workflow/target/connectionposts-workflow-2.0-SNAPSHOT.jar
```

Then when you run the following two commands, which log in a user with the username `cdavisafc` and then retrieves posts from authors followed by her, you can see log messages in each of the four windows. Of course, you'll also see the results of the connectionsposts service invocation.

```
curl -X POST -i -c cookie localhost:8080/login?username=cdavisafc
curl -i -b cookie localhost:8080/connectionsposts
```
