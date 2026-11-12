# WildFly 42 (Jakarta EE 11 = Servlet 6.1), running on Java 25. Version is pinned on purpose.
FROM quay.io/wildfly/wildfly:42.0.0.Final-jdk25

# Manual deploy: put the WAR in WildFly's deployments folder.
# WildFly detects it and deploys it at http://localhost:8082/games (port 8082 is mapped in docker-compose.yml)
COPY games.war /opt/jboss/wildfly/standalone/deployments/games.war
