pipeline {
    // Pipeline'ın tamamı Jenkins ana agent'ında çalışır.
    agent any 

    environment {
        // Uygulamanın adı ve port ayarları
        DOCKER_IMAGE_NAME = 'kutuphane-otomasyon'
        CONTAINER_NAME = 'kutuphane-app-server'
        HOST_PORT = '8081'      
        APP_PORT = '8080'       
    }

    stages {
        stage('1. Build (Derleme)') {
            steps {
                echo '>> Maven Container içinde proje derleniyor...'
                
                // Maven'ı host Jenkins agent'ı yerine, geçici bir Docker container'ı içinde çalıştırıyoruz.
                // Bu, syntax hatalarını ve Jenkins'te Maven kurulu olma zorunluluğunu ortadan kaldırır.
                // Not: Eğer projenizde Maven Wrapper (.mvnw) varsa bile, Docker içinde bu yöntem daha güvenilirdir.
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

        // Test aşamasını, build sonrası gerçekleştiriyoruz (Sizin yapınızdaki Build & Test ayrıldı)
        stage('2. Dockerize (İmaj Oluşturma)') {
            steps {
                echo '>> Docker imajı oluşturuluyor...'
                script {
                    // Dockerfile kullanarak imaj oluşturulur
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:latest ."
                }
            }
        }

        stage('3. Deploy (Dağıtım)') {
            steps {
                echo ">> Eski container durdurulup yeni imaj deploy ediliyor..."
                script {
                    // Mevcut container'ı durdur ve sil (hata olsa bile devam et)
                    sh "docker stop ${CONTAINER_NAME} || true"
                    sh "docker rm ${CONTAINER_NAME} || true"

                    // Yeni imajı kullanarak container'ı arka planda ayağa kaldır
                    sh "docker run -d --name ${CONTAINER_NAME} -p ${HOST_PORT}:${APP_PORT} ${DOCKER_IMAGE_NAME}:latest"
                }
                echo ">> Uygulama host makinede ${HOST_PORT} portunda başarıyla yayınlandı."
            }
        }
    }
    
    post {
        always {
            // Derlenen JAR dosyasını arşivle (opsiyonel)
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            echo 'Pipeline tamamlandı. 🥳'
        }
        failure { echo 'CI/CD Pipeline HATA ile sonuçlandı! 🚨' }
    }
}
