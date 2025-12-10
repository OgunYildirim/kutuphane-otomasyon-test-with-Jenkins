pipeline {
    // Pipeline'ın tamamı Jenkins ana agent'ında çalışır.
    agent any

    environment {
        // Compose dosyanızdaki servis adı
        SERVICE_NAME = 'kutuphane-app'
        DOCKER_IMAGE_NAME = 'kutuphane-otomasyon'
        CONTAINER_NAME = 'kutuphane-app-server'
        HOST_PORT = '8082' // Uygulamanın host portu
    }

    stages {
        stage('1. Checkout') {
            steps {
                echo '=== GitHub deposundan kodlar çekiliyor ==='
                // SCM ayarları Job içinde tanımlandığı için bu yeterlidir.
                checkout scm
            }
        }

        stage('2. Build & Test (Maven Container)') {
            steps {
                echo '=== Maven container ile derleme ve testler çalıştırılıyor ==='
                // Maven'ı geçici bir container içinde çalıştırarak derlemeyi yaparız.
                // KRİTİK ÇÖZÜM: Volume Mount için ${WORKSPACE} (Jenkins'in yolu) kullanılıyor.
                // Bu, pom.xml'in bulunması için kritik.
                sh """
                    # 1. ls -la ile dosya varlığını tekrar kontrol edebiliriz.
                    ls -la
                    echo "Jenkins Çalışma Alanı: ${WORKSPACE}"

                    # 2. Maven Cache Volume Mount yapısı.
                    docker run --rm \\
                    -v ${WORKSPACE}:/app \\      // <--- Jenkins'in KESİN YOLU Mount edildi
                    -v $HOME/.m2:/root/.m2 \\   // <--- Maven Cache
                    -w /app \\
                    maven:3.9-eclipse-temurin-17 \
                    mvn clean package -DskipTests
                """
            }
        }

        stage('3. Dockerize') {
            steps {
                echo '=== Docker imajı, docker-compose build ile oluşturuluyor ==='
                // Compose Build'i kullanarak imajı oluşturur.
                sh "docker-compose build ${SERVICE_NAME}"
            }
        }

        stage('4. Deploy') {
            steps {
                echo '=== Eski container durduruluyor ve yeni imaj deploy ediliyor ==='
                // Compose Up, eski container'ı durdurup yenisini ayağa kaldırır.
                sh "docker-compose up -d --no-build ${SERVICE_NAME}"

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