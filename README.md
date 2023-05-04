

## SchedulePlatform-SearchIndex

Handles indexing and searching of the database in it's own api.



### mySQL Setup (Docker)

*   #### Creating Docker image

       - Get mySQL: `docker pull mysql`

       - Create mySQL container:
       ```shell
       docker run \
            --name mysql-instance \
            -e MYSQL_DATABASE=<mysql_db> \
            -e MYSQL_USER=<username> \
            -e MYSQL_PASSWORD=<password> \
            -e MYSQL_ROOT_PASSWORD=<root_password> \
            --publish 3306:3306 \
            -d mysql
       ```



### Postgres Setup (Docker)

*   #### Creating Docker image

       - Get postgres: `docker pull postgres`

       - Create postgres container: 
       ```shell
       docker run \
            --name postgres-instance \
            -e POSTGRES_PASSWORD=<password> \
            --publish 5432:5432 \
            -d postgres
       ```

*   #### Starting / Stopping Postgres

       - Start: `docker start postgres-instance`

       - Stop: `docker stop postgres-instance`


    If you cannot access postgres on `localhost:5432`,
    
    Run `docker inspect postgres-instance | grep IPAddress` or `docker inspect postgres-instance` to find:
    ```json
    "IPAddress": "xxx.xx.x.x",
    ```
    To get the direct hostname


*   #### Looking in the database (PGAdmin)

       - Get PGAdmin `docker pull dpage/pgadmin4`

       - Create PGAdmin container:
       ```shell
        docker run \
             --name pgadmin-instance \
             -p 5050:80 \
             -e "PGADMIN_DEFAULT_EMAIL=name@gmail.com" \
             -e "PGADMIN_DEFAULT_PASSWORD=admin" \
             -d dpage/pgadmin4
       ```

       - Start: `docker start pgadmin-instance`

       - Stop: `docker stop pgadmin-instance`

       If you cannot access it via `localhost:80/login` or `localhost:5050/login`,

       **NOTE:** Sometimes the browser matters, so if either of the above don't work try swapping browsers.

       Run `docker inspect pgadmin-instance | grep IPAddress` or `docker inspect pgadmin-instance` to find:
       ```json
        "IPAddress": "xxx.xx.x.x",
       ```
       And try using this instead.


       Once you get to the login page, use the email and password from the `docker run` command from earlier

       Then add a new server, set any name, for host you can try `localhost` or the host from the `docker inspect` under the postgres section.

       Username should be `postgres` by default, password is whatever you set earlier.



### Glassfish Setup and Usage

- Download [Glassfish](https://projects.eclipse.org/projects/ee4j.glassfish/downloads) (Full Profile)

- Extract and **cd** into folder

- For **Linux** Run `./glassfish/bin/asadmin start-domain domain1` to start the server

- For **Windows** Run `./glassfish/bin/asadmin.bat start-domain domain1` to start the server

- Once running you can visit `http://localhost:4848/` for admin console

* #### Troubleshooting

    - *(Windows ONLY)* If you're running into an issue where starting the domain1 server times out. Check if anything is running on ports `8080` or `4848`
      
    - Do `netstat -ano | findstr :<PORT>` to see if anything is running on that port. If something shows up, take the **PID** number (last column) and use that to kill the process
       
    - Do `taskkill /PID <PID> /F` to kill process. (example: `netstat -ano | findstr :8080` -> Task running with PID: 1234 -> `taskkill /PID 1234 /F`)
       
    - Now try `start-domain domain1` again, should work.


### Build and Deplay .war file

*   #### Eclipse (EE)

    -   Right click the project `> Export > War File`

    #### NetBeans 

    - Right click the project in file explorer `> Clean and Build` then check the console for where the .war file was built.

    -   Choose output and hit finish

    -   **cd** into the glassfish folder and run `./glassfish/bin/asadmin deploy <path-to-war-file>`

    -   Once deployed visit `http://localhost:8080/<WAR FILE TITLE>/api` 

    - Example, my .WAR compiled as `searchIndex-1.0-SNAPSHOT.war` therefore I access the api via: `http://localhost:8080/searchIndex-1.0-SNAPSHOT/api`


### Update your .war file with Glassfish CLI 
##### (Check Shell Example Below)

- 1. Run `asadmin list-applications --type web` to view all Web Deployments of Glassfish currently running. 
- 2. Run `undeploy <Web Instance>` , Example: `undeploy searchIndex-1.0-SNAPSHOT` 
- 3. Run `deploy <path-to-war-file>` to deploy new version of API

The Example Below is from the Windows **asadmin.bat** CLI tool

```shell
asadmin> list-applications --type web
searchIndex-1.0-SNAPSHOT  <web>
Command list-applications executed successfully.

asadmin> undeploy searchIndex-1.0-SNAPSHOT
Command undeploy executed successfully.

asadmin> deploy C:/Users/jason/.m2/repository/org/ezcampus/searchIndex/1.0-SNAPSHOT/searchIndex-1.0-SNAPSHOT.war
Application deployed with name searchIndex-1.0-SNAPSHOT.
Command deploy executed successfully.
```

<hr/>

##### Setting up IntelliJ 

This will be used for running the glassfish server/api

- Open the root in IntelliJ

- Allow it to create indexes/paths if prompted

- Create run config:
`Run (at the top) > Edit Configurations > + (in top left) > Glassfish Server > Local`

Under Run config settings:

- Application Server: `Glassfish 7.x.x`

- URL: `http://localhost:8080/searchIndex-1.0-SNAPSHOT/api/`

- JRE: `OpenJDK-17 Temurin` or similar (17 or higher should be find)

- Server Domain: `domain1`

- Username: `admin`

- Password: (there is no password)

- Under `Deployment` at the top ` + > Artifact > War artifact`

Apply changes and close