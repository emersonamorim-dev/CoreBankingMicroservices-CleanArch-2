class PagamentoService:
    def processar_pagamento(self, valor: float, usuario_id: str) -> str:
        """
        Processa o pagamento de um empréstimo.
        :param valor: Valor do pagamento
        :param usuario_id: ID do usuário que está fazendo o pagamento
        :return: Confirmação do pagamento
        """
        return f"Pagamento de R${valor} para o usuário {usuario_id} foi processado com sucesso."
