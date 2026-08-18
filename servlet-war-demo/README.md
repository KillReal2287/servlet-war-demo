# Servlet WAR Demo

Minimal servlet application for deploying to an external Apache Tomcat.

Build:

```bash
mvn package
```

WAR file:

```text
target/servlet-war-demo.war
```

Deploy it to Tomcat by copying the WAR into Tomcat's `webapps` directory.

URLs after deployment:

```text
http://localhost:8080/servlet-war-demo/
http://localhost:8080/servlet-war-demo/hello
http://localhost:8080/servlet-war-demo/hello?name=Kirill
```

This project uses `jakarta.servlet-api`, so it is intended for Tomcat 10+.

PostgreSQL in Docker:

```bash
docker compose up -d
```

Connection settings:

```text
URL: jdbc:postgresql://localhost:5432/servlet_demo
Database: servlet_demo
User: servlet_user
Password: servlet_password
```
