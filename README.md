

## SchedulePlatform-SearchIndex

Handles indexing and searching of the database in it's own api.




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