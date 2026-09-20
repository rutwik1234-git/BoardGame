pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'maven3'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    credentialsId: 'git-cred',
                    url: 'https://github.com/rutwik1234-git/BoardGame.git'
            }
        }

       stage('Build') {
            steps {
                sh '''
                    export JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto.x86_64
                    export PATH=$JAVA_HOME/bin:$PATH
                    mvn clean package -DskipTests
                '''
            }
        }

        stage('Docker Build & Push') {
            steps {
                withDockerRegistry(
                    credentialsId: 'docker-cred',
                    toolName: 'docker'
                ) {
                    sh '''
                        docker build -t rutwik02/boardgame:latest .
                        docker push rutwik02/boardgame:latest
                    '''
                }
            }
        }

        stage('Deploy') {
            steps {
                withKubeConfig(
                    credentialsId: 'k8s-cred',
                    serverUrl: 'https://172.31.1.224:6443',
                    namespace: 'webapps'
                ) {
                    sh 'kubectl apply -f deployment-service.yaml'
                }
            }
        }
    }
}
