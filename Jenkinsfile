pipeline { 
    agent any  
    
    tools {
        maven 'maven-instance'
        jdk 'OpenJDK17'
    }
    
    stages { 
        stage('Build War File') { 
            steps { 
                
                dir("searchIndex") {
                    sh "mvn --version"
                    sh "mvn clean package"
                }

                echo 'War file built' 
            }
        }
        
        stage('Build Glassfish Docker')
        {
            sh 'docker run --name glassfish-instance -d ubuntu:latest tail -f /dev/null'

        }

        stage('Run container') 
        {
            script
            {
                def containerId = sh(
                    returnStdout: true, 
                    script: 'docker ps -aqf "name=glassfish-instance"'
                    ).trim()

                sh "docker exec -it ${containerId} apt-get update"
                sh "docker exec -it ${containerId} apt-get install -y wget"
                sh "docker exec -it ${containerId} wget https://download.eclipse.org/ee4j/glassfish/glassfish-7.0.4.zip"
                sh "docker exec -it ${containerId} unzip glassfish-7.0.4.zip"
            }
        }
        // stage('Deploy WAR') 
        // {
        //     steps 
        //     {
        //         script 
        //         {
        //             def containerId = sh(
        //                 returnStdout: true, 
        //                 script: 'docker ps -aqf "name=mycontainer"'
        //                 ).trim()

        //             sh "docker exec -it ${containerId} "
        //             sh "docker cp target/myapp.war ${containerId}:/glassfish-5.1.0/glassfish/domains/domain1/autodeploy/"
        //         }
        //     }
        // }
  }
    }
}
