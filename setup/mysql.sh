#!/bin/sh


DB_NAME="hibernate_db"
USERNAME="test"
PASSWORD="root"
ROOT_PWD="root"
PORT="3306"

docker run \
     --name mysql-instance \
     -e MYSQL_DATABASE=$DB_NAME \
     -e MYSQL_USER=$USERNAME \
     -e MYSQL_PASSWORD=$PASSWORD \
     -e MYSQL_ROOT_PASSWORD=$ROOT_PWD \
     --publish "$PORT:3306" \
     -d mysql

