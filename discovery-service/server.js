const express = require('express');
const app = express();
const discoveryRoutes = require('./src/routes/discoveryRoutes');

// Middleware para JSON
app.use(express.json());

app.use(discoveryRoutes);

// Inicializa o servidor
const PORT = process.env.PORT || 8088;
app.listen(PORT, () => {
  console.log(`Discovery Service rodando na porta ${PORT}`);
});

module.exports = app;
