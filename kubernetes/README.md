# Kubernetes Deployment

*Imixs-Documents* already provides a base deployment configuration for Kubernetes. The setup is based on [Kustomize](https://kubernetes.io/docs/tasks/manage-kubernetes-objects/kustomization/) providing a declarative object management.


## Deploy 

To create the deployment objects form the base-deployment run:

	$ kubectl create --kustomize https://github.com/imixs/imixs-documents/kubernetes/

The service endpoint of Imixs-Documents will be published on port 8080.
This basic deployment configuration assumes that  a default storage class is defined within your kubernetes cluster. This storage class will be used for the database storage and the search index. You can customize the service and persistence volume configuration to your needs by using a custom setup.  
	

## Custom Setups

To create a custom deployment first copy this directory into your environment. To deployment the objects locally run:

	$ kubectl apply --kustomize  ./kubernetes

Now you can create an overlay with custom settings based on the base deployment. First create a new folder like /prod and put again a kustomization.yaml file in here.

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

For example you can add an ingress configuration file to publish the sevice endpoint of Imixs-Documents to a public or private Internet address. :


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
          
          
You can find further details about Kustomize [here](https://github.com/imixs/imixs-cloud/blob/master/doc/KUSTOMIZE.md). 
          