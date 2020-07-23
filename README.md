# Imixs-Documents

'Imixs-Documents' provides a **Open Source Document Management Suite** for small, medium and large enterprises.
The Project is based on the Workflow Suite [Imixs-Office-Worklfow](https://github.com/imixs/imixs-office-workflow/)
and is licensed under the GPL.  

The goal of the project is to provide a powerful and easy-to-use document management suite for companies and organizations.
With the help of '[Imixs-BPMN](https://www.imixs.org/sub_modeler.html)', business processes can be designed within the BPMN 2.0 standard and easily adapted to the individually needs of an enterprise.

## Forms

Forms can be customized in Imixs-Documents within the BPMN model. The following sections list the business items predefined by Imixs-Documents:



 
| Item            | Type   | Description												 	|
|-----------------|--------|------------|-----------------------------------------------------------------------|
|**Billing** 
|billing.total 	  |float   | billing amount	|
|billing.cycle 	  |text    | billing cycle	|
|**Contract** 
|contract.name    |text   |  Contract name	|
|contract.partner |text   |  Contract partner name	|
|contract.start   |date   |  contract start date	|
|contract.end     |date   |  contract end date	|
|contract.fee     |float  |  contract fee per billing cycle	|
|**Creditor** 
|cdtr.name        |text   |  Creditor name	|
|cdtr.iban        |text   |  IBAN number	|
|cdtr.bic         |text   |  BIC number	|
|**Invoice** 
|invoice.number   |text   |  Invoice number	|
|invoice.date     |date   |  Invoice Date|
|invoice.total    |float  |  Invoice total amount|
|invoice.vat      |float  |  Invoice vat |
|invoice.gross    |float  |  Invoice gross amount |





## Contribute
The source code of Imixs-Documents is free available on [Github](https://github.com/imixs/imixs-documents). 
If you have any questions about how Imixs-Documents works and how you can use it in your own project, you can ask your question on the [GitHub Issue Tracker](https://github.com/imixs/imixs-documents/issues). 
With a pull request on GitHub you can share your ideas or improvements that you want to contribute.

 

## Need Help?

[Imixs Software Solutions GmbH](http://www.imixs.com) is an open source company and we are specialized in business process management solutions (BPMS). If you need professional services or consulting for your own individual software project [please contact us](mailto:info@imixs.com). 

 


<br /><br /><img src="small_h-trans.png" />


**Imixs-Office-Workflow** provides a Docker Image to run the service on any Docker host. 
The docker image is based on the docker image [imixs/wildfly](https://hub.docker.com/r/imixs/wildfly/) which can be used for development as also for production.



## Docker for Development
Developers can use a docker image for testing and the development of new features. To build a new container first build the maven artefact running: 

	$ mvn clean install -Pdocker

To start Imixs-Office-Workflow with docker, the docker-compose file 'docker-compose-dev.yml can be used:

	$ docker-compose -f docker-compose-dev.yml up

Note: this command will start several containers, 

* a Postgre SQL database server 
* a Wildfly Server running Imixs-Office-Workflow
* and a Wildfly Server funning the [Imixs-Admin tool](https://www.imixs.org/doc/administration.html) 


### Mount Points
The development configuration sets a local mount point at the following location:

	~/git/imixs-office-workflow/src/docker/deployments

Make sure that this directory exits. During development new versions can easily deployed into this directory which is the auto-deployment folder of Wildfly. For further details see the [imixs/wildfly docker image](https://hub.docker.com/r/imixs/wildfly/).




## Docker for Production

To run Imixs-Office-Workflow in a Docker production environment the project provides several additional maven profiles:


### docker-build

With the profile '_docker-build_' a docker container based on the current version of Imixs-Office-Workflow is created locally
 
	$ mvn clean install -Pdocker


### docker-push

With the '_docker-push_' profile the current version of Imixs-Office-Workflow can be pushed to a remote repository:

	$ mvn clean install -Pdocker-push -Dorg.imixs.docker.registry=localhost:5000

where 'localhost:5000' need to be replaced with the host of a private registry. See the [docker-push command](https://docs.docker.com/docker-cloud/builds/push-images/) for more details.

### docker-hub

Imixs-Office-Workflow is also available on [Docker-Hub](https://hub.docker.com/r/imixs/imixs-office-workflow/). The public docker images can be used for development and production. If you need technical support please contact [imixs.com](http://www.imixs.com) 




## Maven Build
Imixs-Office-Workflow is based on [Maven](http://maven.apache.org/) and runs on the Jakarta EE stack. Imixs-Office-Workflow can be deployed on JBoss/Wildfly server or other Java EE application servers.
To build the application from sources, run the maven install command first:

	$ mvn clean install

Please check the pom.xml file for dependencies and versions. The master-branch of the project is continuously under development and is typically 
against the latest snapshot releases form the Imixs-Workflow project. To run a stable version please build a [tagged version](https://github.com/imixs/imixs-office-workflow/releases). 
To deploy the artifact the application server must provide a database pool named "java:/jdbc/office" and a security domain/realm named 'office'. See also the [Imixs-Workflow Deployment Guide](http://www.imixs.org/doc/deployment/index.html) for further details.

