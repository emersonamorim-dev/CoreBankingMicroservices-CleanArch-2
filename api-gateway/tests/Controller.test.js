const { forwardRequest } = require('../src/interfaces/Controllers');
const { ServiceEntity, RequestEntity, ResponseEntity } = require('../src/domain/Entities');

// Mocks para as entidades
jest.mock('../src/domain/Entities', () => ({
  RequestEntity: jest.fn(),
  ServiceEntity: jest.fn(),
  ResponseEntity: jest.fn(),
}));

describe('forwardRequest', () => {
  let req;
  let res;

  beforeEach(() => {
    req = {
      method: 'POST',
      path: '/test',
      body: { test: 'data' },
    };

    res = {
      status: jest.fn().mockReturnThis(),
      json: jest.fn(),
    };

    jest.clearAllMocks();
  });

  it('deve processar uma requisição válida e retornar status 200', async () => {
    // Mockando a RequestEntity válida
    RequestEntity.mockImplementation((data) => ({
      isValid: jest.fn().mockReturnValue(true),
    }));

    // Mockando a ServiceEntity válida
    ServiceEntity.mockImplementation((data) => ({
      isValid: jest.fn().mockReturnValue(true),
    }));

    // Mockando a ResponseEntity
    ResponseEntity.mockImplementation((data) => data);

    await forwardRequest(req, res);

    expect(RequestEntity).toHaveBeenCalledWith({
      method: req.method,
      path: req.path,
      body: req.body,
    });

    expect(ServiceEntity).toHaveBeenCalledWith(req.body);

    expect(res.status).toHaveBeenCalledWith(200);
    expect(res.json).toHaveBeenCalledWith({
      status: 200,
      data: { message: 'Request forwarded successfully' },
    });
  });

  it('deve retornar status 400 se a RequestEntity for inválida', async () => {
    // Mockando uma RequestEntity inválida
    RequestEntity.mockImplementation(() => ({
      isValid: jest.fn().mockReturnValue(false),
    }));

    await forwardRequest(req, res);

    expect(res.status).toHaveBeenCalledWith(400);
    expect(res.json).toHaveBeenCalledWith({ error: 'Invalid request format' });
  });

  it('deve retornar status 400 se a ServiceEntity for inválida', async () => {
    // Mockando uma RequestEntity válida
    RequestEntity.mockImplementation(() => ({
      isValid: jest.fn().mockReturnValue(true),
    }));

    // Mockando uma ServiceEntity inválida
    ServiceEntity.mockImplementation(() => ({
      isValid: jest.fn().mockReturnValue(false),
    }));

    await forwardRequest(req, res);

    expect(res.status).toHaveBeenCalledWith(400);
    expect(res.json).toHaveBeenCalledWith({ error: 'Invalid service entity' });
  });

  it('deve retornar status 500 se ocorrer um erro inesperado', async () => {
    // Simulando um erro ao processar a requisição
    RequestEntity.mockImplementation(() => {
      throw new Error('Unexpected error');
    });

    await forwardRequest(req, res);

    expect(res.status).toHaveBeenCalledWith(500);
    expect(res.json).toHaveBeenCalledWith({
      error: 'Internal Server Error',
      message: 'Unexpected error',
    });
  });
});
