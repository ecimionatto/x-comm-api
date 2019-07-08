#!/bin/sh
kubectl create -f ./kubMongoVolume.yaml
kubectl create -f ./kubMongoVolumeClaim.yaml
kubectl create -f ./kubMongo.yaml
kubectl create -f ./kubMongoService.yaml
kubectl create -f ./kubApi.yaml
kubectl create -f ./kubApiService.yaml
kubectl create -f ./kubUx.yaml
kubectl create -f ./kubUxService.yaml
kubectl get services
minikube ip