const broker = require('./broker');
const { services } = require('./config');

async function sendMessage(service, message) {
    try {
        const { queue, routingKey } = services[service];
        await broker.publishMessage(queue, routingKey, message);
    } catch (error) {
        console.error('Erro ao enviar mensagem:', error);
    }
}

module.exports = sendMessage;
