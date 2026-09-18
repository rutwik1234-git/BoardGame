pipeline {
    agent any
    
    tools {
        jdk 'jdk17'
        maven 'maven3'
    }
    stages {
        stage('Git Checkout') {
            steps {
                git branch: 'main', credentialsId: 'git-cred', url: 'https://github.com/rutwik1234-git/BoardGame.git'
            }
        }
        stage('Compile') {
            steps {
                sh "mvn compile"
            }
        }
        stage('Test') {
            steps {
                sh "mvn test"
            }
        }
        stage('Build') {
            steps {
                sh "mvn package"
            }
        }
        stage('Build & Tag Docker Image') {
            steps {
                script {
                    withDockerRegistry(credentialsId:'docker-cred', toolName:'docker'){
                        sh "docker build -t shettyadarsha/boardgame:latest ."
                        sh "docker push shettyadarsha/boardgame:latest"
                    }
                }
            }
        }
        stage('Docker Image Scan') {
            steps {
                sh "trivy image --format table -o trivy-image-report.html shettyadarsha/boardgame:latest "
            }
        }
        stage('CleanUp the Docker Images') {
            steps {
               script {
                   withDockerRegistry(credentialsId:'docker-cred', toolName:'docker') {
                            sh "docker system prune -a"
                    }
               }
            }
        }
        stage('deploy to K8s'){
            steps{
                withKubeConfig(caCertificate: '', clusterName: 'kubernetes', contextName: '', credentialsId: 'k8s-cred', namespace: 'webapps', restrictKubeConfigAccess: false, serverUrl: 'https://172.31.1.224:6443') {
                    sh "kubectl apply -f deployment-service.yaml"
                }
            }
        }
        stage('Verify the Deployment') {
            steps {
               withKubeConfig(caCertificate: '', clusterName: 'kubernetes', contextName: '', credentialsId: 'k8s-cred', namespace: 'webapps', restrictKubeConfigAccess: false, serverUrl: 'https://172.31.1.224:6443') {
                        sh "kubectl get pods -n webapps"
                        sh "kubectl get svc -n webapps"
                }
            }
        }
    }
    
    
