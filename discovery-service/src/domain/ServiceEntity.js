class ServiceEntity {
  constructor({ name, host, port, status = 'active', region = 'global' }) {
    this.name = name;
    this.host = host;
    this.port = port;
    this.status = status;
    this.region = region;
  }

  isValid() {
    return this.name && this.host && this.port;
  }
}

module.exports = ServiceEntity;
