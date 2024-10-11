FROM node:20

# Definindo o diretório de trabalho
WORKDIR /app

# Copiando o package.json e package-lock.json
COPY package*.json ./

# Instalando dependências
RUN npm install

# Copiando os arquivos da aplicação
COPY . .

# Expondo a porta da aplicação
EXPOSE 6000

# Comando para iniciar o servidor
CMD ["npm", "start"]
