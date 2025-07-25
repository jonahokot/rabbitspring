# RabbitSpring Application - Quick Start Guide

This guide explains how to run the RabbitSpring producer and consumer applications using Make or Docker.

## Prerequisites

- **[Docker](https://docs.docker.com/engine/install/ubuntu/) & Docker Compose** - For containerized deployment
- **[Make](https://linuxgenie.net/install-use-make-ubuntu-22-04/)** (optional) - For simplified command execution
- **[Java 21](https://builds.openlogic.com/downloadJDK/openlogic-openjdk/21.0.8+9/openlogic-openjdk-21.0.8+9-linux-x64.tar.gz)** (for local development)
- **[Maven](https://www.digitalocean.com/community/tutorials/install-maven-linux-ubuntu)** (for local development)

## Quick Start Options

### Option 1: Using Docker Compose (Recommended)

```bash
# Start all services (RabbitMQ → Producer → Consumer)
docker compose up --build

# Start in background
docker compose up --build -d

# View logs
docker compose logs -f

# Stop all services
docker compose down
```

### Option 2: Using Make Commands

```bash
   make run
```

### Option 3: Local Development with Maven

For local development, you'll need RabbitMQ running and then start each application separately.

#### Step 1: Start RabbitMQ
```bash
# Option A: Using Docker (Recommended)
docker run -d --name rabbitmq \
  -p 5672:5672 -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=myuser \
  -e RABBITMQ_DEFAULT_PASS=secret \
  rabbitmq:3-management

# Option B: If you have RabbitMQ installed locally
# Configure user and password: myuser/secret
# Ensure RabbitMQ is running on localhost:5672
```

#### Step 2: Build and Run Applications
```bash
# Terminal 1: Start Producer
cd rabbitspring-producer
mvn clean compile
mvn spring-boot:run

# Terminal 2: Start Consumer  
cd rabbitspring-consumer
mvn clean compile
mvn spring-boot:run
```

#### Alternative: Build JARs and Run
```bash
# Build both applications
cd rabbitspring-producer && mvn clean package -DskipTests
cd ../rabbitspring-consumer && mvn clean package -DskipTests

# Run the JAR files
java -jar rabbitspring-producer/target/rabbitspring-producer-0.0.1-SNAPSHOT.jar
java -jar rabbitspring-consumer/target/rabbitspring-consumer-0.0.1-SNAPSHOT.jar
```

## Available Make Commands

| Command | Description |
|---------|-------------|
| `make run` | Start all services using Docker Compose |
| `make stop` | Stop all services using Docker Compose |


## Service URLs

Once running, access the services at:

- **Producer**: http://localhost:8081
- **Consumer**: http://localhost:8080  
- **RabbitMQ Management**: http://localhost:15672
  - Username: `myuser`
  - Password: `secret`

## How It Works

1. **RabbitMQ** starts first and creates the message broker
2. **Producer** connects to RabbitMQ and generates mock events every 75 seconds
3. **Consumer** receives events from RabbitMQ and sends webhooks to configured endpoints

## Application Flow
