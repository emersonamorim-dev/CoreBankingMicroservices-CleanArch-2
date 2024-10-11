const amqp = require('amqplib/callback_api');
const MessageBroker = require('../src/broker');
const { rabbitmq } = require('../src/config');

// Mock para o amqplib
jest.mock('amqplib/callback_api', () => ({
  connect: jest.fn(),
}));

describe('MessageBroker', () => {
  let mockChannel;
  let mockConnection;

  beforeEach(() => {
    mockChannel = {
      assertExchange: jest.fn(),
      publish: jest.fn(),
    };

    mockConnection = {
      createChannel: jest.fn((callback) => callback(null, mockChannel)),
    };

    amqp.connect.mockImplementation((url, callback) => {
      if (url === rabbitmq.url) {
        callback(null, mockConnection);
      } else {
        callback(new Error('Connection error'));
      }
    });
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('deve conectar ao RabbitMQ e criar um canal', async () => {
    await MessageBroker.connect();
    expect(amqp.connect).toHaveBeenCalledWith(rabbitmq.url, expect.any(Function));
    expect(mockConnection.createChannel).toHaveBeenCalled();
    expect(mockChannel.assertExchange).toHaveBeenCalledWith(rabbitmq.exchange, 'direct', { durable: true });
  });

  it('deve publicar uma mensagem', async () => {
    const testMessage = { test: 'message' };
    const routingKey = 'testKey';

    await MessageBroker.connect();
    await MessageBroker.publishMessage('testQueue', routingKey, testMessage);

    expect(mockChannel.publish).toHaveBeenCalledWith(
      rabbitmq.exchange,
      routingKey,
      Buffer.from(JSON.stringify(testMessage))
    );
  });

  it('deve lançar um erro se o canal não estiver disponível para publicar mensagens', async () => {
    // Simula que o canal não está disponível
    MessageBroker.channel = null;
    const testMessage = { test: 'message' };

    await expect(MessageBroker.publishMessage('testQueue', 'testKey', testMessage))
      .rejects
      .toThrow('O canal não está disponível');
  });
});
