

## SchedulePlatform-SearchIndex

Handles indexing and searching of the database in it's own api.


### Postgres Setup (Docker)

#### Creating Docker image
- Get postgres: `docker pull postgres`

- Create postgres container: `docker run --name postgres-instance -e POSTGRES_PASSWORD=<password> --publish 5432:5432 -d postgres`

#### Starting / Stopping Postgres

- Start: `docker start postgres-instance`

- Stop: `docker stop postgres-instance`




### Glassfish Setup and Usage

- Download [Glassfish](https://projects.eclipse.org/projects/ee4j.glassfish/downloads)

- Extract and **cd** into folder

- Run `./glassfish/bin/asadmin start-domain domain1` to start the server 

- Once running you can visit `http://localhost:4848/` for admin console

#### Build and Deplay war file

##### Eclipse

- Right click the project `> Export > War File`

- Choose output and hit finish

- **cd** into the glassfish folder and run `./glassfish/bin/asadmin deploy <path-to-war-file>`

- Once deployed visit `http://localhost:8080/searchIndex/api`


### Setting up IntelliJ
This will be used for running the glassfish server/api

Open the root in IntelliJ

Allow it to create indexes/paths if prompted

Create run config:
`Run (at the top) > Edit Configurations > + (in top left) > Glassfish Server > Local`

Run config settings:
Application Server:

Select a fresh version of glassfish 7.0.2 to prevent possible conflicts

URL:
```
http://localhost:8080/searchIndex-1.0-SNAPSHOT/api/
```

JRE: Using OpenJDK-17 Temurin (17 or higher should be find)

Server Domain: `domain1`

Username: `admin`

Password: (there is no password)

Under `Deployment` at the top
` + > Artifact > War artifact`

Apply changes and close