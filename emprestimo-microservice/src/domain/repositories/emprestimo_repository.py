from pymongo import MongoClient

class EmprestimoRepository:
    def __init__(self, mongo_url: str):
        self.client = MongoClient(mongo_url)
        self.db = self.client['emprestimo_db']
        self.collection = self.db['emprestimos']

    def salvar(self, emprestimo: dict):
        """
        Salva um novo empréstimo no MongoDB.
        :param emprestimo: Dados do empréstimo
        :return: ID do empréstimo salvo
        """
        result = self.collection.insert_one(emprestimo)
        return result.inserted_id
