pipeline {
    agent {label '!master'}
    stages {
        stage('Checkout'){
            steps {
                cleanWs()
                dir('sources'){
                    git url: 'https://github.com/oscarvx00/sitas-backend', branch: 'master'
                }
            }
        }
        stage ('Test'){
            environment {
                scannerHome = tool 'SonarQubeScanner'
                RABBITMQ_USER = credentials("RABBITMQ_USER")
                RABBITMQ_PASS = credentials("RABBITMQ_PASS")
                DOWNLOAD_REQUEST_EXCHANGE = "sitas-test-exchange-downloadrequest"
                MONGODB_ENDPOINT = credentials("MONGODB_ENDPOINT")
                MONGODB_DATABASE = "sitas-test"
                MINIO_NODE_ENDPOINT = "http://oscarvx00.ddns.net:10000"
                MINIO_NODE_USER = credentials("MINIO_INTERNAL_USER")
                MINIO_NODE_PASS = credentials("MINIO_INTERNAL_PASS")
                MINIO_NODE_BUCKET = "node-storage-test"
            }
            steps {
                dir('sources'){
                    script {
                        sh './gradlew jacocoTestReport'
                    }

                    withSonarQubeEnv('sonarqube') {
                        sh './gradlew sonarqube --stacktrace'
                    }

                    script {
                        def qualitygate = waitForQualityGate()
                        if(qualitygate.status != 'OK'){
                            error "Pipeline aborted due to quality gate coverage failure"
                        }
                    }
                }
            }
        }
        stage('Deploy'){
            environment {
                JAVA_HOME='/usr/lib/jvm/default-jvm'
            }
            steps {
                dir('deploy') {
                    sh 'cp -r -a ../sources/. ./'
                    sh 'cp -r -a containers/prod/. ./'

                    sh """
                    docker build -t oscarvicente/sitas-backend-prod  .
                    """
                    withCredentials([string(credentialsId: 'dockerhub-pass', variable: 'pass')]) {
                        sh "docker login --username oscarvicente --password $pass; docker push oscarvicente/sitas-backend-prod"
                    }

                    //Deploy in k8s, server configured
                    dir('kube'){
                        sh 'kubectl delete deploy -n sitas sitas-backend'
                        sh 'kubectl apply -f sitas-backend-deploy.yaml'
                        sh 'kubectl apply -f sitas-backend-service.yaml'
                    }
                }
            }
        }
    }
}