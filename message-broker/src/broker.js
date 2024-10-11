const amqp = require('amqplib/callback_api');
const { rabbitmq } = require('./config');

class MessageBroker {
  constructor() {
    this.connection = null;
    this.channel = null;
  }

  async connect() {
    return new Promise((resolve, reject) => {
      amqp.connect(rabbitmq.url, (error, connection) => {
        if (error) {
          return reject(error);
        }

        this.connection = connection;
        connection.createChannel((channelError, channel) => {
          if (channelError) {
            return reject(channelError);
          }

          this.channel = channel;
          this.channel.assertExchange(rabbitmq.exchange, 'direct', { durable: true });
          console.log('Conectado ao RabbitMQ e canal criado');
          resolve();
        });
      });
    });
  }

  async publishMessage(queue, routingKey, message) {
    if (!this.channel) {
      throw new Error('O canal não está disponível');
    }

    const msgBuffer = Buffer.from(JSON.stringify(message));
    this.channel.publish(rabbitmq.exchange, routingKey, msgBuffer);
    console.log(`Mensagem publicada para ${routingKey}:`, message);
  }

  async close() {
    return new Promise((resolve, reject) => {
      if (this.channel) {
        this.channel.close((err) => {
          if (err) return reject(err);
          if (this.connection) {
            this.connection.close((err) => {
              if (err) return reject(err);
              resolve();
            });
          } else {
            resolve();
          }
        });
      } else if (this.connection) {
        this.connection.close((err) => {
          if (err) return reject(err);
          resolve();
        });
      } else {
        resolve();
      }
    });
  }
}

module.exports = new MessageBroker();
