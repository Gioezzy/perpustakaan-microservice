#!/bin/bash

# List of services to build
services=(
  "eureka-server"
  "api-gateway-pustaka"
  "anggota-service"
  "buku-service"
  "peminjaman-command-service"
  "peminjaman-query-service"
  "pengembalian-service"
  "rabbitmq-peminjaman-service"
  "rabbitmq-pengembalian-service"
)

echo "Starting build process for all services..."

for service in "${services[@]}"; do
  if [ -d "$service" ]; then
    echo "------------------------------------------------"
    echo "Building $service..."
    echo "------------------------------------------------"
    cd "$service"
    chmod +x mvnw
    ./mvnw clean package -DskipTests
    if [ $? -ne 0 ]; then
      echo "Build failed for $service"
      exit 1
    fi
    cd ..
  else
    echo "Directory $service not found, skipping..."
  fi
done

echo "------------------------------------------------"
echo "All services built successfully!"
echo "Now run: docker compose build && docker compose up -d"
echo "------------------------------------------------"
