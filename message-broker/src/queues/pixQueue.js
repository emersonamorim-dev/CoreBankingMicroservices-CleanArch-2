const axios = require('axios');
const startConsumer = require('../consumer');

async function processPixMessage(message) {
    console.log('Processando mensagem de pagamento:', message);
    
    const processedMessage = {
        pixId: message.pixId,  
        amount: message.amount        
    };

    try {
        const response = await axios.post('http://localhost:8096/pix/payment', processedMessage);

        console.log(`Resposta do serviço de pagamento: ${response.data}`);
    } catch (error) {
        console.error(`Erro ao processar mensagem de pagamento: ${error.message}`);
    }
}

module.exports = () => startConsumer('pix', processPixMessage);
