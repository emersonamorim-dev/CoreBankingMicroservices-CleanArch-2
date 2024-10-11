from prometheus_client import Counter, start_http_server

emprestimos_processados = Counter('emprestimos_processados_total', 'Total de empréstimos processados')

def iniciar_exportador_prometheus(porta: int):
    start_http_server(porta)

def incrementar_contador():
    emprestimos_processados.inc()
