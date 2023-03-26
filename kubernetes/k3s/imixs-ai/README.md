# Imixs AI

Deployment configuration for Imixs-ML and the Apache-Tika OCR Service

## First Setup

First create a directory for your ML models and copy the default model:

    $ mkdir -p /opt/kubernetes-local-pv/office-imixs-ml
    $ cp -r ./kubernetes/k3s/imixs-ml/models/invoice-de-0.2.0 /opt/kubernetes-local-pv/office-imixs-ml/

## Deployment and apply Changes

Next you can deploy the Imixs-AI module

    $ kubectl apply -f ./kubernetes/k3s/imixs-ai

## Spacy Modelle

There are some scripts that can be used to deploy and backup ML models. After the first deployment, the initial SpaCy model must also be deployed. The ML Deployment Script can be used for this.

### ML-Backup

To do a local backup you can use the script `ml_model_backup.sh`.

	$ /kubernetes/k3s/imixs-ml/ml_model_backup.sh
	
The model files will be stored in a sub directory including the current time stamp



### ML-Deployment

To deploy a specify model you can run the script `ml_model_deploy.sh` :

	$ /kubernetes/k3s/imixs-ml/ml_model_deploy.sh invoice-de-0.2.0


	