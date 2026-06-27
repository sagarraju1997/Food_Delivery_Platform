# Stage 1: Build the repackaged fat JAR
FROM public.ecr.aws/docker/library/maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Deploy using the official AWS Lambda Java Image
FROM public.ecr.aws/lambda/java:17

# Set the active workspace to Lambda's root path
WORKDIR /var/task

# Copy the generated JAR into the active container path
COPY --from=build /app/target/function-service-0.0.1.jar app.jar

# Extract the archive contents into the active directory path
RUN jar -xf app.jar && rm app.jar

# Create the target lib directory before moving files into it
RUN mkdir -p ./lib

# FIXED: Changed 'rmdir BOOT-INF' to 'rm -rf BOOT-INF'
RUN mv BOOT-INF/classes/* ./ && mv BOOT-INF/lib/* ./lib/ && rm -rf BOOT-INF

# Instruct AWS Lambda to execute using the Spring Cloud Function engine
CMD ["org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"]
