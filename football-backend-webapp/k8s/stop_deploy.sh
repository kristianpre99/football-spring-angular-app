#!/bin/bash

#minikube start

# Imposta l'ambiente Docker di Minikube (se necessario)
# eval $(minikube -p minikube docker-env)
echo "Stopping and removing Kubernetes resources..."

# Applicare i file di configurazione
kubectl delete -f k8s/pgsql/deployment.yaml
kubectl delete -f k8s/pgsql/service.yaml

kubectl delete -f k8s/app/deployment.yaml
kubectl delete -f k8s/app/service.yaml



# Verifica se i pod sono in esecuzione
kubectl get pods