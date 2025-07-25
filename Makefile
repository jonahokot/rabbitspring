# Complete Makefile for RabbitMQ Spring Services
.PHONY: help build_consumer build_producer build test run stop clean logs restart status

# Default target - show help when running 'make' without arguments
.DEFAULT_GOAL := help

# Build consumer service
build_consumer:
	@echo "Building consumer service..."
	cd rabbitspring-consumer && mvn clean package -DskipTests

# Build producer service
build_producer:
	@echo "Building producer service..."
	cd rabbitspring-producer && mvn clean package -DskipTests

# Build both services
build: build_consumer build_producer
	@echo "All services built successfully!"

# Run tests for consumer
test_consumer:
	@echo "Running consumer tests..."
	cd rabbitspring-consumer && mvn test

# Run tests for producer
test_producer:
	@echo "Running producer tests..."
	cd rabbitspring-producer && mvn test

# Run all tests
test: test_consumer test_producer
	@echo "All tests completed!"

# Start services with Docker Compose
run:
	@echo "Starting services..."
	docker compose down && \
	docker compose build && \
	docker compose up -d
	@echo "Services started successfully!"

start: run

# Stop Docker Compose services
stop:
	@echo "Stopping services..."
	docker compose down
	@echo "Services stopped!"

# Clean build artifacts and Docker resources
clean:
	@echo "Cleaning build artifacts..."
	cd rabbitspring-consumer && mvn clean
	cd rabbitspring-producer && mvn clean
	@echo "Cleaning Docker resources..."
	docker compose down --volumes --remove-orphans
	@echo "Cleanup completed!"

# View logs from running containers
logs:
	docker compose logs -f

# Restart services (stop + run)
restart: stop run
	@echo "Services restarted!"

# Check status of Docker Compose services
status:
	docker compose ps

# Build and run in one command
deploy: build run
	@echo "Build and deployment completed!"

# Development workflow - build, test, and run
dev: clean build test run
	@echo "Development environment ready!"

# Help target - display available commands
help:
	@echo "Available targets:"
	@echo "  help           - Show this help message"
	@echo "  build_consumer - Build consumer service only"
	@echo "  build_producer - Build producer service only"
	@echo "  build          - Build both services"
	@echo "  test_consumer  - Run consumer tests only"
	@echo "  test_producer  - Run producer tests only"
	@echo "  test           - Run all tests"
	@echo "  run            - Start services with Docker Compose"
	@echo "  stop           - Stop Docker Compose services"
	@echo "  restart        - Stop and start services"
	@echo "  clean          - Clean build artifacts and Docker resources"
	@echo "  logs           - View logs from running containers"
	@echo "  status         - Check status of services"
	@echo "  deploy         - Build and run services"
	@echo "  dev            - Full development workflow (clean, build, test, run)"