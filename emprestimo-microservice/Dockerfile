FROM python:3.11-slim

# Definir o diretório de trabalho dentro do container
WORKDIR /app

# Instalar netcat-openbsd para verificação da conectividade de rede
RUN apt-get update && apt-get install -y netcat-openbsd

# Copiar os arquivos de requerimentos
COPY requirements.txt .

# Instalar as dependências
RUN pip install --no-cache-dir -r requirements.txt

# Copiar todo o código fonte da aplicação para dentro do container
COPY . .

# Expor a porta 8099 no container
EXPOSE 8099

# Definir a variável de ambiente para a porta do Uvicorn
ENV PORT=8099

# Comando para iniciar a aplicação FastAPI usando Uvicorn
CMD ["uvicorn", "src.main:app", "--host", "0.0.0.0", "--port", "8099"]
