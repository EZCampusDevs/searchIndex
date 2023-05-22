pipeline { 
    agent any  
    
    tools {
        maven 'maven-instance'
        jdk 'OpenJDK17'
    }
    
    stages { 
        stage('Build') { 
            steps { 
                
                dir("searchIndex") {
                    sh "mvn --version"
                    sh "mvn clean package"
                }

                echo 'This is a minimal pipeline.' 
            }
        }
    }
}
