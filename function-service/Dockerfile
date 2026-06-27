# Step 1: Compile the code and create a deployment package
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Use the official AWS Lambda Java image
FROM public.ecr.aws/lambda/java:17

# Copy the fat JAR file compiled by Maven into Lambda's classpath root
COPY --from=build /app/target/function-service-0.0.1.jar ${LAMBDA_TASK_ROOT}/lib/

# Tell AWS Lambda to use the official Spring Cloud Function routing engine
CMD ["org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"]