# 1. 베이스 이미지 설정
FROM amazoncorretto:17-alpine-jdk

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. JAR 파일 복사
COPY build/libs/*-SNAPSHOT.jar app.jar

# 4. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]