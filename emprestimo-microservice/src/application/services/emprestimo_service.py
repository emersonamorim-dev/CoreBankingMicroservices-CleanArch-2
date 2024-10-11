from decimal import Decimal
import uuid

class EmprestimoService:
    def calcular_emprestimo(self, valor_principal: Decimal, taxa_juros: Decimal, prazo: int) -> dict:
        """
        Aplica a fórmula de juros compostos para calcular o valor total do empréstimo.
        :param valor_principal: Valor do empréstimo
        :param taxa_juros: Taxa de juros anual
        :param prazo: Prazo do empréstimo em meses
        :return: Dicionário com os detalhes do empréstimo
        """
        juros_mensal = (taxa_juros / 100) / 12
        valor_total = valor_principal * ((1 + juros_mensal) ** prazo)
        emprestimo_id = str(uuid.uuid4())  # Gera um ID único para o empréstimo
        return {
            "id": emprestimo_id,
            "valor_principal": valor_principal,
            "taxa_juros": taxa_juros,
            "prazo": prazo,
            "valor_total": round(valor_total, 2)
        }