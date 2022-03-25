# NIBSS INSTANT PAYMENT SIMULATION ASSESSMENT

This is a sample application that simulate the NIBSS instant money transfer between two bank accounts as 
requested in the assessment. Please follow the procedure below to test. 

*NOTE*: This application can be deployed either on docker or kubernetes, so you must have a docker engine running in local to test

## Build jar file and docker image for the application
Navigate to project directory and run the following commands to build the jar and docker image. 

```
mvn clean compile package -DskipTests

docker build -t dotpay-assessment:1.0.0
```

## Deployment on Docker
Navigate to the project folder where the docker-compose file is located. In the docker compose file, change the application image
name if necessary. The file also contains the MariaDB database which is pulled directly from docker hub.
```
docker-compose up
```

## Deployment on Kubernetes cluster
To deploy on a kubernetes cluster, make sure you have a cluster running either online or offline (Minikube).
Run the following to perform the operations:
1. build docker image and adding a docker hub repo name, this is because the kubernetes pulls images from docker hub. 
2. Edit the image name to take this format {docker-repo-name} / {image-name} : {tag} in the pom file
3. push image to docker hub
4. deploy
```
mvn spring-boot:build-image -Dskiptest
docker push oladapoyuken/dotpay-assessment:0.0.1-SNAPSHOT

kubectl apply -f kubernetes-dotpay-app-deployment.yaml
kubectl apply -f kubernetes-mariadb-deployment.yaml

```

## Api Documentation
After a successful service deployment, enter the follow url on browser to view the api documentations

*docker*:
```
http://localhost:9090/swagger-ui/index.html
```
*kubernetes*:
```
{base-url}/swagger-ui/index.html
```

## Things to note
1. 10 customers account numbers and balance were auto-generated.
2. You can get all customers to view their account numbers in other to make transactions.
3. In the docker-compose file, you can set a cron job expression to set when Commissions can be processed on transactions daily.
Same can be done for the Daily transaction summary too. Run docker-compose up when changes are made.
4. You can get the summary for a specific day, but if no summary has been done by the scheduler for that date, then it temporarily generates one.
5. Test operations using Postman.
