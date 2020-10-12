*Imixs-Documents* provides a **Open Source Document Management Suite** for small, medium and large enterprises.
The Project is based on the Workflow Suite [Imixs-Office-Workflow](https://github.com/imixs/imixs-office-workflow/)
and is licensed under the GPL.  

The goal of the project is to provide a powerful and easy-to-use *Business Process* and *Document Management* suite for companies and organizations.
With the help of '[Imixs-BPMN](https://www.imixs.org/sub_modeler.html)', business processes can be designed within the BPMN 2.0 standard and easily adapted to the individually needs of an enterprise.

## Quick-Installation Guide

Imixs-Office-Workflow provides a Docker Container to run the service on any Docker host. With the [Quick-Installation guide](./install) you can setup an instance of Imixs-Office-Workflow in minutes.

## Workflow Models

*Imixs-Documents* provides a selection of standard workflow models that can be used for a quick start.
The workflow modls are provided in different laguages. Switch into your prefered language for futher details.

 - [German Workflow Models](https://github.com/imixs/imixs-documents/tree/master/workflow/de)
 - [English international workflow models](https://github.com/imixs/imixs-documents/tree/master/workflow/en)

All standard models have included a multi-level approval workflow. The approval is determined by team management at the process and space levels. 
To add a management approval just add a manager into the corresponding process manager section.


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



The following sections list the business items predefined by Imixs-Documents.
For application specific item names the ‘dot.Case’ format is recommended. It’s basically a convention that makes it easier to see what properties are related.


 
| Item            | Type   	| Description													|
|-----------------|---------|---------------------------------------------------------------|
|**Order** 	      |      	|                                                               |
|order.name       | text 	| Order name													|
|order.number     | text	| Order number													|
|order.delivery   | date	| Delivery date													|
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
|dbtr.name        | text  	| debitor name													|
|dbtr.iban        | text  	| IBAN number													|
|dbtr.bic         | text  	| BIC number													|
|**Invoice**      |     	|                                                               |
|invoice.number   | text   	| Invoice number												|
|invoice.date     | date  	| Invoice Date													|
|invoice.total    | float  	| Invoice total amount											|
|invoice.vat      | float  	| Invoice vat 													|
|invoice.gross    | float  	| Invoice gross amount 											|
|**Payment**      |        	|                                                               |
|payment.type 	  | text   	| credit card, SEPA												|
|payment.date 	  | date   	| payment date													|
|payment.total 	  | float   | payment amount												|
|payment.cycle 	  | text  	| payment cycle (monthly, yearly, fixed date					|



