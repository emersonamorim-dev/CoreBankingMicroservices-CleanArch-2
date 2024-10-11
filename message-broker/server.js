const broker = require('./src/broker');
const startEmprestimoQueue = require('./src/queues/emprestimoQueue');
const startPixQueue = require('./src/queues/pixQueue');
const startSegurosQueue = require('./src/queues/segurosQueue');

async function init() {
    try {
        await broker.connect();

        // Inicia os consumidores de mensagens
        startEmprestimoQueue();
        startPixQueue();
        startSegurosQueue();

        console.log('Message Broker ready');
    } catch (error) {
        console.error('Erro ao inicializar message broker:', error);
    }
}

init();
