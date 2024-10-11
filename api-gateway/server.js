const express = require('express');
const app = express();
const routes = require('./src/interfaces/Routes');

app.use(express.json());
app.use('/', routes);

app.listen(7000, () => {
  console.log('API Gateway rodando na porta 7000');
});
