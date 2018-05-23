set -e

chown -R mysql:root /var/lib/mysql
chmod 0660 -R /var/lib/mysql

chown -R mysql:root /var/log/mysql
chmod 0660 -R /var/log/mysql

MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD:-""}
MYSQL_USER_DATABASE=${MYSQL_USER_DATABASE:-""}
MYSQL_USER=${MYSQL_USER:-""}
MYSQL_USER_PASSWORD=${MYSQL_USER_PASSWORD:-""}

rm -rf /var/lib/mysql/*

# Initial Setup
if [ ! -d "/var/lib/mysql/mysql" ] ; then
    mysql_install_db
    mysqld_safe &
    sleep 10
    #mysql_secure_installation
    mysqladmin -u root password ${MYSQL_ROOT_PASSWORD}
    echo "================SETTED ROOT PASSWORD================"
    #mysql -uroot -h localhost -proot -e "SET PASSWORD FOR 'root'@'localhost' = PASSWORD('${MYSQL_ROOT_PASSWORD}');"
    mysql -e " GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'root' WITH GRANT OPTION;" -proot
    mysql -e "CREATE DATABASE ${MYSQL_USER_DATABASE};" -p${MYSQL_ROOT_PASSWORD}
    mysql -e "CREATE USER '${MYSQL_USER}'@'localhost' IDENTIFIED BY '${MYSQL_USER_PASSWORD}';" -p${MYSQL_ROOT_PASSWORD}
    echo "granting pass on daryll"
    mysql -e "GRANT ALL PRIVILEGES ON ${MYSQL_USER_DATABASE}.* TO '${MYSQL_USER}'@'%';" -p${MYSQL_ROOT_PASSWORD}

    echo "starting database initialisation..."
    mysql -udev -h localhost -p$MYSQL_USER_PASSWORD < /etc/daryll-schema.sql
    mysql -udev -h localhost -p$MYSQL_USER_PASSWORD < /etc/daryll-requests.sql
    echo "end database initialisation."

    killall mysqld
    sleep 10
fi

#exec /usr/sbin/mysqld
mysqld_safe

