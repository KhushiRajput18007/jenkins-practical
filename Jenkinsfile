pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Pull code from GitHub repository
                checkout scm
            }
        }
        stage('Build') {
            steps {
                // Build the project using Maven
                sh 'mvn clean compile'
            }
        }
        stage('Test') {
            steps {
                // Run test cases
                sh 'mvn test'
            }
            post {
                always {
                    // Publish JUnit test results
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Package') {
            steps {
                // Generate build artifact (JAR)
                sh 'mvn package'
            }
        }
        stage('Archive Artifact') {
            steps {
                // Archive the generated JAR file
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }
}
