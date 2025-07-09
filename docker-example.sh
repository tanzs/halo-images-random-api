docker run -d \
  --name halo-image-api \
  --network host \
  -e SERVER_PORT=8080 \
  -e MYSQL_HOST=localhost \
  -e MYSQL_PORT=3306 \
  -e MYSQL_DB=halo \
  -e MYSQL_USER=halo \
  -e MYSQL_PASSWORD=halo123 \
  -e REDIS_HOST=localhost \
  -e REDIS_PORT=6379 \
  -e REDIS_PASSWORD=halo123 \
  -e REDIS_DATABASE=0 \
  -e AUTH_KEY=testkey123 \
  --restart=always \
  halo-image-api:1.0.0
