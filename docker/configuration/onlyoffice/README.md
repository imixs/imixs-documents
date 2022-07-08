We changed

			"autoAssembly": {
				"enable": true,
				"interval": "0m",
				"step": "0m"
			},
			
			
See background information here: https://api.onlyoffice.com/editors/faq/saving

We activted the configuraiton change via the docker-compose configuration


    volumes:
      - ./configuration/onlyoffice/default.json:/etc/onlyoffice/documentserver/default.json