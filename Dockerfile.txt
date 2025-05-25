# Use Tomcat 9 with Java 17
FROM tomcat:9.0-jdk17

# Remove the default Tomcat web app
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy your WAR file as ROOT.war (deployed at /)
COPY ROOT.war /usr/local/tomcat/webapps/ROOT.war

# Tell Docker what port your app will run on
EXPOSE 8080
