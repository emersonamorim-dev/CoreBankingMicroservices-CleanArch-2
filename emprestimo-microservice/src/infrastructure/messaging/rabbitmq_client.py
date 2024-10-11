import pika

class RabbitMQClient:
    def __init__(self, rabbitmq_url: str):
        self.connection = pika.BlockingConnection(pika.URLParameters(rabbitmq_url))
        self.channel = self.connection.channel()

    def criar_exchange_e_fila(self, exchange: str, exchange_type: str, fila: str, routing_key: str):
        """
        Cria uma exchange e uma fila no RabbitMQ e faz o binding.
        :param exchange: Nome da exchange
        :param exchange_type: Tipo da exchange (ex: direct, fanout, topic)
        :param fila: Nome da fila
        :param routing_key: Chave de roteamento para ligar a fila à exchange
        """
        self.channel.exchange_declare(exchange=exchange, exchange_type=exchange_type, durable=True)
        self.channel.queue_declare(queue=fila, durable=True)
        self.channel.queue_bind(exchange=exchange, queue=fila, routing_key=routing_key)

    def enviar_mensagem(self, exchange: str, routing_key: str, mensagem: str):
        """
        Envia uma mensagem para a exchange e fila especificadas.
        :param exchange: Nome da exchange
        :param routing_key: Chave de roteamento
        :param mensagem: Mensagem a ser enviada
        """
        self.channel.basic_publish(
            exchange=exchange,
            routing_key=routing_key,
            body=mensagem,
            properties=pika.BasicProperties(
                delivery_mode=2,  # Persiste a mensagem em disco
            )
        )

    def fechar_conexao(self):
        self.connection.close()
