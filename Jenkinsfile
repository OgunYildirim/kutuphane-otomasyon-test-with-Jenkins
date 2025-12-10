pipeline {
    agent any

    environment {
        SERVICE_NAME = 'kutuphane-app'
        DOCKER_IMAGE_NAME = 'kutuphane-otomasyon'
        CONTAINER_NAME = 'kutuphane-app-server'
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
                echo '=== Maven container ile derleme ve testler çalıştırılıyor ==='
                script {
                    // Jenkins workspace zaten bir volume, oradan build yapalım
                    def workspaceDir = pwd()
                    sh """
                        ls -la
                        echo "Workspace: ${workspaceDir}"

                        # Maven cache için volume oluştur
                        docker volume create maven-repo || true

                        # Absolute path ile volume mount
                        docker run --rm \
                        -v maven-repo:/root/.m2 \
                        -v "${workspaceDir}":/app \
                        -w /app \
                        maven:3.9-eclipse-temurin-17 \
                        sh -c "ls -la && mvn clean package"
                    """
                }
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
                    docker run -d --name ${CONTAINER_NAME} -p 8082:8080 ${DOCKER_IMAGE_NAME}:latest
                """
                echo '=== Uygulama http://localhost:8082 adresinde çalışıyor ==='
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