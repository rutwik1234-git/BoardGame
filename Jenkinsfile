pipeline {
    agent any

    tools {
        jdk 'JDK21'     // Ensure this matches your JDK 21 name in Jenkins Tools
        maven 'maven3'  // Ensure this matches your Maven name in Jenkins Tools
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
                // Verifies Java version before executing Maven
                sh 'java -version'
                sh 'mvn clean package'
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
