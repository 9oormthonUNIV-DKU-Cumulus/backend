# 1단계: Gradle Wrapper로 빌드
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app

# Gradle 캐시 활용을 위해 먼저 복사
COPY backend/gradle gradle
COPY backend/gradlew ./gradlew
RUN chmod +x ./gradlew
RUN ls -l ./gradlew
COPY backend/build.gradle backend/settings.gradle ./

# 의존성 먼저 받아오기 (캐시를 위해)
RUN ./gradlew dependencies --no-daemon

# 전체 프로젝트 복사
COPY . .

# 실제 빌드
RUN ./gradlew build -x test --no-daemon



# 2단계: JRE로 실행
FROM eclipse-temurin:17-jre
WORKDIR /app

# 빌드 결과 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 포트 오픈
EXPOSE 8080

# 앱 실행
ENTRYPOINT ["java", "-jar", "app.jar"]