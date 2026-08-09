#!/usr/bin/env bash
set -Eeuo pipefail

MYSQL_DATA_DIR=/var/lib/mysql
MYSQL_SOCKET=/run/mysqld/mysqld.sock

mkdir -p "${MYSQL_DATA_DIR}" /run/mysqld
chown -R mysql:mysql "${MYSQL_DATA_DIR}" /run/mysqld

if [ ! -d "${MYSQL_DATA_DIR}/mysql" ]; then
    echo "Inicializando MySQL efimero..."
    mysqld --initialize-insecure --user=mysql --datadir="${MYSQL_DATA_DIR}"
fi

echo "Iniciando MySQL..."
mysqld \
    --user=mysql \
    --datadir="${MYSQL_DATA_DIR}" \
    --socket="${MYSQL_SOCKET}" \
    --bind-address=127.0.0.1 \
    --port=3306 \
    --innodb-buffer-pool-size=128M \
    --performance-schema=OFF \
    --max-connections=30 \
    --table-open-cache=200 &
MYSQL_PID=$!

for attempt in $(seq 1 60); do
    if mysqladmin --socket="${MYSQL_SOCKET}" --user=root ping --silent; then
        break
    fi

    if ! kill -0 "${MYSQL_PID}" 2>/dev/null; then
        echo "MySQL termino antes de estar disponible."
        wait "${MYSQL_PID}"
    fi

    if [ "${attempt}" -eq 60 ]; then
        echo "MySQL no estuvo disponible dentro del tiempo esperado."
        exit 1
    fi

    sleep 1
done

echo "Cargando datos iniciales..."
mysql --socket="${MYSQL_SOCKET}" --user=root < /app/database/01-agroconecta.sql

mysql --socket="${MYSQL_SOCKET}" --user=root <<SQL
CREATE USER IF NOT EXISTS '${DB_USER}'@'127.0.0.1' IDENTIFIED BY '${DB_PASSWORD}';
ALTER USER '${DB_USER}'@'127.0.0.1' IDENTIFIED BY '${DB_PASSWORD}';
GRANT ALL PRIVILEGES ON \`${DB_NAME}\`.* TO '${DB_USER}'@'127.0.0.1';
FLUSH PRIVILEGES;
SQL

export DB_URL="jdbc:mysql://127.0.0.1:3306/${DB_NAME}?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true"

echo "Iniciando Agroconecta en el puerto ${PORT}..."
exec java -jar /app/agroconecta.jar
