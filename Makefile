build_consumer:
	cd rabbitspring-consumer && mvn clean package -DskipTests

build_producer:
	cd rabbitspring-producer && mvn clean package -DskipTests

run:
	docker compose down && docker compose build && docker compose up -d

stop:
	docker compose down