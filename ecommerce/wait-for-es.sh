#!/bin/sh
set -e

ES_URL=${SPRING_ELASTICSEARCH_URIS:-http://ecommerce-es:9200}

echo "Waiting for Elasticsearch at $ES_URL ..."

until curl -s "$ES_URL" >/dev/null 2>&1; do
  echo "Elasticsearch not up yet... retrying in 3s"
  sleep 3
done

echo "Elasticsearch is up! Starting Spring Boot..."
exec java -jar /app/app.jar
