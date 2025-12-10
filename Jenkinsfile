pipeline {
    // Pipeline'ın tamamı Jenkins ana agent'ında çalışır.
    agent any

    environment {
        // Compose dosyanızdaki servis adı
        SERVICE_NAME = 'kutuphane-app'
    }

    stages {
        stage('1. Build (Maven Container Içinde Derleme)') {
            steps {
                echo '>> Maven Container içinde proje derleniyor...'

                // Söz Dizimi Hatasını Aşmak için DİREKT DOCKER KOMUTU KULLANILIYOR.
                // Bu komut, Maven'ı geçici bir container'da çalıştırarak derlemeyi yapar.
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

        stage('2. Dockerize') {
            steps {
                echo '>> Docker imajı, docker-compose build ile oluşturuluyor...'
                sh "docker-compose build ${SERVICE_NAME}"
            }
        }

        stage('3. Deploy') {
            steps {
                echo ">> docker-compose up ile eski container durdurulup, yeni imaj deploy ediliyor..."
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