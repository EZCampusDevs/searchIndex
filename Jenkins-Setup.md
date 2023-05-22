

### Jenkins Docs and Setup


#### Installing JDKs

- https://www.youtube.com/watch?v=BePJ1bBWk3E

- Go to `Manage Jenkins > Plugin Manager > Available Plugins`

- Install [Eclipse Temurin Installer Plugin](https://plugins.jenkins.io/adoptopenjdk)

- Install [openJDK-native-plugin](https://plugins.jenkins.io/openJDK-native-plugin)

- Go to `Manage Jenkins > Global Tool Configuration`

- Click `JDK Installation`, Set the name, check `Install Automatically`, choose `Adoptium.net`

- Select your version

- See using jenkins tools at the bottom

#### Installing Maven

- Go to `Manage Jenkins > Global Tool Configuration`

- Click `Maven Installation`, `Add Maven`, set the name, check `Install Automatically`

- Select your version

- See using jenkins tools at the bottom



### Using Tools in Jenkinsfile
```
tools {
       jdk 'your-jdk-name-here'
       maven 'your-maven-name-here'
}
```
