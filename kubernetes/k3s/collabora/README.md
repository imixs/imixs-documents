# LibreOffice Online / Collabora

To deploy an instance of collabora we need to provide a configmap with the loolwsd.xml file including the custom confiugration


## First Setup

Assuming the imixs namespace 'imixs-system' already exists

	$ kubectl create configmap lool-config --from-file=./apps/libreoffice/loolwsd.xml -n imixs


## Deployment and apply Changes

	$ kubectl apply -f apps/libreoffice/

## Recreate Config Map


	$ kubectl delete configmap lool-config
	$ kubectl create configmap lool-config --from-file=./apps/libreoffice/loolwsd.xml -n imixs





