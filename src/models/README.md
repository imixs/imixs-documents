


# invoice-de-003

	28.07.2020
	  - 2 x 300


Exclude amazon inovices because of bad quality

Query:

	($workflowgroup:"Rechnungseingang") AND ($taskid:5900) AND NOT amazon
	
Config:

	<item name="workflow.entities">
		<value xsi:type="xs:string">_cdtr_iban|cdtr.iban</value>
		<value xsi:type="xs:string">_cdtr_bic|cdtr.bic</value>
		<value xsi:type="xs:string">_cdtr_name|cdtr.name</value>
		<value xsi:type="xs:string">_amount_brutto|invoice.total</value>
		<value xsi:type="xs:string">_invoicenumber|invoice.number</value>
		<value xsi:type="xs:string">_invoicedate|invoice.date</value>
	</item>
	
	<item name="ml.training.quality"><value xsi:type="xs:string">PARTIAL</value></item>
	
	

# invoice-de-002

Query:

	($workflowgroup:"Rechnungseingang") AND ($taskid:5900)
	
Config:

	<item name="workflow.entities">
		<value xsi:type="xs:string">_cdtr_iban|cdtr.iban</value>
		<value xsi:type="xs:string">_cdtr_bic|cdtr.bic</value>
		<value xsi:type="xs:string">_cdtr_name|cdtr.name</value>
		<value xsi:type="xs:string">_amount_brutto|invoice.total</value>
		<value xsi:type="xs:string">_invoicenumber|invoice.number</value>
		<value xsi:type="xs:string">_invoicedate|invoice.date</value>
	</item>
	
	<item name="ml.training.quality"><value xsi:type="xs:string">FULL</value></item>