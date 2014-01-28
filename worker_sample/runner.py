import vertx

vertx.deploy_verticle('http_server.py')
vertx.deploy_worker_verticle('worker.py','{"dummy":"dummy"}',20)

