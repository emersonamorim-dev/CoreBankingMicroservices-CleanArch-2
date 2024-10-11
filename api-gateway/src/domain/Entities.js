class ServiceEntity {
  constructor({ name, url, status }) {
    this.name = name;
    this.url = url;
    this.status = status || 'active';
  }

  isValid() {
    return this.name && this.url && this.status;
  }
}

class RequestEntity {
  constructor({ method, path, body }) {
    this.method = method;
    this.path = path;
    this.body = body;
  }

  isValid() {
    return this.method && this.path;
  }
}

class ResponseEntity {
  constructor({ status, data }) {
    this.status = status;
    this.data = data;
  }

  isValid() {
    return this.status >= 200 && this.status < 300 && this.data;
  }
}

module.exports = {
  ServiceEntity,
  RequestEntity,
  ResponseEntity
};
