pipeline { 
    agent any  

     tools {
            maven 'maven3'
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
        
stage('Remote Deploy War File') {
    steps {

        withCredentials([usernamePassword(credentialsId: 'MYSQL_USER_PASS_1', passwordVariable: 'MS1_PASSWORD', usernameVariable: 'MS1_USERNAME')]) {

            // Write the bash script to a file
            writeFile file: 'exportCredentials.sh', text: '''
            #!/bin/bash
            export MS1_USERNAME=${MS1_USERNAME}
            export MS1_PASSWORD=${MS1_PASSWORD}
            '''

            dir("searchIndex/target") {
                sshPublisher(publishers: [
                    sshPublisherDesc(configName: '2GB_Glassfish_VPS', transfers: [
                        sshTransfer(cleanRemote: false, excludes: '', execCommand: '''
                                    pwd
                                    chmod +x exportCredentials.sh
                                    ./exportCredentials.sh
                                    rm -rf exportCredentials.sh
                                    ''', execTimeout: 120000, flatten: false, makeEmptyDirs: false, 
                        noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: './warbuilds', remoteDirectorySDF: false, 
                        removePrefix: '', sourceFiles: 'searchIndex.war')
                    ], 
                    usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)
                ])
                
                // Run the bash script
            }
            
            echo "war file copied"
        }
    }
}

    }
    
        
    post {
        always {
            discordSend(
                        description: currentBuild.result, 
                        enableArtifactsList: false, 
                        footer: '', 
                        image: '', 
                        link: '', 
                        result: currentBuild.result, 
                        scmWebUrl: '', 
                        thumbnail: '', 
                        title: env.JOB_BASE_NAME, 
                        webhookURL: "${DISCORD_WEBHOOK_1}"
                    )
        }
    }
}
