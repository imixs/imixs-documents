FROM open-liberty:kernel


# Imixs-Documents
MAINTAINER ralph.soika@imixs.com

# Copy postgres JDBC driver
COPY ./src/docker/configuration/openliberty/postgresql-9.4.1212.jar /opt/ol/wlp/lib

# Add config
COPY --chown=1001:0 ./src/docker/configuration/openliberty/server.xml /config/

# Activate Debug Mode...
#COPY --chown=1001:0 ./src/docker/configuration/openliberty/jvm.options /config/

# Copy sample application
COPY ./target/imixs-documents.war /config/dropins/



