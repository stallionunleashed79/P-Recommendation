#!/bin/bash

export http_proxy="http://proxy.cat.com:80"
export https_proxy="http://proxy.cat.com:80"
export HTTP_PROXY="http://proxy.cat.com:80"
export HTTPS_PROXY="http://proxy.cat.com:80"

echo "Starting..."

set -e
trap 'echo "Cant generate secret file"' ERR
if [ "$SPRING_PROFILES_ACTIVE" == "dev" -o  "$SPRING_PROFILES_ACTIVE" == "int" -o "$SPRING_PROFILES_ACTIVE" == "prod" ]; then
    echo "Preparing server certificate for env <$SPRING_PROFILES_ACTIVE>"
    echo "Pulling secrets..."
    aws ssm get-parameter  --name "platform-encryption-certificate-key" --with-decryption --output text --query 'Parameter.Value' 1>> /home/app/secrets/cert.key
    aws ssm get-parameter  --name "platform-encryption-certificate-crt" --with-decryption --output text --query 'Parameter.Value' 1>> /home/app/secrets/cert.crt
    aws ssm get-parameter  --name "platform-encryption-certificate-chain" --with-decryption --output text --query 'Parameter.Value' 1>> /home/app/secrets/cert.crt
    echo "Converting secrets to p12..."
    openssl pkcs12 -export -in /home/app/secrets/cert.crt -inkey /home/app/secrets/cert.key -out /home/app/secrets/cert.p12 -passout pass:
    echo "Cleaning..."
    rm -f /home/app/secrets/cert.crt
    rm -f /home/app/secrets/cert.key
    echo "Certificate file creation complete..."
fi

echo "Starting java app..."
java -jar /home/app/reco.jar -XX:MaxRAMPercentage=75
