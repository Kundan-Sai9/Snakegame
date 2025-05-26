# Use Tomcat 9 with Java 17
FROM tomcat:9.0-jdk17

# Optional: remove the default web app
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy your WAR with its original name to preserve /SnakeGame context
COPY SnakeGame.war /usr/local/tomcat/webapps/SnakeGame.war

# Optional: custom server config
COPY server.xml /usr/local/tomcat/conf/server.xml

EXPOSE 8080
