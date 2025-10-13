#!/bin/bash

#minikube start

# Imposta l'ambiente Docker di Minikube (se necessario)
# eval $(minikube -p minikube docker-env)

# Applicare i file di configurazione
kubectl apply -f k8s/pgsql/deployment.yaml
kubectl apply -f k8s/pgsql/service.yaml

kubectl apply -f k8s/app/deployment.yaml
kubectl apply -f k8s/app/service.yaml



# Verifica se i pod sono in esecuzione
kubectl get pods