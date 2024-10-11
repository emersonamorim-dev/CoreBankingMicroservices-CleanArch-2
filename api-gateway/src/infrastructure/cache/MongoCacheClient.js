const { MongoClient } = require('mongodb');

// Conecta ao MongoDB
const mongoUrl = 'mongodb://mongo-cache:27017';
const client = new MongoClient(mongoUrl);
let db;

async function connectMongoDB() {
  if (!db) {
    await client.connect();
    db = client.db('api_gateway_cache');  // Nome do banco de dados
  }
}

async function setCache(key, value) {
  await connectMongoDB();
  await db.collection('cache').updateOne(
    { url: key },
    { $set: { url: key, data: value, createdAt: new Date() } },
    { upsert: true }
  );
}

async function getCache(key) {
  await connectMongoDB();
  const cachedData = await db.collection('cache').findOne({ url: key });
  return cachedData ? cachedData.data : null;
}

module.exports = {
  setCache,
  getCache
};
