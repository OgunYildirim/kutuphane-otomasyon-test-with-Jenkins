pipeline {
    // Pipeline'ın tamamı için bir Maven imajı kullan
    agent {
        docker {
            image 'maven:3.8.7-jdk-17' // Maven ve Java 17'nin yüklü olduğu bir imaj
        }
    }
    // ... environment ve stages devam eder ...
    stages {
        stage('1. Kaynak Kodunu Çekme (SCM Checkout)') {
            // ...
        }
        stage('2. Uygulamayı Derleme (Build)') {
            steps {
                echo '>> Maven ile proje derleniyor...'
                // Artık mvn komutu container içinde çalışır
                sh 'mvn clean package -DskipTests'
            }
        }
        // ... Diğer aşamalar devam eder ...
    }
    // ...
}