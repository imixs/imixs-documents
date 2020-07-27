# Kubernetes Deployment

This is a deployment configuration for Kubernetes. The setup is configured to use [Kustomize](https://kubernetes.io/docs/tasks/manage-kubernetes-objects/kustomization/) providing a declarative object management.


## Deploy 

To create the deployment objects form the base-deployment run:

	$ kubectl create --kustomize https://github.com/imixs/imixs-documents/kubernetes/
	

## Deploy Custom Setup

To create a custom deployment first download copy this directory. To deployment the objects localy run:

	$ kubectl apply --kustomize  ./kubernetes


**Note:** In this deployment configuration we use a longhorn persistence volume to store the database and the index data. See [Longhorn.io])(https://longhorn.io/) for details. 
	
## Customize

You can easily create an overlay with custom settings based on your base definition. First create a new folder like /overlay/prod and put again a kustomization.yaml file in here.

	bases:
	- ../kubernetes
	
	resources:
	- 090-ingress.yaml
	
	patchesStrategicMerge:
	- 000-custom-env.yaml


The kustomization.yaml file simply points into the base directory. Now you have the following directory structure:

	.
	├── kubernetes
	│   ├── 010-deployment.yaml
	│   ├── 020-volumes.yaml
	│   ├── 030-service.yaml
	│   └── kustomization.yaml
	└── prod
	│   ├── 000-custom-env.yaml
	│   ├── 090-ingress.yaml
	    └── kustomization.yaml

You can build the overlay with:

	$ kubectl apply --kustomize  ./kubernetes/prod

For example you can add an ingress configuration file:


	---
	###################################################
	# Ingress
	###################################################
	kind: Ingress
	apiVersion: networking.k8s.io/v1beta1
	metadata:
	  name: documents-imixs-tls
	spec:
	  rules:
	  - host: documents.foo.com
	    http:
	      paths:
	      - path: /
	        backend:
	          serviceName: imixs-documents
	          servicePort: 8080


You can also overwrite environment variables in the custom-env.yaml


	apiVersion: apps/v1
	kind: Deployment
	metadata:
	  name: app
	  labels: 
	    app: imixs-documents
	spec:
	  template:
	    spec:
	      containers:
	      - env:
	        - name: POSTGRES_PASSWORD
	          value: my-password
          
          

          