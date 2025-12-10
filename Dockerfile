# =========================================================
# AŞAMA 1: BUILDER (Derleme Ortami) - AS build
# =========================================================
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Bağımlılıkları önbellekleme için çalışma dizinini ayarla
WORKDIR /app

# 1. pom.xml'i kopyala ve bağımlılıkları indir (Önbellekleme için KRİTİK)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Kaynak kodları kopyala ve uygulamayı derle
COPY src ./src
RUN mvn clean package -DskipTests -B

# =========================================================
# AŞAMA 2: RUNTIME (Çalışma Ortami)
# =========================================================
FROM eclipse-temurin:17-jre-alpine

# Çalışma dizinini ayarla
WORKDIR /app

# Güvenlik için non-root user oluştur
RUN addgroup -S spring && adduser -S spring -S -G spring
USER spring:spring

# BUILD aşamasından oluşan JAR dosyasını nihai imaja kopyala
COPY --from=build /app/target/*.jar app.jar

# Portu tanımla
EXPOSE 8080

# Sağlık kontrolü (Health Check)
# API'nizin endpoint'ini kontrol ettiğinden emin olun.
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/api/kitaplar || exit 1

# Uygulamayı başlat
ENTRYPOINT ["java", "-jar", "app.jar"]