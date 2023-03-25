# Imixs ML

Deployment for Imixs-ML and the Apache-Tika OCR Service

## First Setup

First create a directory for your ML models and copy the default model:

    $ mkdir -p /opt/kubernetes-local-pv/office-imixs-ml
    $ cp -r ./kubernetes/k3s/imixs-ml/models/invoice-de-0.2.0 /opt/kubernetes-local-pv/office-imixs-ml/

## Deployment and apply Changes

Next you can deploy the Imixs-ML module

    $ kubectl apply -f ./kubernetes/k3s/imixs-ml

## Spacy Modelle

There are some scripts that can be used to deploy and backup ML models. After the first deployment, the initial spacy model must also be deployed. The ML Deployment Script can be used for this. 

### ML-Backup

Um die Spacy modelle aus dem Container in das git repo zu sichern haben wir einen den Backupscript "ml_model_backup.sh". Dieser sichert die aktuellen Modelle in das Git Repo

	$ /kubernetes/k3s/imixs-ml/ml_model_backup.sh
	
Die modell dateien werden in einem unterverzeichnis mit dem Zeitstempel abgelegt. So kann man gezielt bestimmte stände verwalten.



### ML-Deployment

Um ein bestimmtes Modell neu zu deploymen kann der script "ml_model_deploy.sh" verwendet werden. Er überträgt einen modell ordner in die spacy instanz.

	$ /kubernetes/k3s/imixs-ml/ml_model_deploy.sh invoice-de-0.2.0


	