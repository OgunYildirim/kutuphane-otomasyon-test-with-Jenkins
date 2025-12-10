pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh './mvnw clean compile'
                    } else {
                        bat '.\\mvnw.cmd clean compile'
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh './mvnw test'
                    } else {
                        bat '.\\mvnw.cmd test'
                    }
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    if (isUnix()) {
                        sh './mvnw package -DskipTests'
                    } else {
                        bat '.\\mvnw.cmd package -DskipTests'
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploy ediliyor...'
                echo 'Uygulama başarıyla paketlendi!'
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
            archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
        }
        success {
            echo 'Pipeline başarıyla tamamlandı!'
        }
        failure {
            echo 'Pipeline başarısız oldu!'
        }
    }
}

