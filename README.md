# Imixs-Documents

*Imixs-Documents* provides an **Open Source Process and Document Management Suite** for small, medium and large enterprises.
The Project is based on the Workflow Suite [Imixs-Office-Workflow](https://github.com/imixs/imixs-office-workflow/)
and is licensed under the GPL.  

The goal of the project is to provide a powerful and easy-to-use *Business Process* and *Document Management* suite for companies and organizations.
With the help of '[Imixs-BPMN](https://www.imixs.org/sub_modeler.html)', business processes can be designed within the BPMN 2.0 standard and easily adapted to the individually needs of an enterprise.

<h3 align="center"><a href="https://imixs.github.io/imixs-documents/">Documentation &amp; Quick-Installation Guide</a></h3>


## Workflow Models

*Imixs-Documents* provides a selection of standard workflow models that can be used for a quick start.
The workflow models are provided in different languages. Switch into your preferred language for further details.

 - [German Workflow Models](https://github.com/imixs/imixs-documents/tree/master/workflow/de)
 - [English international workflow models](https://github.com/imixs/imixs-documents/tree/master/workflow/en)

All standard models have included a multi-level approval workflow. The approval is determined by team management at the process and space levels. 
To add a management approval just add a manager into the corresponding process manager section.

See the [documentation](https://imixs.github.io/imixs-documents/) to learn how to interact with it!

## Imixs-ML

Imixs-Documents supports the integration of the [Imixs-ML framework](https://github.com/imixs/imixs-ml). To activate the Imixs-ML functionality a Imixs-ML-Spacy service and an optional Imixs-ML-Training Service need to be started. 
The docker-compose yaml file docker/docker-compose-ml.yml shows an example setup of the service integration.

Optional custom ML models can be integrated by adding a docker volume. E.g:

	./src/models/:/usr/src/app/models/
	
	
## Optical Character Recognition (OCR)

Imixs-Documents includes the OCR functionality provided by the subproject [Imixs-Archive-OCR](https://github.com/imixs/imixs-archive/tree/master/imixs-archive-ocr).
The OCR feature is based on [Apache Tika](https://tika.apache.org/) and Tesseract. To activate the OCR functionality a Tika service need to be started. 

	
## Custom Forms

Forms can be customized in Imixs-Documents within the BPMN model. The following template shows an example:

	<?xml version="1.0"?>
	<imixs-form>
	
	  <imixs-form-section columns="2" label="Vertragsdaten">
	    <item name="contract.name" type="text"  label="Partner:" />
	    <item name="contract.number" type="text"  label="Nummer:" />
	    <item name="contract.start" type="date"  label="Beginn:" />
	    <item name="contract.end" type="date"  label="Ende:" />
	    <item name="contract.fee" type="float"  label="Gebühr:" />
	  </imixs-form-section>
	
  	  <imixs-form-section columns="2" label="Zahlungsdaten">
	    <item name="payment.cycle" type="text"  label="Abrechnungsperiode:" />
	    <item name="payment.type" type="text"  label="Zahlungsart:" />
	    <item name="cdtr.iban" type="text"  label="IBAN:" />
	    <item name="cdtr.bic" type="text"  label="BIC:" />
	    <item name="cdtr.name" type="text"  label="Kreditor:" />
	  </imixs-form-section>
	 
	</imixs-form>


Find details how to model custom forms [here](https://github.com/imixs/imixs-office-workflow/blob/master/doc/modeling/CUSTOM_FORMS.md).

### Predefined Items

The following sections list the business items predefined by Imixs-Documents.
For application specific item names the ‘dot.Case’ format is recommended. It’s basically a convention that makes it easier to see what properties are related.


 
| Item            | Type   	| Description													|
|-----------------|---------|---------------------------------------------------------------|
|**Request**      |      	|                                                               |
|request.subject  | text 	| Subject														|
|request.name     | text 	| Requester name												|
|request.email    | text	| Requester E-Mail												|
|request.number   | text	| Order number													|
|**Order** 	      |      	|                                                               |
|order.name       | text 	| Order name													|
|order.number     | text	| Order number													|
|order.delivery   | date	| Delivery date													|
|order.total   	  | float	| Order total amount											|
|**Invoice**      |     	|                                                               |
|invoice.number   | text   	| Invoice number												|
|invoice.date     | date  	| Invoice Date													|
|invoice.total    | float  	| Invoice total amount											|
|invoice.vat      | float  	| Invoice vat 													|
|invoice.net      | float  	| Invoice net amount 											|
|invoice.currency | text  	| currency code													|
|**Contract** 	  |      	|                                                               |
|contract.name    | text 	| Contract name													|
|contract.partner | text 	| Contract partner name											|
|contract.number  | text	| Contract number												|
|contract.start   | date	| Contract start date											|
|contract.end     | date 	| Contract end date												|
|contract.fee     | float 	| Contract fee per billing cycle								|
|**Creditor**     |        	|                                                               |
|cdtr.name        | text  	| Creditor name													|
|cdtr.iban        | text  	| IBAN number													|
|cdtr.bic         | text  	| BIC number													|
|**Debitor**  	  |        	|                                                               |
|dbtr.name        | text  	| Debitor name													|
|dbtr.iban        | text  	| IBAN number													|
|dbtr.bic         | text  	| BIC number													|
|**Payment**      |        	|                                                               |
|payment.type 	  | text   	| credit card, SEPA												|
|payment.date 	  | date   	| payment date													|
|payment.total 	  | float   | payment amount												|
|payment.cycle 	  | text  	| payment cycle (monthly, yearly, fixed date					|











## Contribute
The source code of Imixs-Documents is free available on [Github](https://github.com/imixs/imixs-documents). 
If you have any questions about how Imixs-Documents works and how you can use it in your own project, you can ask your question on the [GitHub Issue Tracker](https://github.com/imixs/imixs-documents/issues). 
With a pull request on GitHub you can share your ideas or improvements that you want to contribute.

 

## Need Help?

[Imixs Software Solutions GmbH](http://www.imixs.com) is an open source company and we are specialized in business process management solutions (BPMS). If you need professional services or consulting for your own individual software project [please contact us](mailto:info@imixs.com). 

 


<br /><br /><img src="small_h-trans.png" />


**Imixs-Office-Workflow** provides a Docker Image to run the service on any Docker host. 
The docker image is based on the docker image [imixs/wildfly](https://hub.docker.com/r/imixs/wildfly/) which can be used for development as also for production.




## Maven Build
*Imixs-Documents* is based on [Maven](http://maven.apache.org/) and runs on the Jakarta EE stack. Imixs-Office-Workflow can be deployed on JBoss/Wildfly server or other Java EE application servers.
To build the application from sources, run the maven install command first:

	$ mvn clean install

Please check the pom.xml file for dependencies and versions. The master-branch of the project is continuously under development and is typically 
against the latest snapshot releases form the Imixs-Workflow project. To run a stable version please build a [tagged version](https://github.com/imixs/imixs-office-workflow/releases). 
To deploy the artifact the application server must provide a database pool named "java:/jdbc/office" and a security domain/realm named 'office'. See also the [Imixs-Workflow Deployment Guide](http://www.imixs.org/doc/deployment/index.html) for further details.




## Docker for Development
Developers can use a docker image for testing and the development of new features. To build a new container first build the maven artefact running: 

	$ mvn clean install -Pdocker

To start Imixs-Office-Workflow with docker, the docker-compose command line tool an be used:

	$ docker-compose -f docker/docker-compose.yml up

Note: this command will start several containers, 

* a Postgre SQL database server 
* a Wildfly Server running Imixs-Documents

Optional you can run the docker-compose configuration docker/docker-compose-dev.yml which will also start a the [Imixs-Admin tool](https://www.imixs.org/doc/administration.html) 

	$ docker-compose -f docker/docker-compose-dev.yml up

### Mount Points
The development configuration sets a local mount point at the following location:

	~/git/imixs-office-workflow/src/docker/deployments

Make sure that this directory exits. During development new versions can easily deployed into this directory which is the auto-deployment folder of Wildfly. For further details see the [imixs/wildfly docker image](https://hub.docker.com/r/imixs/wildfly/).




## Docker for Production

To run Imixs-Documents in a Docker production environment the project provides several additional maven profiles:


### docker-build

With the profile '_docker-build_' a docker container based on the current version of Imixs-Office-Workflow is created locally
 
	$ mvn clean install -Pdocker


### docker-push

With the '_docker-push_' profile the current version of Imixs-Office-Workflow can be pushed to a remote repository:

	$ mvn clean install -Pdocker-push -Dorg.imixs.docker.registry=localhost:5000

where 'localhost:5000' need to be replaced with the host of a private registry. See the [docker-push command](https://docs.docker.com/docker-cloud/builds/push-images/) for more details.

### docker-hub

Imixs-Office-Workflow is also available on [Docker-Hub](https://hub.docker.com/r/imixs/imixs-office-workflow/). The public docker images can be used for development and production. If you need technical support please contact [imixs.com](http://www.imixs.com) 



## Kubernetes


*Imixs-Documents* provides a base deployment configuration for Kubernetes. The setup is based on [Kustomize](https://kubernetes.io/docs/tasks/manage-kubernetes-objects/kustomization/) providing a declarative object management.


To create the deployment objects from the base-deployment run:

	$ kubectl apply --kustomize https://github.com/imixs/imixs-documents/kubernetes/

The service endpoint of Imixs-Documents will be published on port 8080.
This basic deployment configuration assumes that  a default storage class is defined within your kubernetes cluster. This storage class will be used for the database storage and the search index. You can customize the service and persistence volume configuration to your needs by using a [custom setup](./kubernetes/README.md).  
	


