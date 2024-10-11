const axios = require('axios');
const startConsumer = require('../consumer');

async function processSegurosMessage(message) {
    console.log('Processando mensagem de Seguros:', message);

    const processedMessage = {
        segurosId: message.segurosId,   
        valorSeguro: message.valorSeguro,         
        valor: message.valor, 
        metodo: message.metodo     
    };

    try {
        const response = await axios.post('http://localhost:8020/calcular-seguro', processedMessage);

        console.log(`Resposta do serviço de Seguros: ${response.data}`);
    } catch (error) {
        console.error(`Erro ao processar mensagem de Seguros: ${error.message}`);
    }
}

module.exports = () => startConsumer('seguros', processSegurosMessage);
