pipeline {
    // Pipeline'ın tamamı için bir agent tanımlıyoruz. Sadece build için Docker kullanılacak.
    agent any 

    environment {
        JAR_NAME = 'kutuphaneotomasyon-0.0.1-SNAPSHOT.jar' 
        DOCKER_IMAGE_NAME = 'kutuphane-otomasyon'
        CONTAINER_NAME = 'kutuphane-app-server'
        HOST_PORT = '8081'      
        APP_PORT = '8080'       
    }

    stages {
        stage('1. Kaynak Kodunu Çekme (SCM Checkout)') {
            steps {
                echo '>> GitHub deposundan kodlar çekiliyor...'
            }
        }

        stage('2. Uygulamayı Derleme (Build)') {
            // HATA DÜZELTİLDİ: Stage-level agent tanımı doğru yapıldı.
            agent { 
                docker { 
                    image 'maven:3.8.7-jdk-17' 
                    // Maven Agent'ın workspace'i kullanması ve JAR'ı host'ta bırakması için volume mount ediyoruz.
                    args '-v ${PWD}:/usr/src/app -w /usr/src/app' 
                }
            }
            steps {
                echo '>> Maven ile proje derleniyor ve JAR oluşturuluyor...'
                sh 'mvn clean package -DskipTests' 
            }
        }

        stage('3. Docker İmajını Oluşturma') {
            steps {
                echo '>> Docker imajı oluşturuluyor...'
                script {
                    // Bu aşama, host'taki Jenkins agent'ında çalışır ve host'taki Docker'ı kullanır.
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:latest ."
                }
            }
        }

        stage('4. Dağıtım (Deployment)') {
            steps {
                echo ">> Eski container durdurulup yeni imaj deploy ediliyor..."
                script {
                    sh "docker stop ${CONTAINER_NAME} || true"
                    sh "docker rm ${CONTAINER_NAME} || true"
                    sh "docker run -d --name ${CONTAINER_NAME} -p ${HOST_PORT}:${APP_PORT} ${DOCKER_IMAGE_NAME}:latest"
                }
                echo ">> Uygulama ${HOST_PORT} portunda başarıyla yayınlandı."
            }
        }
    }
    
    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            echo 'Pipeline tamamlandı.'
        }
        success { echo 'CI/CD Pipeline Başarılı! 🥳' }
        failure { echo 'CI/CD Pipeline HATA ile sonuçlandı! 🚨' }
    }
}
