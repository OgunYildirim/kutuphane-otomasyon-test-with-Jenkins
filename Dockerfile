# Multi-stage build için Maven ile derleme
ENTRYPOINT ["java", "-jar", "app.jar"]
# Uygulama başlat

    CMD wget --quiet --tries=1 --spider http://localhost:8080/api/kitaplar || exit 1
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
# Health check

EXPOSE 8080
# Port tanımla

USER spring:spring
# Kullanıcıyı değiştir

COPY --from=build /app/target/*.jar app.jar
# JAR dosyasını kopyala

RUN addgroup -S spring && adduser -S spring -G spring
# Güvenlik için non-root user oluştur

WORKDIR /app

FROM eclipse-temurin:17-jre-alpine
# Runtime image

RUN mvn clean package -DskipTests -B
COPY src ./src
# Kaynak kodları kopyala ve derle

RUN mvn dependency:go-offline -B
COPY pom.xml .
# Önce pom.xml'i kopyala ve dependency'leri indir (cache için)

WORKDIR /app

FROM maven:3.9.6-eclipse-temurin-17 AS build

