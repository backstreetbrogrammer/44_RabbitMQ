@ECHO OFF

set ERLANG_HOME=C:\Program Files\Erlang OTP\erts-14.2
set RABBITMQ_NODE_PORT=5673
set RABBITMQ_DIST_PORT=25673
set RABBITMQ_NODENAME=rabbit1@localhost
set RABBITMQ_MNESIA_BASE=C:\tmp\rabbit1
set RABBITMQ_MNESIA_DIR=C:\tmp\rabbit1\data
set RABBITMQ_LOG_BASE=C:\tmp\rabbit1\logs
set RABBITMQ_CONFIG_FILE=C:\RabbitMQ\rabbitmq_server-3.12.10\config\rabbitmq
set RABBITMQ_ENABLED_PLUGINS_FILE=C:\tmp\rabbit1\enabled_plugins

echo "====== Run RabbitMQ Server ======"

cd "C:\RabbitMQ\rabbitmq_server-3.12.10\sbin"
start rabbitmq-server.bat
