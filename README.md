# RabbitMQ

> This is a tutorial course covering RabbitMQ.

Tools used:

- JDK 11
- Maven
- JUnit 5, Mockito
- IntelliJ IDE

## Table of contents

1. [Introduction to RabbitMQ](https://github.com/backstreetbrogrammer/44_RabbitMQ?tab=readme-ov-file#chapter-01-introduction-to-rabbitmq)
    - [Queues](https://github.com/backstreetbrogrammer/44_RabbitMQ?tab=readme-ov-file#queues)
    - [RabbitMQ](https://github.com/backstreetbrogrammer/44_RabbitMQ?tab=readme-ov-file#rabbitmq)
2. [RabbitMQ Installation](https://github.com/backstreetbrogrammer/44_RabbitMQ?tab=readme-ov-file#chapter-02-rabbitmq-installation)

---

## Chapter 01. Introduction to RabbitMQ

### Queues

In computer science, queue is a collection of entities maintained in a sequence.

Queue is a linear data structure that is open at both ends and the operations are performed in First-In-First-Out (FIFO)
order.

![Queue](Queue.PNG)

Sequence can be modified by:

- adding entities to the beginning or to the end
- removing from the beginning or from the end

**_Synchronous Communication_**

Example: HTTP, RESTful API, WebServices, RPC etc.

This has a simple request-response model where a client sends a request to server and gets the response back.

Best for requests with really short execution time.

![RequestResponse](RequestResponse.PNG)

**_Asynchronous Communication_**

Example: Advanced Message Queuing Protocol (AMQP)

The client gets the request **acknowledgement** back from the server immediately but can continue doing other tasks
rather than waiting for the server to send the complete response (results).

Results will be received at some point in the future without blocking.

![Async](Async.PNG)

Thus, the basic difference is:

![SyncVsAsync](SyncVsAsync.PNG)

**_Most important queue attributes_**

- **Queue Size**: number of messages in the queue
- **Queue Age**: age of the oldest message in the queue

For example, if a queue has only 10 messages (size=10) but age is 5 minutes => messages are not consumed fast enough
or at all.

Similarly, if a queue has 1 million messages but age is 5 seconds => produce and consumption rate is 200_000
messages per seconds.

### RabbitMQ

**RabbitMQ** is an open-source message-broker software (sometimes just called queuing software) that originally
implemented the Advanced Message Queuing Protocol (AMQP).

RabbitMQ supports plugins—standard distribution contains plugins to support protocols like Streaming Text Oriented
Messaging Protocol (STOMP), Message Queuing Telemetry Transport (MQTT), and others.

RabbitMQ is written in **Erlang** Programming Language. Erlang is a functional language, designed to build massively
scalable real-time systems with high-availability requirements.

Erlang processes:

- are very light (memory footprint)
- start very fast
- operate in isolation from other processes
- are scheduled by Erlang's Virtual Machine (default limit of Erlang processes for RabbitMQ is one million)

**_RabbitMQ vs Kafka_**

|                                                 | RabbitMQ                                                  | Kafka                                                             |
|-------------------------------------------------|-----------------------------------------------------------|-------------------------------------------------------------------|
| Released                                        | June 2007                                                 | January 2011                                                      |
| General purpose                                 | messages broker (queue)                                   | messages bus (stream processing - log)                            |
| Messages replay                                 | no                                                        | yes                                                               |
| Consumer                                        | dummy consumer / push model                               | smart consumer / pull model                                       |
| Data throughput (relative to single shard/node) | high (no quoted throughput)                               | highest (no quoted throughput)                                    |
| Data consistency                                | highest (ACKs)                                            | high                                            <br/>             |
| Persistence period                              | no limit                                                  | no limit                                                          |
| Maintenance effort                              | high                                                      | highest                                                           |
| Cost                                            | relatively small                                          | relatively normal                                                 |
| Open Source                                     | yes                                                       | yes                                                               |
| Availability                                    | highly available                                          | highly available, but Zookeeper is needed to manage cluster state |
| Performance                                     | high                                                      | very high - higher throughput                                     |
| Replication                                     | queues are not replicated by design                       | by design                                                         |
| Protocols                                       | standard queue protocols like AMQP, STOMP, HTTP, and MQTT | binary serialized data                                            |
| Storage type                                    | queue                                                     | log                                                               |
| Acknowledgments                                 | sophisticated                                             | basic                                                             |
| Routing                                         | very flexible (exchange, binding keys)                    | message is sent to the topic by a key                             |

**_AMQP Protocol_**

AMQP is an open standard **application layer** protocol:

- Designed for asynchronous communication
- AMQP standardizes the behavior of the messaging publisher and consumer
- Platform independent
- Technology independent (many SDKs)

![RabbitMQ_Message](RabbitMQ_Message.PNG)

AMQP Message is a package of information:

- **Header** (key-value pairs): Metadata — defined by AMQP specification
- **Properties** (key-value pairs): Metadata — application-specific information holder
- **Body** (payload, actual message): `byte[]`

Message limit is `2GB`, but it's better to avoid such a big message if possible.

Message is sent per frames => `131KB` (by default).

![RabbitMQ_diagram](RabbitMQ_diagram.PNG)

Producer and Consumer can be written in any language (Java, Python, C++, etc.) and are independent of each other.

Once a consumer consumes a message, the message is removed from the queue.

---

## Chapter 02. RabbitMQ Installation

**_Download RabbitMQ_**

- Navigate to [RabbitMQ release site](https://github.com/rabbitmq/rabbitmq-server/releases/)
- Download
  [rabbitmq-server-windows-3.12.10.zip](https://github.com/rabbitmq/rabbitmq-server/releases/download/v3.12.10/rabbitmq-server-windows-3.12.10.zip)
- Extract the zip to the desired folder, for example: `C:\RabbitMQ\rabbitmq_server-3.12.10`
- Add RABBITMQ_HOME system variable and edit PATH (add `%RABBITMQ_HOME%\sbin`)

```
RABBITMQ_HOME=C:\RabbitMQ\rabbitmq_server-3.12.10
PATH=%RABBITMQ_HOME%\sbin
```

**_Download Erlang_**

- Navigate to [Erlang site](https://www.erlang.org/downloads)
- Click on [Windows installer](https://github.com/erlang/otp/releases/download/OTP-26.2/otp_win64_26.2.exe)
- Install the exe and follow the defaults to install Erlang
- Add ERLANG_HOME system variable and edit PATH (add `%ERLANG_HOME%\bin`)

```
ERLANG_HOME=C:\Program Files\Erlang OTP\erts-14.2
PATH=%ERLANG_HOME%\bin
```

**_Verify installation_**

- Copy attached `rabbitenv.bat` file to `C:\RabbitMQ`
- Open cmd and run: `cd "C:\RabbitMQ\rabbitmq_server-3.12.10\sbin"`
- Run bat file to set environment variables: `..\..\rabbitenv.bat`
- Enable plugins: `rabbitmq-plugins.bat enable rabbitmq_management`
- Start RabbitMQ server: `rabbitmq-server.bat`

Expected Output:

![VerifyInstallation](VerifyInstallation.PNG)

If any issue, we can run this command on Powershell to check if port 5673 is occupied by some other process.

```
Get-Process -Id (Get-NetTCPConnection -LocalPort 5673).OwningProcess
```

If port 5673 is occupied, we can change the port to 5674 in `rabbitenv.bat`.

**_Configurations_**

Most important configuration variables:

- **RABBITMQ_NODE_PORT** (default 5672): used by AMQP 0-9-1 and 1.0 clients (without TLS)
- **RABBITMQ_DIST_PORT** (default 20000 + **RABBITMQ_NODE_PORT**): used by Erlang distribution for inter-node and CLI
  tools communication
- **RABBITMQ_NODENAME** (Unix default: `rabbit@$HOSTNAME`; Windows default: `rabbit@%COMPUTERNAME%`): unique node name
- **RABBITMQ_MNESIA_BASE**: RabbitMQ server's node database, message store and cluster state files, one for each node
- **RABBITMQ_MNESIA_DIR**: This is **RABBITMQ_MNESIA_BASE** subfolder; includes a schema database, message stores,
  cluster member information and other persistent node states.
- **RABBITMQ_LOG_BASE**: just logs
- **RABBITMQ_CONFIG_FILE**: points `rabbitmq.config` file (note the **".config"** extension is added)
- **RABBITMQ_ENABLED_PLUGINS_FILE**: file to track enabled plugins

**Create the config file:**

- Download the sample rabbit config file from
  [Rabbit Docs](https://github.com/rabbitmq/rabbitmq-server/blob/main/deps/rabbit/docs/rabbitmq.conf.example)
- Change the file name to `rabbitmq.conf`
- Place it in `C:\RabbitMQ\rabbitmq_server-3.12.10\config`
- Rerun:

```
`..\..\rabbitenv.bat`
`rabbitmq-server.bat`
```

Expected Output:

![ConfigInstallation](ConfigInstallation.PNG)

Most important configuration file entries:

- **cluster_name** (default "") - used for automatic clustering
- **listeners.tcp.default** - same as **RABBITMQ_NODE_PORT**
- **heartbeat** (default 60 seconds) - after 60s connection should be considered unreachable by RabbitMQ and client
  libraries. Server suggest this value to client libraries while establishing TCP connection at AMQP protocol level.
  Clients might not follow server's suggestion.
- **frame_max** - (default 131072) - maximum permissible size of a frame (in bytes) to negotiate with clients (AMQP
  protocol level). Larger value improves throughput, smaller value improves latency (not related with max message size
  which is 2GB)
- **channel_max** - (default 0 - unlimited) - number of channels to negotiate with clients. Using more channels
  increases broker's memory footprint,
- **management.tcp.port** (default 15672) - the web-management and RESTful service


