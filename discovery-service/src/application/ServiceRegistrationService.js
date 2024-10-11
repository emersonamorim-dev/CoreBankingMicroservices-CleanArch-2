const MongoServiceRegistry = require('../infrastructure/MongoServiceRegistry');

class ServiceRegistrationService {
  constructor() {
    this.registry = new MongoServiceRegistry();
  }

  async registerService(serviceData) {
    await this.registry.registerService(serviceData);
  }

  async getAllServices() {
    return await this.registry.getAllServices();
  }

  async removeService(serviceName, port) {
    await this.registry.removeService(serviceName, port);
  }
}

module.exports = ServiceRegistrationService;
