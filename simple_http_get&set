import vertx
from core.http import RouteMatcher

server = vertx.create_http_server()

route_matcher = RouteMatcher()

def getUser(req): 
	# read parameter from URI
	userid = req.params['userid']
	req.response.end('You requested user information '+userid)

def addUser(req):
	userid = req.params['userid']
	# read body
	@req.data_handler
	def data_handler(buffer):
		req.response.end('i received body :'+str(buffer))
		print(buffer)
route_matcher.post('/user/:userid',addUser)
	
server.request_handler(route_matcher).listen(8080,'localhost')
