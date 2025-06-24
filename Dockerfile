# ----------- Stage 1: Build WAR using Maven -----------  
FROM maven:3.9.3-eclipse-temurin-17 AS builder

# Set working directory inside the image
WORKDIR /app

# Copy only pom.xml and download dependencies first (cache optimization)
COPY pom.xml .
RUN mvn dependency:go-offline

# Now copy the rest of the project
COPY src ./src

# Build the WAR without running tests
RUN mvn clean package -DskipTests


# ----------- Stage 2: Run WAR on Tomcat 10 (Jakarta EE 10) -----------  
FROM tomcat:10.1-jdk17-corretto

# Set UTF-8 encoding to prevent character issues
ENV CATALINA_OPTS="-Dfile.encoding=UTF-8"

# Clean default Tomcat webapps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR from Maven stage to Tomcat and rename it to ROOT.war
COPY --from=builder /app/target/ATM_Application_New-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose Tomcat port
EXPOSE 8080

# Run Tomcat server
CMD ["catalina.sh", "run"]
