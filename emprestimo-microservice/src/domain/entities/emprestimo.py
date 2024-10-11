from pydantic import BaseModel
from decimal import Decimal

class Emprestimo(BaseModel):
    id: str
    valor_principal: Decimal
    taxa_juros: Decimal
    prazo: int
    valor_total: Decimal
