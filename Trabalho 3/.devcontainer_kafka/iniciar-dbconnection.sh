#!/bin/bash
# Start the first process
/opt/kafka_2.13-2.8.1/bin/connect-standalone.sh /opt/kafka_2.13-2.8.1/config/connect-standalone.properties /opt/kafka_2.13-2.8.1/config/connect-jdbc-source.properties /opt/kafka_2.13-2.8.1/config/connect-jdbc-sink.properties
~