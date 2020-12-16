FROM imixs/imixs-office-workflow:4.4.2

# Deploy artefact
RUN rm -r /opt/jboss/wildfly/standalone/deployments/*
COPY ./target/*.war /opt/jboss/wildfly/standalone/deployments/
