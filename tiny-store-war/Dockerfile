FROM tomcat:10.0.8-jdk11-adoptopenjdk-hotspot

COPY target/dependency/* /usr/local/tomcat/lib/
COPY context/log4j2.properties /usr/local/tomcat/lib/

COPY context/ROOT.xml /usr/local/tomcat/conf/Catalina/localhost/

COPY target/ROOT.war /usr/local/tomcat/webapps/
