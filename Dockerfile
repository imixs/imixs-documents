FROM imixs/imixs-office-workflow

# Deploy artefact
RUN rm -r /opt/jboss/wildfly/standalone/deployments/*
COPY ./target/*.war /opt/jboss/wildfly/standalone/deployments/
