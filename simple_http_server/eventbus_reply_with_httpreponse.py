# -*- encoding: utf-8 -*-
# usage http://localhost:8080/eventbus/hello
# all event bus message handler and reply handler also executed in event loop thread
import vertx
import time
from core.http import RouteMatcher
from core.event_bus import EventBus

server = vertx.create_http_server()

route_matcher = RouteMatcher()


def message_handler(message): # this is executed in event loop
    print 'Im worker and i got a message :'+str(message.body)
    time.sleep(5)
    # do some logic for data base transaction
    # populate return message
    message.reply('This is reply for message :'+str(message.body))

def reply_handler_logic(req,message):
	print 'im reply handler, i just invoked'
   	time.sleep(5)
	key = req.params['key']
   	req.response.end('event bus call is called and replied from reply_handler :'+ key +str(message.body))
        
EventBus.register_handler('call_bus',handler=message_handler)

def url_handler(req): # this is invoked by eventloop thread
        # read parameter from URI
        key = req.params['key']
        def reply_handler(message):
        	reply_handler_logic(req,message)
        print 'im url handler i will send message to event bus'
       	time.sleep(5)
       	print 'Im url handler. I just sent message to event bus'
        EventBus.send('call_bus',key,reply_handler)
                
route_matcher.get('/eventbus/:key',url_handler)

                
server.request_handler(route_matcher).listen(8000,'localhost')
