# Jetty 12.1 = Jakarta Servlet 6.1 (ee11), running on Java 25
FROM jetty:12.1-jdk25

# Make sure Jetty's "ee11" web-app deployer is enabled (needed to deploy a .war)
RUN java -jar "$JETTY_HOME/start.jar" --add-modules=ee11-deploy

# Manual deploy: put the WAR in Jetty's webapps folder.
# Served at http://localhost:8081/wallet (port 8081 is mapped in docker-compose.yml)
COPY wallet.war /var/lib/jetty/webapps/wallet.war
