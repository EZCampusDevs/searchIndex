pipeline { 
    agent any  
    
    tools {
        maven 'maven-instance'
        jdk 'OpenJDK17'
    }
    
    try {
        
    
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
    
    catch(e) {
        currentBuild.result = "FAILED"
        throw e
        
    }
    finally{
        echo "finally block"
    }
}
