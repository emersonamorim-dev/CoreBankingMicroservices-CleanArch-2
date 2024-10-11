const DiscoveryController = require('../src/interfaces/DiscoveryController'); 
const MongoServiceRegistry = require('../src/infrastructure/MongoServiceRegistry');

jest.mock('../src/infrastructure/MongoServiceRegistry'); // Mocka o DynamoServiceRegistry

const mockRequest = (body = {}) => ({
  body,
});

const mockResponse = () => {
  const res = {};
  res.status = jest.fn().mockReturnValue(res);
  res.json = jest.fn().mockReturnValue(res);
  return res;
};

describe('DiscoveryController', () => {
  afterEach(() => {
    jest.clearAllMocks(); // Limpa mocks após cada teste
  });

  describe('registerService', () => {
    test('deve retornar erro 400 se o nome, host ou porta não forem fornecidos', async () => {
      const req = mockRequest({}); // Sem dados
      const res = mockResponse();

      await DiscoveryController.registerService(req, res);

      expect(res.status).toHaveBeenCalledWith(400);
      expect(res.json).toHaveBeenCalledWith({
        message: 'Nome, host e porta são obrigatórios.',
      });
    });

    test('deve registrar o serviço e retornar 201 se todos os dados estiverem corretos', async () => {
      const req = mockRequest({
        name: 'pagamentos',
        host: '192.168.0.10',
        port: 3000,
        status: 'active',
        region: 'global',
      });
      const res = mockResponse();

      await DiscoveryController.registerService(req, res);

      expect(DynamoServiceRegistry.prototype.registerService).toHaveBeenCalledWith({
        name: 'pagamentos',
        host: '192.168.0.10',
        port: 3000,
        status: 'active',
        region: 'global',
      });
      expect(res.status).toHaveBeenCalledWith(201);
      expect(res.json).toHaveBeenCalledWith({ message: 'Serviço registrado com sucesso!' });
    });

    test('deve retornar erro 500 se o DynamoServiceRegistry falhar', async () => {
      const req = mockRequest({
        name: 'pagamentos',
        host: '192.168.0.10',
        port: 3000,
      });
      const res = mockResponse();
      DynamoServiceRegistry.prototype.registerService.mockImplementationOnce(() => {
        throw new Error('Erro no DynamoDB');
      });

      await DiscoveryController.registerService(req, res);

      expect(res.status).toHaveBeenCalledWith(500);
      expect(res.json).toHaveBeenCalledWith({
        message: 'Erro ao registrar o serviço.',
        error: 'Erro no DynamoDB',
      });
    });
  });

  describe('getAllServices', () => {
    test('deve retornar uma lista de serviços com status 200', async () => {
      const req = mockRequest();
      const res = mockResponse();
      const mockServices = [
        { name: 'pagamentos', host: '192.168.0.10', port: 3000, status: 'active', region: 'global' },
      ];
      DynamoServiceRegistry.prototype.getAllServices.mockResolvedValue(mockServices);

      await DiscoveryController.getAllServices(req, res);

      expect(DynamoServiceRegistry.prototype.getAllServices).toHaveBeenCalled();
      expect(res.status).toHaveBeenCalledWith(200);
      expect(res.json).toHaveBeenCalledWith(mockServices);
    });

    test('deve retornar erro 500 se o DynamoServiceRegistry falhar', async () => {
      const req = mockRequest();
      const res = mockResponse();
      DynamoServiceRegistry.prototype.getAllServices.mockImplementationOnce(() => {
        throw new Error('Erro ao obter serviços');
      });

      await DiscoveryController.getAllServices(req, res);

      expect(res.status).toHaveBeenCalledWith(500);
      expect(res.json).toHaveBeenCalledWith({
        message: 'Erro ao obter os serviços.',
        error: 'Erro ao obter serviços',
      });
    });
  });

  describe('removeService', () => {
    test('deve retornar erro 400 se o nome ou porta não forem fornecidos', async () => {
      const req = mockRequest({});
      const res = mockResponse();

      await DiscoveryController.removeService(req, res);

      expect(res.status).toHaveBeenCalledWith(400);
      expect(res.json).toHaveBeenCalledWith({
        message: 'Nome e porta são obrigatórios.',
      });
    });

    test('deve remover o serviço e retornar 200 se todos os dados forem fornecidos', async () => {
      const req = mockRequest({ name: 'pagamentos', port: 3000 });
      const res = mockResponse();

      await DiscoveryController.removeService(req, res);

      expect(MongoServiceRegistry.prototype.removeService).toHaveBeenCalledWith('pagamentos', 3000);
      expect(res.status).toHaveBeenCalledWith(200);
      expect(res.json).toHaveBeenCalledWith({ message: 'Serviço removido com sucesso!' });
    });

    test('deve retornar erro 500 se o MongoServiceRegistry falhar', async () => {
      const req = mockRequest({ name: 'pagamentos', port: 3000 });
      const res = mockResponse();
      MongoServiceRegistry.prototype.removeService.mockImplementationOnce(() => {
        throw new Error('Erro ao remover serviço');
      });

      await DiscoveryController.removeService(req, res);

      expect(res.status).toHaveBeenCalledWith(500);
      expect(res.json).toHaveBeenCalledWith({
        message: 'Erro ao remover o serviço.',
        error: 'Erro ao remover serviço',
      });
    });
  });
});
