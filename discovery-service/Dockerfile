FROM node:20

# Define o diretório de trabalho
WORKDIR /app

# Copia o package.json e instala as dependências
COPY package*.json ./
RUN npm install

# Copia o código
COPY . .

# Expõe a porta do serviço de descoberta
EXPOSE 8099

# Comando de inicialização
CMD ["node", "server.js"]
