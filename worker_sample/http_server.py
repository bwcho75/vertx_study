# usage http://localhost:8080/eventbus/hello
# all event bus message handler and reply handler also executed in event loop thread
import vertx
import time
from core.http import RouteMatcher
from core.event_bus import EventBus

server = vertx.create_http_server()

route_matcher = RouteMatcher()

def reply_handler_logic(req,message):
  print 'im reply handler, i just invoked'
  key = req.params['key']
  req.response.end('event bus call is called and replied from reply_handler :'+ key +str(message.body))
        
def url_handler(req):
  # read parameter from URI
  key = req.params['key']
  def reply_handler(message):
  	reply_handler_logic(req,message)
  print 'im url handler i will send message to event bus '
  print 'Im url handler. I just sent message to event bus '
  EventBus.send('call_bus',key,reply_handler)
                
route_matcher.get('/eventbus/:key',url_handler)

                
server.request_handler(route_matcher).listen(8000,'localhost')
