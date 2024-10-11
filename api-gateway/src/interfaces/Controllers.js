const { ServiceEntity, RequestEntity, ResponseEntity } = require('../domain/Entities');


async function forwardRequest(req, res) {
  try {
    const requestEntity = new RequestEntity({
      method: req.method,
      path: req.path,
      body: req.body
    });

    if (!requestEntity.isValid()) {
      return res.status(400).json({ error: 'Formato de solicitação inválido' });
    }

    const serviceEntity = new ServiceEntity(req.body); 

    if (!serviceEntity.isValid()) {
      return res.status(400).json({ error: 'Entidade de serviço inválida' });
    }

    // encaminhar a requisição para o microserviço correto
    const responseEntity = new ResponseEntity({
      status: 200, 
      data: { message: 'Solicitação encaminhada com sucesso' }
    });

    res.status(200).json(responseEntity);
  } catch (error) {
    res.status(500).json({ error: 'Erro do Servidor Interno', message: error.message });
  }
}

module.exports = {
  forwardRequest
};
