const { MongoClient } = require('mongodb');

const mongoUrl = 'mongodb://mongo-cache:27017';  // URL do MongoDB
const client = new MongoClient(mongoUrl);
let db;

async function connectMongoDB() {
  if (!db) {
    await client.connect();
    db = client.db('api_gateway_cache');  // Nome do banco
  }
}

async function cacheMiddleware(req, res, next) {
  await connectMongoDB();
  const cachedResponse = await db.collection('cache').findOne({ url: req.originalUrl });
  if (cachedResponse) {
    return res.send(cachedResponse.data);
  }
  next();
}

module.exports = {
  cacheMiddleware
};
