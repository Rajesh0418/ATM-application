# ----------- Stage 1: Build WAR using Maven -----------
FROM maven:3.9.3-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Package WAR file without running tests
RUN mvn clean package -DskipTests


# ----------- Stage 2: Deploy WAR on Tomcat 10 (Jakarta EE 10) -----------
FROM tomcat:10.1-jdk17-corretto

# Set file encoding to avoid any charset issues
ENV CATALINA_OPTS="-Dfile.encoding=UTF-8"

# Clear default webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy generated WAR file from build stage to Tomcat webapps folder
COPY --from=builder /app/target/ATM_Application_New-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose default Tomcat port
EXPOSE 8080

# Run Tomcat
CMD ["catalina.sh", "run"]
