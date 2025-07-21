# RabbitSpring Application - Quick Start Guide

This guide explains how to run the RabbitSpring producer and consumer applications using Make or Docker.

## Prerequisites

- **Docker & Docker Compose** - For containerized deployment
- **Make** (optional) - For simplified command execution
- **Java 21** (for local development)
- **Maven** (for local development)

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
