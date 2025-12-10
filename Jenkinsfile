pipeline {
    // Pipeline'ın tamamı için herhangi bir agent'ı kullan (Jenkins'in kurulu olduğu makine)
    agent any

    // Ortam değişkenleri, tüm pipeline adımlarında kullanılabilir.
    environment {
        JAR_NAME = 'kutuphaneotomasyon-0.0.1-SNAPSHOT.jar'
        DOCKER_IMAGE_NAME = 'kutuphane-otomasyon'
        CONTAINER_NAME = 'kutuphane-app-server'
        HOST_PORT = '8081'      // Uygulamanın host makinede yayınlanacağı port
        APP_PORT = '8080'       // Uygulamanın container içindeki portu

        // Bu değişken, Docker komutlarının çalışması için kritik:
        // Jenkins container'ının host Docker soketine erişimini sağlar.
        DOCKER_HOST_SOCK = '/var/run/docker.sock'
    }

    stages {
        stage('1. Kaynak Kodunu Çekme (SCM Checkout)') {
            steps {
                echo '>> GitHub deposundan kodlar çekiliyor...'
                // Jenkins, Job ayarlarından SCM'i otomatik çeker.
            }
        }

        stage('2. Uygulamayı Derleme (Build)') {
            // Sadece bu aşamayı, Maven'ın yüklü olduğu temiz bir Docker Agent içinde çalıştır.
            agent {
                docker {
                    image 'maven:3.8.7-jdk-17'
                    // Maven'ın indirdiği JAR dosyasının host makinede görünmesi için workspace'i mount et.
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
                    // Docker komutlarının çalışabilmesi için host Docker soketini kullan.
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:latest ."
                }
            }
        }

        stage('4. Dağıtım (Deployment)') {
            steps {
                echo ">> Eski container durdurulup yeni imaj deploy ediliyor..."
                script {
                    // Mevcut container'ı durdur ve sil (hata verse bile devam et: || true)
                    sh "docker stop ${CONTAINER_NAME} || true"
                    sh "docker rm ${CONTAINER_NAME} || true"

                    // Yeni imajı kullanarak container'ı ayağa kaldır
                    sh "docker run -d --name ${CONTAINER_NAME} -p ${HOST_PORT}:${APP_PORT} ${DOCKER_IMAGE_NAME}:latest"
                }
                echo ">> Uygulama ${HOST_PORT} portunda başarıyla yayınlandı."
            }
        }
    }

    post {
        always {
            // Derlenen JAR dosyasını build geçmişine arşivle
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            echo 'Pipeline tamamlandı.'
        }
        success { echo 'CI/CD Pipeline Başarılı! 🥳' }
        failure { echo 'CI/CD Pipeline HATA ile sonuçlandı! 🚨' }
    }
}