# usage http://localhost:8080/eventbus/hello
# all event bus message handler and reply handler also executed in event loop thread
import vertx
import time
from core.event_bus import EventBus

def message_handler(message): # this is executed in event loop
    print 'Im worker and i got a message :'+str(message.body)
    time.sleep(1)
    # do some logic for data base transaction
    # populate return message
    message.reply('This is reply for message :'+str(message.body))
      
EventBus.register_handler('call_bus',handler=message_handler)

           
