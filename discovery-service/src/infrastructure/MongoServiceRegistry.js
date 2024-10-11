const { MongoClient } = require('mongodb');

class MongoServiceRegistry {
  constructor() {
    const mongoUrl = process.env.MONGO_URL || 'mongodb://localhost:27019';
    this.client = new MongoClient(mongoUrl);  
    this.dbName = 'serviceRegistry';
    this.collectionName = 'services';
  }
  async connect() {
    if (!this.db) {
      await this.client.connect();
      this.db = this.client.db(this.dbName);
      this.collection = this.db.collection(this.collectionName);
    }
  }

  async ensureCollectionExists() {
    await this.connect();
    const collections = await this.db.listCollections({ name: this.collectionName }).toArray();
    if (collections.length === 0) {
      await this.db.createCollection(this.collectionName);
      await this.collection.createIndex({ name: 1, port: 1 }, { unique: true }); 
      console.log(`Coleção ${this.collectionName} criada com sucesso.`);
    } else {
      console.log(`Coleção ${this.collectionName} já existe.`);
    }
  }

  // Registra ou atualiza um serviço
  async registerService(serviceData) {
    await this.ensureCollectionExists();

    const filter = { name: serviceData.name, port: serviceData.port };
    const update = {
      $set: {
        host: serviceData.host,
        status: serviceData.status || 'active',
        region: serviceData.region || 'global',
      },
    };
    const options = { upsert: true };

    return this.collection.updateOne(filter, update, options);
  }

  // Obtém todos os serviços
  async getAllServices() {
    await this.ensureCollectionExists();

    return this.collection.find({}).toArray();
  }

  async removeService(name, port) {
    await this.ensureCollectionExists();

    const filter = { name: name, port: port };

    return this.collection.deleteOne(filter);
  }
}

module.exports = MongoServiceRegistry;
