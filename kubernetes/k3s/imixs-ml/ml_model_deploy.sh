#!/bin/bash

############################################################
# Script to deploy a spacy ml model into the running container
#
############################################################

echo "========================================================================="
echo "Deploy Imixs-ML Spacy Model...."
echo "========================================================================="

# first compute the POD name....
# See: https://stackoverflow.com/questions/47389443/finding-the-name-of-a-new-pod-with-kubectl
POD=$(kubectl get -n default pod -l app=imixs-ml-spacy -o jsonpath="{.items[0].metadata.name}")

if [ $1 == "" ]
  then
    echo "model folder missing! (run with:  ml_model_deploy.sh invoice-de-0.2.0/)"
  else
    echo "deploy model $1 into POD $POD ...."
    kubectl cp /kubernetes/k3s/imixs-ml/models/$1 krieger-ml/$POD:/usr/src/app/models
    echo "deployment completed"
fi






 