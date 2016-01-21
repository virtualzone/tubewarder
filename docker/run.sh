#!/bin/bash

if [ ! -d /var/lib/mysql/tubewarder ];
then
    echo "Initializing tubewarder database..."
    chown -R mysql:mysql /var/lib/mysql
    /usr/bin/mysql_install_db
    /usr/bin/mysqld_safe > /dev/null 2>&1 &
    RET=1
    while [[ RET -ne 0 ]]; do
        sleep 5
        mysql -uroot -e "status" > /dev/null 2>&1
        RET=$?
    done
    mysql -uroot -e "CREATE DATABASE IF NOT EXISTS tubewarder;"
    mysql -uroot -e "GRANT USAGE ON *.* TO 'tubewarder'@'localhost' IDENTIFIED BY 'tubewarder';"
    mysql -uroot -e "GRANT ALL PRIVILEGES ON tubewarder.* TO 'tubewarder'@'localhost';"
    mysqladmin -uroot shutdown
fi

exec /usr/bin/supervisord -n
