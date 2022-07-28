FROM 10.138.44.34:2702/tomcat:10.0.8-jdk11-adoptopenjdk-hotspot

COPY target/dependency/* /usr/local/tomcat/lib/
COPY target/ROOT.war /usr/local/tomcat/webapps/

COPY context/log4j2.properties /usr/local/tomcat/lib/
COPY context/ROOT.xml /usr/local/tomcat/conf/Catalina/localhost/
