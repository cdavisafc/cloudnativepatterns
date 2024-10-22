# Chapter 4: Basic Request/Response Pattern

This is the first example in the first coding chapter in the book. The goal of the chapter is to introduce both this basic pattern (which really does not need introduction, so ubiquitous it is), but also to foreshadow later content by contrasting this with an event driven approach. You can find the sample app for the latter in a different module of this project (for now, only in the [first edition repo](https://github.com/cdavisafc/cloudnative-abundantsunshine/tree/master/cloudnative-eventdriven)).

## Setup

See the README for this repo for base setup instructions.

## Running the sample

From within this directory:

`mvn clean install`

This will build a single jar file with all three of the RESTful services. You can then run that jar with 

`java -jar target/cloudnative-requestresponse-0.0.2-SNAPSHOT.jar`

Once running, you can curl the various services:

```
curl localhost:8080/connectionsposts/cdavisafc

curl localhost:8080/connections/madmax

curl localhost:8080/posts?userIds=1,2
```