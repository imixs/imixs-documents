#!/bin/bash

############################################################
# Script to backup the spacy ml models from inside the running container
#
############################################################

echo "========================================================================="
echo "Backup Imixs-ML Spacy Models...."
echo "========================================================================="

BACKUP_DATE="$(date +%Y%m%d%H%M)"
BACKUP_PATH="/kubernetes/k3s/imixs-ml/"$BACKUP_DATE"/"

# first compute the POD name....
# See: https://stackoverflow.com/questions/47389443/finding-the-name-of-a-new-pod-with-kubectl
POD=$(kubectl get -n default pod -l app=imixs-ml-spacy -o jsonpath="{.items[0].metadata.name}")


mkdir -p "$BACKUP_PATH"

echo "Backup from POD $POD ...."
kubectl cp imixs-ml/$POD:/usr/src/app/models "$BACKUP_PATH"

echo "Backup completed"