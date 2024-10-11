module.exports = {
    rabbitmq: {
      host: 'amqp://localhost:5681', 
      queue: 'message_queue',  
      exchange: 'message-broker',  
      routingKey: 'service_key', 
    },
  };
  