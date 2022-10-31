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
      
      
# Provide Log Files      

To provide all log files for further analysis run the follwoing command from outside of your docker container:

	$ cd git/imixs-documents/docker
	$ docker exec -it onlyoffice-app tar -zcvf documentserver_logs.tar.gz /var/log/onlyoffice/
	$ docker cp onlyoffice-app:/documentserver_logs.tar.gz ./