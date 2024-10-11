import uuid
import json
import logging
from decimal import Decimal
from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel
from src.domain.events.emprestimo_event import EmprestimoEvent
from src.application.usecase.calcular_emprestimo_usecase import CalcularEmprestimoUsecase, EmprestimoService
from src.infrastructure.messaging.rabbitmq_client import RabbitMQClient 
from src.infrastructure.monitoring.prometheus_metrics import incrementar_contador
from json import JSONEncoder

class DecimalEncoder(JSONEncoder):
    def default(self, obj):
        if isinstance(obj, Decimal):
            return float(obj)  
        return super(DecimalEncoder, self).default(obj)

class EmprestimoRequest(BaseModel):
    valor_principal: Decimal
    taxa_juros: Decimal
    prazo: int

router = APIRouter()

# Instâncias do RabbitMQ
rabbitmq_client = RabbitMQClient(rabbitmq_url="amqp://guest:guest@rabbitmq:5672/")

# Cria instância do serviço de empréstimo
emprestimo_service = EmprestimoService()
usecase = CalcularEmprestimoUsecase(emprestimo_service)

@router.post("/api/emprestimo")
async def calcular_emprestimo(emprestimo_request: EmprestimoRequest):
    """
    Endpoint para calcular o valor total de um empréstimo e enviar um evento ao RabbitMQ.
    """
    try:
        logging.info(f"Recebido pedido de empréstimo: {emprestimo_request}")
        
        # Calcula o empréstimo
        resultado = usecase.calcular(
            valor_principal=emprestimo_request.valor_principal,
            taxa_juros=emprestimo_request.taxa_juros,
            prazo=emprestimo_request.prazo
        )
        
        emprestimo_id = str(uuid.uuid4())
        
        resultado['id'] = emprestimo_id
        
        # Declara a exchange e a fila no RabbitMQ
        rabbitmq_client.criar_exchange_e_fila(
            exchange='emprestimo_exchange', 
            exchange_type='direct', 
            fila='emprestimo_queue', 
            routing_key='emprestimo_key'
        )

        # Cria e envia um evento para o RabbitMQ
        evento = EmprestimoEvent(emprestimo_id, resultado['valor_total'])
        
        # Serializa o evento para JSON usando DecimalEncoder para converter Decimals
        evento_serializado = json.dumps(evento.__dict__, cls=DecimalEncoder)
        
        rabbitmq_client.enviar_mensagem(
            exchange='emprestimo_exchange', 
            routing_key='emprestimo_key', 
            mensagem=evento_serializado
        )

        incrementar_contador()

        logging.info(f"Empréstimo {emprestimo_id} processado com sucesso.")
        return {"resultado": resultado}

    except Exception as e:
        logging.error(f"Erro ao processar empréstimo: {str(e)}")
        raise HTTPException(status_code=500, detail=f"Erro ao processar a solicitação de empréstimo: {str(e)}")
