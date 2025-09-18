FROM imixs/imixs-office-workflow:5.1.3-wildfly-29.0.1.Final-jdk17

# Deploy artefact
RUN rm -r /opt/jboss/wildfly/standalone/deployments/*
COPY ./target/*.war /opt/jboss/wildfly/standalone/deployments/
