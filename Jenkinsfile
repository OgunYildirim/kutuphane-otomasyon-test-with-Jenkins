pipeline {
    // Pipeline'ın tamamı Jenkins ana agent'ında çalışır.
    agent any

    environment {
        // Compose dosyanızda uygulamanın servisine verdiğiniz ismi buraya yazın
        SERVICE_NAME = 'kutuphane-app'
    }

    stages {
        stage('1. Build (Maven Container Içinde Derleme)') {
            steps {
                echo '>> Maven Container içinde proje derleniyor...'

                // Projenin JAR dosyasını oluşturmak için geçici bir Maven container'ı çalıştırır.
                // Bu, Jenkins'te Maven kurulu olma zorunluluğunu ortadan kaldırır.
                sh """
                    docker run --rm \
                    -v ${PWD}:/usr/src/app \
                    -v $HOME/.m2:/root/.m2 \
                    -w /usr/src/app \
                    maven:3.8.7-jdk-17 \
                    mvn clean package -DskipTests
                """
            }
        }

        stage('2. Dockerize (Imaci Olusturma)') {
            steps {
                echo '>> Docker imajı, docker-compose build ile olusturuluyor...'
                // Proje kökündeki Dockerfile'ı kullanarak imajı oluşturur.
                sh "docker-compose build ${SERVICE_NAME}"
            }
        }

        stage('3. Deploy (Dagitim)') {
            steps {
                echo ">> docker-compose up ile eski container durdurulup, yeni imaj deploy ediliyor..."
                // Yeni imajı kullanarak container'ı ayağa kaldırır, eskiyi otomatik durdurur/siler.
                sh "docker-compose up -d --no-build ${SERVICE_NAME}"
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            echo 'Pipeline tamamlandı. 🥳'
        }
        failure { echo 'CI/CD Pipeline HATA ile sonuçlandı! 🚨' }
    }
}