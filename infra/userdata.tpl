#!/bin/bash

sudo apt-get update -y &&
sudo apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg-agent \
    software-properties-common &&
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add - &&
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" &&
sudo apt-get update -y &&
sudo sudo apt-get install docker-ce docker-ce-cli containerd.io -y &&
sudo usermod -aG docker ubuntu
sudo systemctl enable docker.service
sudo systemctl enable containerd.service

docker pull gustosilva/dynamodb-api:latest

docker run --env ACCESS_KEY=ACCESS_KEY --env SECRET_KEY=SECRET_KEY --env SERVICE_ENDPOINT=SERVICE_ENDPOINT --env SIGNING_REGION=SIGNING_REGION -p 80:8080 gustosilva/dynamodb-api:latest