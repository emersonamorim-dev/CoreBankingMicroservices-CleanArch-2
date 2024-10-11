class EmprestimoEvent:
    def __init__(self, emprestimo_id: str, valor_total: float):
        self.emprestimo_id = emprestimo_id
        self.valor_total = valor_total

    def __str__(self):
        return f"Empréstimo {self.emprestimo_id} processado com valor total de {self.valor_total}."

