# Step 1: Use AWS Public ECR for Maven (Bypasses Docker Hub 429 Error)
FROM public.ecr.aws/docker/library/maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Use the official AWS Lambda Java image (Already on ECR)
FROM public.ecr.aws/lambda/java:17
COPY --from=build /app/target/function-service-0.0.1.jar ${LAMBDA_TASK_ROOT}/lib/
CMD ["org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"]
