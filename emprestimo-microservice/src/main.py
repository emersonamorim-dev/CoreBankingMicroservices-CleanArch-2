import os
from fastapi import FastAPI
from src.interfaces.http.routes import router
from src.infrastructure.monitoring.prometheus_metrics import iniciar_exportador_prometheus
import sys
import os

sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

app = FastAPI()

app.include_router(router)

if __name__ == "__main__":
    port = int(os.getenv("PORT", 8099))
    
    iniciar_exportador_prometheus(8000)
    
    # Inicia a aplicação FastAPI
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=port)

