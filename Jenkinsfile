pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
       
                checkout scm
            }
        }
        stage('Build') {
            steps {
                dir('java') {
                    sh 'mvn clean compile'
                }
            }
        }
        stage('Test') {
            steps {
                dir('java') {
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit 'java/target/surefire-reports/*.xml'
                    echo 'Tests completed.'
                }
            }
        }
        stage('Package') {
            steps {
                dir('java') {
                    sh 'mvn package'
                }
            }
        }
        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'java/target/*.jar', fingerprint: true
            }
        }
    }
}
