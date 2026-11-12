# Tomcat 11 = Jakarta Servlet 6.1, running on Java 25
FROM tomcat:11.0-jdk25

# Manual deploy: put the WAR in Tomcat's webapps folder.
# Tomcat unpacks it and serves it at http://localhost:8080/ui
COPY ui.war /usr/local/tomcat/webapps/ui.war
