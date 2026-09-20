pipeline {
    agent any

    tools {
        jdk 'jdk21'
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
                withCredentials([usernamePassword(
                    credentialsId: 'docker-cred',
                    usernameVariable: 'DOCKER_USERNAME',
                    passwordVariable: 'DOCKER_PASSWORD'
                )]) {
                    sh '''
                        set -e
                        echo "$DOCKER_PASSWORD" | docker login --username "$DOCKER_USERNAME" --password-stdin
                        docker build -t rutwik02/boardgame:latest .
                        docker push rutwik02/boardgame:latest
                        docker logout
                    '''
                }
            }
        }

       stage('Deploy') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'k8s-cred', usernameVariable: 'K8S_USER', passwordVariable: 'K8S_PASSWORD')]) {
                // Your kubectl or deployment commands here
                }
            }
        }
    }
}
