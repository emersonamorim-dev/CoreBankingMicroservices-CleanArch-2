from pymongo import MongoClient

def get_mongodb_connection(mongo_url: str):
    client = MongoClient(mongo_url)
    return client['emprestimo_db']
