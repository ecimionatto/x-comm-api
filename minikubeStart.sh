#!/bin/sh
kubectl create -f ./kubMongoVolume.yaml
kubectl create -f ./kubMongoVolumeClaim.yaml
kubectl create -f ./kubMongo.yaml
kubectl create -f ./kubMongoService.yaml
kubectl create -f ./kubApi.yaml
kubectl create -f ./kubApiService.yaml
kubectl get services
minikube ip