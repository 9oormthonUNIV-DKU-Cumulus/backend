# 1단계: Gradle Wrapper로 빌드
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

# backend 디렉토리 전체를 복사
COPY backend/ ./
RUN chmod +x ./gradlew

# 의존성 먼저 받아오기 (캐시를 위해)
RUN ./gradlew dependencies --no-daemon

# 실제 빌드
RUN ./gradlew bootJar --no-daemon


# 2단계: JRE로 실행
FROM eclipse-temurin:17-jre
WORKDIR /app

# 빌드 결과 복사
COPY --from=builder /app/build/libs/backend-0.0.1-SNAPSHOT.jar app.jar

# 포트 오픈
EXPOSE 8080

# 앱 실행
ENTRYPOINT ["java", "-jar", "app.jar"]