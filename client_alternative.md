## Instead of calling the client-api, one can utilize scripts provided by Kafka for more manual order placement
All scripts are delivered with Kafka and can be found under KAFKA_HOME/bin directory.

# Placing an order
`echo '{ "id": 1, "products": [{ "product": {"id": "apple"}, "count": 2 }]}' | ./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic "order.order"`

# Listening for replies
`./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic "api.reply" --from-beginning --zookeeper localhost:2181`