function cacheMiddleware(req, res, next) {
    console.log('Cache middleware executado');
    next();  
  }
  
  module.exports = {
    cacheMiddleware
  };
  