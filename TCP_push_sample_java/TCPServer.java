import org.vertx.java.core.Handler;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.core.net.NetServer;
import org.vertx.java.platform.Verticle;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.VoidHandler;
import org.vertx.java.core.Handler;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TCPServer extends Verticle {
	static int counter = 0;
	private Map<String, String> handlerIdMap;
	private Map<String, Handler<Message> > handlerMap;
	
	public TCPServer(){
		handlerIdMap  = new ConcurrentHashMap<>();
		handlerMap  = new ConcurrentHashMap<>();
	}
	
	// get Remote Socket address as a string.
	public String getRemoteAddressString(NetSocket socket){
		String remoteAddress = socket.remoteAddress().getAddress().getHostAddress()
      	 												+":"+socket.remoteAddress().getPort();
    return remoteAddress;
	}
	
	public static void log(String msg){
		System.out.println("log :"+msg);
	}
	
  public void start() {
  	
  	NetServer server = vertx.createNetServer();
  	
  	// bind connection handler
  	server.connectHandler(new Handler<NetSocket>() {
      public void handle(final NetSocket socket) {

				// ** client connected event handling
				//	

				//generate id for new connected client
   			counter = (counter+1);
        final String remoteAddress = getRemoteAddressString(socket);
      	log("Client has been connected!! "+remoteAddress+" counter :"+counter);

				// create event bus handler
        Handler<Message> eb_handler = new Handler<Message>(){
        	@Override
        	public void handle(Message message){
							log("I got a message :"+message.body());
							// if it gets a message from eb, it sends msg to client. 
							Buffer sndBuffer = new Buffer();
							socket.write("I got a message :"+message.body());
					}// handle
        };// eb_handler
        
      	// register event bus handler to get push request from Event Bus
        log("Handler registered : Obj ref :"+eb_handler.toString());
      	handlerIdMap.put(remoteAddress,"subscriber-"+counter); // IP address to handler name mapping
      	handlerMap.put(remoteAddress,eb_handler); // IP address to handler object mapping
 	      vertx.eventBus().registerHandler("subscriber-"+counter,eb_handler);  	// register handler to event bus

				//** read event handling
      	// after client has been connected data handler is registered
      	socket.dataHandler(new Handler<Buffer>() {
           public void handle(Buffer buffer) {
               log("I received "+buffer.toString() +" size:" + buffer.length() + " bytes of data");
           }
        }); // data handler
        
        //** socket close event handling
        // close handler
        // event bus handler is removed here. 
        socket.closeHandler(new VoidHandler() {
            public void handle() {
               log(remoteAddress+" socket is now closed");
               
               // retrieve handler name and handler object by using remote address
               String handlerId = handlerIdMap.get(remoteAddress);
               log(handlerId + " handler will be removed");
               handlerIdMap.remove(handlerId);
               Handler handler = handlerMap.get(remoteAddress);
               log("Handler removed : obj id is:"+handler);
               handlerMap.remove(handlerId);
               vertx.eventBus().unregisterHandler(handlerId,handler);  	
 	      		}
        });
      
      	
      }
    });
    
    server.listen(1234);
  }


}
