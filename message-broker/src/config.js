module.exports = {
    rabbitmq: {
        url: process.env.RABBITMQ_URL || 'amqp://guest:guest@localhost:5681', 
        exchange: 'message-broker'
    },
    services: {
        emprestimo: {
            queue: 'emprestimoQueue',
            routingKey: 'emprestimo_key'
        },
        pix: {
            queue: 'pixQueue',
            routingKey: 'pix_key'
        },
        seguros: {
            queue: 'segurosQueue',
            routingKey: 'seguros_key'
        }
    }
};
