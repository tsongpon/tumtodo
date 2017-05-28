# tumtodo
tum's todo APIs
**Requirements**

 - java 8 or above
 - postgresql
 - maven 3

**Database setup**
run following script to create database
create db' user:

    CREATE USER tumtodo 
	WITH LOGIN NOSUPERUSER NOCREATEDB NOCREATEROLE
	INHERIT NOREPLICATION 
	CONNECTION LIMIT -1 PASSWORD 'pingu123';

create database:

    CREATE DATABASE tumtodo
	WITH 
	OWNER = tumtodo
	ENCODING = 'UTF8'
	CONNECTION LIMIT = -1;

**Run unit test**

    mvn test

**Start service with development environment**

    mvn spring-boot:run

**Play with service**
service will be available on port `8080`
APIs documentation :

> http://localhost:8080/swagger-ui.html

 
