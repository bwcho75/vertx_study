# usage http://localhost:8080/eventbus/hello
import vertx
from core.http import RouteMatcher
from core.event_bus import EventBus

server = vertx.create_http_server()

route_matcher = RouteMatcher()


def message_handler(message):
	print 'Im worker and i got a message :'+str(message.body)
	message.reply('This is reply for message :'+str(message.body))

def reply_handler(message):
	print 'Im reply handler :'+str(message.body)
	
EventBus.register_handler('call_bus',handler=message_handler)

def url_handler(req): 
	# read parameter from URI
	key = req.params['key']
	req.response.end('event bus call is called :'+ key)
	#print('add user has been called'+ userid)
	EventBus.send('call_bus',userid,reply_handler)
		
route_matcher.get('/eventbus/:key',url_handler)

		
server.request_handler(route_matcher).listen(8080,'localhost')
