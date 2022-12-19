# IoT-Home-Network

This project tries to simulate an IoT network composed by some sensors, a cloud service that processes the sensors data, an ai-agent that makes predictions and a non relational database.

The following image is an schema of the way it works the cloud-service and how do each thread communicate with the others:

![Diagrama sin título drawio](https://user-images.githubusercontent.com/79267515/207462238-1d754e06-2e34-4718-836e-7a4b382604cb.png)

## Execute the project

In order to execute the project the following dependencies are needed:
  - maven
  - jdk17
  - Docker (also docker compose)
  
First of all compile the java code:

```
mvn clean package
```

Once it finishes, create the docker image:

```
docker build -t cloud-service .
```

Finally, run the whole project typing the following command on the root of the project:

```
docker compose up
```
