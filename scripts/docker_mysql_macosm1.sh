sudo mkdir -p /private/keewe/mysql
sudo chmod -R a+rwx /private/keewe/mysql
docker run --name keewe-mysql \
    -e MYSQL_USER=keewe \
    -e MYSQL_PASSWORD=keewee \
    -e MYSQL_DATABASE=keewe \
    -e MYSQL_ALLOW_EMPTY_PASSWORD=yes \
    -e LANG=C.UTF-8 \
    --platform linux/amd64 \
    -d -p 3306:3306 \
    -v /private/keewe/mysql:/var/lib/mysql:rw \
    --user "1000:50" \
    mysql:5.7 \
    --character-set-server="utf8mb4" \
    --collation-server="utf8mb4_unicode_ci"