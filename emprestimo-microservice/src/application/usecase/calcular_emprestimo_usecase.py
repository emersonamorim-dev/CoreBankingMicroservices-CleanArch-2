from decimal import Decimal

class EmprestimoService:
    def calcular_emprestimo(self, valor_principal: Decimal, taxa_juros: Decimal, prazo: int):
        """
        Calcula o valor total a ser pago em um empréstimo baseado em juros compostos.
        :param valor_principal: Valor do empréstimo
        :param taxa_juros: Taxa de juros anual
        :param prazo: Prazo do empréstimo em meses
        :return: Valor total com juros
        """
        # Cálculo baseado em juros compostos
        valor_total = valor_principal * ((1 + (taxa_juros / 100)) ** prazo)
        return {
            "valor_principal": valor_principal,
            "taxa_juros": taxa_juros,
            "prazo": prazo,
            "valor_total": valor_total
        }

class CalcularEmprestimoUsecase:
    def __init__(self, emprestimo_service: EmprestimoService):
        self.emprestimo_service = emprestimo_service

    def calcular(self, valor_principal: Decimal, taxa_juros: Decimal, prazo: int):
        """
        Usa o serviço de empréstimo para calcular o valor total.
        :param valor_principal: Valor do empréstimo
        :param taxa_juros: Taxa de juros anual
        :param prazo: Prazo do empréstimo em meses
        :return: Valor total com juros
        """
        return self.emprestimo_service.calcular_emprestimo(valor_principal, taxa_juros, prazo)

