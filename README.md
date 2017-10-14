amqp-consumer
=======================

Built with Java 8+, Spring-Boot (1.5.6.RELEASE)

##Spring

* Build

`mvn clean install`

* Start RabbitMQ

* Create VHost cogito

* Create User consumer/consumer w/ admin role

* Set client VHosts permissions (Read,Write,Configure): / and cogito

* Run

`mvn spring-boot:run -Drun.arguments="-Xmx256m,-Xms128m"`

##Docker

* Create Network

`docker network create amqp-network`

* Start RabbitMQ

`docker run -d -p 15672:15672 --net=amqp-network --name amqp-broker --hostname amqp-broker rabbitmq:3.6.12-management`

* Create VHost cogito

* Create User consumer/consumer w/ admin role

* Set client VHosts permissions (Read,Write,Configure): / and cogito

* Pull down Image

`docker pull jtdeane/amqp-consumer`

OR

* Build locally

`docker build -t amqp-consumer:latest .`

* Run Docker

`docker run -d -p 9000:9000 -e JAVA_OPTS='-Xmx256m -Xms128m' --net=amqp-network --hostname amqp-consumer amp-consumer:latest` 