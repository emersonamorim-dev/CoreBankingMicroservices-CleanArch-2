import uuid
import json
import logging
from decimal import Decimal
from fastapi import APIRouter, Depends, HTTPException
from pydantic import BaseModel

from src.domain.events.emprestimo_event import EmprestimoEvent
from src.infrastructure.messaging import rabbitmq_client
from src.application.usecase.calcular_emprestimo_usecase import CalcularEmprestimoUsecase
from src.domain.entities.emprestimo import Emprestimo

router = APIRouter()

class EmprestimoInput(BaseModel):
    valor_principal: Decimal
    taxa_juros: Decimal
    prazo: int

@router.post("/emprestimos", response_model=Emprestimo)
def criar_emprestimo(input_data: EmprestimoInput, usecase: CalcularEmprestimoUsecase = Depends()):
    try:
        # Calcula o valor total do empréstimo
        valor_total = usecase.calcular(input_data.valor_principal, input_data.taxa_juros, input_data.prazo)

        if valor_total is None:
            logging.error("O cálculo do valor total retornou None.")
            raise HTTPException(status_code=500, detail="Erro no cálculo do empréstimo.")

        # Gera o empréstimo
        emprestimo = Emprestimo(
            id=str(uuid.uuid4()),
            valor_principal=input_data.valor_principal,
            taxa_juros=input_data.taxa_juros,
            prazo=input_data.prazo,
            valor_total=valor_total
        )

        # Serializa e envia o evento para o RabbitMQ
        evento = EmprestimoEvent(emprestimo.id, emprestimo.valor_total)
        evento_serializado = json.dumps(evento.__dict__)  

        rabbitmq_client.enviar_mensagem(exchange='emprestimo_exchange', routing_key='emprestimo_key', mensagem=evento_serializado)
        logging.info(f"Empréstimo {emprestimo.id} enviado para RabbitMQ com sucesso.")

        return emprestimo

    except KeyError as ke:
        logging.error(f"Erro ao acessar chave inexistente: {ke}")
        raise HTTPException(status_code=500, detail=f"Erro ao acessar a chave: {ke}")

    except Exception as e:
        logging.error(f"Erro ao criar empréstimo: {e}")
        raise HTTPException(status_code=500, detail="Erro ao criar empréstimo.")
