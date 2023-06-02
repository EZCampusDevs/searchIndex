pipeline { 
    agent any  
    
     tools {
            maven 'maven3'
            jdk 'OpenJDK17'
     }

     stages { 
         stage('Build War File') { 
             steps { 
                 dir("helloWorld") {
                     sh "mvn --version"
                     sh "mvn clean package"
                 }
 
                 echo 'War file built' 
             }
         }
        
         stage('Remote Deploy War File')
         {
             steps {
                 dir("helloWorld/target")
                 {
                     sshPublisher(publishers: [sshPublisherDesc(configName: '2GB_Glassfish_VPS', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: '', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: './warbuilds', remoteDirectorySDF: false, removePrefix: '', sourceFiles: 'helloWorld.war')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
                 }    
             }
         }
    }
}
