
pipeline {
    agent any

    stages {
        stage ('Checkout') {
            steps {
                script {
                    checkout([$class             : 'GitSCM'
                              , branches         : [[name: "main"]]
                              , userRemoteConfigs: [[credentialsId: 'hoseong-gh', url: 'https://github.com/CCC-org/bridge-backend.git']]])
                }
            }
        }


        stage('Build') {
            steps {
                script {
                    sh '''
                        ./gradlew :${PJ_NAME}:clean :${PJ_NAME}:bootJar
                    '''
                }

            }
        }

//        stage('Docker Push') {
//            steps {
//                script {
//                    sh '''
//                        cd ${PJ_NAME}/
//                        docker build -t youhoseong/${PJ_NAME} .
//                        docker push youhoseong/${PJ_NAME}
//                    '''
//                }
//            }
//        }

        stage('SSH transfer') {
            steps {
                script {
                    sshPublisher (
                            continueOnError: false, failOnError: true,
                            publishers: [
                                    sshPublisherDesc(
                                            configName: "deploy-server",
                                            verbose: true,
                                            transfers: [
                                                    sshTransfer(
                                                            sourceFiles: "scripts/deploy.sh",
                                                            removePrefix: "scripts",
                                                            remoteDirectory: "",
                                                            execCommand: "echo deploy script success.."
                                                    ),
                                                    sshTransfer(
                                                            sourceFiles: "${PJ_NAME}/build/libs/*.jar",
                                                            removePrefix: "${PJ_NAME}/build/libs",
                                                            remoteDirectory: "${DEPLOY_PATH_DEV}",
                                                            execCommand: "sh deploy.sh ${PJ_NAME} ${DEPLOY_PATH_DEV} ${RUN_DEV}"
                                                    )


                                            ])
                            ])
                }
            }
        }

    }

    post {
        success {
            echo 'Success !!'
        }
        failure {
            echo 'Fail ..'
        }
    }
    environment {
        DEPLOY_PATH_DEV = "/data1/${PJ_NAME}"
        RUN_DEV = "dev"
        RUN_PROD = "prod"
    }
}

