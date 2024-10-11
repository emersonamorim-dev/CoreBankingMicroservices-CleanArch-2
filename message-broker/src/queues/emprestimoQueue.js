const axios = require('axios');
const startConsumer = require('../consumer');

async function processEmprestimoMessage(message) {
    console.log('Mensagem de processamento de emprestimo:', message);
    
    // mensagem pode ser processada ou validada conforme necessário
    const processedMessage = {
        emprestimoId: message.emprestimoId,  
        action: message.action   
    };

    try {
        const response = await axios.post('http://localhost:8099/api/emprestimo', processedMessage);
        console.log(`Resposta do serviço de cartões: ${response.data}`);
    } catch (error) {
        console.error(`Mensagem de erro ao processar cartões: ${error.message}`);
    }
}

module.exports = () => startConsumer('emprestimo', processEmprestimoMessage);
