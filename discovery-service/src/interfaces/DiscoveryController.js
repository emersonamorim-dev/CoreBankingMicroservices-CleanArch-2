const MongoServiceRegistry = require('../infrastructure/MongoServiceRegistry');

class DiscoveryController {
  constructor() {
    this.registry = new MongoServiceRegistry();
  }

  async registerService(req, res) {
    const { name, host, port, status, region } = req.body;

    if (!name || !host || !port) {
      return res.status(400).json({ message: 'Nome, host e porta são obrigatórios.' });
    }

    const serviceData = { 
      name, 
      host, 
      port, 
      status: status || 'active', 
      region: region || 'global' 
    };

    try {
      await this.registry.registerService(serviceData);
      return res.status(201).json({ message: 'Serviço registrado com sucesso!' });
    } catch (error) {
      return res.status(500).json({ message: 'Erro ao registrar o serviço.', error: error.message });
    }
  }

  async getAllServices(req, res) {
    try {
      const services = await this.registry.getAllServices();
      return res.status(200).json(services);
    } catch (error) {
      return res.status(500).json({ message: 'Erro ao obter os serviços.', error: error.message });
    }
  }

  async removeService(req, res) {
    const { name, port } = req.body;

    if (!name || !port) {
      return res.status(400).json({ message: 'Nome e porta são obrigatórios.' });
    }

    try {
      await this.registry.removeService(name, port);
      return res.status(200).json({ message: 'Serviço removido com sucesso!' });
    } catch (error) {
      return res.status(500).json({ message: 'Erro ao remover o serviço.', error: error.message });
    }
  }
}

module.exports = new DiscoveryController();
