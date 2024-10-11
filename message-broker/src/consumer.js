const broker = require('./broker');
const { services } = require('./config');

async function startConsumer(service, processMessage) {
    const { queue } = services[service];

    broker.channel.assertQueue(queue, { durable: true });
    broker.channel.consume(queue, (msg) => {
        if (msg !== null) {
            const messageContent = JSON.parse(msg.content.toString());
            processMessage(messageContent);
            broker.channel.ack(msg);
        }
    });

    console.log(`Começou a consumir mensagens de ${queue}`);
}

module.exports = startConsumer;


