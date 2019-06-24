# X-COMM API
## Springboot
./gradlew runBoot

## Mongo DB
###start
docker run -d -p 27017:27017 --name mongo mongo
###stop
docker stop mongo

##build docker image
docker build -t x-comm-api .
docker run -d -p 8080:8080 --name x-comm-api x-comm-api --network=host 

docker push ecimionatto/x-comm-api