pipeline {
    // Pipeline'ın tamamı Jenkins ana agent'ında çalışacak
    agent any

    environment {
        // Compose dosyanızdaki servis adı
        SERVICE_NAME = 'kutuphane-app'
    }

    stages {
        stage('1. Build & Test') {
            // SADELEŞTİRME ÇÖZÜMÜ: Sadece bu aşama için Maven kurulu bir Docker Agent kullan
            agent {
                docker {
                    image 'maven:3.8.7-jdk-17'
                    // Workspace'i mount et: Kodları build agent'ına taşı
                    args '-v ${PWD}:/usr/src/app -w /usr/src/app'
                }
            }
            steps {
                echo '>> Maven Container içinde proje derleniyor ve test ediliyor...'
                // Sizin istediğiniz sade komut:
                sh 'mvn clean package' // Testlerin otomatik çalışması için 'package' yeterli
            }
        }

        stage('2. Dockerize') {
            steps {
                echo '>> Docker imajı, docker-compose build ile oluşturuluyor...'
                // Dockerfile'ı kullanarak imajı oluştur
                sh "docker-compose build ${SERVICE_NAME}"
            }
        }

        stage('3. Deploy') {
            steps {
                echo ">> docker-compose up ile dağıtım yapılıyor..."
                // Yeni imajı kullanarak container'ı ayağa kaldır (Host Docker'a erişim kritik!)
                sh "docker-compose up -d --no-build ${SERVICE_NAME}"
            }
        }
    }

    post {
        always {
            // Test sonuçlarını archive et (Sadece Stage 1'de testler çalıştıysa)
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            junit '**/target/surefire-reports/*.xml'
            echo 'Pipeline tamamlandı. 🥳'
        }
        failure { echo 'CI/CD Pipeline HATA ile sonuçlandı! 🚨' }
    }
}