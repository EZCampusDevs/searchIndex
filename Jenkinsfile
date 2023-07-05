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
                    dir("searchIndex/target") {
                        sshPublisher(publishers: [
                            sshPublisherDesc(configName: '2GB_Glassfish_VPS', transfers: [
                                sshTransfer(cleanRemote: false, excludes: '', execCommand: '', execTimeout: 120000, flatten: false, makeEmptyDirs: false, 
                                noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: './warbuilds', remoteDirectorySDF: false, 
                                removePrefix: '', sourceFiles: 'searchIndex.war')
                            ], 
                            usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)
                        ])
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
