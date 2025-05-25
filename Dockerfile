# Use Tomcat 9 with Java 17
FROM tomcat:9.0-jdk17

# Remove the default Tomcat web app
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy your WAR file as ROOT.war (deployed at /)
COPY ROOT.war /usr/local/tomcat/webapps/ROOT.war

# Copy the modified server.xml to disable shutdown port
COPY conf/server.xml /usr/local/tomcat/conf/server.xml

# Expose port 8080 for your app
EXPOSE 8080
