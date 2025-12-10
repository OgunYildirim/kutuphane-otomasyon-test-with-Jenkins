pipeline {
    agent any

    environment {
        DOCKER_IMAGE_NAME = 'kutuphane-otomasyon'
        CONTAINER_NAME = 'kutuphane-app-server'
        HOST_PORT = '8082'
    }

    stages {
        stage('1. Checkout') {
            steps {
                echo '=== GitHub deposundan kodlar çekiliyor ==='
                checkout scm
            }
        }

        stage('2. Build & Test') {
            steps {
                echo '=== Maven ile derleme ve testler çalıştırılıyor ==='
                sh '''
                    mvn --version
                    mvn clean package
                '''
            }
        }

        stage('3. Docker Build') {
            steps {
                echo '=== Docker imajı oluşturuluyor ==='
                sh "docker build -t ${DOCKER_IMAGE_NAME}:latest ."
            }
        }

        stage('4. Deploy') {
            steps {
                echo '=== Eski container durduruluyor ve yeni imaj deploy ediliyor ==='
                sh """
                    docker stop ${CONTAINER_NAME} || true
                    docker rm ${CONTAINER_NAME} || true
                    docker run -d --name ${CONTAINER_NAME} -p ${HOST_PORT}:8080 ${DOCKER_IMAGE_NAME}:latest
                """
                echo "=== Uygulama http://localhost:${HOST_PORT} adresinde çalışıyor ==="
            }
        }
    }

    post {
        always {
            echo '=== Test sonuçları ve JAR dosyası arşivleniyor ==='
            junit '**/target/surefire-reports/*.xml'
            archiveArtifacts artifacts: '**/target/*.jar', allowEmptyArchive: true
        }
        success {
            echo '✅ Pipeline başarıyla tamamlandı!'
        }
        failure {
            echo '❌ Pipeline başarısız oldu!'
        }
    }
}

